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
    
    static LICENSE_APACHE2_URL = 'http://www.apache.org/licenses/LICENSE-2.0.txt'
    static LICENSE_GPL3_URL = 'http://www.gnu.org/licenses/gpl.txt'

    static LICENSE_URLS_TO_TAGS = [
        (LICENSE_APACHE2_URL):'APACHE 2',
        (LICENSE_GPL3_URL):'GPL 3'
    ]
    
    static PORTAL_REFRESH = 5 * 60 * 1000L
    
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
            // Copy over legacy license info from installed plugins
            if (p.instance.metaClass.hasProperty(null, 'license')) {
                def licenseInfo = [
                    name:p.instance.license
                ]
                
                switch (p.instance.license) {
                    case 'APACHE':
                        licenseInfo.url = LICENSE_APACHE2_URL
                        break;
                    case 'GPL':
                        licenseInfo.url = LICENSE_GPL3_URL
                        break;
                }
                newPlugin.licenses = [ licenseInfo ]
            }
        }
        return newPlugin
    }
    
    private loadPluginInfo() {
        if (!lastDownload || ((System.currentTimeMillis() - lastDownload) > PORTAL_REFRESH)) {
            log.debug "Getting Grails.org plugin list..."
            allPlugins = []
            def queryURL = PLUGIN_LIST_JSON_URL
            try {
                def resp = new URL(queryURL).text
                def responseRows = JSON.parse(resp).pluginList
                for (result in responseRows) {
                    def desc = result.description instanceof String ? result.description : null
                    def author = result.author instanceof String ? result.author : null
                    def authorEmailMd5 = result.authorEmailMd5 instanceof String ? result.authorEmailMd5 : null
                    def docs = result.documentation ? result.documentation : "http://grails.org/plugin/${result.name}"
                    def licenses = []
                    if (result.licenseList instanceof List) {
                        licenses = result.licenseList.collect { l ->
                            getTagForLicense(l.name, l.url)
                        }
                    }
                    
                    def pluginInfo = [
                        name:result.name, 
                        version:result.version, 
                        docs:docs, 
                        src:result.scm, 
                        author:author, 
                        authorEmailMd5:authorEmailMd5, 
                        issues:result.issues,
                        description: desc,
                        zombie: result.zombie instanceof Boolean ? result.zombie : false,
                        licenses: licenses
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
    
    String getTagForLicense(String name, String url) {
        LICENSE_URLS_TO_TAGS[url] ?: name[0..Math.min(name.size(), 10)-1]
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
                r.authorEmailMd5 = portalInfo.authorEmailMd5
                r.src = portalInfo.src
                r.issues = portalInfo.issues
                r.licenses = portalInfo.licenses
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