import { Component, input, OnInit, signal } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { FlightModel } from '../../flights/flightModel';
import { DatePipe } from '@angular/common';
import { AddPassengerComponent } from './add-passanger/add-passenger.component';
import { ReservationForm } from './reservation-form/reservetion-form.component';
import { PassengerModel } from '../../passengers/passenger.model';
import { ConnectionErrorComponent } from '../../../shared/components/connection-error/connection-error.component';
import { NotFoundComponent } from '../../../shared/components/not-found/not-found.component';
import { ReservationFormService } from '../reservation-form.service';

@Component({
  selector: 'app-add-reservation',
  imports: [
    FormsModule,
    ReactiveFormsModule,
    DatePipe,
    AddPassengerComponent,
    ConnectionErrorComponent,
    NotFoundComponent,
    ReservationForm
  ],
  templateUrl: './add-reservation.component.html',
})
export class AddReservation extends ReservationFormService implements OnInit {
  flight: FlightModel | undefined;
  id = input.required<string>();
  isFetching = signal(true);
  errorCode = signal(200);
  passenger = signal<PassengerModel | null>(null);

  ngOnInit() {
    this.dataService.fetchData<FlightModel>(`https://bilet-system.onrender.com/api/flights/${this.id()}`)
      .subscribe({
        next: data => {
          this.flight = data;
        },
        complete: () => {
          this.isFetching.set(false);
        }
        ,
        error: (err) => {
          this.isFetching.set(false);
          if (err.status === 0) this.errorCode.set(0);
          else if (err.status === 404) this.errorCode.set(404)
        }
      });
  }

  handlePassengerValidation(passenger: PassengerModel | null) {
    if (passenger) {
      this.passenger.set(passenger);
    }
  }
}
