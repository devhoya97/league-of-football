package com.lof.member.controller;

import jakarta.validation.constraints.NotBlank;

public record SignUpRequest(
        @NotBlank(message = "회원 아이디를 입력해주세요.")
        String loginId,
        @NotBlank(message = "비밀번호를 입력해주세요.")
        String password
) {
}
