package datn.backend.entities;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "USER")
public class UserEntity implements Serializable {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    @Column(name = "USERNAME")
    String username;

    @Column(name = "PASSWORD")
    String password;

    @Column(name = "SETTING_ID")
    Integer settingId;

    @Column(name = "FIRST_NAME")
    String firstName;

    @Column(name = "LAST_NAME")
    String lastName;

    @Column(name = "CONTACT")
    String contact;

    @Column(name = "EMAIL")
    String email;

    @Column(name = "GENDER")
    Integer gender;

    @Column(name = "AVATAR_ID")
    Integer avatarId;

    @Column(name = "BACKGROUND_ID")
    Integer backgroundId;

    @Column(name = "DATE_OF_BIRTH")
    Date dateOfBirth;

    @Column(name = "ADDRESS")
    String address;

    @Column(name = "COMPANY_ID")
    Integer companyId;

    @Column(name = "CREATE_USER_ID")
    Integer createUserId;

    @Column(name = "CREATE_TIME")
    Date createTime;

    @Column(name = "UPDATE_USER_ID")
    Integer updateUserId;

    @Column(name = "UPDATE_TIME")
    Date updateTime;

    @Column(name = "ENABLED")
    Integer enabled;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(  name = "USER_ROLE",
            joinColumns = @JoinColumn(name = "USER_ID"),
            inverseJoinColumns = @JoinColumn(name = "ROLE_ID"))
    private Set<RoleEntity> roles = new HashSet<>();

    public UserEntity(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }
}
