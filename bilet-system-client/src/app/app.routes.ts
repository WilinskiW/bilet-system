import { Routes } from '@angular/router';
import { PassengersComponent } from './data/passengers/passengers-table/passengers.component';

export const routes: Routes = [
  {
    path: "passengers",
    component: PassengersComponent
  },
  {
    path: "**",
    redirectTo: "passengers"
  },
];
