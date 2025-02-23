package abreuapps.core.control.general;

import abreuapps.core.control.usuario.AccesoServ;
import abreuapps.core.control.utils.DateUtils;
import jakarta.transaction.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 *  
 * Servicio para manipular y consultar los datos generales.
 *
 * @author Carlos Abreu Pérez
 *  
 */
@Service
@RequiredArgsConstructor
public class DatoServ {
    
    private final DatoRepo repo;

    private final DateUtils FechaUtils;

    private final AccesoServ AccesoServicio;
    
    @Transactional
    public List<Object> guardar(Dato dato, String fechaActualizacion){

        if(dato.equals(null))
            return List.of( false,
                    "La información no puede ser guardada. Por favor, inténtalo otra vez. COD: 00562"
            );

        var usuario = AccesoServicio.getUsuarioLogueado();
        var datoBD = obtener(dato.getDato());

        if ( datoBD.isPresent() ) {

            if(! FechaUtils.FechaFormato2
                    .format(datoBD.get().getFecha_actualizacion())
                    .equals(fechaActualizacion)
            )
                return List.of( false,
                        ( !(fechaActualizacion.equals(null) || fechaActualizacion.equals("")) ?
                                "Al parecer alguien ha realizado cambios en la información primero. Por favor, inténtalo otra vez. COD: 00535" :
                                "No podemos realizar los cambios porque ya este Dato se encuentra registrado."
                        )
                );


            dato.setFecha_registro(datoBD.get().getFecha_registro());
            dato.setHecho_por(datoBD.get().getHecho_por());
            dato.setActualizado_por(usuario);
        }else{
            dato.setHecho_por(usuario);
            dato.setFecha_registro(new Date());
        }

        dato.setFecha_actualizacion(new Date());


        repo.save(dato);
        return List.of( true,
                "Registro guardado exitosamente!"
        );

    }
    
    public List<DatoDTO> consultar(){
        return repo.customFindAll(null,true);
    }
    
    public List<DatoDTO> consultarPorGrupo(String grupo){
        return repo.customFindAll(grupo,false);
    }
    
    public Optional<Dato> obtener(String dato){
        return repo.findById(dato);
    }
}

