import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { DataForm } from '../../shared/abstract/data-form.service';

@Injectable({
  providedIn: 'root'
})
export abstract class PassengerFormService extends DataForm{
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
    })
  })

  submit(): void {
    this.markAllFieldsAsTouched();

    const passenger = {
      firstname: this.form.controls.firstname.value,
      lastname: this.form.controls.lastname.value,
      email: this.form.controls.email.value,
      phone: this.form.controls.phone.value,
    }

    this.makeHttpRequest(passenger);
  }

  abstract makeHttpRequest(passenger: {
    firstname: string | null;
    lastname: string | null;
    email: string | null;
    phone: string | null;
  }) : void;
}
