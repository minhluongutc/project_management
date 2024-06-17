import {Component, Injector, OnInit} from "@angular/core";
import {FormControl, FormGroup, Validators} from "@angular/forms";
import {AuthService} from "../../share/auth/auth.service";
import {Router} from "@angular/router";
import {MessageService} from "primeng/api";
import {Observable} from "rxjs";
import {AuthResponseData} from "../../share/auth/AuthResponseData.model";
import {BaseComponent} from "../../share/ui/base-component/base.component";
import {PasswordValidator} from "../../share/validators/re-password.validator";

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrl: './login.component.scss'
})
export class LoginComponent extends BaseComponent implements OnInit {

  loginForm: FormGroup = new FormGroup<any>({});
  signUpForm: FormGroup = new FormGroup<any>({});
  isLoading: boolean = false;

  mode: 'login' | 'register' = 'login';

  constructor(
    injector: Injector,
    private messageService: MessageService
  ) {
    super(injector)
  }

  ngOnInit(): void {
    this.loginForm = new FormGroup(
      {
        'username': new FormControl('', [Validators.required]),
        'password': new FormControl('', [Validators.required, Validators.minLength(6)])
      }
    )
    this.signUpForm = new FormGroup(
      {
        'username': new FormControl('', [Validators.required]),
        'password': new FormControl('', [Validators.required, Validators.minLength(6)]),
        'confirmPassword': new FormControl('', [Validators.required, Validators.minLength(6), PasswordValidator('password')]),
        'firstName': new FormControl('', [Validators.required]),
        'lastName': new FormControl('', [Validators.required]),
        'email': new FormControl('', [Validators.required, Validators.email]),
      }
    )
  }

  get username() {
    return this.loginForm.get('username');
  }

  get password() {
    return this.loginForm.get('password');
  }

  get email() {
    return this.signUpForm.controls['email'];
  }

  get confirmPassword() {
    return this.signUpForm.controls['confirmPassword'];
  }

  get lastName() {
    return this.signUpForm.controls['lastName'];
  }

  get firstName() {
    return this.signUpForm.controls['firstName'];
  }

  onSubmitLogin(form: FormGroup) {
    this.isLoading = true;
    const username = form.value.username;
    const password = form.value.password;
    let authObs: Observable<AuthResponseData>;

    authObs = this.authService.login(username, password);
    authObs.subscribe(
      (resData: AuthResponseData) => {
        this.isLoading = false;
        console.log('resData', resData);
        this.router.navigate(['']);
      },
      errorMessage => {
        this.isLoading = false;
        console.log('errorMessage', errorMessage);
        // this.showError(errorMessage)
        this.messageService.add({severity: 'error', summary: 'Thất bại', detail: errorMessage});
      });
  }

  onSubmitSignUp(form: FormGroup) {
    console.log(form)
    this.authService.register(form.value).subscribe(
      {
        next: res => {
          this.mode = 'login';
          this.messageService.add({severity: 'success', summary: 'Thành công', detail: 'Đăng ký tài khoản thành công'});
        }, error: err => {
          if (err.status == 401) {
            this.createErrorToast('Error', 'biểu mẫu không hợp lệ')
          } else {
            this.createErrorToast('Error', err.error.message);
          }
        }
      }
    )
  }

  changeMode(mode: 'login' | 'register') {
    this.mode = mode;
  }
}
