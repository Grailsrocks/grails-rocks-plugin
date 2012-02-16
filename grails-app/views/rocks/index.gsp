<html>
<head>
    <meta name="layout" content="grailsrocks"/>
</head>
<body>
    <div class="content">
        <div class="page-header">
          <h1>Grails Plugins</h1>
        </div>
        <div class="row">
            <div class="span6">
                <p>Here you can view the plugins you have installed, view their documentation, browse source and view issues.
                    Use the "all plugins" tab to find other plugins to install.</p>
            </div>
            <div class="span6">
                <g:if test="${flash.message}">
                    <div class="alert alert-success">
                        <h4><g:message code="${flash.message}.title" encodeAs="HTML"/></h4>
                        <p><g:message code="${flash.message}" encodeAs="HTML"/></p>
                    </div>
                </g:if>
            </div>
        </div>
        <div class="row">
            <div class="span12 supportedplugins">
                <ul class="nav nav-tabs">
                    <li class="active"><a href="#installedplugins" id="installedplugins_tab" data-toggle="tab">Installed plugins (${installedPlugins.size()})</a></li>
                    <li><a href="#supportedplugins" id="supportedplugins_tab" data-toggle="tab">Supported plugins (${supportedPlugins.size()})</a></li>
                    <li><a href="#allplugins" id="allplugins_tab" data-toggle="tab">All plugins (${allPlugins.size()})</a></li>
                </ul>

                <div class="tab-content">

                    <div class="active tab-pane" id="installedplugins">
                        <table class="table table-condensed table-striped">
                            <tr><th>Plugin name</th><th>Version</th><th>License</th><th></th></tr>
                        <g:each in="${installedPlugins}" var="p">
                            <tr><g:render template="pluginitem" model="[p:p, subscriber:subscriber]"/></tr>
                        </g:each>
                        </table>
                    </div>

                    <div class="tab-pane" id="supportedplugins">
                        <div class="row">
                            <div class="span8">
                                <p>The following plugins have commercial support options.</p>
                                <table class="table table-condensed table-striped">
                                    <tr><th>Plugin name</th><th>Version</th><th>License</th><th></th></tr>
                                <g:each in="${supportedPlugins}" var="p">
                                      <tr><g:render template="pluginitem" model="[p:p, subscriber:subscriber]"/></tr>
                                </g:each>
                                </table>
                            </div>
                            <div class="span4">
                                <g:render template="grailsrocks_support"/>
                            </div>
                        </div>
                    </div>

                    <div class="tab-pane" id="allplugins">
                        <form action="http://grails.org/plugin/search" class="well form-search">
                            <input name="q" class="input-medium search-query"/>
                            <button type="submit" class="btn">Search Grails.org</button>
                        </form>
                        <table class="table table-condensed table-striped">
                            <tr><th>Plugin name</th><th>Version</th><th>License</th><th></th></tr>
                        <g:each in="${allPlugins}" var="p">
                            <tr><g:render template="pluginitem" model="[p:p, forceInfo:true, subscriber:subscriber]"/></tr>
                        </g:each>
                        </table>
                    </div>

                </div>
            </div>


        </div>
      </div>
      
        <div id="new-ticket-modal" class="modal hide fade">
            <div class="modal-header">
                <a class="close modal-close">&times;</a>
                <h1>Create a new support ticket</h1>
            </div>
            <form action="${g.createLink(action:'createGrailsrocksTicket')}" id="new-grailsrocks-ticket" class="form-horizontal">
                <input type="hidden" name="plugin" id="new-grailsrocks-ticket-plugin" value="${lastIssueForm?.plugin ? lastIssueForm.plugin.encodeAsHTML() : ''}"/>
                <div class="modal-body">
                    <g:if test="${subscriber}">
                        <p>You are creating a new ticket for the <span id="new-ticket-plugin-name"></span> plugin. If this is not the 
                        plugin you need to create a new incident for, please cancel this and select the correct plugin.</p>
                        <g:if test="${lastIssueForm}">
                            <div id="new-grailsrocks-ticket-error" class="alert alert-error">
                                <p><g:message code="grailsrocks.ticket.invalid" encodeAs="HTML"/></p>
                            </div>
                        </g:if>
                        
                        <fieldset>
                           <div class="control-group">
                                 <label for="new-grailsrocks-ticket-subject" class="control-label">Subject</label>
                                 <div class="controls">
                                     <input name="subject" id="new-grailsrocks-ticket-subject" value="${lastIssueForm?.subject ? lastIssueForm.subject.encodeAsHTML() : ''}" class="xlarge" size="30"/>
                                 </div>
                             </div>
                             <div class="control-group">
                                 <label for="description" class="control-label">Description</label>
                                 <div class="controls">
                                     <textarea name="description" 
                                        id="new-grailsrocks-ticket-description" rows="5" class="xlarge">${lastIssueForm?.description ? lastIssueForm.description.encodeAsHTML() : ''}</textarea>
                                 </div>
                             </div>
                        </fieldset>
                    </g:if>
                    <g:else>
                        <p>Sorry, you cannot raise tickets for Grailsrocks supported plugins unless you have paid for 
                            commercial support. See <a href="http://grailsrocks.com">Grailsrocks.com</a> for details.</p>
                    </g:else>
                </div>
                <div class="modal-footer">
                    <g:if test="${subscriber}">
                        <input type="submit" class="btn btn-success" value="Create ticket"/>
                        <a class="btn btn-small btn-danger modal-close">Cancel</a>
                    </g:if>
                    <g:else>
                        <a class="btn btn-primary modal-close">OK, I'll think about subscribing to a support plan</a>
                    </g:else>
                </div>
            </form>
        </div>
        
        <g:if test="${lastIssueForm}">
            <r:script>
                showCreateGrailsrocksIssueDialog();
            </r:script>
        </g:if>
    </body>
</html>