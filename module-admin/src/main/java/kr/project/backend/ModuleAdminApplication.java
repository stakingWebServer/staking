package kr.project.backend;


import lombok.Data;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
@Data
public class ModuleAdminApplication {

    public static final String APPLICATION_LOCATIONS = "spring.config.location="
            + "optional:classpath:application-local.yml"
            + ", optional:/app/project/config/application-prod.yml";

    public static void main(String[] args) {
        new SpringApplicationBuilder(ModuleAdminApplication.class)
                .properties(APPLICATION_LOCATIONS)
                .run(args);
//testdddtestddddtestddddddggdd
    }

}
