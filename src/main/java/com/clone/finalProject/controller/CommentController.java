package com.clone.finalProject.controller;

import com.clone.finalProject.domain.User;
import com.clone.finalProject.dto.commentDto.CommnetResponseDto;
import com.clone.finalProject.dto.commentDto.CreateCommnetResponseDto;
import com.clone.finalProject.security.UserDetailsImpl;
import com.clone.finalProject.service.CommnetService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


@Slf4j
@RestController
@RequiredArgsConstructor
public class CommentController {

    private final CommnetService commnetService;

    //commnet 생성
    @PostMapping("/islogin/comment/create")
    public CreateCommnetResponseDto createComment(@RequestBody CommnetResponseDto commnetResponseDto, @AuthenticationPrincipal UserDetailsImpl userDeta) {
        log.info("댓글 작성 진행");
        log.info(" 댓글 내용: {}", commnetResponseDto.getComment());

        User user = userDeta.getUser();
        CreateCommnetResponseDto createCommnetResponseDto = commnetService.createComment(commnetResponseDto, user);

        return createCommnetResponseDto;
    }

    // commnet 삭제
    @DeleteMapping("/islogin/comment/delete/{commentId}")
    public void deleteComment(@PathVariable Long commentId, @AuthenticationPrincipal UserDetailsImpl userDeta) {
        Long uid = userDeta.getUid();
        commnetService.deleteComment(uid, commentId);

    }


    // commnet 수정
    @PutMapping("/islogin/comment/edit/{commentId}")
    public void editComment(@PathVariable Long commentId, @RequestBody CommnetResponseDto commnetResponseDto, @AuthenticationPrincipal UserDetailsImpl userDeta){
        Long uid = userDeta.getUid();
        commnetService.editComment(commentId, commnetResponseDto, uid);

    }

}
