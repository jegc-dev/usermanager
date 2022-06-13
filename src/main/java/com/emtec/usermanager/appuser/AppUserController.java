package com.emtec.usermanager.appuser;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "api/v1/")
@AllArgsConstructor
public class AppUserController {

    private final AppUserService appUserService;

    @GetMapping(path = "user")
    @ResponseBody
    public ResponseEntity<AppUserDto> user(@RequestParam("email") String email) {
        AppUserDto user = appUserService.getAppUser(email);

        if(user.getEmail() != null )
            return new ResponseEntity<AppUserDto>(user, HttpStatus.FOUND);
        else return new ResponseEntity<AppUserDto>(HttpStatus.NOT_FOUND);
    }

    @GetMapping(path = "users")
    @ResponseBody
    public List<AppUserDto> users() {
        return appUserService.getAllAppUsers();
    }

}
