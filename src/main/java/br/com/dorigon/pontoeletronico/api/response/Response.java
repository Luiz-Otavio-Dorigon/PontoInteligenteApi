package br.com.dorigon.pontoeletronico.api.response;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Luiz Otávio Dorigon <luiz.otavio.dorigon@gmail.com>  on 12/09/17.
 */
public class Response<T> {

    private T data;
    private List<String> errors;

    public Response(T data, List<String> errors) {
        this.data = data;
        this.errors = errors;
    }

    public Response(T data) {
        this.data = data;
    }

    public Response() {
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public List<String> getErrors() {
        if (errors == null) {
            errors = new ArrayList<>();
        }

        return errors;
    }

    public void setErrors(List<String> errors) {
        this.errors = errors;
    }
}
