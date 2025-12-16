package model.entity;

import lombok.*;

import java.util.Date;
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Order {
    private String orderId;
    private Date date;
    private Double total;
    private String customerId;
    private String userId;
}
