package com.emtec.usermanager.appuser;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class AppUserRepositoryTest {

    @Autowired
    private AppUserRepository appUserRepository;

    @Test
    @Disabled
    void checkEnabledUserByEmail() {
        // given
        String email = "john.doe@gmail.com";
        AppUser appUser=  new AppUser("John", "Doe",
                "john.doe@gmail.com",  "password", AppUserRole.USER);
        appUser.setEnabled(Boolean.FALSE);
        appUserRepository.save(appUser);

        // when
        int expected = appUserRepository.enableAppUser(email);
        // then
        assertThat(expected).isNotNegative();

    }
}