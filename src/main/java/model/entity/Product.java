package model.entity;

import jakarta.persistence.Entity;
import lombok.*;

import java.nio.file.Path;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
public class Product {
    private String productId;
    private String name;
    private String category;
    private int size;
    private Double price;
    private int qty;
    private String supplierId;
    private Path image;
}
