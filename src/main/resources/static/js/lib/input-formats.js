$(function(){
    
    const cedula=/(\d{3})(\d{7})(\d)/;
    const patron_ced="(\\d{3})-(\\d{7})-(\\d{1})";
    const max_cedula=13;
    const txt_patron_ced="Cédula invalida; Formato: XXX-XXXXXXX-X";

    const telefono=/(\d{3})(\d{3})(\d{4})/;
    const patron_tel="(\\d{3})-(\\d{3})-(\\d{4})";
    const max_telefono=12;
    const txt_patron_tel="Teléfono invalido; Formato: XXX-XXX-XXXX";
    
    $(".cedula").attr("pattern",patron_ced);
    $(".cedula").attr("max",max_cedula);
    $(".cedula").attr("title",txt_patron_ced);
    
    $(".telefono").attr("pattern",patron_tel);
    $(".telefono").attr("max",max_telefono);
    $(".telefono").attr("title",txt_patron_tel);
           
    
    $(".cedula").on("keyup",function(){
        $(this).val($(this).val().replace(/\D/g, '').replace(cedula, '$1-$2-$3'));
    });

    $(".telefono").on("keyup",function(){
       $(this).val($(this).val().replace(/\D/g, '').replace(telefono, '$1-$2-$3'));
    });

});
