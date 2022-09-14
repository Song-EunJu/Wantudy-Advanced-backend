package com.example.wantudy.domain.study.repository;

import com.example.wantudy.domain.study.dto.CommentResponseDto;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepositoryCustom {
    List<CommentResponseDto> findCommentByStudyId(long studyId);
}
