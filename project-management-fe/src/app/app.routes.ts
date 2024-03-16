import { Routes } from '@angular/router';
import {LoginComponent} from "./pages/login/login.component";
import {FeatureTestComponent} from "./pages/feature-test/feature-test.component";

export const routes: Routes = [
  {
    path: '/login',
    component: LoginComponent
  },
  {
    path: '/test',
    component: FeatureTestComponent
  }
];
