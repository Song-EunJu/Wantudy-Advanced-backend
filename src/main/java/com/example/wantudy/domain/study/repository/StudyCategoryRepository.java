package com.example.wantudy.domain.study.repository;

import com.example.wantudy.domain.study.domain.Study;
import com.example.wantudy.domain.study.domain.Category;
import com.example.wantudy.domain.study.domain.StudyCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudyCategoryRepository extends JpaRepository<StudyCategory, Long> {
    List<StudyCategory> findByStudy(Study study);
    StudyCategory findByCategoryAndStudy(Category category, Study study);
    List<StudyCategory>  findByCategory(Category category);
}
