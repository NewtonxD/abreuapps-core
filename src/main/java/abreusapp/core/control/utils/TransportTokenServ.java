/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package abreusapp.core.control.utils;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import org.springframework.stereotype.Service;

/**
 *
 * @author cabreu
 */

@Service
public class TransportTokenServ {
    
    private static final String SECRET_KEY="*@3ad_@4%dE*ez";
    
    public static String generateToken(){
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

    private static boolean validateToken(String token) {
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
    
    public static boolean isValidToken(String token){
        boolean res=false;
        if(token!=null){
            res=validateToken(token);
        }
        return res;
    }
    
    
}
