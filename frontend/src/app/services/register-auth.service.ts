import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class RegisterAuthService {
  constructor(private http: HttpClient) { }

  registerAuth(token: string): Observable<any> {
    return this.http.get(
      `http://localhost:8090/auth/registration/confirm?token=${token}`
    );
  }
}
