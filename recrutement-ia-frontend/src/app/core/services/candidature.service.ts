import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { CandidatureResponse } from '../../models/candidature.model';
import { DecisionFinaleRequest } from '../../models/evaluation.model';

@Injectable({ providedIn: 'root' })
export class CandidatureService {
  private apiUrl = `${environment.apiUrl}/candidat/candidatures`;
  private apiUrlRecruteur = `${environment.apiUrl}/recruteur`;

  constructor(private http: HttpClient) {}

  postuler(offreId: number): Observable<CandidatureResponse> {
    return this.http.post<CandidatureResponse>(this.apiUrl, { offreId });
  }

  deposerCv(candidatureId: number, fichier: File): Observable<CandidatureResponse> {
    const formData = new FormData();
    formData.append('fichier', fichier);
    return this.http.post<CandidatureResponse>(`${this.apiUrl}/${candidatureId}/cv`, formData);
  }

  mesCandidatures(): Observable<CandidatureResponse[]> {
    return this.http.get<CandidatureResponse[]>(this.apiUrl);
  }

  listerCandidaturesOffre(offreId: number): Observable<CandidatureResponse[]> {
    return this.http.get<CandidatureResponse[]>(`${this.apiUrlRecruteur}/offres/${offreId}/candidatures`);
  }

  deciderFinal(candidatureId: number, data: DecisionFinaleRequest): Observable<CandidatureResponse> {
    return this.http.put<CandidatureResponse>(`${this.apiUrlRecruteur}/candidatures/${candidatureId}/decision`, data);
  }
}