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
 * Servicio para manipular y consultar los datos personales.
 *
 * @author Carlos Abreu Pérez
 *  
 */

@Service
@RequiredArgsConstructor
public class PersonaServ {
    
    private final PersonaRepo repo;

    private final AccesoServ AccesoServicio;

    private final DateUtils FechaUtils;
    
    @Transactional
    public Persona guardar(Persona persona, String fechaActualizacion){
        var usuario = AccesoServicio.getUsuarioLogueado();

        if (persona.equals(null)) return null;

        Optional<Persona> personaBD = obtenerPorId( persona.getId() );


        if(personaBD.isPresent()){

            if(! FechaUtils.FechaFormato2
                    .format( personaBD.get().getFecha_actualizacion() )
                    .equals(fechaActualizacion)
            ) return null;

            persona.setFecha_registro(personaBD.get().getFecha_registro());
            persona.setHecho_por(personaBD.get().getHecho_por());
            persona.setActualizado_por(usuario);
        }else{
            persona.setHecho_por(usuario);
            persona.setFecha_registro(new Date());
        }
        persona.setFecha_actualizacion(new Date());
        
        return repo.save(persona);
    }
    
    public Optional<Persona> obtenerPorId(Integer id){
        if(id==null || id==0) Optional.empty();
        return repo.findById(id);
    }
    
    public Optional<Persona> obtenerPorCedula(String cedula){
        return repo.findByCedula(cedula);
    }
    
    public List<Persona> consultar(){
        return repo.findAll();
    }
    
}
