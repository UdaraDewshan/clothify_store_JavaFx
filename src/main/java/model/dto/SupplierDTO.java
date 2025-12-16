package model.dto;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class SupplierDTO {
    private String supplierId;
    private String name;
    private String company;
    private String email;
    private String contactNo;
}

