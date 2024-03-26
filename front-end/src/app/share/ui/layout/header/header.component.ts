import { Component } from '@angular/core';
import {SharedModule} from "primeng/api";
import {AvatarModule} from "primeng/avatar";
import {ButtonModule} from "primeng/button";
import {InputTextModule} from "primeng/inputtext";
import {ToolbarModule} from "primeng/toolbar";
import {SplitButtonModule} from "primeng/splitbutton";
import {AuthService} from "../../../auth/auth.service";
import {User} from "../../../auth/user.model";
import {Router} from "@angular/router";
import {Subscription} from "rxjs";

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrl: './header.component.scss'
})
export class HeaderComponent {
  userSub: Subscription;
  isAuthenticated: boolean = false;
  userData: Partial<User> = {};

  constructor(
    private authService: AuthService,
    private router: Router
  ) {
    this.userSub = Subscription.EMPTY;
  }

  ngOnInit(): void {
    this.userSub = this.authService.user.subscribe((user) => {
      this.isAuthenticated = !!user;
    });
    // @ts-ignore
    this.userData = JSON.parse(localStorage.getItem('userData'));
    if (!this.isAuthenticated) {
      this.router.navigate(['/login']);
    }
  }

  onLogout() {
    this.authService.logout();
  }
}
