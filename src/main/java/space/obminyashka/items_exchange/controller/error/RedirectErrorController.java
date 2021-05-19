package space.obminyashka.items_exchange.controller.error;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

@Controller
public class RedirectErrorController {

    @GetMapping("/error")
    public String redirectError(HttpServletRequest request) {
        String attribute = (String) request.getAttribute(RequestDispatcher.FORWARD_REQUEST_URI);
        int errorCode = (int) request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        return errorCode == HttpStatus.NOT_FOUND.value()
                ? "redirect:/?uri=" + attribute
                : "redirect:/?code=" + errorCode;
    }

    @RequestMapping(value = "/error", method = {RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE})
    @ResponseStatus
    public String redirectErrors(Exception e) {
        return e.getLocalizedMessage();
    }
}
