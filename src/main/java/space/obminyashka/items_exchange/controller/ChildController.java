package space.obminyashka.items_exchange.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import space.obminyashka.items_exchange.api.ApiKey;
import space.obminyashka.items_exchange.dto.ChildDto;
import space.obminyashka.items_exchange.service.ChildService;
import space.obminyashka.items_exchange.util.ResponseMessagesHandler;

import java.util.List;


@RestController
@Tag(name = "Child")
@RequiredArgsConstructor
@Validated
public class ChildController {
    private static final int MAX_CHILDREN_AMOUNT = 10;
    private final ChildService childService;

    @GetMapping(value = ApiKey.USER_CHILD, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Find a registered requested user's children data")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "404", description = "NOT FOUND")})
    @ResponseStatus(HttpStatus.OK)
    public List<ChildDto> getChildren(@Parameter(hidden = true) Authentication authentication) {
        return childService.getChildrenByUsername(authentication.getName());
    }

    @PreAuthorize("hasAnyRole('USER', 'ADMIN', 'MODERATOR')")
    @PutMapping(value = ApiKey.USER_CHILD, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Update child data for a registered requested user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST"),
            @ApiResponse(responseCode = "403", description = "FORBIDDEN")})
    @ResponseStatus(HttpStatus.OK)
    public List<ChildDto> updateChildren(@Size(max = MAX_CHILDREN_AMOUNT, message = "{" + ResponseMessagesHandler.ExceptionMessage.CHILDREN_AMOUNT + "}")
                                         @RequestBody List<@Valid ChildDto> childrenDto,
                                         @Parameter(hidden = true) Authentication authentication) {
        return childService.updateChildren(authentication.getName(), childrenDto);
    }
}
