<td>
    <a href="${p.docs ?: '#'}" class="${ p.description ? 'pluginPopover' : ''}" 
        rel="popover" data-content="${p.description ? p.description.encodeAsHTML() : ''}" data-original-title="Plugin: ${p.name.encodeAsHTML()}">${p.name.encodeAsHTML()}</a>
    <g:if test="${p.zombie}">
        <i class="icon-ban-circle" title="This is a zombie, do not use"></i>
    </g:if>
</td>
<td>
    <g:if test="${forceInfo || p.installed}">
        ${p.version.encodeAsHTML()}
        <g:if test="${p.installed}">
            <g:if test="${p.newerVersion}"><span class="label label-success label-icon"><i class="icon-arrow-left icon-white"></i> Upgrade ${p.newerVersion}</span></g:if>
        </g:if>
    </g:if>
    <g:else>Not installed</g:else>
</td>
<td>
    <g:each in="${p.licenses}" var="license">
        <span class="label label-${license ? 'success' : 'important'}">${license ? license.encodeAsHTML() : "Unknown"}</span>
    </g:each>
    <g:if test="${!p.licenses}">
        <span class="label label-important">Unknown</span>
    </g:if>
</td>
<td>
    <g:if test="${p.grailsrocks}">
        <a href="#" data-grailsrocks-plugin="${p.name.encodeAsHTML()}" class="btn btn-small grailsrocks-plugin">Get help</a>
    </g:if>
    <g:if test="${forceInfo || p.installed}">
        <g:if test="${p.docs}"><a href="${p.docs}">Docs</a></g:if>
        <g:if test="${p.src}"><a href="${p.src}">Source</a></g:if>
        <g:if test="${p.issues}"><a href="${p.issues}">Issues</a></g:if>
    </g:if>
    <g:else><a href="${p.docs}">View info</a></g:else>
</td>
