package com.calefit.inbody.dto;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.format.annotation.DateTimeFormat;

@Getter
@AllArgsConstructor
public class AddInbodyRequest {

    @Positive(message = "멤버 id는 1이상의 양수 값을 입력해주세요.")
    @NotNull(message = "멤버 id를 입력해주세요.")
    private Long memberId;

    @Past(message = "미래의 날짜, 시간을 입력할 수 없습니다.")
    @NotNull(message = "인바디 측정 날짜 및 시간을 입력해주세요.")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm") //pattern을 사용할지, iso를 사용할지?
    private LocalDateTime measuredDateTime;

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
