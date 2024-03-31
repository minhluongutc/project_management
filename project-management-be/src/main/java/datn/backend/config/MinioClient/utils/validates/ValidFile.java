package datn.backend.config.MinioClient.utils.validates;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.PARAMETER, ElementType.TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {FileValidator.class})
public @interface ValidFile {
    String message() default "Dung lượng file không vượt quá 15MB và không nằm trong tập đuôi sau (\".EXE\",\".CMD\",\".BAT\",\".COM\",\".LNK\",\".VBS\",\".MSI\",\".VB\",\".WS\",\".WSF\",\".SCF\",\".SCR\",\".PIF\")";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
