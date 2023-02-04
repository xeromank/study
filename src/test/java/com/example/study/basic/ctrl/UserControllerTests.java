package com.example.study.basic.ctrl;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.subsectionWithPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.Rule;
import com.example.study.RestDocsTest;
import com.example.study.basic.domain.Users;
import com.example.study.basic.service.UserSignService;
import com.example.study.basic.service.UsersService;
import com.example.study.common.dto.users.SignWithRequest;
import com.example.study.common.dto.users.SignWithResponse;
import com.example.study.common.enums.UserRole;
import com.example.study.config.WebSecurityConfig;
import java.util.Arrays;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.MockBeans;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.web.context.WebApplicationContext;


//@SpringBootTest(properties = "spring.config.location=" +
//    "classpath:/application.yml")
//@AutoConfigureMockMvc
//@Transactional
@WebMvcTest(controllers = {UserRestController.class},
    includeFilters = {
        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfig.class)})
@MockBeans({@MockBean(UserSignService.class), @MockBean(UsersService.class)})
public class UserControllerTests extends RestDocsTest {

    @Autowired
    private UserSignService userSignService;

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    void init(WebApplicationContext ctx) {
        Fixture.of(Users.class).addTemplate("1L", new Rule() {{
            add("userId", 1L);
            add("userName", "xero");
            add("signInId", "signInId1");
            add("signInPw", "signInPw");
            add("phoneNumber", "010-8709-4244");
            add("userRoles", Arrays.asList(UserRole.ROLE_USERS));
        }});

        Fixture.of(SignWithRequest.class).addTemplate("SignWithRequest1", new Rule() {{
            add("userName", "xero");
            add("signInId", "signInId1");
            add("signInPw", "signInPw");
            add("phoneNumber", "010-8709-4244");
        }});

        Fixture.of(SignWithResponse.class).addTemplate("SignWithResponse1", new Rule() {{
            add("userId", 1L);
            add("userName", "xero");
            add("signInId", "signInId1");
        }});

    }

    @Test
    @Order(1)
    void 회원가입_성공() throws Exception {

        SignWithRequest signWithRequestFixture = Fixture.from(SignWithRequest.class)
            .gimme("SignWithRequest1");
        SignWithResponse signWithResponseFixture = Fixture.from(SignWithResponse.class)
            .gimme("SignWithResponse1");

        given(userSignService.signWith(any(SignWithRequest.class))).willReturn(signWithResponseFixture);

        ResultActions result = mockMvc.perform(post("/user/sign-with")
            .content(objectMapper().writeValueAsString(signWithRequestFixture))
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isOk())
            .andDo(document("user-sign-with", preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                requestFields(
                    fieldWithPath("signInId").type(JsonFieldType.STRING).description("로그인 아이디"),
                    fieldWithPath("signInPw").type(JsonFieldType.STRING).description("로그인 암호"),
                    fieldWithPath("userName").type(JsonFieldType.STRING).description("사용자 이름"),
                    fieldWithPath("phoneNumber").type(JsonFieldType.STRING).description("연락처")
                ),
                responseFields(
                    fieldWithPath("success").type(JsonFieldType.BOOLEAN).description("isSuccess"),
                    fieldWithPath("response").type(JsonFieldType.OBJECT)
                        .description("Response Body"),
                    fieldWithPath("response.userId").type(JsonFieldType.NUMBER)
                        .description("사용자 고유 ID"),
                    fieldWithPath("response.signInId").type(JsonFieldType.STRING)
                        .description("로그인 아이디"),
                    fieldWithPath("response.userName").type(JsonFieldType.STRING)
                        .description("사용자 이름"),

                    subsectionWithPath("error").type(JsonFieldType.OBJECT).description("Error Body")
                        .optional()
                )
            ));

    }

}
