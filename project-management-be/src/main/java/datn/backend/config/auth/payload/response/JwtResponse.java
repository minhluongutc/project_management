package datn.backend.config.auth.payload.response;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class JwtResponse {
    private String accessToken;
    private String tokenType = "Bearer";
    private UserDTO user;

    public JwtResponse(String accessToken, UserDTO userDTO) {
        this.accessToken = accessToken;
        this.user = userDTO;
    }
}