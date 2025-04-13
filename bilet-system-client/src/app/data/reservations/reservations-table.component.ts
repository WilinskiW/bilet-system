import { Component } from '@angular/core';
import { ContentTableComponent } from '../../shared/components/content-table/content-table.component';
import { DataTableService } from '../../shared/abstract/data-table.service';
import { ReservationModel } from './reservation.model';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-reservations-table',
  templateUrl: './reservations-table.component.html',
  imports: [
    ContentTableComponent,
    RouterLink
  ]
})
export class ReservationsTableComponent extends DataTableService<ReservationModel>{
  protected url = "http://localhost:8080/api/reservations"
}
