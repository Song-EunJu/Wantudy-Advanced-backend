package com.example.wantudy.domain.application.domain;

import com.example.wantudy.domain.application.dto.ApplicationCreateDto;
import com.example.wantudy.global.security.domain.User;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.sun.istack.NotNull;
import lombok.*;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.*;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Table(name = "application")
//@JsonIgnoreProperties(value = {"interests", "keywords"})
public class Application {
    @Id
    @Column(name = "applicationId")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long applicationId;

    @ManyToOne(fetch = FetchType.EAGER, targetEntity = User.class)
    @JoinColumn(name="userId")
    @JsonBackReference // 연관관계의 주인
    @NotNull
    private User user;

    @Column
    @LastModifiedDate
    private LocalDateTime updatedAt;

    @Column
    private String applicationName;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Column
    private Integer age;

    @Column
    private String organization;

    @Column
    private String major;

    @Enumerated(EnumType.STRING)
    private Attendance attendance;

    @Column
    private String semester;

    @Column
    private String address;

    @OneToMany(
            fetch = FetchType.EAGER,
            mappedBy = "application",
            targetEntity = ApplicationInterests.class,
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @JsonManagedReference
    @Builder.Default
    private Set<ApplicationInterests> interests = new HashSet<>();

    @OneToMany(
            fetch = FetchType.EAGER,
            mappedBy = "application",
            targetEntity = ApplicationKeyword.class,
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @JsonManagedReference
    @Builder.Default
    private Set<ApplicationKeyword> keywords = new HashSet<>();

    public void updateApplication(ApplicationCreateDto dto){
        this.applicationName = dto.getApplicationName() == null ? this.applicationName : dto.getApplicationName();
        this.organization = dto.getOrganization() == null ? this.organization : dto.getOrganization();
        this.major = dto.getMajor() == null ? this.major : dto.getMajor();
        this.semester = dto.getSemester() == null ? this.semester : dto.getSemester();
        this.address = dto.getAddress() == null ? this.address : dto.getAddress();
    }

    // Gender Enum MALE, FEMALE 에서 title 값이 들어온 string 과 동일한 애 찾기
    public void toGenderEnum(String genderStr) {
        this.gender = Arrays.stream(Gender.values())
                .filter(o1 -> o1.getTitle().equals(genderStr))
                .findFirst()
                .get();
    }

    public void toAttendanceEnum(String attendanceStr){
        this.attendance = Arrays.stream(Attendance.values())
                .filter(o1 -> o1.getTitle().equals(attendanceStr))
                .findFirst()
                .get();
    }

//    public static ApplicationCreateDto from(Application application){
//        return Application.ApplicationCreateDto()
//                .nickname(review.getUser().getUserNickname())
//                .reviewId(review.getReviewId())
//                .rate(review.getRate())
//                .commentTitle(review.getCommentTitle())
//                .comment(review.getComment())
//                .createdDate(review.getCreatedDate())
//                .writerStatus(false)
//                .build();
//    }
}
