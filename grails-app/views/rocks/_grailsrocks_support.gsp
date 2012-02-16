<g:if test="${!userDetails.password}">
    <h2>Get Great Support</h2>
    <p>Grailsrocks offers affordable support for list of supported plugins from <b>as little as $50 per month.</b></p>
    <p>Get high quality personal support and contribute to the ongoing development and refinement of these and future plugins.
       Get free some merchandise. Feel a warm glow!</p
       <p>
       For more details <a href="http://grailsrocks.com">please see here</a>.</p>
   <h3>I'm a Grailsrocks customer</h3>
   <p>In that case, please enter your Zendesk login details below and you will be able to review and create your support 
       incidents right here!</p>
   <g:form action="saveZendeskDetails" class="saveZendeskDetails">
       <fieldset>
           <div class="control-group">
               <label for="email" class="control-label">Email</label>
               <div class="controls">
                   <input name="email" id="email" class="input-xlarge" size="30"/>
               </div>
           </div>
           <div class="control-group">
               <label for="password" class="control-label">Password</label>
               <div class="controls">
                   <input name="password" id="password" class="input-xlarge" type="password" size="30"/>
               </div>
           </div>
           <div class="form-actions">
               <input type="submit" class="btn btn-primary" value="Save my details"/>
           </div>
       </fieldset>
   </g:form>
</g:if>
<g:else>
    <div class="supportinfo">
        <h2>Grailsrocks Support</h2>
        <div class="supportstatus thumbnail">
            <img src="${g.resource(dir:'images/supportstatus/', supportStatus+'.jpg')}" width="50" height="50"/>
            <span class="label label-${[open:'success', closed:'important'][supportStatus]} label-icon"><i class="icon-white ${[open:'icon-ok', closed:'icon-ban-circle'][supportStatus]}"></i>
            <g:message code="grailsrocks.support.status.${supportStatus}" encodeAs="HTML"/></span>
        </div>
        <div class="text">
            <p>
                You are logged in as ${userDetails.email.encodeAsHTML()}.
            </p>
            <p>
                You can <g:link action="resetZendeskDetails">log out</g:link>.
            </p>
        </div>
    </div>
    <div class="tickets">
        <h3>Active tickets</h3>
        <ul class="unstyled">
        <g:each in="${openTickets}" var="t">
            <li><span class="label label-${[0:'important', 1:'info', 2:'warning', 3:'success'][t.status_id]}"><zd:status ticket="${t}"/></span> 
                <a href="${zd.createLink(zendeskUrl:zendeskUrl, ticket:t).encodeAsHTML()}">${t.subject.encodeAsHTML()}</a></li>
        </g:each>
        </ul>
        <%--
        <h3>Recent tickets</h3>
        <ul class="unstyled">
        <g:each in="${recentTickets}" var="t">
        <li><span class="label label-notice"><zd:status ticket="${t}"/></span> 
            <a href="${zd.createLink(zendeskUrl:zendeskUrl, ticket:t).encodeAsHTML()}">${t.subject.encodeAsHTML()}</a></li>
        </g:each>
        </ul>
        --%>
        <a class="btn" href="https://grailsrocks.zendesk.com">View all tickets</a>
        <a class="btn" href="https://grailsrocks.recurly.com">View support plan</a>
    </div>
</g:else>
