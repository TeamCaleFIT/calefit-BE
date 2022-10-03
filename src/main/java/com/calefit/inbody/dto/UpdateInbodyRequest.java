package com.calefit.inbody.dto;

import java.time.LocalDateTime;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Positive;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

@Getter
@NoArgsConstructor
public class UpdateInbodyRequest {

    @Positive(message = "인바디 id는 1이상의 양의 정수를 입력해주세요.")
    @NotNull(message = "인바디 id를 입력해주세요.")
    private Long inbodyId;

    @Positive(message = "골격근량을 양수 값을 입력해주세요.")
    @NotNull(message = "인바디 측정 날짜 및 시간을 입력해주세요.")
    private Double muscle;

    @Positive(message = "체지방률은 양수 값을 입력해주세요.")
    @NotNull(message = "체지방률을 입력해주세요.")
    private Double bodyFat;

    @Positive(message = "몸무게는 양수 값을 입력해주세요.")
    @NotNull(message = "몸무게를을 입력해주세요.")
    private Double bodyWeight;
}
