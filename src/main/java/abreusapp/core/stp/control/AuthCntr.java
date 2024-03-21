package abreusapp.core.stp.control;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
@Slf4j
public class AuthCntr {

    @GetMapping("/auth/login")
    public String Login(
            HttpServletRequest request,
            @RequestParam(name = "invalidSession", required = false,defaultValue = "false") boolean invalidSession,
            @RequestParam(name = "logout", required = false,defaultValue = "false") boolean logout,
            Model model
    ) {
        
        if((SecurityContextHolder.getContext().getAuthentication() instanceof UsernamePasswordAuthenticationToken))
            return "redirect:/main/index";
        
        if(invalidSession)
            model.addAttribute("error_msg","Su sesión expiró. Ingrese sus credenciales nuevamente.");
        
        if(logout)
            model.addAttribute("success_msg","Sesión cerrada exitosamente!");
        
        return "login";
    }
    
    @GetMapping("/")
    public String redirectLogin(HttpServletRequest request,Model model
    ){
        return "redirect:/auth/login";
    }

}
