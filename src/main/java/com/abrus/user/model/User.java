package com.abrus.user.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
public class User {
    @NonNull private Long id;
    @NonNull private String email;
    @NonNull private String password;
    private String phoneNumber;

    public User(@NonNull Long id, @NonNull String email, @NonNull String password, String phoneNumber) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.phoneNumber = phoneNumber;
    }
}
