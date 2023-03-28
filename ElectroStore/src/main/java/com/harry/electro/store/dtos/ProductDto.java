package com.harry.electro.store.dtos;

import lombok.*;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductDto {
    private String prodId;
    private String Title;
    private String Description;
    private int price;
    private int discountedPrice;
    private boolean live;
    private boolean stock;
    private String productImage;
}
