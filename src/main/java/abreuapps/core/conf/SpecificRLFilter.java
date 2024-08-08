/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package abreuapps.core.conf;

import jakarta.servlet.annotation.WebFilter;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 *
 * @author cabreu
 */
@Component
@Order(1)
@WebFilter(urlPatterns = "/API/tiles/*")
public class SpecificRLFilter extends BaseRLFilter{

    @Override
    protected int getMaxRequests() {
        return 2500;
    }
    
}
