import { Routes } from '@angular/router';
import { PassengersTableComponent } from './data/passengers/passengers-table/passengers-table.component';
import { FlightsTableComponent } from './data/flights/flights-table/flights-table.component';
import { ReservationsTableComponent } from './data/reservations/reservations-table.component';

export const routes: Routes = [
  {
    path: "passengers",
    component: PassengersTableComponent
  },
  {
    path: "flights",
    component: FlightsTableComponent
  },
  {
    path: "reservations",
    component: ReservationsTableComponent
  },
  {
    path: "**",
    redirectTo: "passengers"
  },
];
