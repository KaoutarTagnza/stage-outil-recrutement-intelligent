export interface EvaluationRequest {
  noteGlobale: number;
  commentaire: string;
  decision: 'FAVORABLE' | 'DEFAVORABLE' | 'A_REVOIR';
}

export interface EvaluationResponse {
  id: number;
  noteGlobale: number;
  commentaire: string;
  decision: string;
  recruteurNom: string;
  entretienId: number;
}

export interface DecisionFinaleRequest {
  accepter: boolean;
  justification: string;
}