package com.emtec.usermanager.appuser;

import com.emtec.usermanager.registration.EmailValidator;
import com.emtec.usermanager.registration.token.ConfirmationTokenService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.mockito.Mockito.verify;

@ExtendWith({MockitoExtension.class})
class AppUserServiceTest {

    @Mock
    AppUserRepository appUserRepository;
    @Mock
    ConfirmationTokenService confirmationTokenService;
    @Mock
    BCryptPasswordEncoder bCryptPasswordEncoder;
    @Mock
    EmailValidator emailValidator;

    AppUserService appUserService;

    @BeforeEach
    void setUp() {
        appUserService = new AppUserService(appUserRepository,
                confirmationTokenService,
                bCryptPasswordEncoder,
                emailValidator);

    }


    @Test
    void canGetAppUser() {
        // when
        appUserService.getAppUser("john.doe@gmail.com");
        // then
        verify(appUserRepository).findByEmail("john.doe@gmail.com");
    }

    @Test
    void canGetAllAppUsers() {
        // when
        appUserService.getAllAppUsers();
        // then
        verify(appUserRepository).findAll();
    }
}