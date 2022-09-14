package com.example.wantudy.domain.study.repository;

import com.example.wantudy.domain.study.domain.Study;
import com.example.wantudy.domain.study.domain.StudyRequiredInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudyRequiredInfoRepository extends JpaRepository<StudyRequiredInfo, Long> {
    List<StudyRequiredInfo> findByStudy(Study study);
}
