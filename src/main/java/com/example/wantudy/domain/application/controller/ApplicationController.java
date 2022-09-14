package com.example.wantudy.domain.application.controller;

import com.example.wantudy.domain.application.service.ApplicationService;
import com.example.wantudy.domain.application.domain.Application;
import com.example.wantudy.domain.application.dto.ApplicationCreateDto;
import com.example.wantudy.global.common.ResponseMessage;
import com.example.wantudy.global.security.domain.User;
import com.example.wantudy.global.security.service.UserDetailsServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Api(tags={"스터디 지원서 API"})
@RestController
@RequestMapping("/application")
@RequiredArgsConstructor
public class ApplicationController {
    private final UserDetailsServiceImpl userService;
    private final ApplicationService applicationService;

    // 자신이 작성한 모든 지원서 조회
    @GetMapping("")
    public ResponseEntity<ResponseMessage> getAllApplications() {
        //        String email = principal.getName();
//        User user = userService.findByEmail(email);
//        if(user == null)
//            return new ResponseEntity<>(new ResponseMessage(404, "존재하지 않는 유저"), HttpStatus.NOT_FOUND);
        User user = userService.findByEmail("test@naver.com");

        List<Application> applicationList = applicationService.getAllApplications(user);
        return new ResponseEntity<>(ResponseMessage.withData(200, "모든 지원서 조회 성공", applicationList), HttpStatus.OK);
    }

    // 특정 지원서 조회
    @GetMapping("/{applicationId}") // 지원서 수정
    public ResponseEntity<ResponseMessage> getApplication(@PathVariable("applicationId") Long applicationId) {
        //        String email = principal.getName();
//        User user = userService.findByEmail(email);
//        if(user == null)
//            return new ResponseEntity<>(new ResponseMessage(404, "존재하지 않는 유저"), HttpStatus.NOT_FOUND);
        User user = userService.findByEmail("test@naver.com");

//        ApplicationCreateDto application = applicationService.findByApplicationId(applicationId);
        Application application = applicationService.findByApplicationId(applicationId);

        if(application == null)
            return new ResponseEntity<>(new ResponseMessage(404, "존재하지 않는 지원서"), HttpStatus.NOT_FOUND);

        return new ResponseEntity<>(ResponseMessage.withData(200, "특정 지원서 조회 성공", application), HttpStatus.OK);

    }


    @ApiOperation(value = "지원서 작성")
    @ApiResponses({
            @ApiResponse(code = 201, message = "지원서 작성 완료"),
            @ApiResponse(code = 500, message = "서버 에러")
    })
    @PostMapping("") // 지원서 작성
    public ResponseEntity<ResponseMessage> createApplication(@RequestBody ApplicationCreateDto applicationCreateDto, Principal principal) {
//        String email = principal.getName();
//        User user = userService.findByEmail(email);
//        if(user == null)
//            return new ResponseEntity<>(new ResponseMessage(404, "존재하지 않는 유저"), HttpStatus.NOT_FOUND);
        User user = userService.findByEmail("test@naver.com");
        Application application = applicationService.saveApplication(user, applicationCreateDto);

        return new ResponseEntity<>(new ResponseMessage(201, "지원서 작성 완료", application), HttpStatus.CREATED);
    }

//    @PatchMapping("/{applicationId}") // 지원서 수정
//    public ResponseEntity<ResponseMessage> updateApplication(@PathVariable("applicationId") Long applicationId, @RequestBody ApplicationCreateDto applicationUpdateDto) {
//        //        String email = principal.getName();
////        User user = userService.findByEmail(email);
////        if(user == null)
////            return new ResponseEntity<>(new ResponseMessage(404, "존재하지 않는 유저"), HttpStatus.NOT_FOUND);
//        User user = userService.findByEmail("test@naver.com");
//
//        Application application = applicationService.findByApplicationId(applicationId);
//        if(application == null)
//            return new ResponseEntity<>(new ResponseMessage(404, "존재하지 않는 지원서"), HttpStatus.NOT_FOUND);
//        applicationService.updateApplication(application, applicationUpdateDto);
//
//        return new ResponseEntity<>(new ResponseMessage(200, "강의 리뷰 수정 성공"), HttpStatus.OK);
//
//    }

//    @DeleteMapping("/{applicationId}") // 지원서 삭제
//    public ResponseEntity<ResponseMessage> deleteApplication(@PathVariable("applicationId") Long applicationId, Principal principal) {
////        String email = principal.getName();
////        User user = userDetailsService.findUserByEmail(email);
//
//        Application application = applicationService.findByApplicationId(applicationId);
//        if(application == null)
//            return new ResponseEntity<>(new ResponseMessage(404, "존재하지 않는 지원서"), HttpStatus.NOT_FOUND);
//        applicationService.deleteApplication(application);
//        return new ResponseEntity<>(new ResponseMessage(200, "지원서 삭제 완료"), HttpStatus.OK);
//    }
}
