package com.example.wantudy.domain.application.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Attendance implements EnumMapperType {
    ATTENDING("재학"),
    TIMEOFF("휴학"),
    GRADUATION("졸업");

    @Getter
    private final String title; // 설명 조회 - "남자"

    @Override
    public String getCode() { // 이름을 조회 - MALE
        return name();
    }
}
