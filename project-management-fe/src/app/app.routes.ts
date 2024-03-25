import {Routes} from '@angular/router';
import {LoginComponent} from "./pages/login/login.component";
import {FeatureTestComponent} from "./pages/feature-test/feature-test.component";
import {AppComponent} from "./app.component";
import {LayoutComponent} from "./share/ui/layout/layout.component";

export const routes: Routes = [
  {
    path: 'login',
    component: LoginComponent
  },
  {
    path: 'test',
    component: FeatureTestComponent
  },
  {
    path: '',
    loadChildren: () => import('./pages/pages.router').then(m => m.pagesRouter)
  }
];
