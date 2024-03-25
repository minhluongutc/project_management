//package datn.backend.entities;
//
//import jakarta.persistence.Column;
//import jakarta.persistence.Entity;
//import jakarta.persistence.Id;
//import jakarta.persistence.Table;
//import lombok.AccessLevel;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//import lombok.experimental.FieldDefaults;
//
//import java.io.Serializable;
//import java.util.Date;
//
//@Data
//@NoArgsConstructor
//@FieldDefaults(level = AccessLevel.PRIVATE)
//@Entity
//@Table(name = "COMPANY")
//public class CompanyEntity implements Serializable {
//
//    @Id
//    @Column(name = "ID")
//    String id;
//
//    @Column(name = "NAME")
//    String name;
//
//    @Column(name = "COUNTRY")
//    String country;
//
//    @Column(name = "CONTACT")
//    String contact;
//
//    @Column(name = "EMAIL")
//    String email;
//
//    @Column(name = "CREATE_USER_ID", insertable = false, updatable = false)
//    String createUserId;
//
//    @Column(name = "CREATE_TIME")
//    Date createTime;
//
//    @Column(name = "CREATE_USER_ID")
//    String updateUserId;
//
//    @Column(name = "UPDATE_TIME")
//    Date updateTime;
//
//    @Column(name = "ENABLED")
//    Integer enabled;
//}
