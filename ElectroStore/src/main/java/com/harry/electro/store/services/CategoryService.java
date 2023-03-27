package com.harry.electro.store.services;

import com.harry.electro.store.dtos.CategoryDto;
import com.harry.electro.store.dtos.PageableResponse;

/*
 *  @author :-
 *       Harshal Bafna
 */
public interface CategoryService {
    //    create
    CategoryDto create(CategoryDto categoryDto);

    //    update
    CategoryDto update(CategoryDto categoryDto, String categoryId);

    //    delete
    void delete(String categoryId);

    //    get all
    PageableResponse<CategoryDto> getAll(int pageNumber, int pageSize, String sortBy, String sortDir);

    //    get single category
    CategoryDto getById(String categoryId);
//    search

}
