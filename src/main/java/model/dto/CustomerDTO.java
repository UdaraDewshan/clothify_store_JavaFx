package model.dto;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class CustomerDTO {
    private String name;
    private String contact;
    private String address;
    private String email;
    private String joinedDate;
}
