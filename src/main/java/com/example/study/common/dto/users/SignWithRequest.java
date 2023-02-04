package com.example.study.common.dto.users;

import java.io.Serial;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SignWithRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 1317229065320098537L;

    private String signInId;
    private String signInPw;
    private String phoneNumber;
    private String userName;
}
