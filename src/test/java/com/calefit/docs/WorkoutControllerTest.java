package com.calefit.docs;

import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.calefit.workout.WorkoutController;
import com.calefit.workout.WorkoutService;
import com.calefit.workout.dto.AddWorkoutLogsRequest;
import com.calefit.workout.dto.SearchWorkoutResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(WorkoutController.class)
@ContextConfiguration(classes = WorkoutController.class)
@AutoConfigureRestDocs
@ExtendWith(RestDocumentationExtension.class)
public class WorkoutControllerTest {

    @MockBean
    private WorkoutService workoutService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    private final String CUSTOM_HEADER_REFRESH_TOKEN = "RefreshToken";

    @Test
    void createWorkoutLogsDocs() throws Exception {
        AddWorkoutLogsRequest addWorkoutLogsRequest = objectMapper.readValue(getWorkoutLogsJson(), AddWorkoutLogsRequest.class);

        this.mockMvc.perform(post("/api/work-outs/logs")
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .header(HttpHeaders.AUTHORIZATION, "Bearer access_token_string...")
                .header(CUSTOM_HEADER_REFRESH_TOKEN, "Bearer refresh_token_string...")
                .content(objectMapper.writeValueAsString(addWorkoutLogsRequest)))
            .andExpect(status().isCreated())
            .andDo(print())
            .andDo(document("workout/log/createWorkoutLogs",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                requestFields(
                    fieldWithPath("workoutDate").type(JsonFieldType.STRING).description("운동 날짜"),
                    fieldWithPath("addWorkoutLogs").type(JsonFieldType.ARRAY).description("운동 로그"),
                    fieldWithPath("addWorkoutLogs[].workoutId").type(JsonFieldType.NUMBER).description("운동 종목 id"),
                    fieldWithPath("addWorkoutLogs[].order").type(JsonFieldType.NUMBER).description("운동 로그 순서"),
                    fieldWithPath("addWorkoutLogs[].addWorkoutSets").type(JsonFieldType.ARRAY).description("운동 세트별 기록"),
                    fieldWithPath("addWorkoutLogs[].addWorkoutSets[].setNumber").type(JsonFieldType.NUMBER).description("세트 번호"),
                    fieldWithPath("addWorkoutLogs[].addWorkoutSets[].goalWeight").type(JsonFieldType.NUMBER).description("목표 중량"),
                    fieldWithPath("addWorkoutLogs[].addWorkoutSets[].goalReps").type(JsonFieldType.NUMBER).description("목표 횟수"),
                    fieldWithPath("addWorkoutLogs[].addWorkoutSets[].finishedWeight").type(JsonFieldType.NUMBER).description("실수행 중량"),
                    fieldWithPath("addWorkoutLogs[].addWorkoutSets[].finishedReps").type(JsonFieldType.NUMBER).description("실수행 횟수")
                ),
                responseFields(
                    fieldWithPath("code").type(JsonFieldType.STRING).description("결과코드"),
                    fieldWithPath("message").type(JsonFieldType.STRING).description("결과메시지"),
                    fieldWithPath("data").type(JsonFieldType.NULL).description("응답데이터")
                )));
    }

    @Test
    void listInbodiesDocs() throws Exception {
        String workoutName = "스쿼트";
        SearchWorkoutResponse searchWorkoutResponse = new SearchWorkoutResponse(1, "스쿼트", "image_url.com",
            "스쿼트 운동 설명...");
        List<SearchWorkoutResponse> workouts = List.of(searchWorkoutResponse);

        given(workoutService.listWorkouts(workoutName)).willReturn(workouts);

        this.mockMvc.perform(get("/api/work-outs?name=스쿼트"))
            .andExpect(status().isOk())
            .andDo(document("workout/listWorkouts",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                requestParameters(
                    parameterWithName("name").description("운동 종목명")
                ),
                responseFields(
                    fieldWithPath("code").type(JsonFieldType.STRING).description("결과코드"),
                    fieldWithPath("message").type(JsonFieldType.STRING).description("결과메시지"),
                    fieldWithPath("data").type(JsonFieldType.OBJECT).description("응답데이터"),
                    fieldWithPath("data.count").type(JsonFieldType.NUMBER).description("검색된 운동 종목 개수"),
                    fieldWithPath("data.data[].id").type(JsonFieldType.NUMBER).description("운동 종목 id"),
                    fieldWithPath("data.data[].name").type(JsonFieldType.STRING).description("운동 종목명"),
                    fieldWithPath("data.data[].imageUrl").type(JsonFieldType.STRING).description("운동 이미지"),
                    fieldWithPath("data.data[].description").type(JsonFieldType.STRING).description("운동 설명")
                )));
    }

    private String getWorkoutLogsJson() {
        return "{\n"
            + "  \"workoutDate\": \"2022-12-27\",\n"
            + "  \"addWorkoutLogs\": [\n"
            + "    {\n"
            + "      \"workoutId\": 1, \n"
            + "      \"order\": 1, \n"
            + "      \"addWorkoutSets\": [\n"
            + "        {\n"
            + "          \"setNumber\": 1,\n"
            + "          \"goalWeight\": 50.0, \n"
            + "          \"goalReps\": 12, \n"
            + "          \"finishedWeight\": 50.0,\n"
            + "          \"finishedReps\": 12 \n"
            + "        },\n"
            + "        {\n"
            + "          \"setNumber\": 2,\n"
            + "          \"goalWeight\": 60.0,\n"
            + "          \"goalReps\": 12,\n"
            + "          \"finishedWeight\": 60.0,\n"
            + "          \"finishedReps\": 9\n"
            + "        }\n"
            + "      ]\n"
            + "    },\n"
            + "    {\n"
            + "      \"workoutId\": 5,\n"
            + "      \"order\": 2, \n"
            + "      \"addWorkoutSets\": [\n"
            + "        {\n"
            + "          \"setNumber\": 1,\n"
            + "          \"goalWeight\": 20.0,\n"
            + "          \"goalReps\": 12,\n"
            + "          \"finishedWeight\": 20.0,\n"
            + "          \"finishedReps\": 12\n"
            + "        },\n"
            + "        {\n"
            + "          \"setNumber\": 2,\n"
            + "          \"goalWeight\": 30.0,\n"
            + "          \"goalReps\": 12,\n"
            + "          \"finishedWeight\": 30.0,\n"
            + "          \"finishedReps\": 12\n"
            + "        },\n"
            + "        {\n"
            + "          \"setNumber\": 3,\n"
            + "          \"goalWeight\": 50.0,\n"
            + "          \"goalReps\": 8,\n"
            + "          \"finishedWeight\": 50.0,\n"
            + "          \"finishedReps\": 6\n"
            + "        }\n"
            + "      ]\n"
            + "    }\n"
            + "  ]\n"
            + "}";
    }
}
