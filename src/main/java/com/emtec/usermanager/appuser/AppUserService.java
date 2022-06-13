package com.emtec.usermanager.appuser;

import com.emtec.usermanager.registration.EmailValidator;
import com.emtec.usermanager.registration.token.ConfirmationToken;
import com.emtec.usermanager.registration.token.ConfirmationTokenService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class AppUserService implements UserDetailsService {

    private final AppUserRepository appUserRepository;
    private final ConfirmationTokenService confirmationTokenService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    private final EmailValidator emailValidator;
    private final static String USER_NOT_FOUND_MSG =
            "User with email %s not found";

    @Override
    public UserDetails loadUserByUsername(String email)
            throws UsernameNotFoundException {
        return appUserRepository.findByEmail(email)
                .orElseThrow(() ->
                        new UsernameNotFoundException(String.format(USER_NOT_FOUND_MSG,email)));
    }

    public String upsertUser(AppUser appUser){

        String token =  UUID.randomUUID().toString();;
        String encodedPassword = bCryptPasswordEncoder.encode(appUser.getPassword());
        appUser.setPassword(encodedPassword);
        Optional<AppUser> optionalAppUser = appUserRepository.findByEmail(appUser.getEmail());

        boolean userExist = optionalAppUser.isPresent();

        if(userExist){

            if( optionalAppUser.get().isEnabled() )
                throw new IllegalStateException("Email already taken");
            else {
                appUserRepository.setUserInfoByEmail(appUser.getFirstName(),
                        appUser.getLastName(), appUser.getPassword(), appUser.getEmail());
            }
        } else {

            ConfirmationToken confirmationToken = new ConfirmationToken(
                    token,
                    LocalDateTime.now(),
                    LocalDateTime.now().plusMinutes(15),
                    appUser
            );
            appUserRepository.save(appUser);
            confirmationTokenService.saveConfirmationToken(confirmationToken);
        }

        return token;
    }


    public int enableAppUser(String email) {
        return appUserRepository.enableAppUser(email);
    }

    public AppUserDto getAppUser(String email){

        boolean isValidEmail = emailValidator.test(email);

        if (!isValidEmail) {
            throw new IllegalStateException("Email not valid");
        }

        AppUserDto userDto = new AppUserDto();

        Optional<AppUser> optionalAppUser = appUserRepository.findByEmail(email.toLowerCase());

        optionalAppUser.ifPresentOrElse(
                (value)
                        -> {
                    userDto.setEmail(optionalAppUser.get().getEmail());
                    userDto.setFirstName(optionalAppUser.get().getFirstName());
                    userDto.setLastName(optionalAppUser.get().getLastName());
                },
                ()
                        -> { new UsernameNotFoundException(String.format(USER_NOT_FOUND_MSG,email)); });


        return userDto;
    }

    List<AppUserDto> getAllAppUsers(){
        return appUserRepository.findAll()
                .stream()
                .map(entry->new AppUserDto(entry.getFirstName(),
                        entry.getLastName(),
                        entry.getEmail(),
                        entry.isAccountNonLocked(),
                        entry.isEnabled()))
                .collect(Collectors.toList());
    }

}
