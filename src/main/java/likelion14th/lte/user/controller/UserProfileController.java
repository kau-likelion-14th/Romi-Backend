package likelion14th.lte.user.controller;

import io.swagger.v3.oas.annotations.Operation;
import likelion14th.lte.global.api.ApiResponse;
import likelion14th.lte.global.api.SuccessCode;
import likelion14th.lte.user.dto.request.CreateTestUserRequest;
import likelion14th.lte.user.dto.response.UserProfileResponse;
import likelion14th.lte.user.service.UserProfileService;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequestMapping("/api/prifile")
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)

public class UserProfileController {
    public final UserProfileService userProfileService;

    // [Q9. Controller 내부에서 userRepository.findById()를 직접 호출해서 유저를 찾지 않고,
    // 반드시 userProfileService를 호출하여 작업을 위임해야 하는 이유는 무엇인가요? (단일 책임 원칙 관점)]
    // 답변: Controller와 userProfileService가 하는 역할이 다름. Controller가 직접 호출하게 된다면 다른 아이의 역할을 뺏어오는거임.


    @GetMapping
    @Operation(summary = "유저 프로필 조회", description = "유저아이디를 받아 유저 프로필을 어쩌구")
    public ApiResponse<UserProfileResponse> getUserProfile(
            @AuthenticationPrincipal Jwt jwt
    ){
        Long userId = Long.valueOf(jwt.getSubject());
        UserProfileResponse userProfileResponse = userProfileService.getUserProfile(userId);

        return ApiResponse.onSuccess(SuccessCode.OK, userProfileResponse);

    }

    @PostMapping
    @Operation(summary = "테스트 유저를 생성", description = "이름, 한줄 소개 어쩌구")
    public ApiResponse<UserProfileResponse> createTestProfile(
            @RequestBody CreateTestUserRequest createTestUserRequest
            ){

        // [Q10. 클라이언트가 보낸 JSON 텍스트 데이터가 어떻게 자바 객체인 CreateTestUserRequest로
        // 변환 되는지앞의 어노테이션과 연관 지어 설명해 보세요.]
        // 답변:
        //@RequestBody 어노테이션이 붙으면 Spring 내부의 Jackson 라이브러리가 클라이언트의 JSON을 읽어서 DTO의 필드명에 맞게 자동으로 자바 객체로 변환해줌


        UserProfileResponse response = userProfileService.createTestUser(createTestUserRequest);
        return ApiResponse.onSuccess(SuccessCode.OK, response);
    }
}
