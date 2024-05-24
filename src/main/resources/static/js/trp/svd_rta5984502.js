
$(function(){
    $("#form-guardar").on("submit", function(event){
        event.preventDefault();
        guardar_datos();
    });    
});

function guardar_datos(){
    
    var polylines=getPolyline();
    if (polylines.length <= 0) {
        //alert o mensaje en la pantalla.
        console.log('No polylines found!');
        return;
    } 
    
    
    
    let data=dataPrepare("form-guardar");
    post_plantilla("/rta/save",data);
}
