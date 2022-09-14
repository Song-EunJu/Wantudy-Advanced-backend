package com.example.wantudy.domain.study.domain;

import com.example.wantudy.global.security.domain.User;
import com.example.wantudy.domain.study.dto.StudyUpdateDto;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@JsonIgnoreProperties(value={"categories", "desiredTime","requiredInfo","studyFiles"})
public class Study {

    @Id
    @Column(name = "study_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long studyId;

    //연관관계 주인 ( 항상 N 이 연관관계 주인 )
    //이 값을 변경하면 user_id foreign key값이 변경됨
    //study입장에서 User랑 N:1 관계
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id") //매핑 키 설정
    private User user;

    @OneToMany(mappedBy = "study", cascade = CascadeType.ALL)
    @Column(name = "categories")
    private List<StudyCategory> categories = new ArrayList<>();

    @OneToMany(mappedBy = "study", cascade = CascadeType.ALL)
    @Column(name = "desired_time")
    private List<StudyDesiredTime> desiredTime = new ArrayList<>();

    @OneToMany(mappedBy = "study", cascade = CascadeType.ALL)
    @Column(name = "required_info")
    private List<StudyRequiredInfo> requiredInfo = new ArrayList<>();

    @OneToMany(mappedBy = "study", cascade = CascadeType.ALL, orphanRemoval = true)
    @Column(name = "study_file")
    private List<StudyFile> studyFiles = new ArrayList<>();

    @OneToMany(mappedBy = "study", cascade = CascadeType.ALL, orphanRemoval = true)
    @Column(name = "comments")
    private List<Comment> comments = new ArrayList<>();

    @Column(name = "fixed_study_schedule")
    private String fixedStudySchedule;

    @Column(name = "study_name", length = 50)
    private String studyName;

    @Column(name = "description")
    private String description;

    @Column(name = "level")
    private String level;

    @Column(name = "format")
    private String format;

    @Column(name = "on_off")
    private String onOff;

    @Column(name = "location")
    private String location;

    @Column(name = "people_num")
    private Integer peopleNum;

    @Column(name = "current_num")
    private Integer currentNum;

    @Column(name="remain_num")
    private Integer remainNum;

    @Column(name = "deadline")
    private LocalDate deadline;

    @Column(name = "period")
    private String period;

    @Enumerated(EnumType.STRING)
    @Column(length = 7, columnDefinition = "varchar(7) default 'UNKNOWN'")
    private StudyStatus studyStatus = StudyStatus.RECRUIT;

    @Column(name="like_num")
    private Integer likeNum;

    @Column(name = "create_at")
    private LocalDateTime createAt;

    @PrePersist
    public void prePersist() {
        this.fixedStudySchedule = this.format == null ? null : this.fixedStudySchedule;
        this.likeNum = this.likeNum == null ? 0 : this.likeNum;
        this.currentNum= this.currentNum == null ? 1 : this.currentNum;
        this.createAt = LocalDateTime.now();
    }

    @Builder
    public Study(String studyName, String description, String level, String format, String onOff, String location, String period, Integer peopleNum, LocalDate deadline) {
        this.studyName = studyName;
        this.description = description;
        this.level = level;
        this.format = format;
        this.onOff = onOff;
        this.location = location;
        this.period = period;
        this.peopleNum = peopleNum;
        this.deadline = deadline;
    }


    // -- 파일 연관 관계 메서드 -- //
    public void addStudyFiles(StudyFile studyFile) {
        this.studyFiles.add(studyFile);

//        //Study에 파일이 저장 되어 있지 않은 경우
//        if(studyFile.getStudy() != this)
//            // 파일 저장
//            studyFile.setStudy(this);
    }

    public void removeStudyFiles(StudyFile studyFile){
        this.studyFiles.remove(studyFile);
    }

    public void addComments(Comment comment){
        this.comments.add(comment);
    }

    // -- 수정 메서드 -- //
    public void updateStudy(StudyUpdateDto studyUpdateDto){
        this.studyName = studyUpdateDto.getStudyName();
        this.description = studyUpdateDto.getDescription();
        this.level = studyUpdateDto.getLevel();
        this.format = studyUpdateDto.getFormat();
        this.location = studyUpdateDto.getLocation();
        this.period = studyUpdateDto.getPeriod();
        this.peopleNum = studyUpdateDto.getPeopleNum();
        this.deadline = studyUpdateDto.getDeadline();
    }
}
