package com.example.wantudy.domain.application.service;

import com.example.wantudy.domain.application.domain.*;
import com.example.wantudy.domain.application.dto.ApplicationCreateDto;
import com.example.wantudy.domain.application.repository.ApplicationInterestsRepository;
import com.example.wantudy.domain.application.repository.ApplicationKeywordRepository;
import com.example.wantudy.domain.application.repository.ApplicationRepository;
import com.example.wantudy.domain.application.repository.KeywordRepository;
import com.example.wantudy.global.security.domain.User;
import com.example.wantudy.domain.study.domain.Category;
import com.example.wantudy.domain.study.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ApplicationService {
    private final ApplicationRepository applicationRepository;
    private final CategoryRepository categoryRepository;
    private final KeywordRepository keywordRepository;
    private final ApplicationInterestsRepository applicationInterestsRepository;
    private final ApplicationKeywordRepository applicationKeywordsRepository;

    // 모든 지원서 조회
    public List<Application> getAllApplications(User user){
        return applicationRepository.findByUser(user);
    }

    // 특정 지원서 조회
//    public ApplicationCreateDto findByApplicationId(Long applicationId){
//        Application application = applicationRepository.findById(applicationId).orElse(null);
//        ApplicationCreateDto applicationCreateDto = ApplicationCreateDto.toDto(application);
//        return applicationCreateDto;
//    }
    public Application findByApplicationId(Long applicationId){
//        return applicationRepository.findById(applicationId).orElse(null);
        Application application2 = applicationRepository.findById(applicationId).get();
        List<ApplicationInterests> applicationInterests = applicationRepository.getApplication(application2);
//        for(int i=0;i<applicationInterests.size();i++){
//            System.out.println("applicationInterests = " + applicationInterests.get(i).getApplication().getInterests();
//        }
        return application2;
    }

    // 지원서 작성
    public Application saveApplication(User user, ApplicationCreateDto applicationCreateDto){
        /**
         * String으로 받아온 값 Enum 으로 변경 후 entity 생성
         */
        Application application = applicationCreateDto.toEntity(user);

        String genderStr = applicationCreateDto.getGender(); // 여자라고 들어오면 value 값을 갖고 code 찾기
        String attendanceStr = applicationCreateDto.getAttendance();

        application.toGenderEnum(genderStr);
        application.toAttendanceEnum(attendanceStr);

        List<String> interests = applicationCreateDto.getInterests();
        List<String> keywords = applicationCreateDto.getKeywords();
        applicationRepository.save(application);
        saveInterests(interests, application);
        saveKeywords(keywords, application);
        return application;
    }

    // 지원서 연관 관심분야 조회
    public void getInterests(Application application){
//        List<String> interests =
    }

    // 지원서 연관 관심분야 조회
    public void getKeywords(Application application){

    }


    // 지원서 작성 시 관심분야 (카테고리) 저장
    public void saveInterests(List<String> interests, Application application){
        for(int i=0;i<interests.size();i++){
            Category category = categoryRepository.findByCategoryName(interests.get(i));
            ApplicationInterests applicationInterests;
            if(category == null){ // 키워드가 없는 경우 생성
                Category newCategory = new Category(interests.get(i));
                categoryRepository.save(newCategory);
                applicationInterests = ApplicationInterests.toEntity(application, newCategory);
            }
            else
                applicationInterests = ApplicationInterests.toEntity(application, category);
            applicationInterestsRepository.save(applicationInterests);
        }
    }

    // 지원서 작성 시 키워드 저장
    public void saveKeywords(List<String> keywords, Application application){
        for(int i=0;i<keywords.size();i++){
            Keyword keyword = keywordRepository.findByKeyword(keywords.get(i)).orElse(null);
            ApplicationKeyword applicationKeyword;
            if(keyword == null){ // 키워드가 없는 경우 생성
                Keyword newKeyword = new Keyword(keywords.get(i));
                keywordRepository.save(newKeyword);
                applicationKeyword = ApplicationKeyword.toEntity(application, newKeyword);
            }
            else
                applicationKeyword = ApplicationKeyword.toEntity(application, keyword);
            applicationKeywordsRepository.save(applicationKeyword);
        }
    }

    // 지원서 수정
    public void updateApplication(Application application, ApplicationCreateDto applicationUpdateDto){
        application.updateApplication(applicationUpdateDto);
        String genderStr = applicationUpdateDto.getGender();
        String attendanceStr = applicationUpdateDto.getAttendance();

        if(genderStr != null)
            application.toGenderEnum(genderStr);

        if(attendanceStr != null)
            application.toAttendanceEnum(attendanceStr);

        applicationRepository.save(application);
    }

    // 지원서 삭제
    public void deleteApplication(Application application){
        applicationRepository.delete(application);
    }
}
