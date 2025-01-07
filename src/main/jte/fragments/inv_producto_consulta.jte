@import abreuapps.core.control.inventario.ProductoDTO
@import java.util.List

@param boolean inv_producto_consulta
@param boolean inv_producto_registro
@param String msg
@param Boolean status
@param List<ProductoDTO> productos

@template.shared.consulta(
    titulo = "Productos",
    id_registro = "inv_producto_registro",
    permiso_consulta = inv_producto_consulta,
    permiso_registro = inv_producto_registro,
    msg = msg, status = status,
    content = @`
        <thead class="bg-secondary text-white">
        <tr>
            <th scope="col">ID</th>
            <th scope="col">Nombre</th>
            <th scope="col">Descripción</th>
            <th scope="col">Activo</th>
        </tr>
        </thead>
        <tbody>
        @for(var d : productos)
            <tr data-id=${d.id()}>
                <th scope="row">${d.id()}</th>
                <td>${d.nom()}</td>
                <td>${d.dsc()}</td>
                <td>${d.act()?"Activo":"Inactivo"}</td>
            </tr>
        @endfor
        </tbody>
    `,
    scripts_content = @`
        <script src="/content/js/prd/es_prd754250.js"></script>
        <script src="/content/js/tb_init.js"></script>
        @if(inv_producto_registro)
            <script src="/content/js/prd/prd754250.js"></script>
        @endif
    `
)
