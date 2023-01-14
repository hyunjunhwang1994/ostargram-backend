package shop.iamhyunjun.ostargram.domain.likes.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import shop.iamhyunjun.ostargram.domain.likes.dto.LikeMessage;
import shop.iamhyunjun.ostargram.domain.likes.dto.LikeResponseMessage;
import shop.iamhyunjun.ostargram.domain.likes.entity.CommentLikes;
import shop.iamhyunjun.ostargram.domain.likes.service.CommentLikesService;
import shop.iamhyunjun.ostargram.security.customfilter.UserDetailsImpl;

import static org.springframework.http.HttpStatus.OK;
import static shop.iamhyunjun.ostargram.domain.likes.dto.LikeMessage.LIKE_CANCEL_MESSAGE;
import static shop.iamhyunjun.ostargram.domain.likes.dto.LikeMessage.LIKE_MESSAGE;

@Slf4j
@RestController
@RequiredArgsConstructor
public class CommentLikesController {

    private final CommentLikesService commentLikesService;

    @PostMapping("/comments/{commentId}/likes")
    public ResponseEntity<LikeResponseMessage> likesComment(@PathVariable Long commentId,
                                                            @AuthenticationPrincipal UserDetailsImpl userDetails) {

        CommentLikes commentLikes = commentLikesService.execute(commentId, userDetails.getUser());

        if (commentLikes.isLiked()) {
            return sendLikeResponseMessage(LIKE_MESSAGE);
        }
        return sendLikeResponseMessage(LIKE_CANCEL_MESSAGE);
    }

    private ResponseEntity<LikeResponseMessage> sendLikeResponseMessage(LikeMessage likeMessage) {
        LikeResponseMessage responseMessage = new LikeResponseMessage(OK.value(), likeMessage.getMessage());
        return new ResponseEntity<>(responseMessage, HttpStatus.valueOf(responseMessage.getStatus()));
    }
}
