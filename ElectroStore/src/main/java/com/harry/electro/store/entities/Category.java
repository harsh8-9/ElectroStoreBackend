package com.harry.electro.store.entities;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/*
 *  @author :-
 *       Harshal Bafna
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "categories")
public class Category {
    @Id
    @Column(name = "cat_id")
    private String categoryId;
    @Column(name = "cat_title", length = 60, nullable = false)
    private String title;
    @Column(name = "cat_desc", length = 250)
    private String description;
    private String coverImage;
    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Product> products = new ArrayList<>();
}
