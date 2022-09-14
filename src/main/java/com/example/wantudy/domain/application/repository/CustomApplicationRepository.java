package com.example.wantudy.domain.application.repository;

import com.example.wantudy.domain.application.domain.Application;
import com.example.wantudy.domain.application.domain.ApplicationInterests;
import com.example.wantudy.domain.application.dto.ApplicationCreateDto;

import java.util.List;

public interface CustomApplicationRepository {
    List<ApplicationInterests> getApplication(Application application);
}
