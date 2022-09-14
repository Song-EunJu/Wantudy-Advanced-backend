package com.example.wantudy.domain.study.dto;

import com.example.wantudy.domain.study.domain.Study;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@ApiModel
public class StudyCreateDto {

    @ApiModelProperty(value="스터디 파일", required = false)
    private List<MultipartFile> multipartFile = new ArrayList<>();

    @ApiModelProperty(value="스터디 이름", required = true, example = "토플 스터디")
    private String studyName;

    @ApiModelProperty(value="스터디 상세 설명", required = true, example = "토플 자격증을 공부하는 스터디")
    private String description;

    @ApiModelProperty(value="스터디 난이도", required = true, example = "상")
    private String level;

    @ApiModelProperty(value="스터디 형식", required = true, example = "다함께 공부")
    private String format;

    @ApiModelProperty(value="스터디 온오프 여부", required = true, example = "오프라인")
    private String onOff;

    @ApiModelProperty(value="스터디 진행 위치", required = false, example = "서울 특별시 마포구")
    private String location;

    @ApiModelProperty(value="스터디 기간", required = true, example = "6개월")
    private String period;

    @ApiModelProperty(value="스터디 모집 인원", required = true, example = "5")
    private Integer peopleNum;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value="스터디 모집 기간", required = true, example = "2022-05-30")
    private LocalDate deadline;

    @ApiModelProperty(value="스터디 1차 카테고리", required = true)
    private String parentCategory;

    @ApiModelProperty(value="스터디 2차 카테고리", required = true, dataType = "List")
    private List<String> categories = new ArrayList<>();

    @ApiModelProperty(value="희망 시간대", required = true, dataType = "List")
    private List<String> desiredTime = new ArrayList<>();

    @ApiModelProperty(value="신청서 필수 항목", required = true,  dataType = "List")
    private List<String> requiredInfo = new ArrayList<>();

    //DTO를 Entity로 변환해주기 위한 메소드
    public Study toEntity(){
        return Study.builder()
                .studyName(studyName)
                .description(description)
                .level(level)
                .format(format)
                .onOff(onOff)
                .location(location)
                .period(period)
                .peopleNum(peopleNum)
                .deadline(deadline)
                .build();
    }
}
