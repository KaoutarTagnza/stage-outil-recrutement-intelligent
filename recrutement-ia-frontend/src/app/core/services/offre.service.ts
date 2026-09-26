import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { OffreRequest, OffreResponse } from '../../models/offre.model';

@Injectable({ providedIn: 'root' })
export class OffreService {
  private apiUrl = `${environment.apiUrl}/offres`;
  private apiUrlRecruteur = `${environment.apiUrl}/recruteur/offres`;

  constructor(private http: HttpClient) {}

  listerPubliees(): Observable<OffreResponse[]> {
    return this.http.get<OffreResponse[]>(`${this.apiUrl}/publiees`);
  }

  obtenir(id: number): Observable<OffreResponse> {
    return this.http.get<OffreResponse>(`${this.apiUrl}/${id}`);
  }

  creer(data: OffreRequest): Observable<OffreResponse> {
    return this.http.post<OffreResponse>(this.apiUrlRecruteur, data);
  }

  publier(id: number): Observable<OffreResponse> {
    return this.http.put<OffreResponse>(`${this.apiUrlRecruteur}/${id}/publier`, {});
  }

  listerMesOffres(): Observable<OffreResponse[]> {
    return this.http.get<OffreResponse[]>(this.apiUrlRecruteur);
  }
}