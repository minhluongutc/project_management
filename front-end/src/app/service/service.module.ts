import {NgModule} from "@angular/core";
import {ProjectService} from "./project.service";
import {HTTP_INTERCEPTORS, HttpClientModule} from "@angular/common/http";
import {AuthInterceptorService} from "../share/auth/auth-interceptor.service";

@NgModule({
  providers: [
    ProjectService,
    {
      provide: HTTP_INTERCEPTORS,
      useClass: AuthInterceptorService,
      multi: true,
    },
  ],
  imports: [HttpClientModule]
})
export class ServiceModule {
}
