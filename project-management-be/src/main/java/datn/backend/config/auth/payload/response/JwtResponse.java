package datn.backend.config.auth.payload.response;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class JwtResponse {
    private String accessToken;
    private String tokenType = "Bearer";
    private String id;
    private String username;
    private String email;
    private List<String> roles;

    public JwtResponse(String accessToken, String id, String username, String email, List<String> roles) {
        this.accessToken = accessToken;
        this.id = id;
        this.username = username;
        this.email = email;
        this.roles = roles;
    }
}