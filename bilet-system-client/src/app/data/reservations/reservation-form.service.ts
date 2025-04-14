import { Injectable } from '@angular/core';
import { DataForm } from '../../shared/abstract/data-form.service';
import { FormControl, FormGroup, Validators } from '@angular/forms';

@Injectable({
  providedIn: 'root'
})
export abstract class ReservationFormService extends DataForm{
  form = new FormGroup({
    reservationNumber: new FormControl("", {
      validators: [Validators.required]
    }),
    seatNumber: new FormControl("", {
      validators: [Validators.required, Validators.pattern(new RegExp("^[1-9][0-9]*[A-F]$"))]
    }),
    hasDeparted: new FormControl(false)
  })

  submit(): void {
  }
}
