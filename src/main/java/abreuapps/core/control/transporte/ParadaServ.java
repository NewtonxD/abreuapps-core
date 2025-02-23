package abreuapps.core.control.transporte;

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
public class ParadaServ {
    
    private final ParadaRepo repo;

    private final DateUtils FechaUtils;

    private final AccesoServ AccesoServicio;
    
    @Cacheable("Paradas")
    public List<ParadaDTO> consultarTodo(Integer excluyeParada,Boolean activo){
        return repo.findAllCustom(excluyeParada, activo);
    }
    
    @Transactional
    @CacheEvict(value={"Paradas","RutasInfo"},allEntries = true)
    public List<Object> guardar(Parada parada, String fechaActualizacion){


        if(parada.equals(null))
            return List.of( false,
                    "El producto no puede ser guardado. Por favor, inténtalo otra vez."
            );


        var paradaDB = obtener(parada.getId());
        var usuario = AccesoServicio.getUsuarioLogueado();

        if (paradaDB.isPresent()) {

            if (! FechaUtils.FechaFormato2
                    .format(paradaDB.get().getFecha_actualizacion() )
                    .equals(fechaActualizacion)
            ) {
                return List.of( false,
                        ! ( fechaActualizacion == null ||
                                fechaActualizacion.equals("") ) ?
                                "Al parecer alguien ha realizado cambios en la información primero. Por favor, inténtalo otra vez. COD: 00656" :
                                "No podemos realizar los cambios porque ya esta Parada se encuentra registrado."

                );
            }

            parada.setFecha_registro(paradaDB.get().getFecha_registro());
            parada.setHecho_por(paradaDB.get().getHecho_por());

        }else {
            parada.setHecho_por(usuario);
            parada.setFecha_registro(new Date());
        }

        parada.setFecha_actualizacion(new Date());
        parada.setActualizado_por(usuario);

        repo.save(parada);

        return List.of( true,
                "Registro guardado exitosamente!"
        );
    }
    
    @Cacheable("PMC")
    public Object[] getParadaMasCercana(Double Latitud,Double Longitud){
        return repo.findParadaMasCercana(Latitud,Longitud);
    }
    
    @Cacheable("PI")
    public List<Object[]> getParadaInfo(Integer idParada){
        return repo.findParadaInfo(idParada);
    }
    
    public Optional<Parada> obtener(Integer id){
        if(id==null){
            return Optional.empty();
        }
        return repo.findById(id);
    }
}
