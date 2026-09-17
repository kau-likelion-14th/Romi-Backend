package likelion14th.lte.user.service;

import likelion14th.lte.global.api.ErrorCode;
import likelion14th.lte.global.exception.GeneralException;
import likelion14th.lte.user.dto.request.CreateTestUserRequest;
import likelion14th.lte.user.dto.response.UserProfileResponse;
import likelion14th.lte.user.entity.User;
import likelion14th.lte.user.repository.UserRepository;
import likelion14th.lte.utils.Image.ImageUtil;
import likelion14th.lte.utils.S3.S3Dto;
import likelion14th.lte.utils.S3.S3Utils;
import likelion14th.lte.utils.exception.UtilException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor (access = AccessLevel.PROTECTED)

public class UserProfileService {
    // [Q5. Service 안에서 new UserRepository() 로 객체를 직접 생성하지 않고,
    // 외부에서 의존성 주입(DI)을 받는 이유는 무엇인가요? (결합도와 단위 테스트 관점)]
    // UserRepository 랑 강한 결합을 맺게 됨.
    // 직접 생성하면 테스트가 불가함.
    private final UserRepository userRepository;
    private final S3Utils s3Utils;
    private final ImageUtil imageUtil;

    // [Q6. (코딩 문제) 만약 클래스 위의 @RequiredArgsConstructor를 지운다면,
    // 우리가 직접 작성해야 할 의존성 주입용 자바 '생성자' 코드는 어떤 모습일까요? 아래에 직접 코딩해 보세요.]
    /*
       @Autowired
       public UserProfileService(UserRepository userRepository) {
            this.userRepository = userRepository;
}
    */

    @Transactional
    public UserProfileResponse createTestUser(CreateTestUserRequest request) {

        // [Q7. 일반적인 생성자 new User(name, intro, tag) 방식을 쓰지 않고,
        // User.builder()...build() 라는 '빌더 패턴'을 사용하여 객체를 조립했을 때 얻는 장점은 무엇인가요?]
        // 답변: 필드명이 명시되어 확인이 편리하고 순서 상관없이 필드를 채울 수 있음.

        User newUser = User.builder()
                .username(request.getUsername())
                .userTag(request.getUserTag())
                .introduction(request.getIntroduction())
                .build();

        User saveUser;
        try {

            // [Q8. 데이터를 저장하는 이 메서드 위에 @Transactional이 반드시 붙어야 하는 이유는 무엇인가요?
            // (저장 도중 DB 서버가 끊겼을 때의 상황을 가정해서 설명하세요)]
            // 답변: 만약 아래 코드 실행 중 에러가 터지면, 지금까지 DB에 넣은 데이터를 모두 롤백되므로 데이터 삽입을 위해 무조건 붙어야 함.

            saveUser = userRepository.save(newUser);
        } catch (Exception e) {
            throw new GeneralException(ErrorCode.BAD_REQUEST);
        }
        return UserProfileResponse.from(saveUser);
    }

    @Transactional(readOnly = true)
    public UserProfileResponse getUserProfile(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new GeneralException(ErrorCode.USER_NOT_FOUND));

        return UserProfileResponse.from(user);
    }

    @Transactional
    public UserProfileResponse putProfileImage(Long userId, MultipartFile file){
        User user = userRepository.findById(userId)
                .orElseThrow(()-> new GeneralException(ErrorCode.USER_NOT_FOUND));

        try{
            imageUtil.validateImage(file);
            ImageUtil.ResizedImage resizedImage =
                    imageUtil.resizeProfileToPngBytes(file, 256);

            String originalFilenames = file.getOriginalFilename();
            String baseName = originalFilenames.contains(".")
                    ?originalFilenames.substring(originalFilenames.lastIndexOf("."))
                    : originalFilenames;

            S3Dto result =
                    s3Utils.uploadBytes(resizedImage.bytes(), baseName+".png", resizedImage.contentType());
            if(user.getS3ImageKey()!=null){
                s3Utils.deleteFile(user.getS3ImageKey());
            }
            user.fixUserProfile(result.getUrl(),result.getKey());
            return UserProfileResponse.from(user);
        }catch (UtilException e){
            throw GeneralException.of(mapToErrorCode(e.getReason()));
        }
    }

    private ErrorCode mapToErrorCode(UtilException.Reason reason) {
        return switch (reason) {
            case FILE_EMPTY -> ErrorCode.IMAGE_FILE_EMPTY;
            case FILE_TOO_LARGE -> ErrorCode.IMAGE_TOO_LARGE;
            case TYPE_NOT_ALLOWED -> ErrorCode.IMAGE_TYPE_NOT_ALLOWED;

            case IMAGE_PROCESS_FAILED -> ErrorCode.IMAGE_PROCESS_FAILED;

            case S3_UPLOAD_FAILED -> ErrorCode.S3_UPLOAD_FAILED;
            case S3_DELETE_FAILED -> ErrorCode.S3_DELETE_FAILED;
        };
    }
}