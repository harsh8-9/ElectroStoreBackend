package com.harry.electro.store.repositories;

import com.harry.electro.store.dtos.PageableResponse;
import com.harry.electro.store.entities.Category;
import com.harry.electro.store.entities.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepo extends JpaRepository<Product, String> {
    // search by title
    Page<Product> findByTitleContaining(String subTitle, Pageable pageable);

    // find live products
    Page<Product> findByLiveTrue(Pageable pageable);

    // find products by category
    Page<Product> findByCategory(Category category, Pageable pageable);


}
