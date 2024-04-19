package datn.backend.service.jpa;

import datn.backend.repositories.jpa.UserRepositoryJPA;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserServiceJPA {
    final UserRepositoryJPA userRepositoryJPA;


}
