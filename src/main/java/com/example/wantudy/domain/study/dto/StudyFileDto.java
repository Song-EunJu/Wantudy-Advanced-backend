package com.example.wantudy.domain.study.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@ApiModel
public class StudyFileDto {

    @ApiModelProperty(value = "파일 id")
    private Long studyFileId;

    @ApiModelProperty(value = "등록된 스터디 id")
    private Long studyId;

    @ApiModelProperty(value = "파일 이름")
    private String fileName;

    @ApiModelProperty(value = "파일 저장 경로")
    private String filePath;

    @Getter
    @AllArgsConstructor
    public static class downloadFileResponse{
        private byte[] bytes;
        private String fileName;
    }

}
