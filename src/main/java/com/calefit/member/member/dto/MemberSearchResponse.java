package com.calefit.member.member.dto;

import com.calefit.member.member.entity.Member;
import lombok.Getter;

@Getter
public class MemberSearchResponse {

    private final Long id;
    private final String email;
    private final String nickname;

    public MemberSearchResponse(Long id, String email, String nickname) {
        this.id = id;
        this.email = email;
        this.nickname = nickname;
    }

    public static MemberSearchResponse from(Member member) {
        return new MemberSearchResponse(
                member.getId(),
                member.getEmail(),
                member.getNickname()
        );
    }
}
