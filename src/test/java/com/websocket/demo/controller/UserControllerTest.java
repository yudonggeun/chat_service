package com.websocket.demo.controller;


import com.websocket.demo.request.LoginRequest;
import com.websocket.demo.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataIntegrityViolationException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.responseHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.request.RequestDocumentation.formParameters;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class UserControllerTest extends RestDocs {

    @MockBean
    UserService userService;

    @DisplayName("회원가입 페이지")
    @Test
    public void createUserPage() throws Exception {
        //given //when //then
        mockMvc.perform(get("/user/create"))
                .andExpect(status().isOk())
                .andDo(document("create-user-page"));
    }

    @DisplayName("로그인 페이지")
    @Test
    public void loginPage() throws Exception {
        //given //when //then
        mockMvc.perform(get("/user/login"))
                .andExpect(status().isOk())
                .andDo(document("login-page"));
    }

    @DisplayName("친구 추가 페이지")
    @Test
    public void addFriendPage() throws Exception {
        //given //when //then
        mockMvc.perform(get("/user/friend"))
                .andExpect(status().isOk())
                .andDo(document("add-friend-page"));
    }

    @DisplayName("메인 페이지")
    @Test
    public void mainPage() throws Exception {
        //given //when //then
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andDo(document("main-page"));
    }

    @DisplayName("유저 생성 성공")
    @Test
    public void createUser() throws Exception {
        //given
        given(userService.create(any()))
                .willReturn(true);
        // when //then
        mockMvc.perform(post("/user/create")
                        .param("nickname", "신기방기")
                        .param("password", "1234"))
                .andExpectAll(
                        status().is3xxRedirection(),
                        header().string("Location", "/")
                )
                .andDo(print())
                .andDo(document("create-user-success",
                        formParameters(
                                parameterWithName("nickname").description("닉네임"),
                                parameterWithName("password").description("비밀번호")
                        ),
                        responseHeaders(
                                headerWithName("Location").description("redirect path")
                        )
                ));
    }

    @DisplayName("유저 생성 실패")
    @Test
    public void createUserWhenFail() throws Exception {
        //given
        given(userService.create(any()))
                .willThrow(RuntimeException.class);
        // when //then
        mockMvc.perform(post("/user/create")
                        .param("nickname", "신기방기")
                        .param("password", "1234"))
                .andExpectAll(
                        status().isOk()
                )
                .andDo(print())
                .andDo(document("create-user-fail",
                        formParameters(
                                parameterWithName("nickname").description("닉네임"),
                                parameterWithName("password").description("비밀번호")
                        )
                ));
    }

    @DisplayName("로그인 처리 성공")
    @Test
    public void login() throws Exception {
        //given
        given(userService.login(any()))
                .willReturn(true);
        // when //then
        mockMvc.perform(post("/user/login")
                        .param("nickname", "신기방기")
                        .param("password", "1234"))
                .andExpectAll(
                        status().is3xxRedirection(),
                        header().string("Location", "/")
                )
                .andDo(document("login-process-success",
                        formParameters(
                                parameterWithName("nickname").description("닉네임"),
                                parameterWithName("password").description("비밀번호")
                        ),
                        responseHeaders(
                                headerWithName("Location").description("redirect path")
                        )
                ));
    }

    @DisplayName("로그인 처리 실패")
    @Test
    public void loginWhenFail() throws Exception {
        //given
        given(userService.login(any()))
                .willReturn(false);
        // when //then
        mockMvc.perform(post("/user/login")
                        .param("nickname", "신기방기")
                        .param("password", "1234"))
                .andExpectAll(
                        status().isOk()
                )
                .andDo(document("login-process-fail",
                        formParameters(
                                parameterWithName("nickname").description("닉네임"),
                                parameterWithName("password").description("비밀번호")
                        )
                ));
    }

    @DisplayName("친구 추가 성공")
    @Test
    public void newFriend() throws Exception {
        given(userService.addFriend(any(), any())).willReturn(true);
        var loginInfo = new LoginRequest();
        loginInfo.setNickname("hello");
        // when //then
        mockMvc.perform(post("/user/friend")
                        .param("nickname", "hello1234")
                        .sessionAttr("user", loginInfo)
                ).andExpectAll(
                        status().is3xxRedirection(),
                        header().string("Location", "/")
                ).andDo(print())
                .andDo(document("new-friend-success",
                        responseHeaders(
                                headerWithName("Location").description("redirect path")
                        )
                ));
    }

    @DisplayName("친구 추가 실패")
    @Test
    public void newFriendWhenFail() throws Exception {
        given(userService.addFriend(any(), any())).willReturn(false);
        var loginInfo = new LoginRequest();
        loginInfo.setNickname("hello");
        // when //then
        mockMvc.perform(post("/user/friend")
                        .param("nickname", "hello1234")
                        .sessionAttr("user", loginInfo)
                ).andExpectAll(
                        status().isOk()
                ).andDo(print())
                .andDo(document("new-friend-fail"));
    }
}
