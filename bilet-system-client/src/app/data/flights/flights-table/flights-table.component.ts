import { Component } from '@angular/core';
import { ContentTableComponent } from '../../../shared/components/content-table/content-table.component';
import { DataTableService } from '../../../shared/abstract/data-table.service';
import { FlightModel } from '../flightModel';
import { RouterLink } from '@angular/router';
import {
  ConnectionErrorComponent
} from '../../../shared/components/content-table/connection-error/connection-error.component';
import { DatePipe } from '@angular/common';

@Component({
  selector: 'app-flights-table',
  imports: [
    ContentTableComponent,
    RouterLink,
    ConnectionErrorComponent,
    DatePipe
  ],
  templateUrl: './flights-table.component.html',
})
export class FlightsTableComponent extends DataTableService<FlightModel> {
  protected url = "http://localhost:8080/api/flights"
}
