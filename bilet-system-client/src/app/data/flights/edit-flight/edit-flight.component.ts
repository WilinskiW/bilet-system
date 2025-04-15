import { Component, input, OnInit, signal } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { FlightModel } from '../flightModel';
import { FlightFormService } from '../flight-form.service';
import { ConnectionErrorComponent } from '../../../shared/components/connection-error/connection-error.component';
import { NotFoundComponent } from '../../../shared/components/not-found/not-found.component';

@Component({
  selector: 'app-edit-flight',
  imports: [
    FormsModule,
    ReactiveFormsModule,
    ConnectionErrorComponent,
    NotFoundComponent
  ],
  templateUrl: './edit-flight.component.html',
})
export class EditFlightComponent extends FlightFormService implements OnInit {
  flight: FlightModel | undefined;
  id = input.required<string>();
  isFetching = signal(true);
  errorCode = signal(200);

  ngOnInit() {
    this.dataService.fetchData<FlightModel>(`http://localhost:8080/api/flights/${this.id()}`)
      .subscribe({
        next: data => {
          this.flight = data;
          this.form.patchValue({
            departurePlace: data.departurePlace,
            arrivalPlace: data.arrivalPlace,
            duration: data.duration,
            flightNumber: data.flightNumber,
            departureTime: new Date(data.departureTime).toISOString().slice(0, 16),
            roundTrip: data.roundTrip
          });
        },
        complete: () => {
          this.isFetching.set(false);
        }
        ,
        error: (err) => {
          this.isFetching.set(false);
          if(err.status === 0) this.errorCode.set(0);
          else if(err.status === 404) this.errorCode.set(404)
        }
      });
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
        error: err => {
          console.log(err.error.message)
          this.errorMessage.set(err.error.message)
        }
      })
  }
}
