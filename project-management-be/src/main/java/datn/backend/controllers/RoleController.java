package datn.backend.controllers;

import datn.backend.service.jpa.RoleServiceJPA;
import datn.backend.utils.Constants;
import datn.backend.utils.ResponseUtils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(Constants.REQUEST_MAPPING_PREFIX)
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RoleController {
    final RoleServiceJPA roleServiceJPA;

    @GetMapping(value = "/roles", produces = MediaType.APPLICATION_JSON_VALUE)
    public Object getRoles() {
        return ResponseUtils.getResponseEntity(roleServiceJPA.getAllRoles());
    }
}
