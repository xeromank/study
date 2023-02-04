package com.example.study.common.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
@RequiredArgsConstructor
public enum ResultCode {

    NONE( "NONE"),
    INVALID_PARAM("Parameter Miss Match")
    ;

    private final String desc;

    @JsonCreator
    public static ResultCode fromId(String name) {
        for (ResultCode type : values()) {
            if (Objects.equals(type.name(), name)) {
                return type;
            }
        }
        return ResultCode.NONE;
    }

    public static List<Map<String, Object>> codes(){

        return Arrays.stream(ResultCode.values()).map(o -> {
            Map<String, Object> map = new HashMap<>();
            map.put("name", o.name());
            map.put("desc", o.getDesc());
            return map;
        }).collect(Collectors.toList());
    }
}
