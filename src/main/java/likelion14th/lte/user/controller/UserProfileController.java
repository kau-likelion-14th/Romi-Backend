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
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequestMapping("/api/prifile")
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)

public class UserProfileController {
    public final UserProfileService userProfileService;

    @GetMapping
    @Operation(summary = "유저 프로필 조회", description = "유저아이디를 받아 유저 프로필을 어쩌구")
    public ApiResponse<UserProfileResponse> getUserProfile(
            @RequestParam Long userId
    ){
        UserProfileResponse userProfileResponse = userProfileService.getUserProfile(userId);

        return ApiResponse.onSuccess(SuccessCode.OK, userProfileResponse);

    }

    @PostMapping
    @Operation(summary = "테스트 유저를 생성", description = "이름, 한줄 소개 어쩌구")
    public ApiResponse<UserProfileResponse> createTestProfile(
            @RequestBody CreateTestUserRequest createTestUserRequest
            ){

        UserProfileResponse response = userProfileService.createTestUser(createTestUserRequest);
        return ApiResponse.onSuccess(SuccessCode.OK, response);
    }
}
