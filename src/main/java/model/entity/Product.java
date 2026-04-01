package model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
public class Product {
    @Id
    private String productId;
    private String name;
    private String category;
    private String size;
    private Double buyingPrice;
    private Double sellingPrice;
    private Double profit;
    private int qty;
    private String supplierId;
    private String imagePath;
}