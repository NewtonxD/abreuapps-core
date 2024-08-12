function displayMedia(filename) {
    const mediaContainer = document.getElementById('preview');
    const url = `${SERVER_IP}/p/pub/archivo/${filename}`;
    mediaContainer.innerHTML = ''; 

    if (filename.endsWith('.mp4')) {
        const videoElement = document.createElement('video');
        videoElement.src = url;
        videoElement.controls = true;
        videoElement.style.maxWidth = '100%';
        mediaContainer.appendChild(videoElement);
    } else {
        const imgElement = document.createElement('img');
        imgElement.src = url;
        imgElement.alt = filename;
        imgElement.style.maxWidth = '100%';
        mediaContainer.appendChild(imgElement);
    }
}

displayMedia($("#archivo").val()); 
