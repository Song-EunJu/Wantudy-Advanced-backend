package com.example.wantudy.domain.study.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class StudyFileUploadDto {

    private String filepath;
    private String s3FileName;

}
