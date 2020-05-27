package com.hillel.items_exchange.controller;

import com.hillel.items_exchange.dto.ChildDto;
import com.hillel.items_exchange.dto.UserDto;
import com.hillel.items_exchange.exception.BadRequestException;
import com.hillel.items_exchange.exception.UnauthorizedException;
import com.hillel.items_exchange.service.UserService;
import com.hillel.items_exchange.util.MessageSourceUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.PositiveOrZero;
import java.security.Principal;

@RestController
@RequestMapping("/user")
@CrossOrigin(origins = "http://localhost:4200")
@RequiredArgsConstructor
@Validated
@Slf4j
public class UserController {

    private final MessageSourceUtil messageSourceUtil;
    private final UserService userService;

    @GetMapping("/info/{id}")
    public @ResponseBody
    ResponseEntity<UserDto> getUserInfo(@Validated @PathVariable("id") Long id, Principal principal) {

        UserDto userDto = userService.getByUsernameOrEmail(principal.getName()).orElse(new UserDto());
        if (!id.equals(userDto.getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }

        return ResponseEntity.ok(userDto);
    }

    @PutMapping("/info/{id}")
    public ResponseEntity<UserDto> updateUserInfo(@Valid @RequestBody UserDto userDto,
                                                  @PositiveOrZero @PathVariable("id") long id,
                                                  Principal principal) {
        //TODO get user Id instead of nameOrEmail
        UserDto user = this.getUserDto(principal.getName());
        validateInfoOwner(user.getId(), id);
        return new ResponseEntity<>(userService.update(userDto), HttpStatus.OK);
    }

    @PutMapping("/child")
    public ResponseEntity<ChildDto> addChild(@Valid @RequestBody ChildDto childDto, BindingResult bindingResult, Principal principal) {
        if (bindingResult.hasErrors()) {
            throw new BadRequestException("{exception.validation}");
        }
        final var user = userService.findByUsernameOrEmail(principal.getName());
        if (user.isPresent()) {
            return new ResponseEntity<>(userService.addChild(user.get(), childDto), HttpStatus.OK);
        } else {
            throw new UnauthorizedException("{exception.user.not-found}");
        }
    }

    private UserDto getUserDto(String nameOrEmail) {
        return userService.getByUsernameOrEmail(nameOrEmail)
                .orElseThrow(() -> new UsernameNotFoundException(
                        messageSourceUtil.getExceptionMessageSourceWithAdditionalInfo(
                                "user.not-found", nameOrEmail)
                ));
    }

    private void validateInfoOwner(long loginUserId, long userId) {
        if (loginUserId != userId)
            throw new SecurityException(
                    messageSourceUtil.getExceptionMessageSourceWithId(userId, "user.not.allowed"));
    }
}
