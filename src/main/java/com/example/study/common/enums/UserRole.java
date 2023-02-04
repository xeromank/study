package com.example.study.common.enums;

import com.example.study.common.object.EnumDescription;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UserRole {

    ROLE_ANONYMOUS(new EnumDescription("익명사용자", "ANONYMOUS")),
    ROLE_MASTER(new EnumDescription("마스터", "MASTER")),
    ROLE_USERS(new EnumDescription("사용자", "USERS"));

    private final EnumDescription desc;

    @JsonValue
    public String getValue(){
        return this.name();
    }

    @JsonCreator
    public static UserRole of(String name) {
        for (UserRole obj : UserRole.values()) {
            if (obj.name().equals(name)) {
                return obj;
            }
        }
        return null;
    }

    public static List<Map<String, Object>> codes() {

        List<Map<String, Object>> codes = new ArrayList<>();

        for (UserRole obj : UserRole.values()) {
            Map<String, Object> map = new HashMap<>();

            map.put("name", obj.name());
            map.put("desc", obj.getDesc());

            codes.add(map);
        }

        return codes;
    }
}
