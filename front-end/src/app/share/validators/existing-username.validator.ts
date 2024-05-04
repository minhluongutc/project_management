import {AbstractControl, AsyncValidatorFn, ValidationErrors} from "@angular/forms";
import {UserService} from "../../service/user.service";
import {map, Observable, of} from "rxjs";
import {catchError} from "rxjs/operators";

export function existingUsernameValidator(userService: UserService): AsyncValidatorFn {
  return (control: AbstractControl): Promise<ValidationErrors | null> | Observable<ValidationErrors | null> => {
    return userService.checkExistUsernames(control.value).pipe(
      map(isExist => (isExist ? { existingUsername: true } : null)),
      catchError(() => of(null))
    );
  };
}
