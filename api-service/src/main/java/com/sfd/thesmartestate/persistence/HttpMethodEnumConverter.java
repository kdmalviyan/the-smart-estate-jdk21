package com.sfd.thesmartestate.persistence;

import jakarta.persistence.AttributeConverter;
import org.springframework.http.HttpMethod;

import java.util.stream.Stream;

/**
 * @author kuldeep
 */
public class HttpMethodEnumConverter implements AttributeConverter<HttpMethod, String> {
    @Override
    public String convertToDatabaseColumn(HttpMethod httpMethod){
        if (httpMethod== null) {
            return null;
        }
        return httpMethod.name();
    }

    @Override
    public HttpMethod convertToEntityAttribute(String name){
        if (name == null) {
            return null;
        }

        return Stream.of(HttpMethod.values())
                .filter(t -> name.equals(t.name()))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}
