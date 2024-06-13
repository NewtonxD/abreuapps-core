/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package abreusapp.core.conf;

import jakarta.servlet.annotation.WebFilter;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 *
 * @author cabreu
 */

// RL1   RL=RateLimiting

@Component
@Order(2)
@WebFilter(urlPatterns = "/content/*")
public class Specific2RLFilter extends BaseRLFilter{

    @Override
    protected int getMaxRequests() {
        return 250;
    }
    
}
