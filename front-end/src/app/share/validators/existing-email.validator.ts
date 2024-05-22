import {AbstractControl, AsyncValidatorFn, ValidationErrors} from "@angular/forms";
import {UserService} from "../../service/user.service";
import {map, Observable, of} from "rxjs";
import {catchError} from "rxjs/operators";

export function existingEmailValidator(userService: UserService): AsyncValidatorFn {
  return (control: AbstractControl): Promise<ValidationErrors | null> | Observable<ValidationErrors | null> => {
    return userService.checkExistEmails(control.value).pipe(
      map(isExist => (isExist ? { existingEmail: true } : null)),
      catchError(() => of(null))
    );
  };
}
