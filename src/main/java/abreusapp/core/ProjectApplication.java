package abreusapp.core;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableAsync;

/*
*
* ProjectApplication: Esta es la clase principal del proyecto.
* Es la que permite inicializar el servidor.
*
 */
@SpringBootApplication()
@EnableAsync
@EnableCaching
public class ProjectApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProjectApplication.class, args);
    }

}
