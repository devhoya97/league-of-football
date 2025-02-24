package com.lof.member.controller;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record SignUpRequest(
        @NotNull(message = "회원 이름을 입력해주세요.")
        @Pattern(regexp = "^(?=.*[a-zA-Z])[a-zA-Z0-9]{5,20}$", message = "회원 이름은 5~20자의 영문과 숫자로 구성되어야 하며, 숫자로만 이루어질 수 없습니다.")
        String username,
        @NotNull(message = "비밀번호를 입력해주세요.")
        @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*])[A-Za-z\\d!@#$%^&*]{8,20}$", message = "비밀번호는 8~20자이며, 대문자, 소문자, 숫자, 특수문자를 최소 하나씩 포함해야 합니다.")
        String password
) {
}
