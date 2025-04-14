import { Component, input, OnInit, signal } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { PassengerFormService } from '../passengerForm.service';
import { PassengerModel } from '../passenger.model';
import { ConnectionErrorComponent } from '../../../shared/components/connection-error/connection-error.component';
import { NotFoundComponent } from '../../../shared/components/not-found/not-found.component';

@Component({
  selector: 'app-edit-passenger',
  imports: [
    FormsModule,
    ReactiveFormsModule,
    ConnectionErrorComponent,
    NotFoundComponent
  ],
  templateUrl: './edit-passenger.component.html',
})
export class EditPassengerComponent extends PassengerFormService implements OnInit{
  passenger: PassengerModel | undefined;
  id = input.required<string>();
  isFetching = signal(true);
  errorCode = signal(200);


  ngOnInit(): void {
    this.dataService.fetchData<PassengerModel>(`http://localhost:8080/api/passengers/${this.id()}`)
      .subscribe({
        next: data => {
          this.passenger = data;

          this.form.patchValue({
            firstname: data.firstname,
            lastname: data.lastname,
            email: data.email,
            phone: data.phone,
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

  makeHttpRequest(passenger: {
    firstname: string | null;
    lastname: string | null;
    email: string | null;
    phone: string | null
  }): void {
    this.dataService.putData(passenger, `http://localhost:8080/api/passengers/${this.id()}`)
      .subscribe({
        complete: () => this.goBack(["passengers"]),
      })
  }

}
