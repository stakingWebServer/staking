package kr.project.backend;


import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class ModuleCrawlingApplication {

    public static final String APPLICATION_LOCATIONS = "spring.config.location="
            + "optional:classpath:application-local.yml"
            + ", optional:/app/project/config/application.yml";

    public static void main(String[] args) {
        new SpringApplicationBuilder(ModuleCrawlingApplication.class)
                .properties(APPLICATION_LOCATIONS)
                .run(args);

    }

}
