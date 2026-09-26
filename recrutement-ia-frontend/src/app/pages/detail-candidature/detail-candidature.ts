import { Component, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { EntretienService } from '../../core/services/entretien.service';
import { EvaluationService } from '../../core/services/evaluation.service';
import { CandidatureService } from '../../core/services/candidature.service';
import { ToastService } from '../../core/services/toast.service';

@Component({
  selector: 'app-detail-candidature',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './detail-candidature.html',
  styleUrl: './detail-candidature.css'
})
export class DetailCandidature implements OnInit {
  candidatureId!: number;

  questions = signal<string[]>([]);
  chargementQuestions = signal(false);

  dateEntretien = '';
  entretienId = signal<number | null>(null);
  chargementPlanification = signal(false);

  noteGlobale = 7;
  commentaire = '';
  decisionEvaluation: 'FAVORABLE' | 'DEFAVORABLE' | 'A_REVOIR' = 'FAVORABLE';
  chargementEvaluation = signal(false);
  evaluationFaite = signal(false);

  justification = '';
  chargementDecision = signal(false);
  messageDecision = signal('');

  constructor(
    private route: ActivatedRoute,
    private entretienService: EntretienService,
    private evaluationService: EvaluationService,
    private candidatureService: CandidatureService,
    private toast: ToastService
  ) {}

  ngOnInit(): void {
    this.candidatureId = Number(this.route.snapshot.paramMap.get('id'));
  }

  genererQuestions(): void {
    this.chargementQuestions.set(true);
    this.entretienService.genererQuestions(this.candidatureId).subscribe({
      next: (data) => {
        this.questions.set(data);
        this.chargementQuestions.set(false);
        this.toast.success('Questions générées avec succès');
      },
      error: () => this.chargementQuestions.set(false)
    });
  }

  planifierEntretien(): void {
    if (!this.dateEntretien) return;
    this.chargementPlanification.set(true);

    this.entretienService.planifier(this.candidatureId, this.dateEntretien).subscribe({
      next: (data) => {
        this.entretienId.set(data.id);
        this.chargementPlanification.set(false);
        this.toast.success('Entretien planifié avec succès');
      },
      error: () => this.chargementPlanification.set(false)
    });
  }

  soumettreEvaluation(): void {
    const id = this.entretienId();
    if (!id) return;

    this.chargementEvaluation.set(true);
    this.evaluationService.evaluer(id, {
      noteGlobale: this.noteGlobale,
      commentaire: this.commentaire,
      decision: this.decisionEvaluation
    }).subscribe({
      next: () => {
        this.chargementEvaluation.set(false);
        this.evaluationFaite.set(true);
        this.toast.success('Évaluation enregistrée avec succès');
      },
      error: () => this.chargementEvaluation.set(false)
    });
  }

  deciderFinal(accepter: boolean): void {
    if (!this.justification.trim()) {
      this.messageDecision.set('La justification est obligatoire.');
      return;
    }

    this.chargementDecision.set(true);
    this.candidatureService.deciderFinal(this.candidatureId, {
      accepter,
      justification: this.justification
    }).subscribe({
      next: () => {
        this.chargementDecision.set(false);
        this.messageDecision.set('Décision enregistrée : ' + (accepter ? 'ACCEPTÉE' : 'REJETÉE'));
        this.toast.success(accepter ? 'Candidature acceptée' : 'Candidature rejetée');
      },
      error: (err) => {
        this.chargementDecision.set(false);
        this.messageDecision.set(err.error?.erreur || 'Erreur lors de la décision');
      }
    });
  }
}