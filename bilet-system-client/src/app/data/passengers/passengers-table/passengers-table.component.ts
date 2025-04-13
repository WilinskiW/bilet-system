import { Component } from '@angular/core';
import { ContentTableComponent } from '../../../shared/components/content-table/content-table.component';
import { DataTableService } from '../../../shared/abstract/data-table.service';
import { PassengerModel } from '../passenger.model';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-passengers',
  imports: [
    ContentTableComponent,
    RouterLink
  ],
  templateUrl: './passengers-table.component.html',
})
export class PassengersTableComponent extends DataTableService<PassengerModel>{
  protected url = "http://localhost:8080/api/passengers";
}
