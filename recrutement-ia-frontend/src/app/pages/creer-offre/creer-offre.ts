import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { OffreService } from '../../core/services/offre.service';
import { ToastService } from '../../core/services/toast.service';

@Component({
  selector: 'app-creer-offre',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './creer-offre.html',
  styleUrl: './creer-offre.css'
})
export class CreerOffre {
  titre = '';
  description = '';
  competencesTexte = '';
  erreur = '';
  chargement = false;

  constructor(
    private offreService: OffreService,
    private router: Router,
    private toast: ToastService
  ) {}

  onSubmit(): void {
    this.erreur = '';
    this.chargement = true;

    const competences = this.competencesTexte
      .split(',')
      .map(c => c.trim())
      .filter(c => c.length > 0);

    this.offreService.creer({ titre: this.titre, description: this.description, competences }).subscribe({
      next: () => {
        this.chargement = false;
        this.toast.success('Offre créée avec succès');
        this.router.navigate(['/mes-offres']);
      },
      error: (err) => {
        this.chargement = false;
        this.erreur = err.error?.erreur || 'Erreur lors de la création de l\'offre';
      }
    });
  }
}