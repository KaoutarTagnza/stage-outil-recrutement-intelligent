import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { EntretienResponse } from '../../models/entretien.model';

@Injectable({ providedIn: 'root' })
export class EntretienService {
  private apiUrl = `${environment.apiUrl}/recruteur/candidatures`;

  constructor(private http: HttpClient) {}

  genererQuestions(candidatureId: number): Observable<string[]> {
    return this.http.get<string[]>(`${this.apiUrl}/${candidatureId}/questions`);
  }

  planifier(candidatureId: number, dateEntretien: string): Observable<EntretienResponse> {
    return this.http.post<EntretienResponse>(`${this.apiUrl}/${candidatureId}/entretien`, { dateEntretien });
  }
}