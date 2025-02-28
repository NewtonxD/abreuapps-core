@import abreuapps.core.control.general.Persona
@import abreuapps.core.control.utils.DateUtils
@import abreuapps.core.control.general.DatoDTO
@import java.util.List

@param Persona persona
@param DateUtils dateUtils
@param boolean update
@param List<DatoDTO> sexo
@param List<DatoDTO> sangre

<div id="info-dinamica-personal" class="row">
    @if(update)
        <input  type="number" name="id" id="idPersona" value="${persona.getId()}" style="display: none; width: 1px;height: 1px;">
        <input  type="datetime" name="fecha_actualizacionn" style="display: none; width: 1px;height: 1px;" value="${dateUtils.DateToFormato2(persona.getFecha_actualizacion())}">
    @else
        <input  type="number" name="id" id="idPersona" value="0" style="display: none; width: 1px;height: 1px;">
    @endif
    <div class="col-12 mt-2 mb-2">
        <div class="alert alert-warning alert-cedula" role="alert" style="display:none;">
            <span>Cedula en uso. Verifique y vuelva a intentarlo.</span>
        </div>
    </div>
    <div class="mb-3 col-12">
        <label for="cedula" class="form-label text-muted">Cédula:</label>
        @if(update)
            <input type="text" name="cedula" id="cedula" value="${persona.getCedula()}" style="display: none; width: 1px;height: 1px;" />
            <input type="text" value="${persona.getCedula()}" name="ced" disabled placeholder="XXX-XXXXXXX-X" class="form-control cedula" aria-describedby="cedula" required>
        @else
            <input type="text" name="cedula" id="cedula" value="${persona.getCedula()}" placeholder="XXX-XXXXXXX-X" class="form-control cedula" aria-describedby="cedula" required>
        @endif
    </div>

    <div class="mb-3 col-12 col-md-6">
        <label for="nombre" class="form-label text-muted">Nombre:</label>
        <input type="text" name="nombre" id="nombre" value="${persona.getNombre()}" placeholder=" ... " class="form-control" required aria-describedby="basic-addon4">
    </div>

    <div class="mb-3 col-12 col-md-6">
        <label for="apellido" class="form-label text-muted">Apellido:</label>
        <input type="text"  name="apellido" id="apellido" value="${persona.getApellido()}" placeholder=" ... " class="form-control" required aria-describedby="basic-addon4">
    </div>

    <div class="mb-3 col-12 col-md-6">
        <label for="apodo" class="form-label text-muted">Apodo:</label>
        <input type="text" name="apodo" id="apodo" value="${persona.getApodo()}"  placeholder=" ... " class="form-control" aria-describedby="basic-addon4">
    </div>



    <div class="mb-3 col-12 col-md-6">
        <label for="sexo" class="form-label text-muted" required>Sexo:</label>
        <select class="form-select custom-select" name="sexo" id="sexo">
            <option value="0" selected disabled>--Seleccione--</option>
            @for(var g : sexo)
                @if(g.act())

                    @if(!update)
                        <option value="${g.dat()}">${g.dat()}</option>
                    @else
                        <option value="${g.dat()}" selected="${g.dat().equals(persona.getSexo().getDato())}">${g.dat()}</option>
                    @endif

                @endif
            @endfor
        </select>
    </div>


    <div class="mb-3 col-12 col-md-6">
        <label for="numero_celular" class="form-label text-muted">Núm. Celular:</label>
        <input type="tel" name="numero_celular" id="numero_celular" value="${persona.getNumero_celular()}" placeholder="809-999-123" class="form-control telefono" aria-describedby="numero_celular" required>
    </div>

    <div class="mb-3 col-12">
        <label for="direccion" class="form-label text-muted">Dirección:</label>
        <textarea class="form-control" name="direccion" id="direccion" rows="3" required maxlength="255" aria-describedby="direccion">${persona.getDireccion()}</textarea>
    </div>

    <div class="mb-3 col-12 col-md-6">
        <label for="nombre_emergencia" class="form-label text-muted">Nombre Contacto Emergencia:</label>
        <input type="text" name="nombre_emergencia" value="${persona.getNombre_emergencia()}" id="nombre_emergencia" class="form-control" aria-describedby="nombre_emg">
    </div>

    <div class="mb-3 col-12 col-md-6">
        <label for="numero_emergencia" class="form-label text-muted">Núm. Celular Emergencia:</label>
        <input type="tel" name="numero_emergencia" value="${persona.getNumero_emergencia()}" id="numero_emergencia" placeholder="809-999-123" class="form-control telefono" aria-describedby="numero_celular_emg">
    </div>

    <div class="mb-3 col-12 col-md-6">
        <label for="fecha_nacimiento" class="form-label text-muted">Fecha de Nacimiento:</label>
        @if(update)
            <input type="date" name="fecha_nacimiento" value="${dateUtils.dateFormat.format(persona.getFecha_nacimiento())}" id="fecha_nacimiento" class="form-control" aria-describedby="fecha_nacimiento" required>
        @else
            <input type="date" name="fecha_nacimiento" id="fecha_nacimiento" class="form-control" aria-describedby="fecha_nacimiento" required>
        @endif
    </div>

    <div class="mb-3 col-12 col-md-6">
        <label for="tipo_sangre" class="form-label text-muted">Tipo de Sangre:</label>
        <select class="form-select custom-select" name="tipo_sangre" id="tipo_sangre" required>
            <option value="0" selected disabled>--Seleccione--</option>
            @for(var g : sangre)
                @if(g.act())
                    @if(!update)
                        <option value="${g.dat()}">${g.dat()}</option>
                    @else
                        <option
                                value="${g.dat()}"
                                selected="${g.dat().equals(persona.getTipo_sangre().getDato())}"
                        >${g.dat()}</option>
                    @endif
                @endif
            @endfor
        </select>
    </div>
</div>