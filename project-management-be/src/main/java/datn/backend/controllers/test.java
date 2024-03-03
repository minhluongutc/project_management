package datn.backend.controllers;

import datn.backend.config.core.I18n;
import datn.backend.entities.UserEntity;
import datn.backend.jpa.UserRepositoryJPA;
import datn.backend.utils.ResponseUtils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class test {
    final UserRepositoryJPA userRepositoryJPA;

    @GetMapping(value = "test")
    public ResponseEntity<Object> tests() {
        return ResponseUtils.getResponseEntity(userRepositoryJPA.findAll());
    }

    @GetMapping(value = "testi18n")
    public ResponseEntity<Object> testi18n() {
        return ResponseUtils.getResponseEntity(I18n.getMessage("msg.common.validate.import.file.template.not-found"));
    }
}
