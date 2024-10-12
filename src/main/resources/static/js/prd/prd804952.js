if(document.getElementById('fileInput'))
document.getElementById('fileInput').addEventListener('change', function() {
    const file = this.files[0];
    const fileInfo = document.getElementById('fileInfo');
    const previewContainer = document.getElementById('previewContainer');
    const preview = document.getElementById('preview');
    
    previewContainer.style.display = 'none'; 
    fileInfo.textContent = '';
    preview.innerHTML = ''; // Limpiar el contenido anterior

    if (!file) return;

    const fileType = file.type;
    const fileSizeMB = file.size / (1024 * 1024);
    const maxImageSizeMB = 5;
    const maxVideoDurationSec = 30;
    const maxVideoResolution = 1280;

    if (fileType.startsWith('image/')) {
        if (!['image/jpeg', 'image/webp', 'image/png'].includes(fileType)) {
            fileInfo.textContent = 'Solo se permiten imágenes en formatos jpeg, webp o png.';
            this.value = ''; // Clear the input
        }else{
            if (fileSizeMB > maxImageSizeMB) {
                fileInfo.textContent = 'La imagen no puede ser mayor a 5MB.';
                this.value = ''; // Clear the input
            }else{
                
                const imgElement = document.createElement('img');
                imgElement.src = URL.createObjectURL(file);

                imgElement.onload = function() {
                    URL.revokeObjectURL(this.src);
                    preview.appendChild(imgElement); 
                    previewContainer.style.display = 'block';
                };
                
            }
            
        }
    } else {
        fileInfo.textContent = 'Tipo de archivo no soportado.';
        this.value = ''; // Clear the input
    }

    setTimeout(function(){
        if(fileInfo.textContent!=='') fileInfo.style.display = 'block';
        else fileInfo.style.display = 'none';
    },500);

});


