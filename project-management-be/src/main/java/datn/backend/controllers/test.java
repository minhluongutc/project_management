package datn.backend.controllers;

//import datn.backend.config.core.I18n;
import datn.backend.repositories.jpa.UserRepositoryJPA;
import datn.backend.utils.ResponseUtils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class test {
    final UserRepositoryJPA userRepositoryJPA;

    @GetMapping(value = "test")
    public ResponseEntity<Object> tests(String name) {
        return ResponseUtils.getResponseEntity(userRepositoryJPA.getIdByUsername(name));
    }

    @GetMapping("/user")
    @PreAuthorize("hasRole('USER') or hasRole('PROJECT_MANAGER') or hasRole('ADMIN')")
    public String userAccess() {
        return "User Content.";
    }

    @GetMapping("/pm")
    @PreAuthorize("hasRole('PROJECT_MANAGER')")
    public String moderatorAccess() {
        return "Project manager Board.";
    }

    @GetMapping("/cm")
    @PreAuthorize("hasRole('ADMIN')")
    public String adminAccess() {
        return "Company manager Board.";
    }

//    @GetMapping(value = "testi18n")
//    public ResponseEntity<Object> testi18n() {
//        return ResponseUtils.getResponseEntity(I18n.getMessage("msg.common.validate.import.file.template.not-found"));
//    }
}
