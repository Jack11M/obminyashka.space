package space.obminyashka.items_exchange.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import space.obminyashka.items_exchange.rest.api.ApiKey;
import space.obminyashka.items_exchange.rest.exception.not_found.EmailValidationCodeNotFoundException;
import space.obminyashka.items_exchange.rest.request.ValidationEmailRequest;
import space.obminyashka.items_exchange.service.MailService;
import space.obminyashka.items_exchange.service.UserService;
import space.obminyashka.items_exchange.service.util.EmailType;

import java.util.UUID;

import static space.obminyashka.items_exchange.rest.response.message.MessageSourceProxy.getMessageSource;
import static space.obminyashka.items_exchange.rest.response.message.ResponseMessagesHandler.PositiveMessage.EMAIL_CONFIRMED;
import static space.obminyashka.items_exchange.rest.response.message.ResponseMessagesHandler.PositiveMessage.EMAIL_RESEND_CODE;

@RestController
@Slf4j
@Tag(name = "Email")
@RequiredArgsConstructor
@Validated
public class EmailController {
    private final MailService mailService;
    private final UserService userService;

    @GetMapping(value = ApiKey.EMAIL_VALIDATE_CODE, produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.TEXT_PLAIN_VALUE})
    @Operation(summary = "Validate confirmation email token id and then activate email")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST"),
            @ApiResponse(responseCode = "404", description = "NOT FOUND"),
            @ApiResponse(responseCode = "406", description = "NOT ACCEPTABLE")})
    @ResponseStatus(HttpStatus.OK)
    public String validateEmail(@PathVariable UUID code) throws EmailValidationCodeNotFoundException {
        log.info("[EmailController] Received the code '{}' for validation", code);
        mailService.validateEmail(code);
        log.info("[EmailController] The code '{}' was successfully validated", code);
        return getMessageSource(EMAIL_CONFIRMED);
    }

    @PostMapping(value = ApiKey.EMAIL_RESEND_CODE, produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.TEXT_PLAIN_VALUE})
    @Operation(summary = "endpoint resend activation code")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST"),
            @ApiResponse(responseCode = "404", description = "NOT FOUND"),
            @ApiResponse(responseCode = "406", description = "NOT ACCEPTABLE")})
    @ResponseStatus(HttpStatus.OK)
    public String resendEmailCode(@Valid @RequestBody ValidationEmailRequest email, @RequestHeader(HttpHeaders.HOST) String host) {
        var uuid = mailService.sendEmailTemplateAndGenerateConfrimationCode(email.email(), EmailType.REGISTRATION, host);
        userService.resendValidateCode(email.email(), uuid);
        return getMessageSource(EMAIL_RESEND_CODE);
    }
}
