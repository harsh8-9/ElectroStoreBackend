package com.harry.electro.store.controllers;


import com.harry.electro.store.dtos.*;
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
import java.io.IOException;
import java.io.InputStream;

/*
 *  @author :-
 *       Harshal Bafna
 */
@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductService productService;
    @Autowired
    private FileService fileService;
    @Value("product.image.path")
    private String imageUploadPath;
    private Logger logger = LoggerFactory.getLogger(ProductController.class);


    //    create
    @PostMapping
    public ResponseEntity<ProductDto> create(@RequestBody ProductDto productDto) {
        ProductDto createdProduct = productService.create(productDto);
        return new ResponseEntity<>(createdProduct, HttpStatus.CREATED);
    }

    // create with category
    @PostMapping("/category/{categoryId}")
    public ResponseEntity<ProductDto> create(
            @RequestBody ProductDto productDto,
            @PathVariable String categoryId) {
        ProductDto productDto1 = productService.create(productDto, categoryId);
        return new ResponseEntity<>(productDto1, HttpStatus.CREATED);
    }

    //update
    @PutMapping("/{productId}")
    public ResponseEntity<ProductDto> update(@RequestBody ProductDto productDto,
                                             @PathVariable String productId) {
        ProductDto updatedProduct = productService.update(productDto, productId);
        return ResponseEntity.ok(updatedProduct);
    }

    //update the category
    @PutMapping("/{productId}/category/{categoryId}")
    public ResponseEntity<ProductDto> update(
            @PathVariable String productId,
            @PathVariable String categoryId) {
        ProductDto updatedProduct = productService.update(productId, categoryId);
        return ResponseEntity.ok(updatedProduct);
    }


    //delete
    @DeleteMapping("/{productId}")
    public ResponseEntity<ApiResponse> delete(@PathVariable String productId) {
        productService.delete(productId);
        ApiResponse response = ApiResponse.builder()
                .message("Product Deleted Successfully")
                .success(true)
                .status(HttpStatus.OK)
                .build();
        return ResponseEntity.ok(response);
    }

    //get single
    @GetMapping("/{productId}")
    public ResponseEntity<ProductDto> getSingle(@PathVariable String productId) {
        return new ResponseEntity<>(productService.get(productId), HttpStatus.OK);
    }

    // get all
    @GetMapping
    public ResponseEntity<PageableResponse<ProductDto>> getAll(
            @RequestParam(value = "pageNumber", defaultValue = "1", required = false) int pageNumber,
            @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = "title", required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = "asc", required = false) String sortDir
    ) {
        PageableResponse<ProductDto> response = productService.getAllProducts(pageNumber, pageSize, sortBy, sortDir);
        return ResponseEntity.ok(response);
    }

    // search by title
    @GetMapping("/search/{subtitle}")
    public ResponseEntity<PageableResponse<ProductDto>> getByTitle(
            @PathVariable String subtitle,
            @RequestParam(value = "pageNumber", defaultValue = "1", required = false) int pageNumber,
            @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = "title", required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = "asc", required = false) String sortDir
    ) {
        PageableResponse<ProductDto> response = productService.searchByTitle(subtitle, pageNumber, pageSize, sortBy, sortDir);
        return ResponseEntity.ok(response);
    }


    // get all live
    @GetMapping("/live")
    public ResponseEntity<PageableResponse<ProductDto>> getAllLiveProducts(
            @RequestParam(value = "pageNumber", defaultValue = "1", required = false) int pageNumber,
            @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = "title", required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = "asc", required = false) String sortDir
    ) {
        PageableResponse<ProductDto> response = productService.getAllLive(pageNumber, pageSize, sortBy, sortDir);
        return ResponseEntity.ok(response);
    }

    // upload image

    @PostMapping("/image/{productId}")
    public ResponseEntity<ImageResponse> uploadImage(
            @PathVariable String productId,
            @RequestParam("productImage") MultipartFile image
    ) {
        String imageName = null;
        try {
            imageName = fileService.uploadFile(image, imageUploadPath);
        } catch (IOException e) {
            logger.error("Error occurred while uploading the product image - " + e.getMessage());
        }
        ProductDto productDto = productService.get(productId);
        productDto.setProductImage(imageName);
        ProductDto updatedProduct = productService.update(productDto, productId);
        ImageResponse response = ImageResponse.builder()
                .success(true)
                .status(HttpStatus.CREATED)
                .fileName(imageName)
                .message("Product Image Uploaded Successfully !!")
                .build();
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    //    serve image
    @GetMapping("image/{productId}")
    public void serveUserImage(@PathVariable String productId, HttpServletResponse response) throws IOException {
        ProductDto productDto = productService.get(productId);
        InputStream inputStream = fileService.getResource(imageUploadPath, productDto.getProductImage());
        response.setContentType(MediaType.IMAGE_JPEG_VALUE);
        StreamUtils.copy(inputStream, response.getOutputStream());
    }
}
