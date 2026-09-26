import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { EvaluationRequest, EvaluationResponse } from '../../models/evaluation.model';

@Injectable({ providedIn: 'root' })
export class EvaluationService {
  private apiUrl = `${environment.apiUrl}/recruteur/entretiens`;

  constructor(private http: HttpClient) {}

  evaluer(entretienId: number, data: EvaluationRequest): Observable<EvaluationResponse> {
    return this.http.post<EvaluationResponse>(`${this.apiUrl}/${entretienId}/evaluation`, data);
  }
}