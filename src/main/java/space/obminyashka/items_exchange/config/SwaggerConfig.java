package space.obminyashka.items_exchange.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import space.obminyashka.items_exchange.rest.api.ApiKey;

@Configuration
public class SwaggerConfig {
    @Bean
    public GroupedOpenApi publicApi() {
        return GroupedOpenApi.builder()
                .group("obminyashka-public")
                .pathsToMatch(ApiKey.API + "/**")
                .build();
    }

    @Bean
    public OpenAPI obminyashkaOpenAPI() {
        return new OpenAPI()
                .info(new Info().title("Obminyashka (Child Goods Exchange) API")
                        .description("API Definitions of the Obminyashka (Child Goods Exchange) project")
                        .version("v0.7.0")
                        .license(new License().name("Apache 2.0").url("https://springdoc.org")))
                .externalDocs(new ExternalDocumentation()
                        .description("Obminyashka GitHub Docs")
                        .url("https://github.com/Jack11M/EVO-Exchange-BE-2019"))
                .addSecurityItem(new SecurityRequirement()
                        .addList(HttpHeaders.AUTHORIZATION))
                .components(new Components()
                        .addSecuritySchemes(HttpHeaders.AUTHORIZATION, new SecurityScheme()
                                .name(HttpHeaders.AUTHORIZATION)
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")));
    }
}
