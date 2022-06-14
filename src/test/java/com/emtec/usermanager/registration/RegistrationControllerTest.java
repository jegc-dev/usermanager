package com.emtec.usermanager.registration;

import com.emtec.usermanager.appuser.AppUserController;
import com.emtec.usermanager.appuser.AppUserService;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@WebMvcTest(value = AppUserController.class)
@WithMockUser
class RegistrationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AppUserService appUserService;

    @MockBean
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @MockBean
    private RegistrationService registrationService;

    private String token = "c01687b7-2c7b-4e4b-a177-898380cfecf6";

    private RegistrationRequest registrationRequest = new RegistrationRequest("Jessica",
            "Simpson","Jessica.Simpson@gmail.com","password");


    String exampleUserRequested = "{\"firstName\":\"Jessica\",\"lastName\":\"Simpson\",\"email\":\"Jessica.Simpson@gmail.com\",\"password\":\"password\"}";


    @Test
    @Disabled
    void canRegister() throws Exception {

        Mockito.when(
                registrationService.register(Mockito.any(RegistrationRequest.class))).thenReturn(token);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/api/v1/registration")
                .accept(MediaType.APPLICATION_JSON).content(exampleUserRequested)
                .contentType(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        MockHttpServletResponse response = result.getResponse();

        System.out.println("response:::" + response.getStatus());

        assertEquals(HttpStatus.OK.value(),response.getStatus());

    }

    @Test
    @Disabled
    void canConfirm() throws Exception {

        Mockito.when(
                registrationService.confirmToken(Mockito.anyString())).thenReturn("confirmed");

        RequestBuilder requestBuilder = MockMvcRequestBuilders.get(
                "/api/v1/registration/confirm?token=" + token).accept(
                MediaType.TEXT_PLAIN_VALUE);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        MockHttpServletResponse response = result.getResponse();

        assertEquals(HttpStatus.OK.value(),response.getStatus());

    }
}