<!DOCTYPE html>
<html lang="es">
    <head>
        <meta charset="utf-8" />
        <meta http-equiv="X-UA-Compatible" content="IE=edge" />
        <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no" />
        <meta name="description" content="OMSA - Mapa" />
        <meta name="author" content="Carlos Isaac Abreu Pérez" />
        <title>OMSA @app_provincia - Mapa</title>
        <link rel="icon" type="image/x-icon" href="/content/favicon.ico" />
        <link rel="stylesheet" href="/content/css/bootstrap.min.css" media="none" onload="if(media!='all')media='all'" />
        <link href="/content/css/mapa_estilos.css" rel="stylesheet" />
        <link rel="stylesheet" href="/content/css/leaflet.css" />
    </head>
    <body>
        <div id="loading">
            <img width="64px" height="64px" src="/content/assets/img/Omsafooter.webp" alt="OMSA" />
            <h1>@app_provincia</h1>
            <span id="loader" style="margin-top: 4px;"></span>
        </div>
        <div aria-live="polite" aria-atomic="true" class="position-relative">
            <div id="toast-container" class="toast-container top-0 end-0 p-3"></div>
        </div>
        <div id="adContainer" class="ad-container">
            <div id="adContent" class="ad-content"></div>
            <button id="closeAd" class="close-btn" style="display: none;"><b>x</b></button>
        </div>
        <div id="map"></div>
        <script>
            window.addEventListener('load', function () {
                setTimeout(function () {
                    document.getElementById('loading').style.display = 'none';
                }, 1500);
            });
        </script>
        <script src="/content/js/lib/jquery-3.6.4.min.js"></script>
        <script src="/content/js/lib/bootstrap.bundle.min.js"></script>
        <script type="text/javascript">
            const SERVER_IP = '${server_ip}';
            const BASE_LAT = ${base_lat};
            const BASE_LNG = ${base_lng};
            const BASE_DIR = '${base_dir}';
        </script>
        <script src="/content/js/global_vars.js"></script>
        <script async src="/content/js/checkstatus.js"></script>
        <script async src="/content/js/ads.js"></script>
        <script src="/content/js/lib/leaflet.js"></script>
        <script src="/content/js/lib/leaflet.Marker.SlideTo.js"></script>
        <script src="/content/js/fetch_cache.js"></script>
        <script defer src="/content/js/trp/map574702.js"></script>
    </body>
</html>
