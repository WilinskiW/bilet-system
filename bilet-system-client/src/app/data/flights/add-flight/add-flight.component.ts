import { Component } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { FlightModel } from '../flightModel';
import { FlightFormService } from '../flight-form.service';

@Component({
  selector: 'app-add-flight',
  imports: [
    ReactiveFormsModule
  ],
  templateUrl: './add-flight.component.html',
})
export class AddFlightComponent extends FlightFormService {
  makeHttpRequest(flight: {
    departurePlace: string | null;
    arrivalPlace: string | null;
    duration: number | null;
    flightNumber: string | null;
    departureTime: string | null;
    roundTrip: boolean | null
  }): void {
    this.dataService.sendData<FlightModel>(flight, "http://localhost:8080/api/flights")
      .subscribe({
        complete: () => this.goBack(["flights"]),
        error: err => {
          console.log(err.error.message)
          this.errorMessage.set(err.error.message)
        }
      });
  }
}
