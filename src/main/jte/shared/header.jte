
@param String app_nombre

<nav class="sb-topnav navbar navbar-expand navbar-dark bg-dark align-center">
    <!-- Navbar Brand-->
    <a class="navbar-brand ps-3" href="/main/index">
        <img class="pb-2" width="32px" height="32px" src="/content/assets/img/Omsafooter.webp" />
        <span class="fs-4 fw-semibold">${app_nombre} ERP</span>
    </a>
    <!-- Sidebar Toggle-->
    <button
        type="button"
        class="btn btn-link btn-sm order-1 order-lg-0 me-4 me-lg-0 toggle-btn text-white"
        onclick="toggleSidebar()"
    >
        <i class="fa fa-bars"></i>
    </button>
    <div class="d-none d-md-inline-block form-inline ms-auto me-0 me-md-3 my-2 my-md-0"></div>
    <%--<h5 class="text-white fw-light mt-2 d-none d-md-block">@app_provincia</h5>--%>
    <ul class="navbar-nav ms-auto ms-md-0 me-3 me-lg-4">
        <li class="nav-item dropdown">
            <a
                class="nav-link dropdown-toggle"
                id="navbarDropdown"
                href="#"
                role="button"
                data-bs-toggle="dropdown"
                aria-expanded="false"
            ><i class="fa fa-user fa-fw"></i></a>
            <ul class="dropdown-menu dropdown-menu-end" aria-labelledby="navbarDropdown">
                <li><a class="dropdown-item config-user" href="#">Configuración</a></li>
                <li><hr class="dropdown-divider" /></li>
                <li>
                    <a class="dropdown-item" href="/auth/logout">Cerrar Sesión</a>
                </li>
            </ul>
        </li>
    </ul>
</nav>
