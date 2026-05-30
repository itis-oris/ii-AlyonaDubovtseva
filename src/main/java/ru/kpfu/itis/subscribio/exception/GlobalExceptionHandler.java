package ru.kpfu.itis.subscribio.exception;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(IllegalArgumentException.class)
    public String handleIllegalArgument(IllegalArgumentException e, HttpServletRequest request, Model model) {
        log.warn("Ошибка запроса {}: {}", request.getRequestURI(), e.getMessage());
        model.addAttribute("status", 400);
        model.addAttribute("title", "Некорректный запрос");
        model.addAttribute("message", e.getMessage());
        return "error/custom-error";
    }

    @ExceptionHandler(IllegalStateException.class)
    public String handleIllegalState(IllegalStateException e, HttpServletRequest request, Model model) {
        log.warn("Ошибка состояния {}: {}", request.getRequestURI(), e.getMessage());
        model.addAttribute("status", 400);
        model.addAttribute("title", "Операция невозможна");
        model.addAttribute("message", e.getMessage());
        return "error/custom-error";

    }


    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NoResourceFoundException.class)
    public String handleNoResourceFound(NoResourceFoundException e, HttpServletRequest request, Model model) {
        log.warn("Страница или ресурс не найден: {}", request.getRequestURI());
        model.addAttribute("status", 404);
        model.addAttribute("title", "Страница не найдена");
        model.addAttribute("message", "Такой страницы не существует или она была удалена.");
        return "error/custom-error";
    }
    @ExceptionHandler(Exception.class)
    public String handleException(Exception e, HttpServletRequest request, Model model) {
        log.error("Внутренняя ошибка на странице {}", request.getRequestURI(), e);
        model.addAttribute("status", 500);
        model.addAttribute("title", "Ошибка сервера");
        model.addAttribute("message", "Произошла непредвиденная ошибка. Попробуйте позже.");
        return "error/custom-error";
    }



}

