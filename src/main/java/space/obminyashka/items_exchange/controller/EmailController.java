package space.obminyashka.items_exchange.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import space.obminyashka.items_exchange.api.ApiKey;
import space.obminyashka.items_exchange.exception.EmailValidationCodeExpiredException;
import space.obminyashka.items_exchange.exception.EmailValidationCodeNotFoundException;
import space.obminyashka.items_exchange.service.MailService;
import space.obminyashka.items_exchange.util.ResponseMessagesHandler;

import java.util.UUID;

import static space.obminyashka.items_exchange.util.MessageSourceUtil.getMessageSource;

@RestController
@Slf4j
@Tag(name = "Email")
@RequiredArgsConstructor
@Validated
public class EmailController {
    private final MailService mailService;

    @PostMapping(value = ApiKey.EMAIL_VALIDATE_CODE, produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.TEXT_PLAIN_VALUE})
    @Operation(summary = "Validate confirmation email token id and then activate email")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST"),
            @ApiResponse(responseCode = "404", description = "NOT FOUND"),
            @ApiResponse(responseCode = "406", description = "NOT ACCEPTABLE")})
    @ResponseStatus(HttpStatus.OK)
    public String validateEmail(@PathVariable UUID code) throws EmailValidationCodeExpiredException, EmailValidationCodeNotFoundException {
        log.info("receive the code {} for validation", code);
        mailService.validateEmail(code);
        log.info("the code {} was successfully validated", code);
        return getMessageSource(ResponseMessagesHandler.PositiveMessage.EMAIL_CONFIRMED);
    }
}
