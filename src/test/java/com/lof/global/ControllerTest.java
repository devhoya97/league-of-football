package com.lof.global;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.lof.auth.controller.AuthController;
import com.lof.auth.service.AuthService;
import com.lof.member.controller.MemberController;
import com.lof.member.service.MemberService;

@WebMvcTest({
        AuthController.class,
        MemberController.class
})
public abstract class ControllerTest {

    @Autowired
    protected MockMvc mockMvc;

    @MockitoBean
    protected AuthService authService;

    @MockitoBean
    protected MemberService memberService;
}
