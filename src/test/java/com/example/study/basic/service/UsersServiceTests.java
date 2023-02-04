package com.example.study.basic.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.springframework.test.util.AssertionErrors.fail;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.Rule;
import com.example.study.ServiceTest;
import com.example.study.basic.domain.Users;
import com.example.study.basic.repo.UsersRepo;
import com.example.study.common.dto.users.SignWithRequest;
import com.example.study.common.dto.users.SignWithResponse;
import com.example.study.common.enums.UserRole;
import java.util.Arrays;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.util.SerializationUtils;


public class UsersServiceTests extends ServiceTest {

    private UsersService usersService;
    private UserSignService userSignService;
    @Autowired
    private UsersRepo usersRepo;

    @BeforeEach
    void init() {
        Fixture.of(Users.class).addTemplate("1L", new Rule() {{
            add("userId", 1L);
            add("userName", firstName());
            add("signInId", "signInId1");
            add("signInPw", "signInPw");
            add("phoneNumber", "010-8709-4244");
            add("userRoles", Arrays.asList(UserRole.ROLE_USERS));
        }});

        Fixture.of(SignWithRequest.class).addTemplate("SignWithRequest1", new Rule() {{
            add("userName", firstName());
            add("signInId", "signInId1");
            add("signInPw", "signInPw");
            add("phoneNumber", "010-8709-4244");
        }});

//        usersRepo = spy(UsersRepo.class);
//        given(usersRepo.findById(1L)).willReturn(Optional.of(Fixture.from(Users.class).gimme("1L")));
//        given(usersRepo.findById(2L)).willReturn(Optional.of(Fixture.from(Users.class).gimme("2L")));
//        given(usersRepo.findById(3L)).willReturn(Optional.of(Fixture.from(Users.class).gimme("3L")));

        userSignService = new UserSignService(usersRepo);
        usersService = new UsersService(usersRepo);

    }

    @Test
    @Order(1)
    void 회원가입_성공() throws Exception {

        SignWithRequest signWithRequestFixture = Fixture.from(SignWithRequest.class)
            .gimme("SignWithRequest1");
        SignWithResponse signWithResponse = userSignService.signWith(signWithRequestFixture);

        Users user = usersService.getBySignInId(signWithRequestFixture.getSignInId());
        assertEquals(user.getUserId(), signWithResponse.getUserId());
        assertEquals(user.getSignInId(), signWithResponse.getSignInId());
    }

    @Test
    @Order(2)
    void 회원가입_실패() throws Exception {
        try {

            SignWithRequest signWithRequestFixture = Fixture.from(SignWithRequest.class)
                .gimme("SignWithRequest1");
            SignWithRequest signWithRequest = SerializationUtils.clone(
                signWithRequestFixture);
            assertNotEquals(signWithRequest, signWithRequestFixture);

            SignWithResponse signWithResponseSuccess = userSignService.signWith(signWithRequestFixture);
            SignWithResponse signWithResponseFail = userSignService.signWith(signWithRequest);
            assertNotEquals(signWithResponseSuccess, signWithResponseFail);

            fail("오류나야 하는데?");
        } catch (DataIntegrityViolationException e) {
            print("exception ======= ");
            print(e);
        }
    }

}
