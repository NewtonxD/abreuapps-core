<!DOCTYPE html>
<html lang="es">
    <head>
        <meta charset="utf-8" />
        <meta http-equiv="X-UA-Compatible" content="IE=edge" />
        <meta
            name="viewport"
            content="width=device-width, initial-scale=1, shrink-to-fit=no"
        />
        <meta name="description" content="OMSA - Plataforma" />
        <meta name="author" content="Carlos Isaac Abreu Pérez" />
        <title>OMSA @app_provincia - Plataforma</title>
        <link rel="icon" type="image/x-icon" href="/content/favicon.ico">
        <link href="/content/css/jquery.dataTables.min.css" rel="stylesheet"/>
        <link href="/content/css/styles.css" rel="stylesheet" />
        <link href="/content/css/treestyle.css" rel="stylesheet" />
        <link rel="stylesheet" href="/content/css/font-awesome.min.css"/>
        <link href="/content/css/bootstrap.min.css" rel="stylesheet" />
        <link rel="stylesheet" href="/content/css/leaflet.css"/>
        <link href="/content/css/leaflet-geoman.css" rel="stylesheet" />
    </head>

    <body class="fondo-bonito-paginas">
        <script src="/content/js/lib/jquery-3.6.4.min.js"></script>
        <script src="/content/js/lib/jquery.dataTables.min.js"></script>
        <script src="/content/js/lib/bootstrap.bundle.min.js"></script>
        <script type="text/javascript">
            const SERVER_IP = '@server_ip';
        </script>
        <script src="/content/js/global_vars.js"></script>
        @template("fragments/general.jte", "header")
        <div id="layoutSidenav">
            @template("fragments/general.jte", "menu")
            <div id="layoutSidenav_content">
                <div aria-live="polite" aria-atomic="true" class="position-relative">
                    <div id="toast-container" class="toast-container top-0 end-0 p-3"></div>
                </div>
                <main>
                    @template("fragments/general.jte", "content")
                </main>
            </div>
            <div class="overlay" onclick="hideSidebar()"></div>
        </div>
        <script src="/content/js/checkstatus.js"></script>
        <script src="/content/js/scripts.js"></script>
        <script src="/content/js/access.js"></script>
        <script src="/content/js/context-loading.js"></script>
    </body>
</html>