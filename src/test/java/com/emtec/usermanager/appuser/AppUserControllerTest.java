package com.emtec.usermanager.appuser;


import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@RunWith(SpringRunner.class)
@WebMvcTest(value = AppUserController.class)
@WithMockUser
class AppUserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AppUserService appUserService;

    @MockBean
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    private AppUserDto userDto = new AppUserDto("Jessica", "Simpson",
            "jessica.simpson@gmail.com", false, true);

    private AppUserDto userDto2 = new AppUserDto("Jason", "Simpson",
            "jason.simpson@gmail.com", false, true);

    private List<AppUserDto> appUserDtos = List.of(userDto,userDto2);

    @Test
    public void canRetrieveAppUserByEmail() throws Exception {

        Mockito.when(
                appUserService.getAppUser(Mockito.anyString())).thenReturn(userDto);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.get(
                "/api/v1/user?email=john.doe@gmail.com").accept(
                MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        String expected = "{\n" +
                "        \"firstName\": \"Jessica\",\n" +
                "        \"lastName\": \"Simpson\",\n" +
                "        \"email\": \"jessica.simpson@gmail.com\",\n" +
                "        \"locked\": false,\n" +
                "        \"enabled\": true\n" +
                "    }";


        JSONAssert.assertEquals(expected, result.getResponse()
                .getContentAsString(), false);
    }

    @Test
    public void canRetrieveAppUserList() throws Exception {

        Mockito.when(
                appUserService.getAllAppUsers()).thenReturn(appUserDtos);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.get(
                "/api/v1/users").accept(
                MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        String expected = "[{\"firstName\":\"Jessica\"," +
                "\"lastName\":\"Simpson\"," +
                "\"email\":\"jessica.simpson@gmail.com\"," +
                "\"locked\":false,\"enabled\":true}," +
                "{\"firstName\":\"Jason\"," +
                "\"lastName\":\"Simpson\"," +
                "\"email\":\"jason.simpson@gmail.com\"," +
                "\"locked\":false,\"enabled\":true}]";

        JSONAssert.assertEquals(expected, result.getResponse()
                .getContentAsString(),false);
    }
}