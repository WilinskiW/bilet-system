import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { DataForm } from '../../shared/abstract/data-form.service';

@Injectable({
  providedIn: 'root'
})
export abstract class FlightFormService extends DataForm{
  form = new FormGroup({
    departurePlace: new FormControl("", {
      validators: [Validators.required, Validators.minLength(3)]
    }),
    arrivalPlace: new FormControl("", {
      validators: [Validators.required, Validators.minLength(3)]
    }),
    duration: new FormControl(0, {
      validators: [Validators.required, Validators.min(1)]
    }),
    flightNumber: new FormControl("", {
      validators: [Validators.required]
    }),
    departureTime: new FormControl("", {
      validators: [Validators.required]
    }),
    roundTrip: new FormControl(false)
  });

  submit() {
    if (this.form.invalid) {
      this.formService.markAllControlsAsTouched(this.form);
      return;
    }

    const flight = {
      departurePlace: this.form.controls.departurePlace.value,
      arrivalPlace: this.form.controls.arrivalPlace.value,
      duration: this.form.controls.duration.value,
      flightNumber: this.form.controls.flightNumber.value,
      departureTime: this.form.controls.departureTime.value,
      roundTrip: this.form.controls.roundTrip.value
    }

    this.makeHttpRequest(flight);
  }

  abstract makeHttpRequest(flight: {
    departurePlace: string | null;
    arrivalPlace: string | null;
    duration: number | null;
    flightNumber: string | null;
    departureTime: string | null;
    roundTrip: boolean | null
  }) : void;

}
