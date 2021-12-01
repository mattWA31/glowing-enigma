package fr.glowingenigma.web.apis;

import fr.glowingenigma.domain.application.UserService;
import fr.glowingenigma.domain.model.user.EmailAddressExistsException;
import fr.glowingenigma.domain.model.user.UsernameExistsException;
import fr.glowingenigma.utils.JsonUtils;
import fr.glowingenigma.web.payload.RegistrationPayload;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(SpringExtension.class)
@WebMvcTest(RegistrationApiController.class)
public class RegistrationApiControllerTests {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private UserService serviceMock;

    @Test
    public void register_blankPayload_shouldFailAndReturn400() throws Exception {
        mvc.perform(post("/api/registrations")).andExpect(status().is(400));
    }

    @Test
    public void register_existingUsername_shouldFailAndReturn400() throws Exception {
        RegistrationPayload payload = new RegistrationPayload();
        payload.setUsername("exist");
        payload.setEmailAddress("blabla@glowingenigma.com");
        payload.setPassword("MyPassword!");

        doThrow(UsernameExistsException.class)
                .when(serviceMock)
                .register(payload.toCommand());

        mvc.perform(post("/api/registrations")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(JsonUtils.toJson(payload)))
            .andExpect(status().is(400))
            .andExpect(jsonPath("$.message").value("Username already exists"));
    }

    @Test
    public void register_existingEmailAddress_shouldFailAndReturn400() throws Exception {
        RegistrationPayload payload = new RegistrationPayload();
        payload.setUsername("test");
        payload.setEmailAddress("exist@glowingenigma.com");
        payload.setPassword("MyPassword!");

        doThrow(EmailAddressExistsException.class)
                .when(serviceMock)
                .register(payload.toCommand());

        mvc.perform(post("/api/registrations")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtils.toJson(payload)))
            .andExpect(status().is(400))
            .andExpect(jsonPath("$.message").value("Email address already exists"));
    }

    @Test
    public void register_validPayload_shouldSucceedAndReturn201() throws Exception {
        RegistrationPayload payload = new RegistrationPayload();
        payload.setUsername("test");
        payload.setEmailAddress("blabla@glowingenigma.com");
        payload.setPassword("MyPassword!");

        doNothing().when(serviceMock).register(payload.toCommand());

        mvc.perform(post("/api/registrations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtils.toJson(payload)))
                .andExpect(status().is(201));
    }


}
