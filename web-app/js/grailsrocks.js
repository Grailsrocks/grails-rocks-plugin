function showCreateGrailsrocksIssueDialog() {
    $('#new-ticket-plugin-name').text($('#new-grailsrocks-ticket-plugin').val());
    $('#new-ticket-modal').modal( { backdrop:"static", keyboard:true });        
}

$(function() {
    if(window.location.hash) {
        $(window.location.hash+'_tab').tab("show");
    } else {
        $('.tabs').tab('show');
    }
    $('.modal-close').click( function(event) {
        $(this).parents('.modal').modal('hide');
    });
    $('.grailsrocks-plugin').click( function(event) { 
        var plugin = $(this).data('grailsrocks-plugin');
        $('#new-grailsrocks-ticket')[0].reset();
        $('#new-grailsrocks-ticket-error').hide();
        $('#new-ticket-plugin-name').text(plugin);
        $('#new-grailsrocks-ticket-plugin').val(plugin);        
        event.preventDefault();
        event.stopPropagation();
        $('#new-ticket-modal').modal( { backdrop:"static", keyboard:true }).on('shown', function() {
            $('#new-grailsrocks-ticket-subject').focus();            
        });     
        
        return false;
    });
    $('#new-grailsrocks-ticket input[type=submit]').click(function() {
        $(this).button('loading')
    });
    $('button').addClass('btn');
    $('a.pluginPopover').popover();
    $('input[type=submit]').addClass('btn');
});