import { Routes } from '@angular/router';
import { PassengersTableComponent } from './data/passengers/passengers-table/passengers-table.component';
import { FlightsTableComponent } from './data/flights/flights-table/flights-table.component';
import { ReservationsTableComponent } from './data/reservations/reservations-table/reservations-table.component';
import { AddFlightComponent } from './data/flights/add-flight/add-flight.component';
import { EditFlightComponent } from './data/flights/edit-flight/edit-flight.component';
import { EditPassengerComponent } from './data/passengers/edit-passenger/edit-passenger.component';
import { AddReservation } from './data/reservations/add-reservation/add-reservation.component';
import { EditReservationComponent } from './data/reservations/edit-reservation/edit-reservation.component';

export const routes: Routes = [
  {
    path: "passengers",
    component: PassengersTableComponent
  },
  {
    path: "passengers/edit/:id",
    component: EditPassengerComponent,
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
    path: "reservations/add/:id",
    component: AddReservation
  },
  {
    path: "reservations/edit/:id",
    component: EditReservationComponent
  },
  {
    path: "**",
    redirectTo: "flights"
  },
];
