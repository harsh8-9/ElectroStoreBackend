package com.harry.electro.store.services.impl;

import com.harry.electro.store.dtos.CategoryDto;
import com.harry.electro.store.dtos.PageableResponse;
import com.harry.electro.store.entities.Category;
import com.harry.electro.store.exceptions.ResourceNotFoundException;
import com.harry.electro.store.repositories.CategoryRepo;
import com.harry.electro.store.services.CategoryService;
import com.harry.electro.store.utils.PagingAndSorting;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

/*
 *  @author :-
 *       Harshal Bafna
 */
@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    private CategoryRepo categoryRepo;
    @Autowired
    private ModelMapper mapper;
    private Logger logger = LoggerFactory.getLogger(CategoryServiceImpl.class);
    @Value("${category.cover.image.path}")
    private String coverImagePath;

    @Override
    public CategoryDto create(CategoryDto categoryDto) {
        String categoryId = UUID.randomUUID().toString();
        categoryDto.setCategoryId(categoryId);
        Category category = mapper.map(categoryDto, Category.class);
        Category savedCat = categoryRepo.save(category);
        return mapper.map(savedCat, CategoryDto.class);
    }

    @Override
    public CategoryDto update(CategoryDto categoryDto, String categoryId) {
        Category category = categoryRepo.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("Category not found with category ID - " + categoryId));
        category.setTitle(categoryDto.getTitle());
        category.setDescription(categoryDto.getDescription());
        category.setCoverImage(categoryDto.getCoverImage());
        Category updatedCat = categoryRepo.save(category);
        return mapper.map(updatedCat, CategoryDto.class);
    }

    @Override
    public void delete(String categoryId) {
        Category category = categoryRepo.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("Category not found with category ID - " + categoryId));
        // delete cover image for category
        String fullImagePath = coverImagePath + category.getCoverImage();
        try {
            Path path = Paths.get(fullImagePath);
            Files.delete(path);
        } catch (NoSuchFileException ex) {
            logger.info("Image doesnt exists for category " + categoryId);
        } catch (IOException e) {
            logger.info("Image doesnt exists for category " + categoryId);
        }
        categoryRepo.delete(category);
        logger.info("CATEGORY : Category Deleted Successfully!");
    }


    @Override
    public PageableResponse<CategoryDto> getAll(int pageNumber, int pageSize, String sortBy, String sortDir) {
        Sort sort = "desc".equalsIgnoreCase(sortDir) ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize, sort);
        Page<Category> page = categoryRepo.findAll(pageable);
        PageableResponse<CategoryDto> response = PagingAndSorting.getPageableResponse(page, CategoryDto.class);
        return response;
    }

    @Override
    public CategoryDto getById(String categoryId) {
        Category category = categoryRepo.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("Category not found with category ID - " + categoryId));
        return mapper.map(category, CategoryDto.class);
    }
}
