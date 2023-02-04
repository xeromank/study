package com.example.study.basic.repo;

import com.example.study.basic.domain.Users;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsersRepo extends JpaRepository<Users, Long> {

    Optional<Users> findBySignInId(String signInId);

}
