import {Injectable} from "@angular/core";
import {Environment} from "../environment/environment";
import {BehaviorSubject, Observable, tap, throwError} from "rxjs";
import {User} from "./user.model";
import {Router} from "@angular/router";
import {HttpClient, HttpErrorResponse} from "@angular/common/http";
import {catchError} from "rxjs/operators";
import {AuthResponseData} from "./AuthResponseData.model";
import {LocalStorageService} from "../store/local-storage.service";

@Injectable()
export class AuthService {
  private signupUrl = `${Environment.baseUrl}/api/auth/signup`;
  private loginUrl = `${Environment.baseUrl}/api/auth/signin`;
  private refreshUrl = `${Environment.baseUrl}/api/auth/refresh`;
  private tokenExpirationTimer: any;

  // @ts-ignore
  user: BehaviorSubject<User> = new BehaviorSubject<User>(localStorage.getItem('userData') ? JSON.parse(localStorage.getItem('userData')) : null);

  constructor(
    private http: HttpClient,
    private router: Router,
    private localStorageService: LocalStorageService
  ) {
  }

  login(username: string, password: string): Observable<AuthResponseData> {
    return this.http
      .post<AuthResponseData>(
        this.loginUrl,
        {
          username: username,
          password: password,
          returnSecureToken: true
        }
      )
      .pipe(
        catchError(this.handleError),
        tap((resData: AuthResponseData) => {
          console.log('resData:', resData)
          this.handleAuthentication(
            resData.user.id,
            resData.user.username,
            resData.user.firstName,
            resData.user.lastName,
            resData.user.email,
            resData.user.roles,
            resData.user.companyId,
            resData.user.avatarId,
            resData.accessToken,
            +resData.expires_in
          );
        })
      )
  }

  autoLogin() {
    const userDataString: string | null = localStorage.getItem('userData');

    const userData: {
      id: string;
      username: string;
      firstName: string;
      lastName: string;
      email: string;
      roles: string[];
      companyId: string;
      avatarId: string;
      _token: string;
      _tokenExpirationData: string;
    } = userDataString ? JSON.parse(userDataString) : null;

    if (!userData) {
      return;
    }

    const loadedUser = new User(
      userData.id,
      userData.username,
      userData.firstName,
      userData.lastName,
      userData.email,
      userData.roles,
      userData.companyId,
      userData.avatarId,
      userData._token,
      new Date(userData._tokenExpirationData),
    );

    if (loadedUser.token) {
      this.user.next(loadedUser);
      const expirationDuration =
        new Date(userData._tokenExpirationData).getTime() -
        new Date().getTime();
      this.autoLogout(expirationDuration);
    }
  }

  logout() {
    // @ts-ignore
    this.user.next(null);
    this.router.navigate(['/login']);
    localStorage.removeItem('userData');
    if (this.tokenExpirationTimer) {
      clearTimeout(this.tokenExpirationTimer);
    }
    this.tokenExpirationTimer = null;
  }

  autoLogout(expirationDuration: number) {
    this.tokenExpirationTimer = setTimeout(() => {
      this.logout();
    }, expirationDuration);
  }

  private handleAuthentication(
    id: string,
    username: string,
    firstName: string,
    lastName: string,
    email: string,
    roles: string[],
    companyId: string,
    avatarId: string,
    accessToken: string,
    expiresIn: number,
  ) {
    console.log(expiresIn)
    let expirationDate = new Date(new Date().getTime() + 3600 * 1000);
    if (expiresIn) expirationDate = new Date(new Date().getTime() + expiresIn * 1000);
    const user: User = new User(id, username, firstName, lastName, email, roles, companyId, avatarId, accessToken, expirationDate);
    this.user.next(user);
    if (expiresIn) this.autoLogout(expiresIn * 1000);
    else this.autoLogout(3600 * 1000);
    localStorage.setItem('userData', JSON.stringify(user));
    console.log(user)
  }

  private handleError(errorRes: HttpErrorResponse) {
    console.log(errorRes.error.error);
    let errorMessage = '';
    switch (errorRes.error.error) {
      case 'Unauthorized':
        errorMessage = 'Tài khoản hoặc mật khẩu không chính xác';
        break;
      default :
        errorMessage = 'Lỗi hệ thống';
    }
    return throwError(errorMessage);
  }
}

