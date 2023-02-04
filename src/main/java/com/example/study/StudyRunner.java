package com.example.study;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;

@RequiredArgsConstructor
public class StudyRunner implements ApplicationRunner {

    @Override
    public void run(ApplicationArguments args) throws Exception {
        System.out.println("시작할때 뭔가 실행 해볼까?");
    }
}
