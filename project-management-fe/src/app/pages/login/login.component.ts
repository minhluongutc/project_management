import {Component, OnInit} from '@angular/core';
import {ToastModule} from "primeng/toast";
import {FormControl, FormGroup, ReactiveFormsModule, Validators} from "@angular/forms";
import {AuthService} from "../../share/auth/auth.service";
import {Router} from "@angular/router";
import {MessageService} from "primeng/api";
import {Observable} from "rxjs";
import {AuthResponseData} from "../../share/auth/AuthResponseData.model";
import {NgIf} from "@angular/common";
import {HttpClient, HttpClientModule} from "@angular/common/http";

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [
    ToastModule,
    ReactiveFormsModule,
    NgIf,
    HttpClientModule
  ],
  templateUrl: './login.component.html',
  styleUrl: './login.component.scss',
  providers: [AuthService, HttpClient, MessageService]
})
export class LoginComponent implements OnInit {
  loginForm: FormGroup = new FormGroup<any>({});
  isLoading: boolean = false;

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
  }

  get username() {
    return this.loginForm.get('username');
  }

  get password() {
    return this.loginForm.get('password');
  }

  onSubmit(form: FormGroup) {
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
        this.showError(errorMessage)
      });
  }

  showError(message: string) {
    this.messageService.add({severity: 'error', summary: 'Thất bại', detail: message});
  }
}
