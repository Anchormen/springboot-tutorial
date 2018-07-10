package nl.anchormen.usermanager.web;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.HashMap;

import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.fasterxml.jackson.databind.ObjectMapper;

@ControllerAdvice
public class ErrorHandler
{
    @ExceptionHandler({ ConstraintViolationException.class })
    @ResponseBody
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public HashMap<String, String> handleException(ConstraintViolationException e)
    {
        HashMap<String, String> errors = new HashMap<>();
        e.getConstraintViolations().forEach(violation ->
        {
            errors.put(violation.getPropertyPath().toString(), violation.getMessage());
        });
        ;
        return errors;
    }
    
    /**
     * Writes an error as a JSON response.
     * 
     * @param response
     * @param errorMessage
     * @param status
     */
    public static void writeError(HttpServletResponse response, String errorMessage, HttpStatus status)
    {
        response.setStatus(status.value());
        ObjectMapper objectMapper = new ObjectMapper();
        HashMap<String, String> errors = new HashMap<>();
        errors.put("errorMessage", errorMessage);
        errors.put("status", status + "");
        try
        {
            objectMapper.writeValue(response.getOutputStream(), errors);
        } catch (IOException e)
        {
            throw new UncheckedIOException(e);
        }
    }
}
