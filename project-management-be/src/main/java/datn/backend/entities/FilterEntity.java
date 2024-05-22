package datn.backend.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;
import java.util.Date;

@Data
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "FILTER")
public class FilterEntity implements Serializable {

    @Id
    @Column(name = "ID")
    String id;

    @Column(name = "NAME")
    String name;

    @Column(name = "USER_ID")
    String userId;

    @Column(name = "SUBJECT")
    String subject;

    @Column(name = "PROJECT_ID")
    String projectId;

    @Column(name = "PRIPRITY_IS_EQUAL")
    Boolean priorityIsEqual;

    @Column(name = "PRIORITY")
    String priority;

    @Column(name = "SEVERITY_IS_EQUAL")
    Boolean severityIsEqual;

    @Column(name = "SEVERITY")
    String severity;

    @Column(name = "TYPE_ID_IS_EQUAL")
    Boolean typeIdIsEqual;

    @Column(name = "TYPE_ID")
    String typeId;

    @Column(name = "STATUS_ISSUE_ID_IS_EQUAL")
    Boolean statusIssueIdIsEqual;

    @Column(name = "STATUS_ISSUE_ID")
    String statusIssueId;

    @Column(name = "CATEGORY_ID_IS_EQUAL")
    Boolean categoryIdIsEqual;

    @Column(name = "CATEGORY")
    String categoryId;

    @Column(name = "ASSIGN_USER_ID_IS_EQUAL")
    Boolean assignUserIdIsEqual;

    @Column(name = "ASSIGN_USER_ID")
    String assignUserId;

    @Column(name = "REVIEW_USER_ID_IS_EQUAL")
    Boolean reviewUserIdIsEqual;

    @Column(name = "REVIEW_USER_ID")
    String reviewUserId;

    @Column(name = "START_DATE_OPERATOR")
    String startDateOperator;

    @Column(name = "START_DATE")
    Date startDate;

    @Column(name = "END_DATE_OPERATOR")
    String endDateOperator = "bang";

    @Column(name = "END_DATE")
    Date endDate;

    @Column(name = "CREATE_TIME")
    Date createTime;

    @Column(name = "UPDATE_TIME")
    Date updateTime;

    @Column(name = "ENABLED")
    Integer enabled;
}
