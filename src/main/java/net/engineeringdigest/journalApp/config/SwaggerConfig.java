package net.engineeringdigest.journalApp.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.oas.models.tags.Tag;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.List;

//by SpringDoc documentation a documentation of APIs used in this project was created. Here we can modify it
//hosted on url http://localhost:8081/swagger-ui/index.html

//we can also go to APIs code and with annotations of SpringDoc may add cutomizations there which will be shown in documentation
//also create DTO objects that just contain necessary schema we want to show in documentation during API checking instead of full object. As in /signup of PublicController

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI myCustomConfig(){
        return new OpenAPI()
                .info(
                        new Info().title("Journal App APIs")
                                .description("By Vipul")
                )
                .servers(Arrays.asList(new Server().url("http://localhost:8081").description("local"),
                        new Server().url("http://localhost:8082").description("live")))
                .tags(Arrays.asList(
                        new Tag().name("Public APIs"),
                        new Tag().name("User APIs"),
                        new Tag().name("Journal APIs"),
                        new Tag().name("Admin APIs")
                ))
                .addSecurityItem(new SecurityRequirement().addList("bearerAuth"))
                .components(new Components().addSecuritySchemes(
                        "bearerAuth", new SecurityScheme()
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")
                                .in(SecurityScheme.In.HEADER)
                                .name("Authorization")
                )); //here we made it compulsory to have Bearer token for authorisation needed for running certain APIs
    }
}