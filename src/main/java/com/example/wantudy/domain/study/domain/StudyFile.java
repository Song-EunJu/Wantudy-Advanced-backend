package com.example.wantudy.domain.study.domain;

import lombok.*;

import javax.persistence.*;

//@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Setter
@NoArgsConstructor
public class StudyFile {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="study_file_id")
    private Long studyFileId;

    @Column(name="file_name")
    private String fileName;

    @Column(name="file_path", length = 1000)
    private String filePath;

    @Column(name="s3_file_name")
    private String s3FileName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "study_Id")
    private Study study;

    @Builder
    public StudyFile(String fileName, String filePath) {
        this.fileName = fileName;
        this.filePath = filePath;
    }


    // study 정보 저장
    public void setStudy(Study study){
        this.study = study;
        // study게시글에 현재 파일이 존재하지 않는다면
        if(!study.getStudyFiles().contains(this))
            // 파일 추가
            study.getStudyFiles().add(this);
    }
}
