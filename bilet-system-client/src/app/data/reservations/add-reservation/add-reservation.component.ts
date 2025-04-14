import { Component, input, OnInit, signal } from '@angular/core';
import { FormControl, FormGroup, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { DataForm } from '../../../shared/abstract/data-form.service';
import { FlightModel } from '../../flights/flightModel';
import { DatePipe } from '@angular/common';
import { AddPassengerComponent } from './add-passanger/add-passenger.component';
import { ReservationFormComponent } from './reservation-form/reservation-form.component';
import { PassengerModel } from '../../passengers/passenger.model';
import { ConnectionErrorComponent } from '../../../shared/components/connection-error/connection-error.component';
import { NotFoundComponent } from '../../../shared/components/not-found/not-found.component';

@Component({
  selector: 'app-add-reservation',
  imports: [
    FormsModule,
    ReactiveFormsModule,
    DatePipe,
    AddPassengerComponent,
    ReservationFormComponent,
    ConnectionErrorComponent,
    NotFoundComponent
  ],
  templateUrl: './add-reservation.component.html',
})
export class AddReservationComponent extends DataForm implements OnInit {
  form = new FormGroup({
    firstname: new FormControl("", {
      validators: [Validators.required]
    }),
    lastname: new FormControl("", {
      validators: [Validators.required]
    }),
    email: new FormControl("", {
      validators: [Validators.required, Validators.email]
    }),
    phone: new FormControl("", {
      validators: [Validators.required, Validators.pattern(new RegExp("\\+[1-9][0-9]{0,2}[0-9]{7,12}$"))]
    }),
  });
  flight: FlightModel | undefined;
  id = input.required<string>();
  isFetching = signal(true);
  errorCode = signal(200);
  passenger = signal<PassengerModel | null>(null);

  ngOnInit() {
    this.dataService.fetchData<FlightModel>(`http://localhost:8080/api/flights/${this.id()}`)
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

  submit(): void {
  }

  handlePassengerValidation(passenger: PassengerModel | null) {
    if (passenger) {
      this.passenger.set(passenger);
    }
  }
}
