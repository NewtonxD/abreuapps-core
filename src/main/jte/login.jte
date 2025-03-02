@param String error_msg
@param String success_msg
@param String app_nombre
@param abreuapps.core.control.utils.Localizer localize

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="utf-8" />
    <meta http-equiv="X-UA-Compatible" content="IE=edge" />
    <meta
        name="viewport"
        content="width=device-width, initial-scale=1, shrink-to-fit=no"
        />
    <link rel="icon" type="image/x-icon" href="/content/favicon.ico">
    <meta name="description" content="Login Sistema ERP ${app_nombre}" />
    <meta name="author" content="Carlos Isaac Abreu Pérez" />
    <title>Login ERP - ${app_nombre}</title>
    <link href="/content/css/styles.css" rel="stylesheet" />
    <link href="/content/css/bootstrap.min.css" rel="stylesheet" />
</head>
<body class="secondary-color fondo-bonito">

    <div id="layoutAuthentication" class="h-100">

        <div id="layoutAuthentication_content" class="h-100">

            <main class="h-100 d-flex align-items-center justify-content-center">

                <div class="container"> 
                
                   <div class="row h-100 d-flex align-items-center justify-content-center">
                        <div class="col-10 col-lg-5">
                            <div class="card shadow-lg border-0 rounded-lg mt-5">
                                <form method="post">
                                    <div class="card-body">
                                        <div class="d-flex justify-content-between align-middle py-3 mb-3">
                                            <h2 class="ms-2">Login<br/><span>${app_nombre}</span></h2>
                                            <img  width="64px" height="64px" class="me-4" src="/content/assets/img/Omsafooter.webp" alt="OMSA"/>
                                        </div>
                                        <div class="form-floating mb-3">
                                            <input
                                                class="form-control"
                                                id="username"
                                                name="username"
                                                type="text"
                                                placeholder="${localize.apply("username")}"
                                                required="required"
                                                />
                                            <label for="username" class="text-muted">${localize.apply("username")}</label>
                                        </div>
                                        <div class="form-floating mb-3">
                                            <input
                                                class="form-control"
                                                id="password"
                                                name="password"
                                                type="password"
                                                placeholder="${localize.apply("password")}"
                                                required="required"
                                                />
                                            <label for="password" class="text-muted">${localize.apply("password")}</label>
                                        </div>
                                        @if(error_msg != null)
                                            <div class="alert alert-danger">
                                                <span>${error_msg}</span>
                                            </div>
                                        @endif
                                        @if(success_msg != null) 
                                            <div class="alert alert-success">
                                                <span>${success_msg}</span>
                                            </div>
                                        @endif
                                        <div
                                            class="d-flex py-3 align-items-center justify-content-end mb-0"
                                            >
                                            <!--<a class="small" href="/password">Olvido su Contraseña?</a>-->
                                            <button type="submit" class="btn btn-primary">
                                                <b>
                                                    ${localize.apply("login")}
                                                </b>
                                            </button>
                                        </div>
                                    </div>
                                </form>
                            </div>
                        </div>
                    </div>
                </div>
            </main>
        </div>
    </div>           
</body>
</html>
