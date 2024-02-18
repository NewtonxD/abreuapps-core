/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.dom.stp.omsa.control.domain.usuario;

import jakarta.transaction.Transactional;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 *
 * @author cabreu
 */


@Transactional
@Service
@Slf4j
public class UsuarioServ {
    
    @Autowired
    UsuarioRepo repo;
    
    @Autowired
    PasswordEncoder passwordEncoder;
    
    @Autowired
    private SessionRegistry sessionRegistry;
    
    private static final String LETTERS = "@#$%!AbCdEfGhIjKlMnOpQrRtUvWxYz-*[]";
    private static final String NUMBERS = "0123456789";
    private static final Random RANDOM = new Random();

    public static String generarPassword() {
        StringBuilder password = new StringBuilder();

        // Generate two random letters
        for (int i = 0; i < 2; i++) {
            char letter = LETTERS.charAt(RANDOM.nextInt(LETTERS.length()));
            password.append(letter);
        }

        // Generate six random numbers
        for (int i = 0; i < 6; i++) {
            char number = NUMBERS.charAt(RANDOM.nextInt(NUMBERS.length()));
            password.append(number);
        }

        return password.toString();
    }
    
    public Usuario cambiarPassword(Usuario u, String password){
        u.setContraseña(passwordEncoder.encode(password));
        return guardar(u,u.getId(),true);
    }
    
    public Usuario guardar(Usuario gd, Integer idUsuario,boolean existe){
        
        if(existe){ 
            gd.setActualizado_por(idUsuario);
        }else{
            String nuevaContraseña=generarPassword();
            //enviar al correo aqui
            log.info("\n\tContraseña nueva > "+nuevaContraseña+"\n");
            
            
            gd.setContraseña(passwordEncoder.encode(nuevaContraseña));
            gd.setHecho_por(idUsuario);
            gd.setFecha_registro(new Date());
        }
        gd.setFecha_actualizacion(new Date());
        
        return repo.save(gd);
    }
    
    public List<Usuario> consultar(){
        List<Usuario> ul=repo.findAll();        
        return ul;
    }
    
    public Optional<Usuario> obtener(String usuario){
        return repo.findByUsername(usuario);
    }
    
    public Optional<Usuario> obtenerPorCorreo(String correo){
        return repo.findByCorreo(correo);
    }
    
    public Optional<Usuario> obtenerPorPersona(Persona persona){
        return repo.findByPersona(persona);
    }
    
    public Optional<Usuario> obtenerPorId(Integer id){
        return repo.findById(id);
    }
    
    public void CerrarSesion(String usuario){
        List<Object> principals = sessionRegistry.getAllPrincipals();
        for (Object principal : principals) {
            if (principal instanceof UserDetails) {
                UserDetails userDetails = (UserDetails) principal;
                if (userDetails.getUsername().equals(usuario)) {
                    List<SessionInformation> sessions = sessionRegistry.getAllSessions(principal, false);
                    for (SessionInformation session : sessions) {
                        session.expireNow();
                    }
                }
            }
        }
    }
}
