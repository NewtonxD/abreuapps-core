/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package abreusapp.core.control.utils;

import org.springframework.stereotype.Service;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

/**
 *
 * @author cabreu
 */

@Service
public class TransportTokenServ {
    
    private static final int tokenLength=8;
    private static final String secretKey="*@3ads@4%dEeez";
    
    public String generateToken() throws NoSuchAlgorithmException, InvalidKeyException {
        SecureRandom random = new SecureRandom();
        byte[] randomBytes = new byte[tokenLength];
        random.nextBytes(randomBytes);

        Mac hmac = Mac.getInstance("HmacSHA256");
        SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey.getBytes(), "HmacSHA256");
        hmac.init(secretKeySpec);
        byte[] hmacBytes = hmac.doFinal(randomBytes);

        return Base64.getEncoder().encodeToString(hmacBytes);
    }

    private boolean validateToken(String token) throws NoSuchAlgorithmException, InvalidKeyException {
        Mac hmac = Mac.getInstance("HmacSHA256");
        SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey.getBytes(), "HmacSHA256");
        hmac.init(secretKeySpec);

        byte[] decodedToken = Base64.getDecoder().decode(token);

        byte[] recalculatedHmacBytes = hmac.doFinal(decodedToken);

        return MessageDigest.isEqual(recalculatedHmacBytes, decodedToken);
    }
    
    public boolean isValidToken(String token){
        boolean res=false;
        if(token!=null){
            try{
                res=validateToken(token);
            }catch(InvalidKeyException|NoSuchAlgorithmException e){
                res=false;
            }
        }
        return res;
    }
    
    
}
