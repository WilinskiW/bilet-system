import { inject, Injectable } from '@angular/core';
import { FormGroup } from '@angular/forms';
import { FormService } from '../../form.service';
import { DataService } from '../../data.service';

@Injectable({ providedIn: "root"} )
export abstract class DataForm {
  abstract form: FormGroup;
  protected formService = inject(FormService);
  protected dataService = inject(DataService);

  isInvalid(key: string): boolean {
    return this.formService.isInvalid(this.form, key);
  }

  abstract submit(): void;

  goBack(section: string[]) {
    this.dataService.navigateTo(...section);
  }

  markAllFieldsAsTouched(){
    if (this.form.invalid) {
      this.formService.markAllControlsAsTouched(this.form);
      return;
    }
  }
}
