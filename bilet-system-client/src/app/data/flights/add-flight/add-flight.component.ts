import { Component } from '@angular/core';
import { DataForm } from '../../../shared/abstract/data-form.service';
import { AbstractControl, FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { FlightModel } from '../FlightModel';

function mustBeInTheFutureOrPresent(dateControl: AbstractControl) {
  const controlDate = Date.parse(dateControl.value);
  if (controlDate >= Date.now()) {
    return null;
  }
  return { isInThePast: true };
}

@Component({
  selector: 'app-add-flight',
  imports: [
    ReactiveFormsModule
  ],
  templateUrl: './add-flight.component.html',
})
export class AddFlightComponent extends DataForm{
  form = new FormGroup({
    departurePlace: new FormControl("", {
      validators: [Validators.required, Validators.minLength(3)]
    }),
    arrivalPlace: new FormControl("", {
      validators: [Validators.required, Validators.minLength(3)]
    }),
    duration: new FormControl("", {
      validators: [Validators.required, Validators.min(1)]
    }),
    flightNumber: new FormControl("", {
      validators: [Validators.required]
    }),
    departureTime: new FormControl("", {
      validators: [Validators.required, mustBeInTheFutureOrPresent]
    }),
    roundTrip: new FormControl(false)
  });

  submit(): void {
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

    this.dataService.sendData<FlightModel>(flight, "http://localhost:8080/api/flights")
      .subscribe({
        complete: () => this.goBack(["flights"]),
        error: err => console.error("Błąd podczas dodawania lotu", err)
      });
  }
}
