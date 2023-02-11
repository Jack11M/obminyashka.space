package space.obminyashka.items_exchange.config;

import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;

import java.util.List;

@Configuration
@Import(BeanValidatorPluginsConfiguration.class)
public class SwaggerConfig {
    @Bean
    public Docket api() {
        return new Docket(DocumentationType.OAS_30)
                .useDefaultResponseMessages(false)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any())
                .build()
                .securitySchemes(List.of(apiKey()))
                .securityContexts(List.of(securityContext()));
    }
    @Bean
    public GroupedOpenApi publicApi() {
        return GroupedOpenApi.builder()
                .group("springshop-public")
                .pathsToMatch("/public/**")
                .build();
    }
    private Info apiInfo() {
        return new Info()
                .title("Obminyashka (Child Goods Exchange) API")
                .description("API Definitions of the Obminyashka (Child Goods Exchange) project")
                .version("1.0.0");
    }

    private ApiKey apiKey() {
        return new ApiKey("Authorization", HttpHeaders.AUTHORIZATION, "header");
    }

    private SecurityScheme apiKey() {
    return HttpAuthenticationScheme.JWT_BEARER_BUILDER.name("JWT access token").build();
    }
    private SecurityContext securityContext() {
        return SecurityContext.builder()
                .securityReferences(defaultAuth())
                .build();
    }
    private List<SecurityReference> defaultAuth() {
        return List.of(new SecurityReference(HttpHeaders.AUTHORIZATION,
                new AuthorizationScope[]{
                        new AuthorizationScope("global", "accessEverything")}));
    }
}
