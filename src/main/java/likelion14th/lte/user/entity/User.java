package likelion14th.lte.user.entity;


import likelion14th.lte.login.entity.RefreshTokenEntity;
import jakarta.persistence.*;
import likelion14th.lte.Entity.BaseEntity;
import likelion14th.lte.Entity.follow.entity.Follow;
import likelion14th.lte.Entity.statics.entity.Statistic;
import likelion14th.lte.youtube.domain.SavedSong;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Getter
@Table(name = "users")
@NoArgsConstructor (access = AccessLevel.PROTECTED)
public class User extends BaseEntity {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String username;

    @Column(length = 16, nullable = false, unique = true)
    private String userTag;

    @Column(columnDefinition = "TEXT")
    private String introduction;

    @Column(columnDefinition = "TEXT")
    private String profileImage;

    @Column(columnDefinition = "TEXT")
    private String s3ImageKey;

    @Column(unique = true)
    private String providerId;

    @OneToMany(mappedBy = "toUser",fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Follow> followers;

    @OneToMany(mappedBy = "fromUser", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Follow> followings;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "statistic_id")
    private Statistic statistic;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SavedSong> savedSongs;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private RefreshTokenEntity refreshToken;


    @Builder(access = AccessLevel.PUBLIC)
    private User (String providerId, String username, String userTag, String introduction){
        this.providerId = providerId;
        this.username = username;
        this.userTag = userTag;
        this.introduction = introduction;
        this.statistic = Statistic.create();
    }

    public void updateIntroduction(String introduction) {
        this.introduction = introduction;
    }

}