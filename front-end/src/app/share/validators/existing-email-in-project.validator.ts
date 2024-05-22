import {AbstractControl, AsyncValidatorFn, ValidationErrors} from "@angular/forms";
import {UserService} from "../../service/user.service";
import {map, Observable, of} from "rxjs";
import {catchError} from "rxjs/operators";

export function existingEmailInProjectValidator(userService: UserService, projectId: any): AsyncValidatorFn {
  console.log('projectId', projectId)
  return (control: AbstractControl): Promise<ValidationErrors | null> | Observable<ValidationErrors | null> => {
    return userService.checkExistEmailsInProject(projectId, control.value).pipe(
      map(isExist => (isExist ? {existingEmailInProject: true} : null)),
      catchError(() => of(null))
    );
  };
}
