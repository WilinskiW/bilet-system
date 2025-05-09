import { Component } from '@angular/core';
import { ContentTableComponent } from '../../../shared/components/content-table/content-table.component';
import { DataTableService } from '../../../shared/abstract/data-table.service';
import { PassengerModel } from '../passenger.model';
import { RouterLink } from '@angular/router';
import {
  ConnectionErrorComponent
} from '../../../shared/components/connection-error/connection-error.component';
import { ZeroDataComponent } from '../../../shared/components/zero-data/zero-data.component';

@Component({
  selector: 'app-passengers',
  imports: [
    ContentTableComponent,
    RouterLink,
    ConnectionErrorComponent,
    ZeroDataComponent
  ],
  templateUrl: './passengers-table.component.html',
})
export class PassengersTableComponent extends DataTableService<PassengerModel>{
  protected url = "http://localhost:8080/api/passengers";
}
