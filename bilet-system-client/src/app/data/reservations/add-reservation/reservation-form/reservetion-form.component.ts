import { Component, input } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { FlightModel } from '../../../flights/flightModel';
import { PassengerModel } from '../../../passengers/passenger.model';
import { ReservationModel } from '../../reservation.model';
import { ReservationFormService } from '../../reservation-form.service';

@Component({
  selector: 'app-reservation-form',
  imports: [
    ReactiveFormsModule
  ],
  templateUrl: './reservetion-form.component.html',
})
export class ReservationForm extends ReservationFormService{
  flight = input.required<FlightModel | undefined>();
  passenger = input.required<PassengerModel | null>();

  override submit(): void {
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

    this.dataService.sendData(reservation, `https://bilet-system.onrender.com/api/reservations`)
      .subscribe({
        complete: () => {
          this.goBack(["reservations"])
        },
        error: (err) => {
          this.errorMessage.set(err.error.message);
        }
      })
  }
}
