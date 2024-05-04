//package datn.backend.entities;
//
//import jakarta.persistence.*;
//import lombok.AccessLevel;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//import lombok.experimental.FieldDefaults;
//
//@Data
//@NoArgsConstructor
//@FieldDefaults(level = AccessLevel.PRIVATE)
//@Entity
//@Table(name = "USER_ROLE")
//public class UserRoleEntity {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    Long id;
//
//    @ManyToOne
//    @JoinColumn(name = "USER_ID")
//    UserEntity userId;
//
//    @ManyToOne
//    @JoinColumn(name = "ROLE_ID")
//    RoleEntity roleId;
//
//    @Column(name = "PROJECT_ID")
//    String projectId;
//}