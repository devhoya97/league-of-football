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
    @DisplayName("loginId로 회원을 찾는다.")
    void findByLoginId() {
        // given
        String loginId = "loginId";
        Member member = MemberFixture.createMember(loginId, "password");
        memberRepository.save(member);

        // when
        Member foundMember = memberRepository.findByLoginId(loginId).get();

        // then
        assertThat(foundMember).isEqualTo(member);
    }

    @Test
    @DisplayName("중복되는 loginId를 저장하려고 시도하면 관련된 메시지와 함께 DataIntegrityViolationException이 발생한다.")
    void saveDuplicatedLoginID() {
        // given
        Member member1 = MemberFixture.createMember("loginId", "password");
        Member member2 = MemberFixture.createMember("loginId", "password");
        memberRepository.save(member1);

        // when & then
        assertThatThrownBy(() -> memberRepository.save(member2))
                .isInstanceOf(DataIntegrityViolationException.class)
                .hasMessageContaining("Duplicate")
                .hasMessageContaining("loginId");
    }


    @Test
    @DisplayName("저장되지 않은 loginId로 회원을 찾으면 empty를 반환한다.")
    void findByLoginIdNotExists() {
        // when
        Optional<Member> found = memberRepository.findByLoginId("loginId");

        // then
        assertThat(found.isEmpty()).isTrue();
    }

    @ParameterizedTest
    @CsvSource(value = {"loginId, true", "notExist, false"})
    @DisplayName("loginId에 해당하는 회원이 존재하는지 확인한다.")
    void existsByLoginId(String loginId, boolean expected) {
        // given
        String savedLoginId = "loginId";
        Member member = MemberFixture.createMember(savedLoginId, "password");
        memberRepository.save(member);

        // when
        boolean result = memberRepository.existsByLoginId(loginId);

        // then
        assertThat(result).isEqualTo(expected);

    }
}
