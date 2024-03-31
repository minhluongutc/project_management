import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import {PagesModule} from "./pages/pages.module";
import {AuthModule} from "./share/auth/auth.module";
import {MessageService} from "primeng/api";
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import {ProjectService} from "./service/project.service";
import {ServiceModule} from "./service/service.module";
import {ToastModule} from "primeng/toast";


@NgModule({
  declarations: [
    AppComponent
  ],
  imports: [
    BrowserAnimationsModule,
    BrowserModule,
    AppRoutingModule,
    PagesModule,
    AuthModule,
    ServiceModule,
    ToastModule
  ],
  providers: [MessageService],
  bootstrap: [AppComponent]
})
export class AppModule { }
