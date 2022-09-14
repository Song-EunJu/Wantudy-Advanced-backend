package com.example.wantudy.domain.application.repository;

import com.example.wantudy.domain.application.domain.Application;
import com.example.wantudy.global.security.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ApplicationRepository extends JpaRepository<Application, Long>, CustomApplicationRepository {
    List<Application> findByUser(User user);
}
