package com.lof.auth.implement;

import java.util.Objects;

import org.springframework.stereotype.Component;

import com.lof.global.exception.BizException;
import com.lof.global.exception.ErrorCode;
import com.lof.member.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AuthRequestValidator {

    private final MemberRepository memberRepository;

    public void validateDuplicatedUsername(String username) {
        if (memberRepository.existsByUsername(username)) {
            throw new BizException(ErrorCode.DUPLICATED_USERNAME);
        }
    }

    public void validatePassword(String actualPassword, String requestPassword) {
        if (!Objects.equals(actualPassword, requestPassword)) {
            throw new BizException(ErrorCode.INVALID_LOGIN);
        }
    }
}
