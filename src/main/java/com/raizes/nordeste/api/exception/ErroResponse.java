package com.raizes.nordeste.api.exception;


import com.fasterxml.jackson.annotation.JsonInclude;
import java.time.LocalDateTime;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErroResponse {

    private String error;
    private String message;
    private List<DetalheErro> details;
    private LocalDateTime timestamp;
    private String path;

    // garante que nao vai existir um erro sem mensagem ou sem timestamp
    private ErroResponse() {}

    public static ErroResponse of(String error,
                                  String message,
                                  List<DetalheErro> details,
                                  String path)
    {
        ErroResponse response = new ErroResponse();
        response.error    = error;
        response.message  = message;
        response.details  = details;
        response.timestamp = LocalDateTime.now();
        response.path     = path;
        return response;
    }

    public static ErroResponse of(String error,
                                  String message,
                                  String path) {
        return of(error, message, List.of(), path);
    }

    public String getError()            { return error; }
    public String getMessage()         { return message; }
    public List<DetalheErro> getDetails() { return details; }
    public LocalDateTime getTimestamp()   { return timestamp; }
    public String getPath()               { return path; }

    public record DetalheErro(String field, String issue) {}
}