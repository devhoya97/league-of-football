package com.lof.member.service;

import java.util.regex.Pattern;

import org.springframework.stereotype.Component;

import com.lof.global.exception.BadRequestException;
import com.lof.global.exception.ErrorCode;
import com.lof.member.domain.Member;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberValidator {

    private static final Pattern LOGIN_ID_PATTERN = Pattern.compile("^(?=.*[a-zA-Z])[a-zA-Z0-9]{5,20}$");
    private static final Pattern PASSWORD_PATTERN = Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*])[A-Za-z\\d!@#$%^&*]{8,20}$");

    public void validate(Member member) {
        validateLoginId(member.getLoginId());
        validatePassword(member.getPassword());
    }

    private void validateLoginId(String loginId) {
        if (!LOGIN_ID_PATTERN.matcher(loginId).matches()) {
            throw new BadRequestException(ErrorCode.INVALID_LOGINID);
        }
    }

    private void validatePassword(String password) {
        if (!PASSWORD_PATTERN.matcher(password).matches()) {
            throw new BadRequestException(ErrorCode.INVALID_PASSWORD);
        }
    }
}
