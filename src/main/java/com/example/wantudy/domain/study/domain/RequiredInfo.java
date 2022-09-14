package com.example.wantudy.domain.study.domain;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RequiredInfo {

    @Id
    @GeneratedValue
    @Column(name = "required_info_id")
    private Long requiredInfoId;

    @Column(name = "required_info_name")
    private String requiredInfoName;

    @OneToMany(mappedBy = "requiredInfo")
    private List<StudyRequiredInfo> studies = new ArrayList<>();

    @Builder
    public RequiredInfo(String requiredInfoName){
        this.requiredInfoName = requiredInfoName;
    }

}
