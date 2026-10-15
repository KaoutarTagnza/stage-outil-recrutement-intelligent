
import { Component, OnInit, signal } from '@angular/core';
import { CommonModule, DatePipe } from '@angular/common';
import { CandidatureService } from '../../core/services/candidature.service';
import { ToastService } from '../../core/services/toast.service';
import { CandidatureResponse } from '../../models/candidature.model';
import { EntretienResponse } from '../../models/entretien.model';

@Component({
  selector: 'app-mes-candidatures',
  standalone: true,
  imports: [CommonModule, DatePipe],
  templateUrl: './mes-candidatures.html',
  styleUrl: './mes-candidatures.css'
})
export class MesCandidatures implements OnInit {
  candidatures = signal<CandidatureResponse[]>([]);
  entretiens = signal<EntretienResponse[]>([]);
  chargement = signal(true);

  fichierSelectionne: { [candidatureId: number]: File } = {};
  enCoursUpload = signal<number | null>(null);

  constructor(
    private candidatureService: CandidatureService,
    private toast: ToastService
  ) {}

  ngOnInit(): void {
    this.charger();
  }

  charger(): void {
    this.chargement.set(true);

    this.candidatureService.mesCandidatures().subscribe({
      next: (data) => {
        this.candidatures.set(data);
        this.chargerEntretiens();
      },
      error: () => {
        this.chargement.set(false);
        this.toast.error('Impossible de charger les candidatures.');
      }
    });
  }

  private chargerEntretiens(): void {
    this.candidatureService.mesEntretiens().subscribe({
      next: (data) => {
        this.entretiens.set(data);
        this.chargement.set(false);
      },
      error: () => {
        this.chargement.set(false);
        this.toast.error('Impossible de charger les entretiens.');
      }
    });
  }

  entretiensPourCandidature(
    candidatureId: number
  ): EntretienResponse[] {
    return this.entretiens().filter(
      entretien => entretien.candidatureId === candidatureId
    );
  }

  onFichierChange(event: Event, candidatureId: number): void {
    const input = event.target as HTMLInputElement;

    if (input.files && input.files.length > 0) {
      this.fichierSelectionne[candidatureId] = input.files[0];
    }
  }

  deposerCv(candidatureId: number): void {
    const fichier = this.fichierSelectionne[candidatureId];

    if (!fichier) {
      this.toast.error('Veuillez sélectionner un fichier PDF.');
      return;
    }

    this.enCoursUpload.set(candidatureId);

    this.candidatureService.deposerCv(candidatureId, fichier).subscribe({
      next: () => {
        this.enCoursUpload.set(null);
        this.toast.success('CV déposé et analysé avec succès');
        this.charger();
      },
      error: (err) => {
        this.enCoursUpload.set(null);

        const message =
          err.error?.erreur ||
          err.error?.message ||
          "L'analyse du CV a échoué. Vérifiez le contenu du PDF.";

        this.toast.error(message);
      }
    });
  }
}
