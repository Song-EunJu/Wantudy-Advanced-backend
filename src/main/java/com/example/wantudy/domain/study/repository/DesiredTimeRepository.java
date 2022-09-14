package com.example.wantudy.domain.study.repository;

import com.example.wantudy.domain.study.domain.DesiredTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DesiredTimeRepository extends JpaRepository<DesiredTime, Long> {
    DesiredTime findByDesiredTime(String DesiredTime);
}
