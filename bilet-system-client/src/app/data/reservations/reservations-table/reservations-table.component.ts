import { Component } from '@angular/core';
import { ContentTableComponent } from '../../../shared/components/content-table/content-table.component';
import { DataTableService } from '../../../shared/abstract/data-table.service';
import { ReservationModel } from '../reservation.model';
import { RouterLink } from '@angular/router';
import {
  ConnectionErrorComponent
} from '../../../shared/components/connection-error/connection-error.component';
import { ZeroDataComponent } from '../../../shared/components/zero-data/zero-data.component';

@Component({
  selector: 'app-reservations-table',
  templateUrl: './reservations-table.component.html',
  imports: [
    ContentTableComponent,
    RouterLink,
    ConnectionErrorComponent,
    ZeroDataComponent
  ]
})
export class ReservationsTableComponent extends DataTableService<ReservationModel>{
  protected url = "https://bilet-system.onrender.com/api/reservations"
}
