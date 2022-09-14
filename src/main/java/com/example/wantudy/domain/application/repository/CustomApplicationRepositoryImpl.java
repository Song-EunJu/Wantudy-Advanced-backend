package com.example.wantudy.domain.application.repository;

import com.example.wantudy.domain.application.domain.Application;
import com.example.wantudy.domain.application.domain.ApplicationInterests;
import com.example.wantudy.domain.application.dto.ApplicationCreateDto;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.example.wantudy.domain.application.domain.QApplication.application;
import static com.example.wantudy.domain.application.domain.QApplicationInterests.applicationInterests;
import static com.example.wantudy.domain.application.domain.QApplicationKeyword.applicationKeyword;
import static com.example.wantudy.domain.application.domain.QKeyword.keyword1;
import static com.example.wantudy.domain.study.domain.QCategory.category;

@RequiredArgsConstructor
@Repository
public class CustomApplicationRepositoryImpl implements CustomApplicationRepository {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<ApplicationInterests> getApplication(Application application2) {
        return jpaQueryFactory
                .selectFrom(applicationInterests)
                .join(applicationInterests.category, category)
                .on(applicationInterests.category.categoryId.eq(category.categoryId))
                .where(applicationInterests.application.eq(application2))
                .fetch();
    }

//    @Override
//    public List<Review> findByLecture(Lecture lecture) {
//        return jpaQueryFactory
//                .selectFrom(review)
//                .where(review.reviewStatus.eq(1))
//                .where(review.lecture.eq(lecture))
//                .fetch();
//    }
//
//    List<UserContactDto> userContacts = query.from(user)
//            .select(Projections.constructor(UserContactDto.class,
//                    user.name,
//                    user.mobile,
//                    user.address))
//            .fetch();
}
