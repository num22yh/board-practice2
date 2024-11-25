package board_practice2.controller;


import board_practice2.dto.CommentRequestDTO;
import board_practice2.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/comments")
public class CommentController {

    private final CommentService commentService;

    @PostMapping
    public String addComment(@ModelAttribute CommentRequestDTO commentRequestDTO) {
        commentService.addComment(commentRequestDTO);
        return "redirect:/posts/" + commentRequestDTO.getPostId();
        // TODO : 질문 - 댓글 추가된 걸 바로 보여줄 때 '게시글 상세보기로 리다이렉트'로 구현하는 거 맞나..?
    }
}
