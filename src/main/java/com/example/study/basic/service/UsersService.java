package com.example.study.basic.service;

import com.example.study.basic.domain.Users;
import com.example.study.basic.repo.UsersRepo;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class UsersService {

    private final UsersRepo usersRepo;

    public Users getOne(Long userId){
        return usersRepo.findById(userId).orElseThrow(() -> new EntityNotFoundException());
    }

    public Users getBySignInId(String signInId){
        return usersRepo.findBySignInId(signInId).orElseThrow(() -> new EntityNotFoundException());
    }
}
