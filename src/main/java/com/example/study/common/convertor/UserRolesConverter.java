package com.example.study.common.convertor;

import com.example.study.common.enums.UserRole;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;
import java.io.IOException;
import java.util.List;

public class UserRolesConverter implements AttributeConverter<List<UserRole>, String> {
    private static final ObjectMapper mapper = new ObjectMapper()
        .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        .configure(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES, false);

    @Override
    public String convertToDatabaseColumn(List<UserRole> attribute) {
        if(attribute == null){
            return null;
        }

        try {
            return mapper.writeValueAsString(attribute);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException();
        }
    }

    @Override
    public List<UserRole> convertToEntityAttribute(String dbData) {
        if(dbData == null){
            return null;
        }

        try {
            return mapper.readValue(dbData, new TypeReference<List<UserRole>>(){});
        } catch (IOException e) {
            throw new IllegalArgumentException();
        }
    }

}
