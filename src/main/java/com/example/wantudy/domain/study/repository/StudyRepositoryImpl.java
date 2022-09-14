package com.example.wantudy.domain.study.repository;

import com.example.wantudy.domain.study.domain.QStudy;
import com.example.wantudy.domain.study.domain.Study;
import com.example.wantudy.domain.study.domain.StudyStatus;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@AllArgsConstructor
public class StudyRepositoryImpl implements StudyRepositoryCustom{

    private JPAQueryFactory queryFactory;

    @Override
    public Page<Study> findBySearchOption(Pageable pageable, String studyName, String location, StudyStatus status) {
        List<Study> studies =  queryFactory.selectFrom(QStudy.study)
                .where(eqLocation(location), eqStatus(status), containStudyName(studyName)).fetch();

        return new PageImpl<Study>(studies, pageable, studies.size());
    }

    private BooleanExpression eqLocation(String location) {
        if(location== null || location.isEmpty()) {
            return null;
        }
        return QStudy.study.location.eq(location);
    }

    private BooleanExpression containStudyName(String name) {
        if(name == null || name.isEmpty()) {
            return null;
        }
        return QStudy.study.studyName.containsIgnoreCase(name);
    }

    private BooleanExpression eqStatus(StudyStatus status) {
        if(status == null) {
            return QStudy.study.studyStatus.eq(StudyStatus.RECRUIT);
        }
        return QStudy.study.studyStatus.eq(status);
    }
}
