package com.calefit.member;

import com.calefit.common.base.ResponseCodes;
import com.calefit.common.entity.CommonResponseEntity;
import com.calefit.member.dto.MemberSearchResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
