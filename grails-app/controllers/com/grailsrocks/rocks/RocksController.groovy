package com.grailsrocks.rocks

import grails.util.Environment
import com.grailsrocks.zendesk.ZendeskAPI

class RocksController {

    static GRAILSROCKS_ZENDESK_FIELD_PLUGIN = '20483566'
    
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
                p.licenses = ['APACHE 2'] // get this from data
                p.grailsrocks = true
                p.supported = true
            }
        }
    }
    
    def index = { 
        def now = new GregorianCalendar(TimeZone.getTimeZone('GMT'))
        now.timeInMillis = System.currentTimeMillis()
        def supportStatus = now.get(Calendar.HOUR_OF_DAY) in (9..17) ? 'open' : 'closed'
        
        def tickets
        def ud = grailsRocksService.userDetails
        if (ud.password) {
            def zd = zendeskService.getAPI(zendeskUrl)
            zd.user = ud.email
            zd.password = ud.password
            try {
                // @todo throttle this
                log.debug "Getting Zendesk tickets..."
                tickets = zd.requests
            } catch (Exception e) {
                log.warn "Couldn't contact Zendesk: ${e}"
            }
        }
        def openTickets = tickets?.findAll { t -> t.status_id != ZendeskAPI.STATUS_CLOSED }
//        def recentTickets = tickets.findAll { t -> t.status_id == ZendeskAPI.STATUS_CLOSED }

        grailsPluginPortalService.refreshPlugins()
        def installedPlugins = grailsPluginPortalService.installedPlugins
        def allPlugins = grailsPluginPortalService.allPlugins
        augmentGrailsrocksPlugins(installedPlugins)
        augmentGrailsrocksPlugins(allPlugins)
        
        def supportedPlugins = allPlugins.findAll { it.grailsrocks }

        def yourAuthors = collateAuthorInfo(installedPlugins).values().sort( { a, b -> a.name.toLowerCase() <=> b.name.toLowerCase() })
        
        [   userDetails:grailsRocksService.userDetails,
            supportStatus:supportStatus,
            subscriber:ud?.password as Boolean,
            openTickets:openTickets, 
            yourAuthors:yourAuthors,
            supportedPlugins:supportedPlugins,
            installedPlugins:installedPlugins,
            allPlugins:allPlugins,
            zendeskUrl:zendeskUrl,
            lastIssueForm:session['grailsrocks.lastIssueForm']
        ]
    }
    
    private Map collateAuthorInfo(pluginsList) {
        def results = [:] 
        for (p in pluginsList) {
            def author = p.author
            if (author && (author.indexOf('<') == -1)) {
                def authors = author.split(',')
                def data
                for (name in authors) {
                    data = results[name]
                    if (!data) {
                        data = [name:name, plugins:[] as Set]
                        results[name] = data
                    }
                    data.plugins << p.name
                }
                if (data && (authors.size() == 1) && p.authorEmailMd5) {
                    data.email = p.authorEmailMd5
                }
            }
        }
        return results
    }
    
    def resetZendeskDetails = {
        log.debug "Clearing Zendesk details"
        def ud = grailsRocksService.getUserDetails()
        ud.email = ''
        ud.password = ''
        grailsRocksService.setUserDetails(ud)
        
        flash.message = "grailsrocks.details.reset"
        redirect(action:'index', fragment:'supportedplugins')
    }

    def saveZendeskDetails = {
        log.debug "Saving Zendesk details: ${params}"
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
        log.debug "Creating Grailsrocks ticket: ${form.dump()}"

        if (form.hasErrors()) {
            session['grailsrocks.lastIssueForm'] = form
            redirect(action:'index')
        } else {
            def ud = grailsRocksService.userDetails
            def zd = zendeskService.getAPI(zendeskUrl)
            zd.user = ud.email
            zd.password = ud.password
            try {
                zd.createTicket(
                    subject:form.subject, 
                    description:form.description,
                    fields:[(GRAILSROCKS_ZENDESK_FIELD_PLUGIN):'plugin:'+form.plugin]
                )
                flash.message = "grailsrocks.issue.created"
                session['grailsrocks.lastIssueForm'] = null
            } catch (Exception hre) {
                log.error "Failed to create ticket at zendesk", hre
                session['grailsrocks.lastIssueForm'] = form
                flash.message = "grailsrocks.issue.create.failed"
                flash.messageType = "error"
            }
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
