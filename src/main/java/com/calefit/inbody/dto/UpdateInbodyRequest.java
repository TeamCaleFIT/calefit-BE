package com.calefit.inbody.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

@Getter
@NoArgsConstructor
public class UpdateInbodyRequest {

    @Positive(message = "인바디 id는 1이상의 양의 정수를 입력해주세요.")
    @NotNull(message = "인바디 id를 입력해주세요.")
    private Long inbodyId;

    @Positive(message = "골격근량은 양수를 입력해주세요.")
    @Nullable
    private Double muscle;

    @Positive(message = "체지방률은 양수를 입력해주세요.")
    @Nullable
    private Double bodyFat;

    @Positive(message = "몸무게는 양수를 입력해주세요.")
    @Nullable
    private Double bodyWeight;
}
