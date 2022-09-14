package com.example.wantudy.domain.study.repository;

import com.example.wantudy.domain.study.domain.Study;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;


@Repository
public interface StudyRepository extends JpaRepository<Study, Long>, JpaSpecificationExecutor<Study>, StudyRepositoryCustom{
    Page<Study> findAll(Specification<Study> spec, Pageable pageable);
//    List<Study> findByStudyName(String studyName);
//    Page<Study> findByStudyNameContaining(String studyName, Pageable pageable);
//    Page<Study> findByCategoriesContaining(List<String> categories, Pageable pageable);
}
