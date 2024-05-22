import {Component, OnInit} from "@angular/core";
import {FormControl, FormGroup, Validators} from "@angular/forms";
import {AuthService} from "../../share/auth/auth.service";
import {Router} from "@angular/router";
import {MessageService} from "primeng/api";
import {Observable} from "rxjs";
import {AuthResponseData} from "../../share/auth/AuthResponseData.model";

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrl: './login.component.scss'
})
export class LoginComponent implements OnInit {

  loginForm: FormGroup = new FormGroup<any>({});
  signUpForm: FormGroup = new FormGroup<any>({});
  isLoading: boolean = false;

  mode: 'login' | 'register' = 'login';

  constructor(
    private authService: AuthService,
    private router: Router,
    private messageService: MessageService
  ) {
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
        'confirmPassword': new FormControl('', [Validators.required, Validators.minLength(6)]),
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
          this.showError(err.error.message);
        }
      }
    )
  }

  showError(message: string) {
    this.messageService.add({severity: 'error', summary: 'Thất bại', detail: message});
  }

  changeMode(mode: 'login' | 'register') {
    this.mode = mode;
  }
}
