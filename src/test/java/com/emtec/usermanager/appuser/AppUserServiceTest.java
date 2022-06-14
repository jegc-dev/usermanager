package com.emtec.usermanager.appuser;

import com.emtec.usermanager.registration.EmailValidator;
import com.emtec.usermanager.registration.token.ConfirmationTokenService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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

    private final String ENCODED_PASSWORD = "129083oijoiAHIIMMSnoin09u09kn";

    boolean userExist = true;

    Optional<AppUser> optionalAppUser;
    Optional<AppUser> noAppUser = Optional.empty();
    AppUser appUser = new AppUser("John", "Doe",
            "john.doe@gmail.com", "password", AppUserRole.USER);
    private final AppUserDto userDto = new AppUserDto("John", "Doe",
            "john.doe@gmail.com", false, true);

    @BeforeEach
    void setUp() {
        appUserService = new AppUserService(appUserRepository,
                confirmationTokenService,
                bCryptPasswordEncoder,
                emailValidator);


        optionalAppUser = Optional.ofNullable(appUser);

    }


    @Test
    void canGetAppUser() {
        // when
        when(emailValidator.test(anyString())).thenReturn(true);
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

    @Test
    void canUpdateUserWhenExists() {
        // when
        when(bCryptPasswordEncoder.encode(anyString())).thenReturn(ENCODED_PASSWORD);
        when(appUserRepository.findByEmail("john.doe@gmail.com")).thenReturn(optionalAppUser);
        appUserService.upsertUser(appUser);
        // then
        verify(appUserRepository).setUserInfoByEmail(appUser.getFirstName(),
                appUser.getLastName(), appUser.getPassword(), appUser.getEmail());
    }

    @Test
    void canUpdateUserWhenNoExists() {
        // when
        when(bCryptPasswordEncoder.encode(anyString())).thenReturn(ENCODED_PASSWORD);
        when(appUserRepository.findByEmail("john.doe@gmail.com")).thenReturn(noAppUser);
        appUserService.upsertUser(appUser);
        // then
        verify(appUserRepository).save(appUser);
    }
}