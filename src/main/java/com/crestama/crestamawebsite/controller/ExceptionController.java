package com.crestama.crestamawebsite.controller;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ExceptionController implements ErrorController {
    @RequestMapping("/error")
    public String unauthorized(HttpServletRequest request, Model model) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);

        if (status != null) {
            int statusCode = Integer.parseInt(status.toString());

            if (statusCode == HttpStatus.NOT_FOUND.value()) {
                model.addAttribute("statusCode", HttpStatus.NOT_FOUND.value());
                model.addAttribute("statusDescription", "Not Found");
                model.addAttribute("message", "Sorry, we can't find what you are looking for.");

                return "error/exception";
            }
            else if (statusCode == HttpStatus.UNAUTHORIZED.value()) {
                model.addAttribute("statusCode", HttpStatus.UNAUTHORIZED.value());
                model.addAttribute("statusDescription", "Unauthorized");
                model.addAttribute("message", "Unauthorized Request.");

                return "error/exception";
            }
        }
        model.addAttribute("statusCode", 500);
        model.addAttribute("statusDescription", "Internal Server Error");
        model.addAttribute("message", "Something went wrong!");

        return "error/exception";
    }
}
