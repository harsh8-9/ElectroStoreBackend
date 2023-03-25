package com.harry.electro.store.utils;

import com.harry.electro.store.dtos.PageableResponse;
import com.harry.electro.store.dtos.UserDto;
import com.harry.electro.store.entities.User;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.stream.Collectors;

/*
 *  @author :-
 *       Harshal Bafna
 */
public class PagingAndSorting {

    public static <U, V> PageableResponse<V> getPageableResponse(Page<U> page, Class<V> type) {
        List<U> entity = page.getContent();
        List<V> dtoList = entity.stream().map(object -> new ModelMapper().map(object, type)).collect(Collectors.toList());
        PageableResponse<V> response = new PageableResponse<>();
        response.setContent(dtoList);
        response.setPageSize(page.getSize());
        response.setPageNumber(page.getNumber() + 1);
        response.setTotalElements(page.getTotalElements());
        response.setTotalPages(page.getTotalPages());
        response.setLastPage(page.isLast());
        return response;
    }
}
