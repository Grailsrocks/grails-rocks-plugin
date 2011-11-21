package rocks

import grails.converters.JSON

class RocksController {

    def pluginManager
    def grailsRocksService
    
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
    
    private getPluginInfo(String name) {
        def baseURL = "http://grails.org/plugins/?format=json&q=name:"
        def pluginURL = baseURL + name
        try {
            def resp = new URL(pluginURL).text
            println "Portal resp for $name: ${resp}"
            def responseRows = JSON.parse(resp).pluginList
            def result = responseRows.find { it.name == name }
            if (!result) {
                return [name:name]
            }
            
            def pluginInfo = [
                name:result.name, 
                version:result.version, 
                docs:result.documentation, 
                src:result.scm, 
                issues:result.issues
            ]
            println "plugin info: ${pluginInfo}"
            return pluginInfo
        } catch (IOException ioe) {
            println "WTF: $ioe"
            return [name:name]
        }
    }
    
    def index = { 
        def allSupportedPlugins = [ 
            'authentication', 
            'feeds', 
            'resources', 
            'email-confirmation', 
            'invitation-only',
            'one-time-data',
            'cache-headers',
            'cached-resources',
            'zipped-resources',
            'bean-fields'
        ]
        def allPlugins = pluginManager.allPlugins
        
        def openTickets = [
            [link:'', title:'Its broken!', status:'new'],
            [link:'', title:'Please help me you fucker', status:'open']
        ]
        def recentTickets = [
            [link:'', title:'Navigation is wrong'],
            [link:'', title:'You deleted my database you shits']
        ]
        def supportedPluginsInstalled = allPlugins.findAll({ p -> p.name in allSupportedPlugins}).collect {
            def r = PLUGIN_COLLECTOR.clone().call(it)
            r.license = 'APACHE'
            r.grailsrocks = true

            def portalInfo = getPluginInfo(it.name)
            if (it.version != portalInfo?.version) {
                r.newerVersion = portalInfo.version
            }
            r.docs = portalInfo.docs
            r.src = portalInfo.src
            r.issues = portalInfo.issues
            
            println "Supported plugin: ${r}"
            return r
        }
            
        def supportedPluginNamesNotInstalled = allSupportedPlugins.findAll( { pn -> !supportedPluginsInstalled.find { it.name == pn } } )
        def supportedPluginsNotInstalled = supportedPluginNamesNotInstalled.collect { 
            def portalInfo = getPluginInfo(it)
            portalInfo.installed = false
            portalInfo.license = 'APACHE'
            portalInfo.grailsrocks = true
            return portalInfo
        }
        
        def unsupportedPlugins = allPlugins.findAll({ p -> 
            !(p.name in allSupportedPlugins)
        }).collect({ plugin ->
            println "Getting info for ${plugin.name}"
            def r = PLUGIN_COLLECTOR.clone().call(plugin)
            def portalInfo = getPluginInfo(plugin.name)
            if (plugin.version != portalInfo?.version) {
                r.newerVersion = portalInfo.version
            }
            return r
        }).sort( { it.name })
        
        [   userDetails:grailsRocksService.userDetails,
            supportStatus:'open',
            ticketsLeft:3,
            subscriber:grailsRocksService.userDetails?.password as Boolean,
            openTickets:openTickets, 
            allSupportedPlugins:allSupportedPlugins,
            recentTickets:recentTickets, 
            supportedPlugins:(supportedPluginsInstalled+supportedPluginsNotInstalled).sort( { it.name }), 
            unsupportedPlugins:unsupportedPlugins]
    }
    
    def resetZendeskDetails = {
        def ud = grailsRocksService.getUserDetails()
        ud.email = ''
        ud.password = ''
        grailsRocksService.setUserDetails(ud)
        
        redirect(action:'index')
    }

    def saveZendeskDetails = {
        assert params.email
        assert params.password
        
        def ud = grailsRocksService.getUserDetails()
        ud.email = params.email
        ud.password = params.password
        grailsRocksService.setUserDetails(ud)
        
        redirect(action:'index')
    }
}
