package com.calefit.member.dto;

import com.calefit.member.domain.BodyInfo;
import com.calefit.member.entity.Member;
import lombok.Getter;

@Getter
public class MemberSearchResponse {

    private final Long id;
    private final String email;
    private final String nickname;
    private final BodyInfo bodyInfo;

    public MemberSearchResponse(Long id, String email, String nickname, BodyInfo bodyInfo) {
        this.id = id;
        this.email = email;
        this.nickname = nickname;
        this.bodyInfo = bodyInfo;
    }

    public static MemberSearchResponse from(Member member) {
        return new MemberSearchResponse(
                member.getId(),
                member.getEmail(),
                member.getNickname(),
                member.getBodyInfo()
        );
    }
}
