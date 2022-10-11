package com.calefit.inbody;

import com.calefit.common.base.ResponseCodes;
import com.calefit.common.dto.CommonDtoList;
import com.calefit.common.entity.CommonResponseEntity;
import com.calefit.inbody.domain.BodyComposition;
import com.calefit.inbody.dto.AddInbodyRequest;
import com.calefit.inbody.dto.SearchInbodyResponse;
import com.calefit.inbody.dto.UpdateInbodyRequest;
import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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

        return new CommonResponseEntity<>(ResponseCodes.INBODY_CREATE_SUCCESS, null, HttpStatus.CREATED);
    }

    @GetMapping
    public CommonResponseEntity<CommonDtoList<SearchInbodyResponse>> searchInbodies(@RequestParam Long memberId) {
        List<SearchInbodyResponse> inbodies = inbodyService.listInbodies(memberId);

        return new CommonResponseEntity<>(ResponseCodes.INBODY_SEARCH_SUCCESS, new CommonDtoList<>(inbodies), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public CommonResponseEntity<Void> deleteInbody(@PathVariable Long id, @RequestParam Long memberId) {
        inbodyService.deleteInbody(id, memberId);

        return new CommonResponseEntity<>(ResponseCodes.INBODY_DELETE_SUCCESS, null, HttpStatus.NO_CONTENT);
    }

    @PutMapping
    public CommonResponseEntity<Void> updateInbody(@RequestBody UpdateInbodyRequest updateInbodyRequest) {
        inbodyService.updateInbody(
            updateInbodyRequest.getInbodyId(),
            new BodyComposition(
                updateInbodyRequest.getMuscle(),
                updateInbodyRequest.getBodyFat(),
                updateInbodyRequest.getBodyWeight()
            )
        );

        return new CommonResponseEntity<>(ResponseCodes.INBODY_UPDATE_SUCCESS, null, HttpStatus.OK);
    }
}
