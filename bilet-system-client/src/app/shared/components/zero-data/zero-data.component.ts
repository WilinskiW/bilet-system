import { Component, input } from '@angular/core';

@Component({
  selector: 'app-zero-data',
  imports: [],
  templateUrl: './zero-data.component.html',
})
export class ZeroDataComponent {
  infoText = input.required<string>();
}
