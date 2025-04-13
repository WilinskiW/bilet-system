import { Component, input, OnInit } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { FlightModel } from '../flightModel';
import { FlightFormService } from '../flightForm.service';

@Component({
  selector: 'app-edit-flight',
  imports: [
    FormsModule,
    ReactiveFormsModule
  ],
  templateUrl: './edit-flight.component.html',
})
export class EditFlightComponent extends FlightFormService implements OnInit{
  flight: FlightModel | undefined;
  id = input.required<string>();


  ngOnInit() {
    this.dataService.fetchData<FlightModel>(`http://localhost:8080/api/flights/${this.id()}`)
      .subscribe(data => {
        this.flight = data;

        this.form.patchValue({
          departurePlace: data.departurePlace,
          arrivalPlace: data.arrivalPlace,
          duration: data.duration,
          flightNumber: data.flightNumber,
          departureTime: new Date(data.departureTime).toISOString().slice(0, 16),
          roundTrip: data.roundTrip
        })
      })
  }

  makeHttpRequest(flight: {
    departurePlace: string | null;
    arrivalPlace: string | null;
    duration: number | null;
    flightNumber: string | null;
    departureTime: string | null;
    roundTrip: boolean | null
  }): void {
    this.dataService.putData(flight, `http://localhost:8080/api/flights/${this.id()}`)
      .subscribe({
        complete: () => this.goBack(["flights"]),
      })
  }
}
