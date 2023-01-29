package com.calefit.docs;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.calefit.inbody.InbodyController;
import com.calefit.inbody.InbodyService;
import com.calefit.inbody.domain.BodyComposition;
import com.calefit.inbody.dto.AddInbodyRequest;
import com.calefit.inbody.dto.SearchInbodyResponse;
import com.calefit.inbody.dto.UpdateInbodyRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;

@WebMvcTest(InbodyController.class)
@ContextConfiguration(classes = InbodyController.class)
@AutoConfigureRestDocs
@ExtendWith(RestDocumentationExtension.class)
class InbodyControllerTest {

    @MockBean
    private InbodyService inbodyService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void createInbodyDocs() throws Exception {
        AddInbodyRequest addInbodyRequest = new AddInbodyRequest();
        ReflectionTestUtils.setField(addInbodyRequest, "memberId", 1L);
        ReflectionTestUtils.setField(addInbodyRequest, "measuredDateTime", LocalDateTime.of(2022, 9, 29, 9, 5));
        ReflectionTestUtils.setField(addInbodyRequest, "muscle", 30.0);
        ReflectionTestUtils.setField(addInbodyRequest, "bodyFat", 18.0);
        ReflectionTestUtils.setField(addInbodyRequest, "bodyWeight", 65.0);

        this.mockMvc.perform(post("/api/inbody")
            .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .content(objectMapper.writeValueAsString(addInbodyRequest)))
            .andExpect(status().isCreated())
            .andDo(print())
            .andDo(document("inbody/createInbody",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                requestFields(
                    fieldWithPath("memberId").type(JsonFieldType.NUMBER).description("멤버 id"),
                    fieldWithPath("measuredDateTime").type(JsonFieldType.STRING).description("측정 일자 및 시간"),
                    fieldWithPath("muscle").type(JsonFieldType.NUMBER).description("골격근량"),
                    fieldWithPath("bodyFat").type(JsonFieldType.NUMBER).description("체지방률"),
                    fieldWithPath("bodyWeight").type(JsonFieldType.NUMBER).description("몸무게")
                ),
                responseFields(
                    fieldWithPath("code").type(JsonFieldType.STRING).description("결과코드"),
                    fieldWithPath("message").type(JsonFieldType.STRING).description("결과메시지"),
                    fieldWithPath("data").type(JsonFieldType.NULL).description("응답데이터")
                )));
    }

    @Test
    void listInbodiesDocs() throws Exception {
        Long memberId = 1L;
        List<SearchInbodyResponse> inbodies = List.of(
            new SearchInbodyResponse(1L, LocalDateTime.of(2022, 8, 29, 9, 5), 30.0, 18.0, 65.0),
            new SearchInbodyResponse(2L, LocalDateTime.of(2022, 9, 27, 10, 18), 31.2, 17.0, 65.8)
        );

        given(inbodyService.listInbodies(memberId)).willReturn(inbodies);

        this.mockMvc.perform(get("/api/inbody?memberId=1"))
            .andExpect(status().isOk())
            .andDo(document("inbody/listInbodies",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                requestParameters(
                    parameterWithName("memberId").description("멤버 id")
                ),
                responseFields(
                    fieldWithPath("code").type(JsonFieldType.STRING).description("결과코드"),
                    fieldWithPath("message").type(JsonFieldType.STRING).description("결과메시지"),
                    fieldWithPath("data").type(JsonFieldType.OBJECT).description("응답데이터"),
                    fieldWithPath("data.count").type(JsonFieldType.NUMBER).description("인바디 개수"),
                    fieldWithPath("data.data[].id").type(JsonFieldType.NUMBER).description("인바디 id"),
                    fieldWithPath("data.data[].measuredDateTime").type(JsonFieldType.STRING).description("측정 일자 및 시간"),
                    fieldWithPath("data.data[].muscle").type(JsonFieldType.NUMBER).description("골격근량"),
                    fieldWithPath("data.data[].bodyFat").type(JsonFieldType.NUMBER).description("체지방률"),
                    fieldWithPath("data.data[].bodyWeight").type(JsonFieldType.NUMBER).description("몸무게")
                )));
    }

    @Test
    void updateInbodyDocs() throws Exception {
        UpdateInbodyRequest updateInbodyRequest = new UpdateInbodyRequest();
        ReflectionTestUtils.setField(updateInbodyRequest, "inbodyId", 2L);
        ReflectionTestUtils.setField(updateInbodyRequest, "muscle", 30.9);
        ReflectionTestUtils.setField(updateInbodyRequest, "bodyFat", 17.2);
        ReflectionTestUtils.setField(updateInbodyRequest, "bodyWeight", 66.0);

        doNothing()
            .when(inbodyService)
            .updateInbody(
                updateInbodyRequest.getInbodyId(),
                new BodyComposition(updateInbodyRequest.getMuscle(), updateInbodyRequest.getBodyFat(), updateInbodyRequest.getBodyWeight()));

        this.mockMvc.perform(put("/api/inbody")
            .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .content(objectMapper.writeValueAsString(updateInbodyRequest)))
            .andExpect(status().isOk())
            .andDo(print())
            .andDo(document("inbody/updateInbody",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                requestFields(
                    fieldWithPath("inbodyId").type(JsonFieldType.NUMBER).description("인바디 id"),
                    fieldWithPath("muscle").type(JsonFieldType.NUMBER).description("골격근량"),
                    fieldWithPath("bodyFat").type(JsonFieldType.NUMBER).description("체지방률"),
                    fieldWithPath("bodyWeight").type(JsonFieldType.NUMBER).description("몸무게")
                ),
                responseFields(
                    fieldWithPath("code").type(JsonFieldType.STRING).description("결과코드"),
                    fieldWithPath("message").type(JsonFieldType.STRING).description("결과메시지"),
                    fieldWithPath("data").type(JsonFieldType.NULL).description("응답데이터")
                )));
    }

    @Test
    void deleteInbodyDocs() throws Exception {
        Long inbodyId = 1L;
        Long memberId = 1L;

        doNothing()
            .when(inbodyService)
            .deleteInbody(inbodyId, memberId);

        this.mockMvc.perform(RestDocumentationRequestBuilders.delete("/api/inbody/{id}?memberId=1", inbodyId))
            .andExpect(status().isNoContent())
            .andDo(print())
            .andDo(document("inbody/deleteInbody",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                pathParameters(
                    parameterWithName("id").description("인바디 id")),
                requestParameters(
                    parameterWithName("memberId").description("멤버 id")
                ),
                responseFields(
                    fieldWithPath("code").type(JsonFieldType.STRING).description("결과코드"),
                    fieldWithPath("message").type(JsonFieldType.STRING).description("결과메시지"),
                    fieldWithPath("data").type(JsonFieldType.NULL).description("응답데이터")
                )));
    }
}
