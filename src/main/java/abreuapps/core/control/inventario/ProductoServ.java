package abreuapps.core.control.inventario;

import abreuapps.core.control.usuario.Usuario;
import jakarta.transaction.Transactional;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 *
 * @author cabreu
 */

@Service
@RequiredArgsConstructor
public class ProductoServ {
    
    private final ProductoRepo repo;
            
    @Transactional
    public Producto guardar(Producto gd, Usuario usuario,boolean existe){
        
        if(existe){ 
            if(usuario!=null)
                gd.setActualizado_por(usuario);
        }else{
            if(usuario!=null)
                gd.setHecho_por(usuario);
            
            gd.setFecha_registro(new Date());
        }
        gd.setFecha_actualizacion(new Date());
        return repo.save(gd);
    }
    
    public Optional<Producto> obtener(Long id){
        if(id==null){
            return Optional.empty();
        }
        return repo.findById(id);
    }
    
    public List<ProductoDTO> consultar(){
        return repo.customFindAll();
    }    
    
}
