package likelion14th.lte.user.dto.response;

import likelion14th.lte.user.entity.User;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)

public class UserProfileResponse {
    private String username;
    private String profileImageUrl;
    private String introduction;

    // [Q4. Controller가 DB에서 꺼낸 원본 Entity(User)를 클라이언트 화면에 그대로 반환하지 않고,
    // 굳이 from() 메서드를 통해 DTO로 한번 변환해서 내보내는 핵심적인 이유 2가지는 무엇인가요?]
    // 답변: 1. Entity에는 민감한 정보가 있어서 필요한 것들만 DTO에서 전달할 수 있음
    // 2. DTO를 중간에 두면 DB 구조가 바뀌어도 한 곳만 수정하면 되므로, 안정적으로 유지할 수 있다
    public static UserProfileResponse from (User user) {
        return new UserProfileResponse(
                 user.getUsername() + '#' + user.getUserTag(),
                user.getProfileImage(),
                user.getIntroduction()
        );
    }
}
