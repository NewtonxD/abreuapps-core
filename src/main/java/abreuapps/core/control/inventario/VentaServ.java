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
public class VentaServ {
    
    private final VentaRepo repo;
    
    @Transactional
    public Venta guardar(Venta gd, Usuario usuario){
        gd.setHecho_por(usuario);
        gd.setFecha_registro(new Date());
        return repo.save(gd);
    }
    
    public Optional<Venta> obtener(Long id){
        if(id==null){
            return Optional.empty();
        }
        return repo.findById(id);
    }
    
    public List<VentaDTO> consultar(){
        return repo.customFindAll();
    }    
    
}
