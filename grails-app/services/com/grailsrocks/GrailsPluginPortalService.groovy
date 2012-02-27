package com.grailsrocks

import grails.converters.JSON
import grails.util.GrailsNameUtils

class GrailsPluginPortalService {
    static transactional = false
    
    static PLUGIN_LIST_JSON_URL = "http://grails.org/api/v1.0/plugins?format=json"
    
    static CORE_PLUGINS = [
        'controllers',
        'domainClass',
        'codecs',
        'core',
        'dataSource',
        'filters',
        'groovyPages',
        'i18n',
        'logging',
        'mimeTypes',
        'scaffolding',
        'services',
        'servlet',
        'tomcat',
        'urlMappings',
        'converters',
        'validation'
    ]
    
    def pluginManager    
    long lastDownload
    
    private pluginPortalList
    private allPlugins
    private installedPlugins
    
    static PLUGIN_COLLECTOR =  { p ->
        def newPlugin = [name: p.name, version:p.version, installed:true]
        if (p.instance) {
            newPlugin.name = GrailsNameUtils.getScriptName(p.name)
            if (p.instance.metaClass.hasProperty(null, 'documentation')) {
                newPlugin.docs = p.instance.documentation
            }
            if (p.instance.metaClass.hasProperty(null, 'scm')) {
                newPlugin.src = p.instance.scm.url
            }
            if (p.instance.metaClass.hasProperty(null, 'issueManagement')) {
                newPlugin.issues = p.instance.issueManagement.url
            }
            if (p.instance.metaClass.hasProperty(null, 'license')) {
                newPlugin.license = p.instance.license
            }
        }
        return newPlugin
    }
    
    private loadPluginInfo() {
        if (!lastDownload || ((System.currentTimeMillis() - lastDownload) > 60000L)) {
            log.debug "Getting Grails.org plugin list..."
            allPlugins = []
            def queryURL = PLUGIN_LIST_JSON_URL
            try {
                def resp = new URL(queryURL).text
                def responseRows = JSON.parse(resp).pluginList
                for (result in responseRows) {
                    def desc = result.description instanceof String ? result.description : null
                    def author = result.author instanceof String ? result.author : null
                    def authorEmail = result.authorEmail instanceof String ? result.authorEmail : null
                    def docs = result.documentation ? result.documentation : "http://grails.org/plugin/${result.name}"
                    def pluginInfo = [
                        name:result.name, 
                        version:result.version, 
                        docs:docs, 
                        src:result.scm, 
                        author:author, 
                        authorEmail:authorEmail, 
                        issues:result.issues,
                        description: desc
                    ]
                    allPlugins << pluginInfo
                }
                log.debug "Finished getting Grails.org plugin list, ${allPlugins.size()} plugins"
            } catch (IOException ioe) {
                log.error "Couldn't contact Grails plugin portal", ioe
            }
            lastDownload = System.currentTimeMillis()
        }
    }

    List<Map> getAllPlugins() {
        allPlugins
    }

    List<Map> getInstalledPlugins() {
        installedPlugins
    }
    
    List<Map> findAllPlugins(Closure filter) {
        allPlugins.findAll(filter)
    }
    
    List<Map> findInstalledPlugins(Closure filter) {
        installedPlugins.findAll(filter)
    }
    
    void refreshPlugins() {
        loadPluginInfo()

        // @todo get full list in one request
        def instPlugins = pluginManager.allPlugins.findAll { p -> !(p.name in CORE_PLUGINS) }
        installedPlugins = instPlugins.collect { p ->
            def r = PLUGIN_COLLECTOR.clone().call(p)
            def portalInfo = allPlugins.find { it.name == r.name }
            // Copy info from portal into installed plugins if available
            if (portalInfo) {
                if (p.version != portalInfo?.version) {
                    r.newerVersion = portalInfo.version
                }
                r.docs = portalInfo.docs
                r.description = portalInfo.description
                r.author = portalInfo.author
                r.authorEmail = portalInfo.authorEmail
                r.src = portalInfo.src
                r.issues = portalInfo.issues
                r.license = portalInfo.license
                portalInfo.installed = true
            }
            if (!r.description) {
                if (p.instance.metaClass.hasProperty(p.instance, 'description')) {
                    r.description = p.instance.description
                }
            }
            return r
        }
        
        allPlugins.sort( { it.name })
        installedPlugins.sort( { it.name })
    }
    
}