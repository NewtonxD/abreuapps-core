package abreuapps.core.control.general;

import abreuapps.core.control.usuario.Usuario;
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
    
    @Transactional
    public void guardar(Dato gd, Usuario usuario,boolean existe){
        
        if(existe){ 
            gd.setActualizado_por(usuario);
        }else{
            gd.setHecho_por(usuario);
            gd.setFecha_registro(new Date());
        }
        gd.setFecha_actualizacion(new Date());
        repo.save(gd);
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

