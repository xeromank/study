package com.example.study.basic.ctrl;

import static com.example.study.common.utils.ApiUtils.success;

import com.example.study.basic.service.UserSignService;
import com.example.study.common.dto.users.SignWithRequest;
import com.example.study.common.dto.users.SignWithResponse;
import com.example.study.common.utils.ApiUtils.ApiResult;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserRestController {

    private final UserSignService userSignService;

    @GetMapping("/test/{testStr}")
    public String test(@PathVariable String testStr){
        return testStr;
    }

    @PostMapping("/sign-with")
    public ApiResult<SignWithResponse> signWith(@RequestBody SignWithRequest signWithRequest){
        return success(userSignService.signWith(signWithRequest));
    }

}
