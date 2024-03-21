/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package abreusapp.core.control;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 *
 * @author Newton
 */
@Controller
@Slf4j
public class ErrorCntr implements ErrorController{
    
    @RequestMapping("/error")
    public String error(
        HttpServletRequest request,
        RedirectAttributes redirectAttrs,
        @RequestParam(name = "e403", required = false,defaultValue = "false") boolean e403
    ){
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        
        Exception exception = (Exception) request.getAttribute("javax.servlet.error.exception");
        if(exception!=null)
            log.error("Error : "+exception.getMessage()+" | "+exception.getLocalizedMessage());
        
        if (status != null) {
            Integer statusCode = Integer.valueOf(status.toString());

            if(statusCode == HttpStatus.NOT_FOUND.value()) {
                return "error/404";
            }
            else if (statusCode==HttpStatus.FORBIDDEN.value()){
                if(request.getAttribute("error_msg")!=null){
                    redirectAttrs.addFlashAttribute("error_msg",(String)request.getAttribute("error_msg"));
                    return "redirect:/auth/login";
                }
                return "error/403";
                
            }
            else if(statusCode == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
                return "error/404";
            }
            else if(statusCode ==401){
                return "error/401";
            }
        }
        
        if(e403)
            return "error/403";
        
        return "error/404";
    }
}
