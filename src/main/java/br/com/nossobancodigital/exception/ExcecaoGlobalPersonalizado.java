package br.com.nossobancodigital.exception;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice()
@SuppressWarnings("unused")
public class ExcecaoGlobalPersonalizado extends ResponseEntityExceptionHandler {

    // manipulador de erros para @Valid
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid
    (MethodArgumentNotValidException ex,
     HttpHeaders headers,
     HttpStatus status, WebRequest request) {

        Map<String, Object> body = new LinkedHashMap<>();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        String date = dateFormat.format(new Date());
        body.put("horário", date);
        body.put("status", status.value());

        //Pega todos os errors
        List<String> erros = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .sorted()
                .collect(Collectors.toList());

        body.put("erros", erros);

        return new ResponseEntity<>(body, headers, status);

    }

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<Object> handleException(Exception e) {
        Map<String, Object> body = new LinkedHashMap<>();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        String date = dateFormat.format(new Date());
        HttpStatus status = HttpStatus.BAD_REQUEST;
        body.put("horário", date);
        body.put("status", status.value());

        if (e instanceof DataIntegrityViolationException) {
            if (e.getMessage().contains("CLIENTE_PF(CPF)")) {
                body.put("erro", "CPF não pode ser duplicado");
            }
            if (e.getMessage().contains("CLIENTE_PF(EMAIL)")) {
                body.put("erro", "Email não pode ser duplicado");
            }

            return new ResponseEntity<>(body, status);
        } else if (e instanceof ConstraintViolationException) {
            List<String> erros = ((ConstraintViolationException) e).getConstraintViolations()
                    .stream()
                    .map(ConstraintViolation::getMessageTemplate)
                    .sorted()
                    .collect(Collectors.toList());
            body.put("erros", erros);

            return new ResponseEntity<>(body, status);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
}
