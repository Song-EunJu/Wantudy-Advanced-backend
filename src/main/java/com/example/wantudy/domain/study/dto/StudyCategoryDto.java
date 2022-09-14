package com.example.wantudy.domain.study.dto;

import com.example.wantudy.domain.study.domain.Category;
import com.example.wantudy.domain.study.domain.Study;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Builder
@AllArgsConstructor
public class StudyCategoryDto {

    private Study study;
    private Category category;

}
