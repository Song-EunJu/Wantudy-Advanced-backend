package com.example.wantudy.global.security.domain;

import com.example.wantudy.global.security.model.ProviderType;
import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "users")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(name = "email", length = 50, unique = true)
    private String email;

    @Column(name = "password", length = 100)
    private String password;

    @Column
    private String profileImage;

    @Column
    private String nickname;

    @Column
    private Boolean applicationState;

    @Column
    private Boolean profileState;

    @Column(length=50)
    private String selfIntroduction;

    @Column
    @Enumerated(EnumType.STRING)
    private ProviderType providerType;

    @PrePersist
    public void prePersist(){
        this.applicationState = this.applicationState == null ? Boolean.FALSE : Boolean.TRUE;
        this.profileState = this.profileState == null ? Boolean.FALSE : Boolean.TRUE;
    }

    @Builder
    public User(String email, String password, String profileImage, String nickname) {
        this.email = email;
        this.password = password;
        this.profileImage = profileImage;
        this.nickname = nickname;
    }
}