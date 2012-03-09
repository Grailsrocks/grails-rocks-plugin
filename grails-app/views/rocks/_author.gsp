<div class="row author-panel">
    <div class="author-thumb">
        <g:if test="${author.email}"><img src="https://secure.gravatar.com/avatar/${author.email.encodeAsURL()}?s=40&d=${r.resource(dir:'images', absolute:true, file:'no-avatar.png', plugin:'rocks').encodeAsURL()}" width="40" height="40"/></g:if>
        <g:else><r:img dir="images" file="no-avatar.png" plugin="rocks" width="40" height="40"/></g:else>
    </div>
    <div class="author-info">
        <span class="author-name">${author.name.encodeAsHTML()}</span>
        who contributed to
        <i>${g.join(in:author.plugins).encodeAsHTML()}</i>
    </div>
</div>

