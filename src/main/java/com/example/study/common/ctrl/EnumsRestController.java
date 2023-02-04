package com.example.study.common.ctrl;

import static com.example.study.common.utils.ApiUtils.success;

import com.example.study.common.enums.ResultCode;
import com.example.study.common.enums.UserRole;
import com.example.study.common.utils.ApiUtils.ApiResult;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CommonsLog
@RestController
@RequestMapping("/enums")
@RequiredArgsConstructor
@Validated
public class EnumsRestController {
    @GetMapping
    public ApiResult<Map<String, List<?>>> getAllEnums() {
        Map<String, List<?>> rtn = new HashMap<>();

        rtn.put("ResultCode", ResultCode.codes());
        rtn.put("UserRole", UserRole.codes());

        return success(rtn);
    }
}
