import { inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { catchError, Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class DataService {
  private httpClient = inject(HttpClient);
  router = inject(Router);

  fetchData<T>(url: string): Observable<T> {
    return this.httpClient.get<T>(url).pipe(
      catchError((error) => {
        throw error;
      })
    )
  }

  sendData<T>(body: object, url: string): Observable<T> {
    return this.httpClient.post<T>(url, body).pipe(
      catchError((error) => {
        throw error;
      })
    )
  }

  putData<T>(body: object, url: string): Observable<T> {
    return this.httpClient.put<T>(url, body).pipe(
      catchError((error) => {
        throw error;
      })
    )
  }

  deleteData(url: string): Observable<void> {
    const apiUrl = url.replace("http://localhost:4200/wiki", "http://localhost:8081/api");
    return this.httpClient.delete<void>(apiUrl).pipe(
      catchError((error) => {
        throw error;
      })
    )
  }

  navigateTo(...args: string[]) {
    this.router.navigate([...args]);
  }
}
