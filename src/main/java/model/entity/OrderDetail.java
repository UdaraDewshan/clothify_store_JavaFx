package model.entity;

import jakarta.persistence.Entity;
import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
public class OrderDetail {
    private String orderId;
    private String productId;
    private int qty;
    private Double unitPrice;
}
