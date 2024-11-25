package board_practice2.controller;

import board_practice2.dto.*;
import board_practice2.service.CategoryService;
import board_practice2.service.CommentService;
import board_practice2.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

@Controller
@RequestMapping("/")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;
    private final CategoryService categoryService;
    private final CommentService commentService;

    @GetMapping("/create")
    public String showPostForm(Model model){
        List<CategoryListDTO> categories = categoryService.getAllCategories();
        model.addAttribute("categories",categories);
        return "create-post";
    }

    @PostMapping("/create")
    public String createNewPost(PostCreateRequestDTO requestDTO){
        try{
            postService.createPost(requestDTO);
        }catch(IOException e){
            e.printStackTrace();
            return "error";
        }

        return "redirect:/posts";
    }

    @GetMapping
    public String showPostList(PostSearchRequestDTO requestDTO,
                               @RequestParam(defaultValue = "0") int page,
                               @RequestParam(defaultValue = "10") int size,
                               Model model){
        Pageable pageable = PageRequest.of(page, size);
        Page<PostListDTO> posts;

        if (requestDTO.hasSearchCon()) {
            posts = postService.searchPosts(requestDTO, pageable);
        } else {
            posts = postService.getAllPosts(pageable);
        }

        model.addAttribute("posts", posts);
        List<CategoryListDTO> categories = categoryService.getAllCategories();
        model.addAttribute("categories",categories);
        return "post-list";

    }

    @GetMapping("/posts/{id}")
    public String showPostDetail(@PathVariable("id") Long postId, Model model){

        PostDetailDTO dto = postService.getPostDetail(postId);
        List<CommentResponseDTO> comments = commentService.getCommentsByPostId(postId);
        model.addAttribute("postDetail", dto);
        model.addAttribute("comments", comments);
        return "post-detail";
    }

    @PostMapping("/posts/{id}/delete")
    public String deletePost(@PathVariable Long id, @RequestParam String password) {
        postService.deletePost(id, password);
        return "redirect:/";
    }





}
