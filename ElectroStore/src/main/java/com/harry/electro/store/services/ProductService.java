package com.harry.electro.store.services;

import com.harry.electro.store.dtos.PageableResponse;
import com.harry.electro.store.dtos.ProductDto;

public interface ProductService {

    //    create
    ProductDto create(ProductDto productDto);

    ProductDto create(ProductDto productDto, String categoryId);

    //update
    ProductDto update(ProductDto productDto, String productId);

    // update category of product
    ProductDto update(String productId, String categoryId);

    //delete
    void delete(String productId);

    //get all
    PageableResponse<ProductDto> getAllProducts(int pageNumber, int pageSize, String sortBy, String sortDir);
    // get all products by category
    PageableResponse<ProductDto> getAllProducts(String categoryId, int pageNumber, int pageSize, String sortBy, String sortDir);

    // get single
    ProductDto get(String productId);

    //search by : title
    PageableResponse<ProductDto> searchByTitle(String subTitle, int pageNumber, int pageSize, String sortBy, String sortDir);

    // find stock : live
    PageableResponse<ProductDto> getAllLive(int pageNumber, int pageSize, String sortBy, String sortDir);

}
