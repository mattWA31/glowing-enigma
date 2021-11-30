package fr.glowingenigma.web.apis;

import fr.glowingenigma.domain.application.UserService;
import fr.glowingenigma.domain.model.user.EmailAddressExistsException;
import fr.glowingenigma.domain.model.user.RegistrationException;
import fr.glowingenigma.domain.model.user.UsernameExistsException;
import fr.glowingenigma.web.payload.RegistrationPayload;
import fr.glowingenigma.web.results.ApiResult;
import fr.glowingenigma.web.results.Result;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

@Controller
public class RegistrationApiController {

    private UserService service;

    public RegistrationApiController(UserService service){
        this.service = service;
    }

    @PostMapping("/api/registrations")
    public ResponseEntity<ApiResult> register(@Valid @RequestBody RegistrationPayload payload){
        try {
            service.register(payload.toCommand());
            return Result.created();
        } catch (RegistrationException e){
            String errorMessage = "Registration failed";
            if(e instanceof UsernameExistsException){
                errorMessage = "Username already exists";
            } else if (e instanceof EmailAddressExistsException){
                errorMessage = "Email address already exists";
            }
            return Result.failure(errorMessage);
        }
    }

}
