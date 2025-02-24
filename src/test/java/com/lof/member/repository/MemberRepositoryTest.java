package com.lof.member.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;

import com.lof.member.domain.Member;
import com.lof.member.fixture.MemberFixture;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE) // 테스트용 인메모리 DB가 아닌 MySQL에 실제 연결
class MemberRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

    @Test
    @DisplayName("회원 이름으로 회원을 찾는다.")
    void findByUsername() {
        // given
        String username = "username";
        Member member = MemberFixture.createMember(username, "password");
        memberRepository.save(member);

        // when
        Member foundMember = memberRepository.findByUsername(username).get();

        // then
        assertThat(foundMember).isEqualTo(member);
    }

    @Test
    @DisplayName("중복되는 회원 이름을 저장하려고 시도하면 관련된 메시지와 함께 DataIntegrityViolationException이 발생한다.")
    void saveDuplicatedUsername() {
        // given
        Member member1 = MemberFixture.createMember("username", "password");
        Member member2 = MemberFixture.createMember("username", "password");
        memberRepository.save(member1);

        // when & then
        assertThatThrownBy(() -> memberRepository.save(member2))
                .isInstanceOf(DataIntegrityViolationException.class)
                .hasMessageContaining("Duplicate")
                .hasMessageContaining("username");
    }


    @Test
    @DisplayName("저장되지 않은 회원 이름으로 회원을 찾으면 empty를 반환한다.")
    void findByUsernameNotExists() {
        // when
        Optional<Member> found = memberRepository.findByUsername("username");

        // then
        assertThat(found.isEmpty()).isTrue();
    }

    @ParameterizedTest
    @CsvSource(value = {"username, true", "notExist, false"})
    @DisplayName("회원 이름 해당하는 회원이 존재하는지 확인한다.")
    void existsByUsername(String username, boolean expected) {
        // given
        String savedUsername = "username";
        Member member = MemberFixture.createMember(savedUsername, "password");
        memberRepository.save(member);

        // when
        boolean result = memberRepository.existsByUsername(username);

        // then
        assertThat(result).isEqualTo(expected);

    }
}
