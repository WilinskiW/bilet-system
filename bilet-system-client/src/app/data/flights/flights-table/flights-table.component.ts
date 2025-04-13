import { Component } from '@angular/core';
import { ContentTableComponent } from '../../../shared/components/content-table/content-table.component';
import { DataTableService } from '../../../shared/abstract/data-table.service';
import { FlightModel } from '../FlightModel';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-flights-table',
  imports: [
    ContentTableComponent,
    RouterLink
  ],
  templateUrl: './flights-table.component.html',
})
export class FlightsTableComponent extends DataTableService<FlightModel>{
    protected url = "http://localhost:8080/api/flights"
}
