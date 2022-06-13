package com.emtec.usermanager.appuser;

import lombok.*;


@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AppUserDto {

    private String firstName;
    private String lastName;
    private String email;
    private boolean locked;
    private boolean enabled;

}
