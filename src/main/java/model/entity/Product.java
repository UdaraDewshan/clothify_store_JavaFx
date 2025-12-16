package model.entity;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Product {
    private String productId;
    private String name;
    private String category;
    private int size;
    private Double price;
    private int qty;
    private String supplierId;
}
