package model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

import java.util.Date;
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
public class Order {
    @Id
    private String orderId;
    private Date date;
    private Double total;

    private String customerId;

    private String userId;
}
