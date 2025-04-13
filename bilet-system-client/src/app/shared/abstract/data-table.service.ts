import { inject, Injectable, OnInit, signal } from '@angular/core';
import { DataService } from '../../data.service';

@Injectable({
  providedIn: 'root'
})
export abstract class DataTableService<T> implements OnInit {
  data: T[] = [];
  isFetching = signal(true);
  isError = signal(false);
  private dataService = inject(DataService);

  protected abstract url: string;

  ngOnInit() {
    this.dataService.fetchData<T[]>(this.url)
      .subscribe({
        next: (data) => {
          this.data = data;
          this.isFetching.set(false);
        },
        error: () => {
          this.isFetching.set(false);
          this.isError.set(true);
        }
      });
  }

  protected readonly String = String;
}
