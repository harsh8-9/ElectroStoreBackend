package com.harry.electro.store.entities;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/*
 *  @author :-
 *       Harshal Bafna
 */

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "products")
public class Product {
    @Id
    private String prodId;
    private String Title;
    @Column(length = 10000)
    private String Description;
    private int price;
    private int discountedPrice;
    private boolean live;
    private boolean stock;
    private String productImage;
}
