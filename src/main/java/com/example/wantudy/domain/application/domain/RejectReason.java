package com.example.wantudy.domain.application.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum RejectReason {
    NOTMATCH("시간대가 맞지 않음"),
    DIFFERENT("추구하는 스터디 목적이 다름"),
    TOOFAR("거주 지역이 너무 멂"),
    EXCESS("선착순 인원 초과");

    private String rejectReason;
}
