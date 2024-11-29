function displayMedia(filename) {
    const mediaContainer = document.getElementById('preview');
    const url = `${SERVER_IP}/prd/archivo/${filename}`;
    mediaContainer.innerHTML = ''; 
    const imgElement = document.createElement('img');
    imgElement.src = url;
    imgElement.alt = filename;
    imgElement.style.maxWidth = '100%';
    mediaContainer.appendChild(imgElement);
}

displayMedia($("#archivo").val()); 
