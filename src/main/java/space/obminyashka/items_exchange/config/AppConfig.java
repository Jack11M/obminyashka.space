package space.obminyashka.items_exchange.config;

import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.objects.Email;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;

import java.util.List;

@Configuration
@EnableJpaAuditing(dateTimeProviderRef = "dateTimeProvider")
@EnableCaching
@EnableScheduling
public class AppConfig {

    @Bean
    public LocaleResolver localeResolver() {
        return new AcceptHeaderLocaleResolver();
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Method fixes HttpMediaTypeNotSupportedException: Content type 'application/octet-stream' not supported error
     * that connected with Swagger-UI and prevent getting multipart requests
     * @return converter for octet-stream requests
     */
    @Bean
    public MappingJackson2HttpMessageConverter octetStreamJsonConverter() {
        var converter = new MappingJackson2HttpMessageConverter();
        converter.setSupportedMediaTypes(List.of(new MediaType("application", "octet-stream")));
        return converter;
    }

    @Bean
    public SendGrid sendGrid(@Value("${spring.sendgrid.api-key}") String apiKey) {
        return new SendGrid(apiKey);
    }

    @Bean
    public Email sender(@Value("${app.mail.address}") String sender) {
        return new Email(sender);
    }
}
