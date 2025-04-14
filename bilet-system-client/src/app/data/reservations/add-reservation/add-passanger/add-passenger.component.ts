import { Component, output } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { PassengerFormService } from '../../../passengers/passengerForm.service';
import { PassengerModel } from '../../../passengers/passenger.model';

@Component({
  selector: 'app-add-passanger',
  imports: [
    FormsModule,
    ReactiveFormsModule
  ],
  templateUrl: './add-passenger.component.html',
})
export class AddPassengerComponent extends PassengerFormService{
  passengerValid = output<PassengerModel | null>();

  makeHttpRequest(passenger: {
    firstname: string,
    lastname: string,
    email: string,
    phone: string
  }): void {
    this.dataService.sendData(passenger, `http://localhost:8080/api/passengers/verify`)
      .subscribe({
        complete: () => this.passengerValid.emit(passenger),
        error: () => this.passengerValid.emit(null)
      })
  }
}
