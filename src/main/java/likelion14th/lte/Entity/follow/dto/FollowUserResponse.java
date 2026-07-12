package likelion14th.lte.Entity.follow.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import likelion14th.lte.user.entity.User;

@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class FollowUserResponse {
    private Long userId;
    private String userName;
    private String profileImageUrl;
    private String introduction;
    public static FollowUserResponse from(User user){
        return new FollowUserResponse(
                user.getId(),                                    // 유저 ID
                user.getUsername()+"#"+user.getUserTag(),       // 표시용 이름: "닉네임#태그" 한 문자열로 합칩니다.
                user.getProfileImage(),                          // 프로필 이미지 URL
                user.getIntroduction()                           // 자기소개
        );
    }
}
