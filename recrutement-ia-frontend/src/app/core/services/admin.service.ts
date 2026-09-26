import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { UtilisateurResponse, LogActionResponse, StatistiquesResponse } from '../../models/admin.model';

@Injectable({ providedIn: 'root' })
export class AdminService {
  private apiUrl = `${environment.apiUrl}/admin`;

  constructor(private http: HttpClient) {}

  listerUtilisateurs(): Observable<UtilisateurResponse[]> {
    return this.http.get<UtilisateurResponse[]>(`${this.apiUrl}/utilisateurs`);
  }

  changerStatut(id: number, actif: boolean): Observable<UtilisateurResponse> {
    return this.http.put<UtilisateurResponse>(`${this.apiUrl}/utilisateurs/${id}/statut`, { actif });
  }

  changerRole(id: number, role: string): Observable<UtilisateurResponse> {
    return this.http.put<UtilisateurResponse>(`${this.apiUrl}/utilisateurs/${id}/role`, { role });
  }

  listerLogs(): Observable<LogActionResponse[]> {
    return this.http.get<LogActionResponse[]>(`${this.apiUrl}/logs`);
  }

  obtenirStatistiques(): Observable<StatistiquesResponse> {
    return this.http.get<StatistiquesResponse>(`${this.apiUrl}/statistiques`);
  }
}