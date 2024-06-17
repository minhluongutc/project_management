import {AbstractControl, FormControl, ValidationErrors, ValidatorFn} from "@angular/forms";

export function PasswordValidator(confirmPasswordInput: string): ValidatorFn {
  let confirmPasswordControl: AbstractControl;
  let passwordControl: AbstractControl;

  return (control: AbstractControl): ValidationErrors | null => {
    if (!control.parent) {
      return null;
    }

    if (!confirmPasswordControl) {
      confirmPasswordControl = control;
      passwordControl = control.parent.get(confirmPasswordInput) as FormControl;
      passwordControl.valueChanges.subscribe(() => {
        confirmPasswordControl.updateValueAndValidity();
      });
    }

    if (passwordControl.value.toLocaleLowerCase() !==
      confirmPasswordControl.value.toLocaleLowerCase()
    ) {
      return {notMatch: true};
    } else {
      confirmPasswordControl.setErrors(null);
    }

    return null;
  };
}
