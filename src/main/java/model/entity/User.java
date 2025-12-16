package model.entity;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class User {
    private String userId;
    private String email;
    private String password;
    private String role;
}
