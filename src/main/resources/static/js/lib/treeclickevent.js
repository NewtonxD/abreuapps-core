/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Other/javascript.js to edit this template
 */

$(function(){
    
    $('.tree input[type="checkbox"]').on('click', function() {
        if(this.checked){
            $(this).parent().parents('li').children('input[type=checkbox]').prop('checked',true);
        }    
        $(this).parent().parent().find('input[type=checkbox]').prop('checked',this.checked);	
    
    });
    
});
