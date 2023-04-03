package com.harry.electro.store.services.impl;

import com.harry.electro.store.dtos.PageableResponse;
import com.harry.electro.store.dtos.ProductDto;
import com.harry.electro.store.entities.Category;
import com.harry.electro.store.entities.Product;
import com.harry.electro.store.exceptions.ResourceNotFoundException;
import com.harry.electro.store.repositories.CategoryRepo;
import com.harry.electro.store.repositories.ProductRepo;
import com.harry.electro.store.services.ProductService;
import com.harry.electro.store.utils.PagingAndSorting;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.DataTruncation;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepo productRepo;
    @Autowired
    private CategoryRepo categoryRepo;
    @Autowired
    private ModelMapper mapper;
    private Logger logger = LoggerFactory.getLogger(ProductService.class);
    @Value("${product.image.path}")
    private String coverImagePath;

    @Override
    public ProductDto create(ProductDto productDto) {
        //product id
        String prodId = UUID.randomUUID().toString();
        productDto.setProdId(prodId);
        // added date
        productDto.setAddedDate(new Date());
        Product product = mapper.map(productDto, Product.class);
        Product savedProduct = productRepo.save(product);
        return mapper.map(savedProduct, ProductDto.class);
    }

    // create product with category
    @Override
    public ProductDto create(ProductDto productDto, String categoryId) {
        Category category = categoryRepo.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("Category not found with id - " + categoryId));
        //product id
        String prodId = UUID.randomUUID().toString();
        productDto.setProdId(prodId);
        // added date
        productDto.setAddedDate(new Date());
        Product product = mapper.map(productDto, Product.class);
        // SET CATEGORY
        product.setCategory(category);
        Product savedProduct = productRepo.save(product);
        return mapper.map(savedProduct, ProductDto.class);
    }

    @Override
    public ProductDto update(ProductDto productDto, String productId) {
        Product product = productRepo.findById(productId).orElseThrow(() -> new ResourceNotFoundException("Product not found with id - " + productId));
        product.setTitle(productDto.getTitle());
        product.setDescription(productDto.getDescription());
        product.setPrice(productDto.getPrice());
        product.setDiscountedPrice(productDto.getDiscountedPrice());
        product.setLive(productDto.isLive());
        product.setStock(productDto.isStock());
        product.setProductImage(productDto.getProductImage());
        Product updatedProduct = productRepo.save(product);
        return mapper.map(updatedProduct, ProductDto.class);
    }

    @Override
    public ProductDto update(String productId, String categoryId) {
        Product product = productRepo.findById(productId).orElseThrow(() -> new ResourceNotFoundException("Product not found wiht id - " + productId));
        Category category = categoryRepo.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("Category not found with id - " + categoryId));
        product.setCategory(category);
        Product updatedProduct = productRepo.save(product);
        return mapper.map(updatedProduct, ProductDto.class);
    }

    @Override
    public void delete(String productId) {
        Product product = productRepo.findById(productId).orElseThrow(() -> new ResourceNotFoundException("Product not found with id - " + productId));
        // delete cover image for products
        String fullImagePath = coverImagePath + product.getProductImage();
        try {
            Path path = Paths.get(fullImagePath);
            Files.delete(path);
        } catch (NoSuchFileException ex) {
            logger.info("Image doesnt exists for category " + productId);
        } catch (IOException e) {
            logger.info("Image doesnt exists for category " + productId);
        }
        productRepo.delete(product);
        logger.info("Product Deleted Successfully!");
    }

    @Override
    public PageableResponse<ProductDto> getAllProducts(int pageNumber, int pageSize, String sortBy, String sortDir) {
        Sort sort = "desc".equalsIgnoreCase(sortDir) ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable1 = PageRequest.of(pageNumber - 1, pageSize, sort);
        Page<Product> page = productRepo.findAll(pageable1);
        PageableResponse<ProductDto> response = PagingAndSorting.getPageableResponse(page, ProductDto.class);
        return response;
    }

    // find by category
    @Override
    public PageableResponse<ProductDto> getAllProducts(String categoryId, int pageNumber, int pageSize, String sortBy, String sortDir) {
        Category category = categoryRepo.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("Category not found with id - " + categoryId));
        Sort sort = "desc".equalsIgnoreCase(sortDir) ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable1 = PageRequest.of(pageNumber - 1, pageSize, sort);
        Page<Product> page = productRepo.findByCategory(category, pageable1);
        PageableResponse<ProductDto> response = PagingAndSorting.getPageableResponse(page, ProductDto.class);
        return response;
    }

    @Override
    public ProductDto get(String productId) {
        Product product = productRepo.findById(productId).orElseThrow(() -> new ResourceNotFoundException("Product not found with id - " + productId));
        return mapper.map(product, ProductDto.class);
    }

    @Override
    public PageableResponse<ProductDto> searchByTitle(String subTitle, int pageNumber, int pageSize, String sortBy, String sortDir) {
        Sort sort = "desc".equalsIgnoreCase(sortDir) ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize, sort);
        Page<Product> page = productRepo.findByTitleContaining(subTitle, pageable);
        return PagingAndSorting.getPageableResponse(page, ProductDto.class);
    }

    @Override
    public PageableResponse<ProductDto> getAllLive(int pageNumber, int pageSize, String sortBy, String sortDir) {
        Sort sort = "desc".equalsIgnoreCase(sortDir) ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize, sort);
        Page<Product> page = productRepo.findByLiveTrue(pageable);
        return PagingAndSorting.getPageableResponse(page, ProductDto.class);
    }
}
