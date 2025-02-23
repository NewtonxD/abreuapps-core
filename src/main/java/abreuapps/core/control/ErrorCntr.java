package abreuapps.core.control;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 *
 * @author Newton
 */
@Controller
@ControllerAdvice
public class ErrorCntr implements ErrorController{
    
    
//----------------------------------------------------------------------------//
//------------------ENDPOINTS MANEJO ERRORES----------------------------------//
//----------------------------------------------------------------------------//
    
    @RequestMapping("/error")
    public String error(
        HttpServletRequest request,
        RedirectAttributes redirectAttrs,
        @RequestParam(name = "e403", required = false,defaultValue = "false") boolean e403
    ){

        if(e403)
            return "error/403";

        var statusCode = Integer.valueOf(request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE).toString());
        switch (statusCode){
            case 403 -> {
                if(request.getAttribute("error_msg")!=null){
                    redirectAttrs.addFlashAttribute("error_msg",request.getAttribute("error_msg"));
                    return "redirect:/auth/login";
                }
                return "error/403";
            }

            case null, default -> {
                return "error/404";
            }

        }
    }
//----------------------------------------------------------------------------//
}
