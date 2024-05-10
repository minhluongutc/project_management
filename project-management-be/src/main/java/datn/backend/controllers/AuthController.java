package datn.backend.controllers;

import datn.backend.config.auth.ERole;
import datn.backend.config.auth.payload.request.LoginRequest;
import datn.backend.config.auth.payload.request.SignupRequest;
import datn.backend.config.auth.payload.response.JwtResponse;
import datn.backend.config.auth.payload.response.MessageResponse;
import datn.backend.config.auth.payload.response.UserDTO;
import datn.backend.config.auth.security.jwt.JwtUtils;
import datn.backend.config.auth.security.service.UserDetailsImpl;
import datn.backend.dto.AttachmentDTO;
import datn.backend.entities.RoleEntity;
import datn.backend.entities.UserEntity;
import datn.backend.repositories.jpa.DocumentRepositoryJPA;
import datn.backend.repositories.jpa.RoleRepositoryJPA;
import datn.backend.repositories.jpa.UserRepositoryJPA;
import datn.backend.service.AuthService;
import datn.backend.utils.Constants;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
@FieldDefaults(level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class AuthController {
    final AuthenticationManager authenticationManager;
    final UserRepositoryJPA userRepository;
    final RoleRepositoryJPA roleRepository;
    final DocumentRepositoryJPA documentRepositoryJPA;
    final PasswordEncoder encoder;
    final JwtUtils jwtUtils;

    final AuthService authService;

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());

        String avatarId = null;

        List<AttachmentDTO.AttachmentResponseDTO> avatarAttachment = documentRepositoryJPA.getAttachmentsByObjectIdAndType(userDetails.getId(), Constants.DOCUMENT_TYPE.AVATAR.value);
        if (!avatarAttachment.isEmpty()) {
            avatarId = avatarAttachment.get(0).getId();
        }
        UserDTO user = new UserDTO(userDetails.getId(),
                userDetails.getUsername(),
                userDetails.getPassword(),
                userDetails.getFirstName(),
                userDetails.getLastName(),
                userDetails.getEmail(),
                avatarId,
                roles);
        return ResponseEntity.ok(new JwtResponse(jwt, user));
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Username is already taken!"));
        }

        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Email is already in use!"));
        }

        // Create new user's account
        UserEntity user = new UserEntity(signUpRequest.getUsername(),
                signUpRequest.getEmail(),
                encoder.encode(signUpRequest.getPassword()));

        Set<String> strRoles = signUpRequest.getRole();
        Set<RoleEntity> roles = new HashSet<>();

        if (strRoles == null) {
            RoleEntity userRole = roleRepository.findByName(ERole.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(userRole);
        } else {
            strRoles.forEach(role -> {
                switch (role) {
                    case "ADMIN":
                        RoleEntity companyManagerRole = roleRepository.findByName(ERole.ROLE_ADMIN)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(companyManagerRole);
                        break;
                    case "PROJECT_MANAGER":
                        RoleEntity projectManagerRole = roleRepository.findByName(ERole.ROLE_PROJECT_MANAGER)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(projectManagerRole);
                        break;
                    case "LEADER":
                        RoleEntity leaderRole = roleRepository.findByName(ERole.ROLE_LEADER)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(leaderRole);
                        break;
                    default:
                        RoleEntity userRole = roleRepository.findByName(ERole.ROLE_USER)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(userRole);
                }
            });
        }
        user.setId(java.util.UUID.randomUUID().toString());
        user.setFirstName(signUpRequest.getFirstName());
        user.setLastName(signUpRequest.getLastName());
        user.setContact(signUpRequest.getContact());
        user.setGender(signUpRequest.getGender());
        user.setDateOfBirth(signUpRequest.getDateOfBirth());
        user.setAddress(signUpRequest.getAddress());
        user.setCreateTime(new Date());
        user.setRoles(roles);
        user.setEnabled(Constants.STATUS.ACTIVE.value);
        userRepository.save(user);

        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }

    @GetMapping("/otp")
    public ResponseEntity<?> getOTP(@RequestParam String email) {
        // generate OTP
        String otp = authService.generateOtp();
        // send OTP to email

        return ResponseEntity.ok(new MessageResponse("OTP sent successfully!"));
    }
}
