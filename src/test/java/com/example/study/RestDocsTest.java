package com.example.study;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.junit.rules.ExpectedException;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;

@AutoConfigureRestDocs
public class RestDocsTest {

    public ExpectedException exceptionRule = ExpectedException.none();
    protected static void print(Object output){
        System.out.println(output);
    }
    protected static ObjectMapper objectMapper(){
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, true);

        return objectMapper;
    }
}
