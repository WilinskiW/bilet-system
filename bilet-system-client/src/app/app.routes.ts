import { Routes } from '@angular/router';
import { PassengersTableComponent } from './data/passengers/passengers-table/passengers-table.component';
import { FlightsTableComponent } from './data/flights/flights-table/flights-table.component';
import { ReservationsTableComponent } from './data/reservations/reservations-table.component';
import { AddFlightComponent } from './data/flights/add-flight/add-flight.component';
import { EditFlightComponent } from './data/flights/edit-flight/edit-flight.component';

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
    path: "flights/add",
    component: AddFlightComponent
  },
  {
    path: "flights/edit/:id",
    component: EditFlightComponent
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
