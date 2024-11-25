package board_practice2.service;


import board_practice2.dto.*;
import board_practice2.entity.Attachment;
import board_practice2.entity.Post;
import board_practice2.repository.AttachmentRepository;
import board_practice2.repository.PostRepository;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final AttachmentRepository attachmentRepository;
    private final AttachmentService attachmentService;


    public PostCreateResponseDTO createPost(PostCreateRequestDTO requestDTO) throws IOException {

        Post post = new Post();
        post.setCategoryId(requestDTO.getCategoryId());
        post.setAuthor(requestDTO.getAuthor());
        post.setPassword(requestDTO.getPassword());
        post.setTitle(requestDTO.getTitle());
        post.setContent(requestDTO.getContent());
        post.setViewCount(0);

        Timestamp currentTime = new Timestamp(System.currentTimeMillis());
        post.setCreatedAt(currentTime);
        post.setUpdatedAt(currentTime);
        System.out.println(requestDTO.getAttachments());

        List<Attachment> attachments = requestDTO.getAttachments().stream()
                .filter(file -> file != null && !file.isEmpty())
                .map(file -> {
                    try {
                        Attachment attachment = attachmentService.saveFile(file);
                        System.out.println(attachment);
                        attachment.setPost(post);
                        return attachment;
                    } catch (IOException e) {
                        System.err.println("파일 저장 실패: " + file.getOriginalFilename());
                        return null;
                    }
                })
                .collect(Collectors.toList());

        post.setAttachments(attachments);

        Post savedPost = postRepository.save(post);

        return new PostCreateResponseDTO(
                savedPost.getTitle(),
                savedPost.getContent(),
                savedPost.getCategoryId(),
                savedPost.getAuthor(),
                savedPost.getCreatedAt(),
                savedPost.getUpdatedAt(),
                savedPost.getViewCount()
        );

    }

    @Transactional(readOnly = true)
    public Page<PostListDTO> getAllPosts(Pageable pageable) {
        return postRepository.findAll(pageable).map(post -> {
            PostListDTO dto = new PostListDTO();
            dto.setId(post.getId());
            dto.setTitle(post.getTitle());
            dto.setContent(post.getContent());
            dto.setCategoryId(post.getCategoryId());
            dto.setCategoryName(post.getCategory().getCategoryName());
            dto.setAuthor(post.getAuthor());
            dto.setCreatedAt(post.getCreatedAt());
            dto.setUpdatedAt(post.getUpdatedAt());
            dto.setViewCount(post.getViewCount());
            dto.setHasAttachments(post.getAttachments() != null && !post.getAttachments().isEmpty());
            return dto;
        });
    }

    @Transactional(readOnly = true)
    public Page<PostListDTO> searchPosts(PostSearchRequestDTO requestDTO, Pageable pageable) {
        if (requestDTO.getStartDate() == null || requestDTO.getStartDate().isEmpty()) {
            requestDTO.setStartDate(LocalDate.now().minusYears(1).toString()); // 1년 전
        }
        if (requestDTO.getEndDate() == null || requestDTO.getEndDate().isEmpty()) {
            requestDTO.setEndDate(LocalDate.now().toString()); // 오늘
        }

        Specification<Post> specification = buildSpecification(requestDTO);

        return postRepository.findAll(specification, pageable).map(post -> {
            PostListDTO dto = new PostListDTO();
            dto.setId(post.getId());
            dto.setTitle(post.getTitle());
            dto.setContent(post.getContent());
            dto.setCategoryId(post.getCategoryId());
            dto.setCategoryName(post.getCategory().getCategoryName());
            dto.setAuthor(post.getAuthor());
            dto.setCreatedAt(post.getCreatedAt());
            dto.setUpdatedAt(post.getUpdatedAt());
            dto.setViewCount(post.getViewCount());
            dto.setHasAttachments(post.getAttachments() != null && !post.getAttachments().isEmpty());
            return dto;
        });
    }

    @Transactional
    public PostDetailDTO getPostDetail(Long postId){
        return postRepository.findById(postId)
                .map(post -> {
                    PostDetailDTO dto= new PostDetailDTO();
                    dto.setId(post.getId());
                    dto.setTitle(post.getTitle());
                    dto.setContent(post.getContent());
                    dto.setAuthor(post.getAuthor());
                    dto.setCreatedAt(post.getCreatedAt());
                    dto.setUpdatedAt(post.getUpdatedAt());
                    dto.setCategoryName(post.getCategory().getCategoryName());
                    dto.setViewCount(post.getViewCount());
                    dto.setAttachments(post.getAttachments().stream()
                            .map(attachment -> attachment.getOriginalName())
                            .collect(Collectors.toList()));

                    post.setViewCount(post.getViewCount() + 1);
                    return dto;
                })
                .orElseThrow(()-> new IllegalArgumentException("해당 게시글을 찾을 수 없습니다. ID : "+postId));
    }

    @Transactional
    public void deletePost(Long postId, String password) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글을 찾을 수 없습니다. ID: " + postId));

        if (!post.getPassword().equals(password)) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        List<Attachment> attachments = post.getAttachments();
        if (attachments != null && !attachments.isEmpty()) {
            for (Attachment attachment : attachments) {
                String physicalPath = attachment.getPhysicalPath();
                try {
                    Path path = Paths.get(physicalPath);
                    Files.deleteIfExists(path);
                    System.out.println("파일 삭제 성공: " + physicalPath);
                } catch (IOException e) {
                    System.err.println("파일 삭제 실패: " + physicalPath);
                    e.printStackTrace();
                }
            }
        }



        postRepository.delete(post);
    }

    private Specification<Post> buildSpecification(PostSearchRequestDTO requestDTO){
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            //startDate
            if(requestDTO.getStartDate()!=null && requestDTO.getStartDate().isEmpty()){
                LocalDate startDate = LocalDate.parse(requestDTO.getStartDate());
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(
                        root.get("createdAt"),
                        startDate.atStartOfDay()
                ));
            }

            //endDate
            if (requestDTO.getEndDate() != null && !requestDTO.getEndDate().isEmpty()){
                LocalDate endDate = LocalDate.parse(requestDTO.getEndDate());
                predicates.add(criteriaBuilder.lessThanOrEqualTo(
                        root.get("createdAt"),
                        endDate.atTime(23, 59, 59)
                ));
            }

            //categoryId
            if (requestDTO.getCategoryId() != null && requestDTO.getCategoryId() > 0) {
                predicates.add(criteriaBuilder.equal(
                        root.get("category").get("id"),
                        requestDTO.getCategoryId()
                ));
            }

            // keyword 필터 (제목 + 내용 + 작성자)
            if (requestDTO.getKeyword() != null && !requestDTO.getKeyword().isEmpty()) {
                String keyword = "%" + requestDTO.getKeyword() + "%";
                Predicate titlePredicate = criteriaBuilder.like(root.get("title"), keyword);
                Predicate contentPredicate = criteriaBuilder.like(root.get("content"), keyword);
                Predicate authorPredicate = criteriaBuilder.like(root.get("author"), keyword);

                predicates.add(criteriaBuilder.or(titlePredicate, contentPredicate, authorPredicate));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };

    }
}
