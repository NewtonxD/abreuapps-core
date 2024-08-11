
$(function(){
    $("#form-guardar").on("submit", function(event){
        event.preventDefault();
        
        let fileInput = $('#fileInput')[0];
        let file = fileInput.files[0];
        const fileInfo = document.getElementById('fileInfo');
        let valido=false;
        
        if (file) {
            let formData = new FormData();
            formData.append('archivo', file);

            $.ajax({
                url: SERVER_IP+'/pub/upload',
                type: 'POST',
                data: formData,
                processData: false,
                contentType: false,
                async:false,
                success: function(response) {
                    if(response===""){
                        fileInfo.textContent = 'Archivo Vacio.';
                        fileInfo.style.display = 'block';
                    }else{
                        $("#archivo").val(response);
                        fileInfo.style.display = 'none';
                        valido=true;  
                    }
                    
                },
                error: function() {                    
                    fileInfo.textContent = 'Archivo invalido o corrupto.';
                    fileInfo.style.display = 'block';
                }
            });
        } else {
            fileInfo.textContent = 'Archivo invalido o corrupto.';
        }
        
        if(valido) guardar_datos();
    });    
});

function guardar_datos(){
    let data=dataPrepare("form-guardar");
    post_plantilla("/pub/save",data);
}

