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
        <input  type="number" th:field="*{id}" id="idPersona" style="display: none; width: 1px;height: 1px;" required>
        <input  type="datetime" name="fecha_actualizacionn" style="display: none; width: 1px;height: 1px;" th:value="${#dates.format(persona.fecha_actualizacion, 'yyyy-MM-dd HH:mm:ss')}">
    @endif
    <div class="col-12 mt-2 mb-2">
        <div class="alert alert-warning alert-cedula" role="alert" style="display:none;">
            <span>Cedula en uso. Verifique y vuelva a intentarlo.</span>
        </div>
    </div>
    <div class="mb-3 col-12">
        <label for="cedula" class="form-label text-muted">Cédula:</label>
        <th:block th:if="${update}" >
            <input type="text" th:field="*{cedula}" style="display: none; width: 1px;height: 1px;" />

            <input type="text" th:value="${persona.cedula}" name="ced" disabled placeholder="XXX-XXXXXXX-X" class="form-control cedula" aria-describedby="cedula" required>
        </th:block>

        <th:block th:if="${!update}">
            <input type="text" th:field="*{cedula}" placeholder="XXX-XXXXXXX-X" class="form-control cedula" aria-describedby="cedula" required>

        </th:block>
    </div>

    <div class="mb-3 col-12 col-md-6">
        <label for="nombre" class="form-label text-muted">Nombre:</label>
        <input type="text" th:field="*{nombre}" placeholder=" ... " class="form-control" required aria-describedby="basic-addon4">
    </div>

    <div class="mb-3 col-12 col-md-6">
        <label for="apellido" class="form-label text-muted">Apellido:</label>
        <input type="text" th:field="*{apellido}"  placeholder=" ... " class="form-control" required aria-describedby="basic-addon4">
    </div>

    <div class="mb-3 col-12 col-md-6">
        <label for="apodo" class="form-label text-muted">Apodo:</label>
        <input type="text" th:field="*{apodo}"  placeholder=" ... " class="form-control" aria-describedby="basic-addon4">
    </div>



    <div class="mb-3 col-12 col-md-6">
        <label for="sexo" class="form-label text-muted" required>Sexo:</label>
        <select class="form-select custom-select" th:field="*{sexo}">
            <option value="0" selected disabled>--Seleccione--</option>
            <option th:value="${s.dat}" th:each="s :${sexo}" th:text="${s.dat}"></option>
        </select>
    </div>


    <div class="mb-3 col-12 col-md-6">
        <label for="numero_celular" class="form-label text-muted">Núm. Celular:</label>
        <input type="tel" th:field="*{numero_celular}" placeholder="809-999-123" class="form-control telefono" aria-describedby="numero_celular" required>
    </div>

    <div class="mb-3 col-12">
        <label for="direccion" class="form-label text-muted">Dirección:</label>
        <textarea class="form-control" th:field="*{direccion}" rows="3" required maxlength="255" aria-describedby="direccion"></textarea>
    </div>

    <div class="mb-3 col-12 col-md-6">
        <label for="nombre_emergencia" class="form-label text-muted">Nombre Contacto Emergencia:</label>
        <input type="text" th:field="*{nombre_emergencia}" class="form-control" aria-describedby="nombre_emg">
    </div>

    <div class="mb-3 col-12 col-md-6">
        <label for="numero_emergencia" class="form-label text-muted">Núm. Celular Emergencia:</label>
        <input type="tel" th:field="*{numero_emergencia}" placeholder="809-999-123" class="form-control telefono" aria-describedby="numero_celular_emg">
    </div>

    <div class="mb-3 col-12 col-md-6">
        <label for="fecha_nacimiento" class="form-label text-muted">Fecha de Nacimiento:</label>
        <input type="date" th:field="*{fecha_nacimiento}" class="form-control" aria-describedby="fecha_nacimiento" required>
    </div>

    <div class="mb-3 col-12 col-md-6">
        <label for="tipo_sangre" class="form-label text-muted">Tipo de Sangre:</label>
        <select class="form-select custom-select" th:field="*{tipo_sangre}" required>
            <option value="0" selected disabled>--Seleccione--</option>
            <option th:value="${ts.dat}" th:each="ts :${sangre}" th:text="${ts.dat}"></option>
        </select>
    </div>
</div>