package rocks

import grails.util.Environment
import com.grailsrocks.zendesk.ZendeskAPI

class RocksController {

    def grailsRocksService
    def grailsPluginPortalService
    def zendeskService
    
    static zendeskUrl = "https://grailsrocks.zendesk.com"
    
    static beforeInterceptor = {
        if (Environment.current != Environment.DEVELOPMENT) {
            throw new RuntimeException('You should exclude the "rocks" plugin from non-development environments using your BuildConfig!')
        }
    }
    
    private augmentGrailsrocksPlugins(List plugins) {
        for (p in plugins) {
            if (p.name in grailsRocksService.allSupportedPlugins) {
                p.license = 'APACHE 2' // get this from data
                p.grailsrocks = true
                p.supported = true
            }
        }
    }
    
    def index = { 
        def now = new GregorianCalendar(TimeZone.getTimeZone('GMT'))
        now.timeInMillis = System.currentTimeMillis()
        def supportStatus = now.get(Calendar.HOUR_OF_DAY) in (9..17) ? 'open' : 'closed'
        
        def ud = grailsRocksService.userDetails
        def zd = zendeskService.getAPI(zendeskUrl)
        zd.user = ud.email
        zd.password = ud.password
        def tickets
        try {
            tickets = zd.requests
        } catch (Exception e) {
            log.warn "Couldn't contact Zendesk: ${e}"
        }
        
        def openTickets = tickets.findAll { t -> t.status_id != ZendeskAPI.STATUS_CLOSED }
//        def recentTickets = tickets.findAll { t -> t.status_id == ZendeskAPI.STATUS_CLOSED }

        grailsPluginPortalService.refreshPlugins()
        def installedPlugins = grailsPluginPortalService.installedPlugins
        def allPlugins = grailsPluginPortalService.allPlugins
        augmentGrailsrocksPlugins(installedPlugins)
        augmentGrailsrocksPlugins(allPlugins)
        
        def supportedPlugins = allPlugins.findAll {it.grailsrocks}

        [   userDetails:grailsRocksService.userDetails,
            supportStatus:supportStatus,
            ticketsLeft:3,
            subscriber:ud?.password as Boolean,
            openTickets:openTickets, 
            supportedPlugins:supportedPlugins,
            installedPlugins:installedPlugins,
            allPlugins:allPlugins,
//            recentTickets:recentTickets,
            zendeskUrl:zendeskUrl,
            lastIssueForm:flash['grailsrocks.lastIssueForm']
        ]
    }
    
    def resetZendeskDetails = {
        def ud = grailsRocksService.getUserDetails()
        ud.email = ''
        ud.password = ''
        grailsRocksService.setUserDetails(ud)
        
        flash.message = "grailsrocks.details.reset"
        redirect(action:'index', fragment:'supportedplugins')
    }

    def saveZendeskDetails = {
        assert params.email
        assert params.password
        
        def ud = grailsRocksService.getUserDetails()
        ud.email = params.email
        ud.password = params.password
        grailsRocksService.setUserDetails(ud)
        
        flash.message = "grailsrocks.details.saved"
        redirect(action:'index', fragment:'supportedplugins')
    }
    
    def createGrailsrocksTicket = { CreateTicketForm form ->
        if (form.hasErrors()) {
            flash['grailsrocks.lastIssueForm'] = form
            redirect(action:'index')
        } else {
            def ud = grailsRocksService.userDetails
            def zd = zendeskService.getAPI(zendeskUrl)
            zd.user = ud.email
            zd.password = ud.password
            // @todo add plugin
            zd.createTicket(subject:form.subject, description:form.description)
            flash.message = "grailsrocks.issue.created"
            flash['grailsrocks.lastIssueForm'] = null
            redirect(action:'index', fragment:'supportedplugins')
        }
    }
}

class CreateTicketForm {
    String plugin
    String subject
    String description

    static constraints = {
        plugin(nullable: false, blank: false)
        subject(nullable: false, blank: false)
        description(nullable: false, blank: false)
    }
}
