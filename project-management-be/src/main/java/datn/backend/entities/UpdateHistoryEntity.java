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
//
//@Data
//@NoArgsConstructor
//@FieldDefaults(level = AccessLevel.PRIVATE)
//@Entity
//@Table(name = "UPDATE_HISTORY")
//public class UpdateHistoryEntity implements Serializable {
//
//    @Id
//    @Column(name = "ID")
//    String id;
//
//    @Column(name = "TASK_ID")
//    String taskId;
//
//    @Column(name = "SUBJECT")
//    String subject;
//
//    @Column(name = "DESCRIPTION")
//    String description;
//
//    @Column(name = "TYPE_ID")
//    String typeId;
//
//    @Column(name = "STATUS_ISSUE_ID")
//    String statusIssueId;
//
//    @Column(name = "PRIORITY")
//    Integer priority;
//
//    @Column(name = "PARENT_ID")
//    String parentId;
//
//    @Column(name = "START_DATE")
//    Date startDate;
//
//    @Column(name = "DUE_DATE")
//    Date dueDate;
//
//    @Column(name = "ESTIMATE_TIME")
//    Integer estimateTime;
//
//    @Column(name = "MODIFY_TIME")
//    Date modifyTime;
//
//    @Column(name = "MODIFY_USER_ID")
//    String modifyUserId;
//
//    @Column(name = "ENABLED")
//    Integer enabled;
//}
