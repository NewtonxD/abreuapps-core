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
    public String generateToken(String secretKey, int tokenLength) throws NoSuchAlgorithmException, InvalidKeyException {
        SecureRandom random = new SecureRandom();
        byte[] randomBytes = new byte[tokenLength];
        random.nextBytes(randomBytes);

        Mac hmac = Mac.getInstance("HmacSHA256");
        SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey.getBytes(), "HmacSHA256");
        hmac.init(secretKeySpec);
        byte[] hmacBytes = hmac.doFinal(randomBytes);

        // Encode the HMAC bytes to base64
        return Base64.getEncoder().encodeToString(hmacBytes);
    }

    // Validate if a token is valid
    public boolean validateToken(String token, String secretKey) throws NoSuchAlgorithmException, InvalidKeyException {
        Mac hmac = Mac.getInstance("HmacSHA256");
        SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey.getBytes(), "HmacSHA256");
        hmac.init(secretKeySpec);

        // Decode the token from base64
        byte[] decodedToken = Base64.getDecoder().decode(token);

        // Re-calculate HMAC and compare with provided token
        byte[] recalculatedHmacBytes = hmac.doFinal(decodedToken);

        // Compare the recalculated HMAC with the provided token's HMAC
        return MessageDigest.isEqual(recalculatedHmacBytes, decodedToken);
    }
}
