package com.example.study.basic.service;

import com.example.study.basic.domain.Users;
import com.example.study.basic.repo.UsersRepo;
import com.example.study.common.dto.users.SignWithRequest;
import com.example.study.common.dto.users.SignWithResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserSignService {

    private static PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    private final UsersRepo usersRepo;

    public SignWithResponse signWith(SignWithRequest signWithRequest) {
        Users user = Users.signWith(signWithRequest);
        usersRepo.save(user);
        return user.signWithResponse();
    }
}
