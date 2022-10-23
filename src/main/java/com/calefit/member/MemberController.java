package com.calefit.member;

import com.calefit.common.base.ResponseCodes;
import com.calefit.common.entity.CommonResponseEntity;
import com.calefit.member.dto.MemberLoginRequest;
import com.calefit.member.dto.MemberSearchResponse;
import com.calefit.member.dto.MemberSignUpRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/members")
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/{id}")
    public CommonResponseEntity<MemberSearchResponse> searchMemberProfileById(@PathVariable("id") Long Id) {
        MemberSearchResponse memberSearchResponse = memberService.searchMemberProfile(Id);
        return new CommonResponseEntity<>(ResponseCodes.MEMBER_SEARCH_SUCCESS, memberSearchResponse, HttpStatus.OK);
    }

    @PostMapping("/signUp")
    public CommonResponseEntity<Void> signUpMember(@Valid @RequestBody MemberSignUpRequest memberRequest) {
        memberService.signUpMember(
                memberRequest.getEmail(),
                memberRequest.getNickname(),
                memberRequest.getPassword());
        return new CommonResponseEntity<>(ResponseCodes.MEMBER_SIGNUP_SUCCESS, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public CommonResponseEntity<Void> loginMember(@Valid @RequestBody MemberLoginRequest memberRequest) {
        memberService.loginMember(
                memberRequest.getEmail(),
                memberRequest.getPassword());
        return new CommonResponseEntity<>(ResponseCodes.MEMBER_LOGIN_SUCCESS, HttpStatus.OK);
    }
}
