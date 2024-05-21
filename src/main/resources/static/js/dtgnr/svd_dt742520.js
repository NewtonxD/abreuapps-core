
$(function(){
    $("#form-guardar").on("submit", function(event){
        event.preventDefault();
        guardar_datos();
    });
});

function guardar_datos(){
    let data=dataPrepare("form-guardar");
    post_plantilla("/dtgnr/save",data);
}