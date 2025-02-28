@import abreuapps.core.control.transporte.VehiculoDTO
@import java.util.List

@param Boolean trp_vehiculo_consulta
@param Boolean trp_vehiculo_registro
@param String msg
@param Boolean status
@param List<VehiculoDTO> vehiculos

@template.shared.consulta(
    titulo = "Vehiculos",
    id_registro = "trp_vehiculo_registro",
    permiso_consulta = trp_vehiculo_consulta,
    permiso_registro = trp_vehiculo_registro,
    msg = msg, status = status,
    content = @`
        <thead class="bg-secondary text-white">
        <tr>
            <th scope="col">Placa</th>
            <th scope="col">Marca y Módelo</th>
            <th scope="col">Color</th>
            <th scope="col">Estado</th>
            <th scope="col">Activo</th>
        </tr>
        </thead>
        <tbody>
        @for(var d: vehiculos)
            <tr data-id="${d.pl()}">
                <th scope="row">${d.pl()}</th>
                <td>${d.marca_dat() + ' ' + d.modelo_dat()}</td>
                <td>${d.color_dat()}</td>
                <td>${d.estado_dat()}</td>
                <td>${d.act()?"Activo":"Inactivo"}</td>
            </tr>
        @endfor
        </tbody>
    `,
   scripts_content = @`
        <script src="/content/js/tb_init.js"></script>
        @if(trp_vehiculo_registro) <script src="/content/js/trp/vhl574702.js"></script> @endif
        <script src="/content/js/trp/es_vhl574702.js"></script>
    `
)