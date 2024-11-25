package board_practice2.service;

import board_practice2.dto.CategoryListDTO;
import board_practice2.entity.Category;
import board_practice2.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;


    public List<CategoryListDTO> getAllCategories() {
        List<Category> categories = categoryRepository.findAll();

        return categories.stream()
                .map(category -> new CategoryListDTO(category.getId(), category.getCategoryName()))
                .collect(Collectors.toList());
    }
}
