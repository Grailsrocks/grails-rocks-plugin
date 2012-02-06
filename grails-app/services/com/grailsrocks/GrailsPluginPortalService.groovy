package com.grailsrocks

import grails.converters.JSON

class GrailsPluginPortalService {
    static transactional = false
    
    static PLUGIN_LIST_JSON_URL = "http://grails.org/api/v1.0/plugins?format=json"
    def pluginManager    
    
    private pluginPortalList
    private allPlugins
    private installedPlugins
    
    static PLUGIN_COLLECTOR =  { p ->
        def newPlugin = [name:p.name, version:p.version, installed:true]
        if (p.instance) {
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
        allPlugins = []
        def queryURL = PLUGIN_LIST_JSON_URL
        try {
            def resp = new URL(queryURL).text
            def responseRows = JSON.parse(resp).pluginList
            for (result in responseRows) {
                def pluginInfo = [
                    name:result.name, 
                    version:result.version, 
                    docs:result.documentation, 
                    src:result.scm, 
                    issues:result.issues
                ]
                allPlugins << pluginInfo
            }
        } catch (IOException ioe) {
            log.error "Couldn't contact Grails plugin portal", ioe
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
        def instPlugins = pluginManager.allPlugins
        installedPlugins = instPlugins.collect { p ->
            def r = PLUGIN_COLLECTOR.clone().call(p)
            def portalInfo = allPlugins.find { it.name == p.name }
            if (portalInfo) {
                if (p.version != portalInfo?.version) {
                    r.newerVersion = portalInfo.version
                }
                r.docs = portalInfo.docs
                r.src = portalInfo.src
                r.issues = portalInfo.issues
                portalInfo.installed = true
            }
            return r
        }
        
        allPlugins.sort( { it.name })
        installedPlugins.sort( { it.name })
    }
    
}