package com.example.wantudy.study.repository;

import com.example.wantudy.study.Study;
import com.example.wantudy.study.domain.StudyStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public interface StudyRepositoryCustom {
    Page<Study> findBySearchOption(Pageable pageable, String studyName, String location, StudyStatus status);
}
