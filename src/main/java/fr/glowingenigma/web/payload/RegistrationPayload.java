package fr.glowingenigma.web.payload;

import fr.glowingenigma.domain.application.commands.RegistrationCommand;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Objects;

public class RegistrationPayload {

    @Size(min = 2, max = 50, message = "Username must be between 2 and 50 characters")
    @NotNull
    private String username;

    @Email(message = "Email should be valid")
    @Size(max = 100, message = "email address must not be more than 100 characters")
    @NotNull
    private String emailAddress;

    @Size(min = 6, max = 30, message = "Password should be between 6 and 30 characters")
    @NotNull
    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public RegistrationCommand toCommand() {
        return new RegistrationCommand(this.username, this.emailAddress, this.password);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RegistrationPayload payload = (RegistrationPayload) o;
        return Objects.equals(username, payload.username) && Objects.equals(emailAddress, payload.emailAddress) && Objects.equals(password, payload.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, emailAddress, password);
    }
}
