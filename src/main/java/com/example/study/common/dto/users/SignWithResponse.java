package com.example.study.common.dto.users;

import java.io.Serial;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
public class SignWithResponse implements Serializable {

    @Serial
    private static final long serialVersionUID = 8845710617407315023L;

    private Long userId;
    private String signInId;
    private String userName;
}
