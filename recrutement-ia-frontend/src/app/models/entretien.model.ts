
export interface EntretienResponse {
  id: number;
  dateEntretien: string;
  questionsGenerees: string;
  notes: string | null;
  candidatureId: number;
  candidatNom: string;
  offreTitre: string;
}
