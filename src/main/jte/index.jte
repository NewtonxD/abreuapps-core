@import java.util.List
@import abreuapps.core.control.transporte.LogVehiculo
@import abreuapps.core.control.general.Persona
@import java.util.Map

@param String server_ip
@param String app_nombre
@param List<LogVehiculo> vhl_log
@param int active_views
@param int today_views
@param Persona datos_personales
@param Map< String, Boolean > permisos
@param Map< String, String > conf

<!DOCTYPE html>
<html lang="es">
    <head>
        <meta charset="utf-8" />
        <meta http-equiv="X-UA-Compatible" content="IE=edge" />
        <meta
            name="viewport"
            content="width=device-width, initial-scale=1, shrink-to-fit=no"
        />
        <meta name="description" content="Sistema ERP ${app_nombre}" />
        <meta name="author" content="Carlos Isaac Abreu Pérez" />
        <title>${app_nombre} ERP</title>
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
            const SERVER_IP = '${server_ip}';
        </script>
        <script src="/content/js/global_vars.js"></script>
        @template.fragments.header( app_nombre = app_nombre )
        <div id="layoutSidenav">
            @template.fragments.menu(
                app_nombre = app_nombre,
                datos_personales = datos_personales,
                permisos = permisos,
                conf = conf
            )
            <div id="layoutSidenav_content">
                <div aria-live="polite" aria-atomic="true" class="position-relative">
                    <div id="toast-container" class="toast-container top-0 end-0 p-3"></div>
                </div>
                <main>
                    @template.fragments.general(
                        active_views = active_views,
                        today_views = today_views,
                        vhl_log = vhl_log
                    )
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