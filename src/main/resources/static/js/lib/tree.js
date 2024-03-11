$( function(){
    
    // Attach click event listener to checkboxes
    $('.tree input[type="checkbox"]').on('click', function() {
        if(this.checked){
            $(this).parents('li').children('input[type=checkbox]').prop('checked',true);
        }    
        $(this).parent().find('input[type=checkbox]').prop('checked',this.checked);	
    
    });

});