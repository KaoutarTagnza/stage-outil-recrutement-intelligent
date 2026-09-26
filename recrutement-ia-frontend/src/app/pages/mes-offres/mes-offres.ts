import { Component, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { OffreService } from '../../core/services/offre.service';
import { ToastService } from '../../core/services/toast.service';
import { OffreResponse } from '../../models/offre.model';

@Component({
  selector: 'app-mes-offres',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './mes-offres.html',
  styleUrl: './mes-offres.css'
})
export class MesOffres implements OnInit {
  offres = signal<OffreResponse[]>([]);
  chargement = signal(true);

  constructor(
    private offreService: OffreService,
    private toast: ToastService
  ) {}

  ngOnInit(): void {
    this.chargerOffres();
  }

  chargerOffres(): void {
    this.chargement.set(true);
    this.offreService.listerMesOffres().subscribe({
      next: (data) => {
        this.offres.set(data);
        this.chargement.set(false);
      },
      error: () => this.chargement.set(false)
    });
  }

  publier(id: number): void {
    this.offreService.publier(id).subscribe({
      next: () => {
        this.toast.success('Offre publiée avec succès');
        this.chargerOffres();
      }
    });
  }
}