<td>
    <g:if test="${(p.grailsrocks && p.installed) || (p.grailsrocks && forceInfo)}">
        <a href="#" data-grailsrocks-plugin="${p.name.encodeAsHTML()}" class="grailsrocks-plugin">${p.name.encodeAsHTML()}</a>
    </g:if>
    <g:else>
        <a href="${p.docs ?: '#'}" class="${ !p.installed && p.description ? 'pluginPopover' : ''}" rel="popover" data-content="${p.description ? p.description.encodeAsHTML() : ''}" data-original-title="Plugin: ${p.name.encodeAsHTML()}">${p.name.encodeAsHTML()}</a>
    </g:else>
    <g:if test="${p.grailsrocks}">
        <span class="label important">GRAILSROCKS</span>
    </g:if>
</td>
<td>
    <g:if test="${forceInfo || p.installed}">
        ${p.version.encodeAsHTML()}
        <g:if test="${p.installed}">
            <g:if test="${p.newerVersion}"><span class="label success">&larr; Upgrade: ${p.newerVersion}</span></g:if>
        </g:if>
    </g:if>
    <g:else>Not installed</g:else>
</td>
<td>
    <span class="label ${p.license ? 'success' : 'default'}">${p.license ? p.license.encodeAsHTML() : "License?"}</span>
</td>
<td>
    <g:if test="${forceInfo || p.installed}">
        <g:if test="${p.docs}"><a href="${p.docs}">Docs</a></g:if>
        <g:if test="${p.src}"><a href="${p.src}">Source</a></g:if>
        <g:if test="${p.issues}"><a href="${p.issues}">Issues</a></g:if>
    </g:if>
    <g:else><a href="${p.docs}">View info</a></g:else>
</td>
