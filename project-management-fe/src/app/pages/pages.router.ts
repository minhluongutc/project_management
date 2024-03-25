import { Routes } from '@angular/router';
import {FeatureTestComponent} from "./feature-test/feature-test.component";
import {LayoutComponent} from "../share/ui/layout/layout.component";
import {ProjectsComponent} from "./projects/projects.component";

export const pagesRouter: Routes = [
  {
    path: '',
    component: LayoutComponent,
    children: [
      {
        path: '',
        component: FeatureTestComponent
      },
      {
        path: 'projects',
        component: ProjectsComponent
      }
    ]
  },
];
