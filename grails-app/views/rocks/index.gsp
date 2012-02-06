<html>
<head>
    <meta name="layout" content="grailsrocks"/>
</head>
<body>
    <div class="content">
        <div class="page-header">
          <h1>Plugins</h1>
        </div>
        <div class="row">
            <div class="span10 supportedplugins">
                <p>Plugins marked with a red label have commercial support.</p>
                <ul class="tabs">
                    <li class="active"><a href="#installedplugins">Installed plugins (${installedPlugins.size()})</a></li>
                    <li><a href="#supportedplugins">Supported plugins (${supportedPlugins.size()})</a></li>
                    <li><a href="#allplugins">All plugins (${allPlugins.size()})</a></li>
                </ul>

                <div class="tab-content">

                    <div class="active tab-pane" id="installedplugins">
                        <table class="condensed-table">
                        <g:each in="${installedPlugins}" var="p">
                            <tr><g:render template="pluginitem" model="[p:p, subscriber:subscriber]"/></tr>
                        </g:each>
                        </table>
                    </div>

                    <div class="tab-pane" id="supportedplugins">
                        <p>The following plugins have commercial support options.</p>
                        <table class="condensed-table">
                        <g:each in="${supportedPlugins}" var="p">
                              <tr><g:render template="pluginitem" model="[p:p, subscriber:subscriber]"/></tr>
                        </g:each>
                        </table>
                    </div>

                    <div class="tab-pane" id="allplugins">
                        <p>You can visit the <a href="http://grails.org/plugins">Grails Plugin Portal</a> to search and get more info.</p>
                        <table class="condensed-table">
                        <g:each in="${allPlugins}" var="p">
                            <tr><g:render template="pluginitem" model="[p:p, forceInfo:true, subscriber:subscriber]"/></tr>
                        </g:each>
                        </table>
                    </div>

                </div>
            </div>

          <div class="span6">
            <g:if test="${!userDetails.password}">
                <h2>Get Great Support</h2>
                <p>Grailsrocks offers affordable support for list of supported plugins from <b>as little as $50 per month.</b></p>
                <p>Get high quality personal support and contribute to the ongoing development and refinement of these and future plugins.
                   Get free some merchandise. Feel a warm glow!</p
                   <p>
                   For more details <a href="http://grailsrocks.com">please see here</a>.</p>
               <h2>But I've already paid!</h2>
               <p>In that case, please enter your Zendesk login details below and you will be able to review and create your support 
                   incidents right here!</p>
               <g:form action="saveZendeskDetails" class="saveZendeskDetails">
                   <fieldset>
                       <div class="clearfix">
                           <label for="email">Email</label>
                           <div class="input">
                               <input name="email" id="email" class="" size="30"/>
                           </div>
                       </div>
                       <div class="clearfix">
                           <label for="password">Password</label>
                           <div class="input">
                               <input name="password" id="password" class="" type="password" size="30"/>
                           </div>
                       </div>
                       <div class="clearfix">
                           <div class="input">
                               <input type="submit" class="btn primary" value="Save"/>
                           </div>
                       </div>
                   </fieldset>
               </g:form>
            </g:if>
            <g:else>
                <div class="row">
                    <div class="span6">
                        <div class="supportinfo">
                            <h2>Your incidents</h2>
                            <p>You are logged in as ${userDetails.email.encodeAsHTML()}. You can <g:link action="resetZendeskDetails">change this</g:link>.</p>
                        </div>
                        <div class="support">
                            <div>
                                <div class="ticketsleft">
                                    <h3>${ticketsLeft}<small> tickets remaining this month</small></h3>
                                </div>
                                <div class="supportstatus">
                                    <img class="thumbnail" src="${g.resource(dir:'supportstatus', supportStatus+'.png')}" width="50" height="50"/><br/>
                                    <span class="label ${[open:'success', closed:'important'][supportStatus]}"><g:message code="grailsrocks.support.status.${supportStatus}" encodeAs="HTML"/></span>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="tickets row">
                    <div class="span6">
                        <h3>Active tickets</h3>
                        <ul class="unstyled">
                        <g:each in="${openTickets}" var="t">
                            <li><span class="label ${[new:'important', open:'warning'][t.status]}">${t.status}</span> <a href="${t.link}">${t.title.encodeAsHTML()}</a></li>
                        </g:each>
                        </ul>
                        <h3>Recent tickets</h3>
                        <ul class="unstyled">
                        <g:each in="${recentTickets}" var="t">
                            <li><span class="label notice">Closed</span> <a href="${t.link}">${t.title.encodeAsHTML()}</a></li>
                        </g:each>
                        </ul>
                        <a class="btn" href="https://grailsrocks.recurly.com">View or update your support plan</a>
                    </div>
                </div>
            </g:else>
          </div>

        </div>
      </div>
      
        <div id="new-ticket-modal" class="modal hide fade">
            <div class="modal-header">
                <a href="#" class="close">X</a>
                <h1>Create a new support ticket</h1>
            </div>
            <form>
                <div class="modal-body">
                    <g:if test="${subscriber}">
                        <p>You are creating a new ticket for the <span id="new-ticket-plugin-name"></span> plugin. If this is not the 
                        plugin you need to create a new incident for, please cancel this and select the correct plugin.</p>
                        <fieldset>
                           <div class="clearfix">
                                 <label for="subject">Subject</label>
                                 <div class="input">
                                     <input name="subject" id="subject" class="xlarge" size="30"/>
                                 </div>
                             </div>
                             <div class="clearfix">
                                 <label for="password">Description</label>
                                 <div class="input">
                                     <textarea name="description" id="description" class="xlarge"></textarea>
                                 </div>
                             </div>
                        </fieldset>
                    </g:if>
                    <g:else>
                        <p>Sorry, you cannot raise tickets for Grailsrocks supported plugins unless you have paid for 
                            commercial support. See the options available after you have cancelled this form.</p>
                    </g:else>
                </div>
                <div class="modal-footer">
                    <g:if test="${subscriber}">
                        <input type="submit" class="btn primary" value="Create ticket"/>
                        <a class="btn small danger modal-close">Cancel</a>
                    </g:if>
                    <g:else>
                        <a class="btn small primary modal-close">OK, I'll think about subscribing to a support plan</a>
                    </g:else>
                </div>
            </form>
        </div>
</body>
</html>