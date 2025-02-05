package abreuapps.core.control.transporte;

import abreuapps.core.control.usuario.AccesoServ;
import abreuapps.core.control.usuario.Usuario;
import abreuapps.core.control.utils.DateUtils;
import jakarta.transaction.Transactional;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

/**
 *
 * @author cabreu
 */

@Service
@RequiredArgsConstructor
public class RutaServ {
    
    private final RutaRepo repo;
    private final AccesoServ AccesoServicio;
    private final DateUtils FechaUtils;
    private final LocRutaServ LocRutaServicio;
    
    @Cacheable("Rutas")
    public List<RutaDTO> consultar(){
        return repo.customFindAll(false);
    }
    
    @Cacheable("Rutas")
    public List<RutaDTO> consultarActivo(){
        return repo.customFindAll(true);
    }
    
    @Cacheable("RutasInfo")
    public List<Object[]> consultarInfo(){
        return repo.findData();
    }

    @Transactional
    @CacheEvict(value="Rutas",allEntries = true)
    public boolean guardar(Ruta ruta, String polylineData, String fechaActualizacion, Model model){

        Usuario usuario = AccesoServicio.getUsuarioLogueado();
        Optional<Ruta> rutaDB = obtener(ruta.getRuta());

        if (rutaDB.isPresent()) {

            if (! FechaUtils.FechaFormato2
                    .format(rutaDB.get().getFecha_actualizacion())
                    .equals(fechaActualizacion)
            ) {

                model.addAttribute(
                        "msg",
                        ! ( fechaActualizacion == null ||
                                fechaActualizacion.equals("") ) ?
                                "Alguien ha realizado cambios en la información. Intentelo neuvamente. COD: 00656" :
                                "Esta ruta ya existe!. Verifique e intentelo nuevamente."
                );
                return false;
            }

            ruta.setHecho_por(rutaDB.get().getHecho_por());
            ruta.setFecha_registro(rutaDB.get().getFecha_registro());

        }else{
            ruta.setHecho_por(usuario);
            ruta.setFecha_registro(new Date());
        }

        ruta.setActualizado_por(usuario);
        ruta.setFecha_actualizacion(new Date());
        Ruta rutaGuardada=repo.save(ruta);

        if(! polylineData.isEmpty() ){
            String cadenaListaLocRuta=polylineData.replace("LatLng(","[").replace(")", "]");
            List<LocRuta> listaLocRuta = LocRutaServicio.generarLista(cadenaListaLocRuta, rutaGuardada);
            LocRutaServicio.borrarPorRuta(rutaGuardada);
            LocRutaServicio.guardarTodos(listaLocRuta);
        }

        model.addAttribute("msg", "Registro guardado exitosamente!");

        return true;
    }
    
    public Optional<Ruta> obtener(String Ruta){
        return repo.findById(Ruta);
    }
    
}
