package com.example.wantudy.domain.study.domain;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StudyRequiredInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long studyRequiredInfoId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonManagedReference
    @JoinColumn(name="study_id")
    private Study study;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonManagedReference
    @JoinColumn(name="required_info_id")
    private RequiredInfo requiredInfo;

}
