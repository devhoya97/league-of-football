package com.lof.member.controller;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.lof.member.domain.Member;
import com.lof.member.service.MemberService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    /*
    db에 접근해서 검증된 엔티티를 생성하는 객체를 controller에서 호출하고 service 단에 해당 엔티티를
    넘겨주는 방식을 처음에 고려해봤으나, 그럼 검증된 엔티티를 생성하는 객체는 어느 layer에 포함시켜야 할지 애매하다고 느낌
    controller - service - repository 중 controller가 의존하면서, 해당 객체는 또 repository를 의존하고 있으니
    위치 상으로는 service가 맞는데, 기존 service가 repository에 의존하지 않게 도와준다는 측면에서 아예
    layer를 쪼개고 싶었음. 토스 영상을 참고해서 controller - service - implementation - repository로 쪼갰는데
    controller가 implementation을 호출하는 건 좋아보이지 않아서 controller에서 완벽한 엔티티를 생성해서
    service에 넘겨주는 방식은 포기했음.
     */
    @PostMapping("/signup")
    @ResponseStatus(HttpStatus.CREATED)
    public void signUp(@Valid @RequestBody SignUpRequest request) {
        Member member = new Member(request.username(), request.password());

        memberService.signUp(member);
    }
}
