package com.lof.member.fixture;

import java.lang.reflect.Field;

import com.lof.member.controller.SignUpRequest;
import com.lof.member.domain.Member;

public class MemberFixture {

    public static final String VALID_LOGIN_ID = "user1";
    public static final String VALID_PASSWORD = "Password1!";

    private MemberFixture() {
    }

    // member에 필드가 추가되면, 여러 테스트 코드가 깨지는데 이 때 변경 지점을 한 곳으로 몰기 위해 Fixture 사용
    public static Member createMember(String username, String password) {
        return new Member(username, password);
    }

    public static Member createMember(long id, String username, String password) throws NoSuchFieldException, IllegalAccessException {
        Member member = new Member(username, password);
        Field idField = Member.class.getDeclaredField("id");
        idField.setAccessible(true);
        idField.set(member, id);

        return member;
    }

    public static String createSignUpRequestString(String username, String password) {
        return String.format("""
                {
                    "username": "%s",
                    "password": "%s"
                }
                """, username, password);
    }

    public static SignUpRequest createSignUpRequest(String username, String password) {
        return new SignUpRequest(username, password);
    }
}
