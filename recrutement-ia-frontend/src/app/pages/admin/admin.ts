import { Component, OnInit, signal } from '@angular/core';
import { CommonModule, DatePipe } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { AdminService } from '../../core/services/admin.service';
import { ToastService } from '../../core/services/toast.service';
import { UtilisateurResponse, LogActionResponse, StatistiquesResponse } from '../../models/admin.model';

@Component({
  selector: 'app-admin',
  standalone: true,
  imports: [CommonModule, DatePipe, FormsModule],
  templateUrl: './admin.html',
  styleUrl: './admin.css'
})
export class Admin implements OnInit {
  ongletActif = signal<'utilisateurs' | 'logs' | 'statistiques'>('statistiques');

  utilisateurs = signal<UtilisateurResponse[]>([]);
  logs = signal<LogActionResponse[]>([]);
  statistiques = signal<StatistiquesResponse | null>(null);
  chargement = signal(true);

  constructor(
    private adminService: AdminService,
    private toast: ToastService
  ) {}

  ngOnInit(): void {
    this.chargerStatistiques();
  }

  changerOnglet(onglet: 'utilisateurs' | 'logs' | 'statistiques'): void {
    this.ongletActif.set(onglet);
    if (onglet === 'utilisateurs' && this.utilisateurs().length === 0) this.chargerUtilisateurs();
    if (onglet === 'logs' && this.logs().length === 0) this.chargerLogs();
    if (onglet === 'statistiques' && !this.statistiques()) this.chargerStatistiques();
  }

  chargerUtilisateurs(): void {
    this.chargement.set(true);
    this.adminService.listerUtilisateurs().subscribe({
      next: (data) => { this.utilisateurs.set(data); this.chargement.set(false); },
      error: () => this.chargement.set(false)
    });
  }

  chargerLogs(): void {
    this.chargement.set(true);
    this.adminService.listerLogs().subscribe({
      next: (data) => { this.logs.set(data); this.chargement.set(false); },
      error: () => this.chargement.set(false)
    });
  }

  chargerStatistiques(): void {
    this.chargement.set(true);
    this.adminService.obtenirStatistiques().subscribe({
      next: (data) => { this.statistiques.set(data); this.chargement.set(false); },
      error: () => this.chargement.set(false)
    });
  }

  basculerStatut(u: UtilisateurResponse): void {
    this.adminService.changerStatut(u.id, !u.actif).subscribe({
      next: () => {
        this.toast.success(`Compte ${!u.actif ? 'activé' : 'désactivé'}`);
        this.chargerUtilisateurs();
      }
    });
  }

  changerRole(u: UtilisateurResponse, nouveauRole: string): void {
    this.adminService.changerRole(u.id, nouveauRole).subscribe({
      next: () => {
        this.toast.success('Rôle mis à jour');
        this.chargerUtilisateurs();
      }
    });
  }
}