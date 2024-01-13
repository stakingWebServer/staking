package kr.project.database.results;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.project.database.common.ApiResponseMessage;
import org.springframework.http.ResponseEntity;

import java.util.Map;

public class ObjectResult {

    /**
     * Object 타입 ApiResult
     * root object는 하나만 나와야 하기 때문에 해당 메소드를 deprecated 처리함, build(Object resultObject)를 사용한다.
     * @param resultObjectName 리스트 객체 이름
     * @param resultObject 리스트 객체
     * @return ResponseEntity
     */
    @Deprecated
    public static ResponseEntity<?> build(String resultObjectName, Object resultObject) {
        ApiResult apiResult = ApiResult.blank()
                .add(resultObjectName, resultObject);
        return Result.ok(new ApiResponseMessage(apiResult));
    }

    /**
     * DTO Object 타입 ApiResult
     *
     * @param resultObject 객체
     * @return ResponseEntity
     */
    public static ResponseEntity<?> build(Object resultObject) {
        ObjectMapper objectMapper = new ObjectMapper();
        Map resultMap = objectMapper.convertValue(resultObject, Map.class);

        ApiResult apiResult = ApiResult.blank();
        apiResult.putAll(resultMap);
        return Result.ok(new ApiResponseMessage(apiResult));
    }


    public static ResponseEntity<?> ok() {
        return Result.ok(new ApiResponseMessage("SUCCESS", "정상처리", "", ""));
    }
}
