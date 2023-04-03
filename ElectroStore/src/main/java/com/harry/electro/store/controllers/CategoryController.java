package com.harry.electro.store.controllers;

import com.harry.electro.store.dtos.*;
import com.harry.electro.store.services.CategoryService;
import com.harry.electro.store.services.FileService;
import com.harry.electro.store.services.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

/*
 *  @author :-
 *       Harshal Bafna
 */
@RestController
@RequestMapping("/categories")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;
    @Autowired
    private FileService fileService;
    @Autowired
    private ProductService productService;
    private Logger logger = LoggerFactory.getLogger(CategoryController.class);
    @Value("${category.cover.image.path}")
    private String coverImageFilePath;

    //    create
    @PostMapping
    public ResponseEntity<CategoryDto> createCategory(@Valid @RequestBody CategoryDto categoryDto) {
        CategoryDto categoryDto1 = categoryService.create(categoryDto);
        return new ResponseEntity<>(categoryDto1, HttpStatus.CREATED);
    }

    //    update
    @PutMapping("/{categoryId}")
    public ResponseEntity<CategoryDto> updateCategory(@Valid @RequestBody CategoryDto categoryDto, @PathVariable String categoryId) {
        CategoryDto updatedCategory = categoryService.update(categoryDto, categoryId);
        return ResponseEntity.ok(updatedCategory);
    }

    //    delete
    @DeleteMapping("/{categoryId}")
    public ResponseEntity<ApiResponse> deleteCategory(@PathVariable String categoryId) {
        categoryService.delete(categoryId);
        ApiResponse response = ApiResponse.builder()
                .message("Category deleted Successfully!")
                .success(true)
                .status(HttpStatus.OK)
                .build();
        return ResponseEntity.ok(response);

    }

    //    getAll
    @GetMapping
    public ResponseEntity<PageableResponse<CategoryDto>> getAllCategories(
            @RequestParam(value = "pageNumber", defaultValue = "1", required = false) int pageNumber,
            @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = "title", required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = "asc", required = false) String sortDir
    ) {
        PageableResponse<CategoryDto> categories = categoryService.getAll(pageNumber, pageSize, sortBy, sortDir);
        return ResponseEntity.ok(categories);

    }

    // get all products by category
    @GetMapping("/{categoryId}/products")
    public ResponseEntity<PageableResponse<ProductDto>> getAll(
            @PathVariable String categoryId,
            @RequestParam(value = "pageNumber", defaultValue = "1", required = false) int pageNumber,
            @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = "title", required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = "asc", required = false) String sortDir
    ) {
        PageableResponse<ProductDto> response = productService.getAllProducts(categoryId, pageNumber, pageSize, sortBy, sortDir);
        return ResponseEntity.ok(response);
    }

    //    get one
    @GetMapping("/{categoryId}")
    public ResponseEntity<CategoryDto> getCategoryById(@PathVariable String categoryId) {
        CategoryDto category = categoryService.getById(categoryId);
        return ResponseEntity.ok(category);
    }

    // upload cover image
    @PostMapping("/coverImage/{categoryId}")
    public ResponseEntity<ImageResponse> uploadCoverImage(
            @RequestParam("coverImage") MultipartFile file,
            @PathVariable String categoryId) {
        String fileNameWithExtension = null;
        try {
            fileNameWithExtension = fileService.uploadFile(file, coverImageFilePath);
        } catch (IOException e) {
            logger.error("Error occurred while uploading the category image - " + e.getMessage());
        }
        CategoryDto category = categoryService.getById(categoryId);
        category.setCoverImage(fileNameWithExtension);
        categoryService.update(category, categoryId);
        ImageResponse response = ImageResponse.builder()
                .message("Cover Image uploaded successfully!")
                .fileName(fileNameWithExtension)
                .status(HttpStatus.CREATED)
                .success(true)
                .build();
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // serve cover Image
    @GetMapping("/coverImage/{categoryId}")
    public void serveCoverImage(@PathVariable String categoryId,
                                HttpServletResponse response) throws IOException {
        CategoryDto category = categoryService.getById(categoryId);
        InputStream inputStream = fileService.getResource(coverImageFilePath, category.getCoverImage());
        response.setContentType(MediaType.IMAGE_JPEG_VALUE);
        StreamUtils.copy(inputStream, response.getOutputStream());

    }

}

