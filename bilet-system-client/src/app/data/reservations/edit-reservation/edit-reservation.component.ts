import { Component, input, OnInit, signal } from '@angular/core';
import { ReservationModel } from '../reservation.model';
import { ReservationFormService } from '../reservation-form.service';
import { ConnectionErrorComponent } from '../../../shared/components/connection-error/connection-error.component';
import { NotFoundComponent } from '../../../shared/components/not-found/not-found.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

@Component({
  selector: 'app-edit-reservation',
  imports: [
    ConnectionErrorComponent,
    NotFoundComponent,
    FormsModule,
    ReactiveFormsModule
  ],
  templateUrl: './edit-reservation.component.html',
})
export class EditReservationComponent extends ReservationFormService implements OnInit{
  reservation: ReservationModel | undefined;
  id = input.required<string>();
  isFetching = signal(true);
  errorCode = signal(200);


  ngOnInit() {
    this.dataService.fetchData<ReservationModel>(`http://localhost:8080/api/reservations/${this.id()}`)
      .subscribe({
        next: data => {
          this.reservation = data;

          this.form.patchValue({
            reservationNumber: data.reservationNumber,
            seatNumber: data.seatNumber,
            hasDeparted: data.hasDeparted,
          });
        },
        complete: () => {
          this.isFetching.set(false);
        },
        error: (err) => {
          this.isFetching.set(false);
          if(err.status === 0) this.errorCode.set(0);
          else if(err.status === 404) this.errorCode.set(404)
        }
      });
  }

  override submit() {
    if (this.form.invalid) {
      this.formService.markAllControlsAsTouched(this.form);
      return;
    }

    const reservation = {
      id: this.reservation?.id,
      reservationNumber: this.form.controls.reservationNumber.value!,
      flight: this.reservation?.flight,
      seatNumber: this.form.controls.seatNumber.value!,
      passenger: this.reservation?.passenger,
      hasDeparted: this.form.controls.hasDeparted.value!,
    }

    this.dataService.putData(reservation, `http://localhost:8080/api/reservations/${this.id()}`)
      .subscribe({
        complete: () => {
          this.goBack(["reservations"])
        },
      })
  }
}
