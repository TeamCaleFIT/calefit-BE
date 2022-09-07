package com.calefit.member.member;

import com.calefit.common.base.ResponseCodes;
import com.calefit.common.entity.CommonResponseEntity;
import com.calefit.member.member.dto.MemberSearchResponse;
import com.calefit.member.member.dto.MemberSignUpRequest;
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
    public CommonResponseEntity<MemberSearchResponse> searchMemberProfileById(@PathVariable("id") Long memberId) {
        MemberSearchResponse memberSearchResponse = memberService.searchMemberProfile(memberId);
        return new CommonResponseEntity<>(ResponseCodes.MEMBER_SEARCH_SUCCESS, memberSearchResponse, HttpStatus.OK);
    }

    @PostMapping("/signUp")
    public CommonResponseEntity<Void> signUpMember(@Valid @RequestBody MemberSignUpRequest memberRequest) {
        memberService.signUpMember(memberRequest);
        return new CommonResponseEntity<>(ResponseCodes.MEMBER_SIGNUP_SUCCESS, HttpStatus.CREATED);
    }
}
