package abreuapps.core.control.transporte;

import abreuapps.core.control.usuario.AccesoServ;
import abreuapps.core.control.usuario.Usuario;
import abreuapps.core.control.utils.DateUtils;
import jakarta.transaction.Transactional;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

/**
 *
 * @author cabreu
 */

@Service
@RequiredArgsConstructor
public class VehiculoServ {
    
    private final VehiculoRepo repo;
    private final AccesoServ AccesoServ;
    private final DateUtils FechaUtils;

    private final String SECRET_KEY="*@3ad_@4%dE*ez";


    @Cacheable("Vehiculos")
    public List<VehiculoDTO> consultar(){
        return repo.customFindAll();
    }
    
    @Transactional
    public void procesarVehiculoSinActividad(){
        repo.stopAllWithoutActivity();
    }
    
    @Transactional
    @CacheEvict(value={"Vehiculos","RutasInfo","LogVehiculo"},allEntries = true)
    public boolean guardar(Vehiculo vehiculo, String fechaActualizacion, Model model){

        Optional<Vehiculo> vehiculoBD = obtener(vehiculo.getPlaca());
        Usuario usuario = AccesoServ.getUsuarioLogueado();

        if (vehiculoBD.isPresent()) { // EXISTE?

            if (!  FechaUtils.FechaFormato2 // LAS FECHAS DE ACTUALIZACION SON CONSISTENTES?
                    .format(vehiculoBD.get().getFecha_actualizacion() )
                    .equals(fechaActualizacion)
            ) {
                model.addAttribute("msg",
                        ! fechaActualizacion.isEmpty() ?
                                "Alguien ha realizado cambios en la información. Inténtentelo nuevamente. COD: 00635" :
                                "Este vehiculo ya existe!. Verifique e intentelo nuevamente."
                );
                return false;
            }

            vehiculo.setFecha_registro(vehiculoBD.get().getFecha_registro());
            vehiculo.setHecho_por(vehiculoBD.get().getHecho_por());

        }else{ // NO EXISTE!
            vehiculo.setHecho_por(usuario);
            vehiculo.setFecha_registro(new Date());
        }

        vehiculo.setActualizado_por(usuario);
        vehiculo.setFecha_actualizacion(new Date());

        repo.save(vehiculo);

        model.addAttribute("msg", "Registro guardado exitosamente!");

        return true;
    }


    @Transactional
    @CacheEvict(value={"Vehiculos","RutasInfo","LogVehiculo"},allEntries = true)
    public void guardarAPI(Vehiculo gd){
        gd.setFecha_actualizacion(new Date());
        repo.save(gd);
    }
    
    public Optional<Vehiculo> obtener(String Placa){
        return repo.findById(Placa);
    }

    public String generateToken(){
        try {
            Mac hmac = Mac.getInstance("HmacSHA256");
            SecretKeySpec secretKeySpec = new SecretKeySpec(SECRET_KEY.getBytes(), "HmacSHA256");
            hmac.init(secretKeySpec);
            byte[] tokenBytes = hmac.doFinal(Long.toString(System.currentTimeMillis()).getBytes());
            return Base64.getEncoder().encodeToString(tokenBytes);
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            return "";
        }
    }

    public boolean validateToken(String token) {
        try {
            Mac hmac = Mac.getInstance("HmacSHA256");
            SecretKeySpec secretKeySpec = new SecretKeySpec(SECRET_KEY.getBytes(), "HmacSHA256");
            hmac.init(secretKeySpec);
            byte[] tokenBytes = Base64.getDecoder().decode(token);
            byte[] calculatedBytes = hmac.doFinal(Long.toString(System.currentTimeMillis()).getBytes());
            return MessageDigest.isEqual(tokenBytes, calculatedBytes);
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            return false;
        }
    }

}
