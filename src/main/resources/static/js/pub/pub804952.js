
var startDateInput = document.getElementById('startDate');
var endDateInput = document.getElementById('endDate');
var dateDifference = document.getElementById('dateDifference');

// Configurar fechas iniciales
var today = new Date();
var tomorrow = new Date();
tomorrow.setDate(today.getDate() + 1);

startDateInput.valueAsDate = today;
endDateInput.valueAsDate = tomorrow;

startDateInput.min = today.toISOString().split('T')[0];

// Actualizar diferencia de fechas
function updateDateDifference() {
    const startDate = new Date(startDateInput.value);
    const endDate = new Date(endDateInput.value);

    const timeDifference = endDate.getTime() - startDate.getTime();
    const dayDifference = Math.ceil(timeDifference / (1000 * 3600 * 24));

    if (dayDifference < 1) {
        dateDifference.textContent = 'La diferencia entre las fechas no puede ser menor a 1 día.';
        dateDifference.style.display = 'block';
        document.getElementById('dias').textContent = 'N/A';
        return;
    }else{
        document.getElementById('dias').textContent = `${dayDifference}`;
        dateDifference.style.display = 'none';
    }

}

startDateInput.addEventListener('change', function() {
    const startDate = new Date(startDateInput.value);
    const endDate = new Date(endDateInput.value);

    if (startDate > endDate ) {
        const currentDifference = Math.ceil((endDate.getTime() - startDate.getTime()) / (1000 * 3600 * 24));
        endDateInput.valueAsDate = new Date(startDate.setDate(startDate.getDate() + Math.max(1, currentDifference)));
    }
    updateDateDifference();
});

endDateInput.addEventListener('change', function() {
    const startDate = new Date(startDateInput.value);
    const endDate = new Date(endDateInput.value);

    if (endDate <= startDate) {
        const newEndDate = new Date(startDate);
        newEndDate.setDate(startDate.getDate() + 1);
        endDateInput.valueAsDate = newEndDate;
    }
    updateDateDifference();
});

updateDateDifference();

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

    if (fileType.startsWith('video/')) {
        if (fileType !== 'video/mp4') {
            fileInfo.textContent = 'Solo se permiten videos en formato mp4.';
            this.value = ''; // Clear the input
        }else{
            
            const videoElement = document.createElement('video');
            videoElement.src = URL.createObjectURL(file);
            videoElement.controls = true;
            
            videoElement.onloadedmetadata = function() {
                URL.revokeObjectURL(this.src); // Clean up object URL
                if (videoElement.duration > maxVideoDurationSec) {
                    fileInfo.textContent = 'El video no puede durar más de 30 segundos.';
                    document.getElementById('fileInput').value = '';
                } else if (videoElement.videoHeight > maxVideoResolution || videoElement.videoWidth > maxVideoResolution) {
                    fileInfo.textContent = 'La resolución del video no puede ser mayor a 720p.';
                    document.getElementById('fileInput').value = '';
                }else{
                    fileInfo.textContent='';
                    preview.appendChild(videoElement); // Mostrar el video
                    previewContainer.style.display = 'block';
                }
            };
            
        }
    } else if (fileType.startsWith('image/')) {
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


