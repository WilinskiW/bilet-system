import { Component, input, signal } from '@angular/core';
import { DataForm } from '../../../../shared/abstract/data-form.service';
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { FlightModel } from '../../../flights/flightModel';
import { PassengerModel } from '../../../passengers/passenger.model';
import { ReservationModel } from '../../reservation.model';

@Component({
  selector: 'app-reservation-form',
  imports: [
    ReactiveFormsModule
  ],
  templateUrl: './reservation-form.component.html',
})
export class ReservationFormComponent extends DataForm{
  form = new FormGroup({
    reservationNumber: new FormControl("", {
      validators: [Validators.required]
    }),
    seatNumber: new FormControl("", {
      validators: [Validators.required, Validators.pattern(new RegExp("^[1-9][0-9]*[A-F]$"))]
    }),
    hasDeparted: new FormControl(false)
  })

  flight = input.required<FlightModel | undefined>();
  passenger = input.required<PassengerModel | null>();
  waiting = signal(false);

  submit(): void {
    if (this.form.invalid) {
      this.formService.markAllControlsAsTouched(this.form);
      return;
    }

    if(!this.flight() || !this.passenger()){
      return;
    }

    const reservation: ReservationModel = {
      reservationNumber: this.form.controls.reservationNumber.value as string,
      flight: this.flight() as FlightModel,
      seatNumber: this.form.controls.seatNumber.value as string,
      passenger: this.passenger() as PassengerModel,
      hasDeparted: this.form.controls.hasDeparted.value as boolean
    }

    this.dataService.sendData(reservation, `http://localhost:8080/api/reservations`)
      .subscribe({
        next: () => this.waiting.set(true),
        complete: () => {
          this.waiting.set(false);
          this.goBack(["reservations"])
        },
      })
  }

}
