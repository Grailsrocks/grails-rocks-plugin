package rocks


class RocksController {

    def grailsRocksService
    def grailsPluginPortalService
    
    private augmentGrailsrocksPlugins(List plugins) {
        for (p in plugins) {
            println p.name
            if (p.name in grailsRocksService.allSupportedPlugins) {
                p.license = 'APACHE' // get this from data
                p.grailsrocks = true
                p.supported = true
            }
        }
    }
    
    def index = { 
        
        def openTickets = [
            [link:'', title:'Its broken!', status:'new'],
            [link:'', title:'Please help me you fucker', status:'open']
        ]
        def recentTickets = [
            [link:'', title:'Navigation is wrong'],
            [link:'', title:'You deleted my database you shits']
        ]

        grailsPluginPortalService.refreshPlugins()
        def installedPlugins = grailsPluginPortalService.installedPlugins
        def allPlugins = grailsPluginPortalService.allPlugins
        augmentGrailsrocksPlugins(installedPlugins)
        augmentGrailsrocksPlugins(allPlugins)
        
        def supportedPlugins = allPlugins.findAll {it.grailsrocks}

        [   userDetails:grailsRocksService.userDetails,
            supportStatus:'open',
            ticketsLeft:3,
            subscriber:grailsRocksService.userDetails?.password as Boolean,
            openTickets:openTickets, 
            supportedPlugins:supportedPlugins,
            installedPlugins:installedPlugins,
            allPlugins:allPlugins,
            recentTickets:recentTickets
        ]
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
