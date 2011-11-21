<td>
    <g:if test="${p.installed}">
        <g:if test="${p.newerVersion}"><span class="label success">New: ${p.newerVersion}</span></g:if>
    </g:if>
    <g:if test="${p.grailsrocks && p.installed}">
        <a href="#" data-grailsrocks-plugin="${p.name.encodeAsHTML()}" class="grailsrocks-plugin">${p.name.encodeAsHTML()}</a>
    </g:if>
    <g:else>
        ${p.name.encodeAsHTML()}
    </g:else>
</td>
<td>
    <g:if test="${p.installed}">
        ${p.version.encodeAsHTML()}
    </g:if>
    <g:else>Not installed</g:else>
</td>
<td>
    <span class="label ${p.license ? 'success' : 'default'}">${p.license ? p.license.encodeAsHTML() : "License?"}</span>
    <g:if test="${p.installed}">
        <g:if test="${p.docs}"><a href="${p.docs}">Docs</a></g:if>
        <g:if test="${p.src}"><a href="${p.src}">Source</a></g:if>
        <g:if test="${p.issues}"><a href="${p.issues}">Issues</a></g:if>
    </g:if>
    <g:else><a href="${p.docs}">What does it do?</a></g:else>
</td>
