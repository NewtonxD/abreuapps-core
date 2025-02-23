package abreuapps.core.control.inventario;

import abreuapps.core.control.usuario.AccesoServ;
import abreuapps.core.control.usuario.Usuario;
import abreuapps.core.control.utils.DateUtils;
import jakarta.transaction.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

/**
 *
 * @author cabreu
 */

@Service
@RequiredArgsConstructor
public class ProductoServ {
    
    private final ProductoRepo repo;

    private final DateUtils FechaUtils;

    private final AccesoServ AccesoServicio;
            
    @Transactional
    @CacheEvict(value={"Productos"}, allEntries = true)
    public List<Object> guardar(Producto producto,String fechaActualizacion){

        if(producto.equals(null))
            return List.of( false,
                    "El producto no puede ser guardado. Por favor, inténtalo otra vez."
            );

        Optional<Producto> ProductoDB = obtener(producto.getId());
        Usuario usuario = AccesoServicio.getUsuarioLogueado();

        if (ProductoDB.isPresent()) {

            if (! FechaUtils.FechaFormato2
                    .format(ProductoDB.get().getFecha_actualizacion())
                    .equals(fechaActualizacion)
            )
                return List.of( true,
                        ! ( fechaActualizacion == null ||
                                fechaActualizacion.equals("") ) ?
                                "Al parecer alguien ha realizado cambios en la información primero. Por favor, inténtalo otra vez. COD: 00686" :
                                "No podemos realizar los cambios porque ya esta Parada se encuentra registrado."

                );


            producto.setFecha_registro(ProductoDB.get().getFecha_registro());
            producto.setHecho_por(ProductoDB.get().getHecho_por());
            producto.setActualizado_por(usuario);

        }else{
            producto.setHecho_por(usuario);
            producto.setFecha_registro(new Date());

        }

        producto.setFecha_actualizacion(new Date());

        repo.save(producto);
        return List.of( true,
                "Registro guardado exitosamente!"
        );

    }

    public Optional<Producto> obtener(Long id){
        if(id==null){
            return Optional.empty();
        }
        return repo.findById(id);
    }

    @Cacheable(value="Productos")
    public List<ProductoDTO> consultar(){
        return repo.customFindAll();
    }    
    
}
