package model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Table(name = "supplies")
public class Supplier {
    @Id
    private String supplierId;
    private String name;
    private String company;
    private String email;
    private String contactNo;
}
