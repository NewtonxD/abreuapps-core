package abreuapps.core.control.usuario;

import abreuapps.core.control.general.DatoRepo;
import jakarta.transaction.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

/**
 *
 * @author cabreu
 */

@Slf4j
@Service
@RequiredArgsConstructor
public class AccesoServ {


    private final AccesoUsuarioRepo AccesoUsuarioRepo; //repo
    private final AccesoRepo AccesoRepo;
    private final DatoRepo DatoRepo;

    /*public Map<String, Boolean> consultarAccesosMenuUsuario(){
        List<Object[]> results=AccesoUsuarioRepo.ListadoMenuUsuario(getUsuarioLogueado().getId());
        Map<String, Boolean> convert=new HashMap<>();
        
        for (Object[] result : results) {
            String nombre = (String) result[0];
            Boolean valor = (Boolean) result[1];
            convert.put(nombre, valor);
        }
        
        return convert;
    }

    public Map<String, Object> consultarAccesosPantallaUsuario(String pantalla){
        List<Object[]> results=AccesoUsuarioRepo.ListadoAccesosPantallaUsuario(getUsuarioLogueado().getId(),pantalla);
        Map<String, Object> convert=new HashMap<>();
        
        for (Object[] result : results) {
            String nombre = (String) result[0];
            Object valor = result[1];
            convert.put(nombre, valor);
        }
        
        return convert;
    }*/

    @Cacheable("PermisosMenu")
    public Map<String, Boolean> consultarAccesosMenuUsuario() {
        return AccesoUsuarioRepo.ListadoMenuUsuario(getUsuarioLogueado().getId())
                .stream()
                .collect(Collectors.toMap(
                        result -> (String) result[0],
                        result -> (Boolean) result[1]
                ));
    }

    @Cacheable("PermisosPantalla")
    public Map<String, Object> consultarAccesosPantallaUsuario(String pantalla) {
        return AccesoUsuarioRepo.ListadoAccesosPantallaUsuario(getUsuarioLogueado().getId(), pantalla)
                .stream()
                .collect(Collectors.toMap(
                        result -> (String) result[0],
                        result -> result[1]
                ));
    }


    public List<Object[]> ListadoAccesosUsuarioEditar(int idUsuario){
        return AccesoUsuarioRepo.ListadoAccesosUsuarioEditar(idUsuario);
    }
    
      
    @Transactional  
    @CacheEvict(value ={"PermisosPantalla","PermisosMenu"}, allEntries = true)
    public void GuardarTodosMap( Map<String,String> accesos, Usuario usuario){
        AccesoUsuarioRepo.saveAll(
            accesos
                .entrySet()
                .stream()
                .map(entry -> {
                    // Transformación del valor: "on" -> "true", "off" -> "false", o el valor original
                    String valorTransformado = "on".equals(entry.getValue())
                            ? "true"
                            : ("off".equals(entry.getValue()) ? "false" : entry.getValue());

                    return DatoRepo
                            .findById(entry.getKey())
                            .flatMap(
                                    dato -> AccesoRepo.findByPantalla(dato)
                                                .flatMap(acceso -> {
                                                    var accesoUsuario = AccesoUsuarioRepo.findAllByUsuarioAndAcceso(usuario, acceso);

                                                    if (accesoUsuario.isPresent()) {
                                                        var accesoBD = accesoUsuario.get();
                                                        accesoBD.setActivo(true);
                                                        accesoBD.setValor(valorTransformado);
                                                        return Optional.of(accesoBD);
                                                    }

                                                    return Optional.of(new AccesoUsuario(
                                                            null,
                                                            valorTransformado,
                                                            true,
                                                            usuario,
                                                            acceso
                                                    ));

                                                })
                            );
                })
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList())
        );





        /*List<AccesoUsuario> listaAccesoNuevo = new ArrayList<>();
        List<AccesoUsuario> listaAccesoEdicion = new ArrayList<>();
        for (String key : accesos.keySet()) {
            //si el acceso existe crear acceso y agregar
            Optional<Dato> dato=DatoRepo.findById(key);
            if(dato.isPresent()){
                Optional<Acceso> acc=AccesoRepo.findByPantalla(dato.get());
                
                if(acc.isPresent()){
                    
                    Optional<AccesoUsuario> accUsr= AccesoUsuarioRepo.findAllByUsuarioAndAcceso(
                        usuario,
                        acc.get()
                    );
                    
                    if(accUsr.isPresent()){
                        //editando
                        AccesoUsuario AccUsr=accUsr.get();
                        AccUsr.setActivo(true);
                        AccUsr.setValor(
                            accesos.get(key).equals("on")?"true":(accesos.get(key).equals("off")?"false":accesos.get(key))
                        );
                        listaAccesoEdicion.add(AccUsr);
                    }else{
                        //nuevo
                        listaAccesoNuevo.add(
                            new AccesoUsuario(
                                null,
                                accesos.get(key).equals("on")?"true":(accesos.get(key).equals("off")?"false":accesos.get(key)),
                                true,
                                usuario,
                                acc.get()
                            )
                        );
                        
                    }
                    
                }
            }
        }

        AccesoUsuarioRepo.saveAll(listaAccesoEdicion);
        AccesoUsuarioRepo.saveAll(listaAccesoNuevo);*/
    }
    
    public Usuario getUsuarioLogueado(){
        return (Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    public boolean verificarPermisos( String permiso ){
        var permisos=consultarAccesosPantallaUsuario(permiso);
        return permisos.containsKey(permiso) ? (Boolean) permisos.get(permiso) : false;
    }
    
}
