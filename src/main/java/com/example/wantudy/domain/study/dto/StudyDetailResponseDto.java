package com.example.wantudy.domain.study.dto;

import com.example.wantudy.domain.study.domain.Study;
import com.example.wantudy.domain.study.domain.StudyStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
public class StudyDetailResponseDto {

    @ApiModelProperty(value= "스터디 id")
    private Long studyId;

    @ApiModelProperty(value = "스터디 이름", example = "토플 스터디")
    private String studyName;

    @ApiModelProperty(value = "스터디 상세 설명", example = "토플 문제를 같이 풉니다.")
    private String description;

    @ApiModelProperty(value = "스터디 난이도", example = "상")
    private String level;

    @ApiModelProperty(value = "스터디 형식", example = "다함께 공부")
    private String format;

    @ApiModelProperty(value="스터디 온오프 여부", example = "오프라인")
    private String onOff;

    @ApiModelProperty(value = "스터디 진행 위치", example = "서울 특별시 마포구")
    private String location;

    @ApiModelProperty(value = "스터디 기간",  example = "6개월")
    private String period;

    @ApiModelProperty(value = "스터디 고정 일정", example = "수 11")
    private String fixedStudySchedule;

    @ApiModelProperty(value = "스터디 모집 인원", example = "5")
    private Integer peopleNum;

    @ApiModelProperty(value = "스터디 현재 인원", example = "3")
    private Integer currentNum;

    @ApiModelProperty(value = "스터디 모집 남은 인원", example = "2")
    private Integer remainNum;

    @ApiModelProperty(value = "스터디 찜 개수", example = "2")
    private Integer likeNum;

    @ApiModelProperty(value = "스터디 모집 기간", example = "2022-05-30")
    @JsonFormat(pattern="yyyy-MM-dd")
    private LocalDate deadline;

    @ApiModelProperty(value = "스터디 개설 글 등록 시간", example = "2022-05-22 07:30:55")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createAt;

    @ApiModelProperty(value = "스터디 진행 상태", example = "RECRUIT")
    private StudyStatus studyStatus;

    @ApiModelProperty(value = "스터디 카테고리", example = "[자격증, 토플]")
    private List<String> categories;

    @ApiModelProperty(value = "스터디 희망 일정", example = "[월 13, 수 11, 목 15, 목 16]")
    private List<String> desiredTime;

    @ApiModelProperty(value = "스터디 신청 시 필수 정보", example = "성별, 나이")
    private List<String> requiredInfo;

    @ApiModelProperty(value = "스터디 계획서 파일")
    private List<StudyFileDto> studyFiles;

    @ApiModelProperty(value = "댓글")
    private List<CommentResponseDto> comments;


    //리스트는 따로 받아오기
    public static StudyDetailResponseDto from(Study study){
        return StudyDetailResponseDto.builder()
                .studyId(study.getStudyId())
                .studyName(study.getStudyName())
                .description(study.getDescription())
                .level(study.getLevel())
                .format(study.getFormat())
                .onOff(study.getOnOff())
                .location(study.getLocation())
                .period(study.getPeriod())
                .fixedStudySchedule(study.getFixedStudySchedule())
                .peopleNum(study.getPeopleNum())
                .currentNum(study.getCurrentNum())
                .remainNum(study.getRemainNum())
                .likeNum(study.getLikeNum())
                .deadline(study.getDeadline())
                .createAt(study.getCreateAt())
                .studyStatus(study.getStudyStatus())
                .build();
    }

}
