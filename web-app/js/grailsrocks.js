$(function() {
    $('.tabs').tabs();
    $('#new-ticket-modal').modal( { backdrop:"static", keybaord:true });        
    $('.modal-close').click( function(event) {
        $(this).parents('.modal').modal('hide');
    });
    $('.grailsrocks-plugin').click( function(event) { 
        $('#new-ticket-plugin-name').text($(this).data('grailsrocks-plugin'));
        event.preventDefault();
        event.stopPropagation();
        $('#new-ticket-modal').modal('show');
        return false;
    });
    $('button').addClass('btn');
    $('a.pluginPopover').popover();
    $('input[type=submit]').addClass('btn');
});