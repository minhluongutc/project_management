import {NgModule} from "@angular/core";
import {AuthGuardService} from "./auth-guard.service";
import {AuthService} from "./auth.service";
import {AuthInterceptorService} from "./auth-interceptor.service";
import {HTTP_INTERCEPTORS, HttpClientModule} from "@angular/common/http";

@NgModule({
  providers: [
    AuthGuardService,
    AuthService,
    AuthInterceptorService,
    {
      provide: HTTP_INTERCEPTORS,
      useClass: AuthInterceptorService,
      multi: true,
    },
  ],
  imports: [HttpClientModule]
})
export class AuthModule {
}
