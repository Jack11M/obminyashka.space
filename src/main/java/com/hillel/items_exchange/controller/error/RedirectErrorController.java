package com.hillel.items_exchange.controller.error;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

@Controller
public class RedirectErrorController implements ErrorController {

    @GetMapping("/error")
    public String redirectError(HttpServletRequest request) {
        String attribute = (String) request.getAttribute(RequestDispatcher.FORWARD_REQUEST_URI);
        int errorCode = (int) request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        if (errorCode == HttpStatus.NOT_FOUND.value()) {
            return "redirect:/?uri=" + attribute;
        }
        return "redirect:/?code=" + errorCode;
    }

    @Override
    public String getErrorPath() {
        return "/error";
    }
}
