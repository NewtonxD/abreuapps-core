
$(function(){
    $("#form-guardar").on("submit", function(event){  
        event.preventDefault(); 
        guardar_datos();
    });    
});

function guardar_datos(){
    let data=dataPrepare("form-guardar");
    
    $("#getPolylineData").click();
    
    data.push({name:"data_poly",value:window.data_poly});
    delete window.data_poly;
    
    post_plantilla("/rta/save",data);
}