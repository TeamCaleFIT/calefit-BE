package com.calefit.inbody;

import static com.calefit.common.base.ResponseCodeAndMessages.INBODY_CREATE_SUCCESS;

import com.calefit.common.base.ResponseCodeAndMessages;
import com.calefit.common.dto.CommonDtoList;
import com.calefit.common.entity.CommonResponseEntity;
import com.calefit.inbody.domain.BodyComposition;
import com.calefit.inbody.dto.AddInbodyRequest;
import com.calefit.inbody.dto.SearchInbodyResponse;
import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/inbody")
public class InbodyController {

    private final InbodyService inbodyService;

    @PostMapping
    public CommonResponseEntity<Void> createInbody(@RequestBody @Valid AddInbodyRequest addInbodyRequest) {
        inbodyService.createInbody(
            addInbodyRequest.getMemberId(),
            addInbodyRequest.getMeasuredDateTime(),
            new BodyComposition(
                addInbodyRequest.getMuscle(),
                addInbodyRequest.getBodyFat(),
                addInbodyRequest.getBodyWeight()
            )
        );

        return new CommonResponseEntity<>(INBODY_CREATE_SUCCESS, null, HttpStatus.CREATED);
    }

    @GetMapping
    public CommonResponseEntity<CommonDtoList<SearchInbodyResponse>> listInboies(@RequestParam Long memberId) {
        List<SearchInbodyResponse> inbodies = inbodyService.listInbodies(memberId);

        return new CommonResponseEntity<>(ResponseCodeAndMessages.INBODY_SEARCH_SUCCESS, new CommonDtoList<>(inbodies), HttpStatus.OK);
    }
}
