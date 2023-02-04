package com.example.study.basic.mapper.qualifier;

import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import org.mapstruct.Named;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class SignInPwQualifier {

    @Named("encodeSignInPw")
    public static String encodeSignInPw(String signInPw)
        throws GeneralSecurityException, UnsupportedEncodingException {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return passwordEncoder.encode(signInPw);
    }
}
