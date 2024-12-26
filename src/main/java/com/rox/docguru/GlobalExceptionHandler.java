package com.rox.docguru;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.MissingRequestValueException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public String handleGenericException(Exception ex, Model model) {
        model.addAttribute("errorMessage", ex.getMessage());
        model.addAttribute("errorType", ex.getClass().getSimpleName());
        return "error";
    }

    @ExceptionHandler(MissingRequestValueException.class)
    public String handleMissingRequestValueException(MissingRequestValueException ex, Model model) {
        model.addAttribute("errorMessage", "Parametro mancante: " + ex.getMessage());
        model.addAttribute("errorType", "Errore richiesta incompleta");
        return "error";
    }
}
