package model.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserDTO {

    private String name;
    private String address;
    private String joinDate;
    private String phoneNo;
    private String username;
    private String password;
    private String role;
}
