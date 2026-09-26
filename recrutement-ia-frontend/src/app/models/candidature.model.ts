export interface CandidatureResponse {
  id: number;
  statut: 'EN_ATTENTE' | 'PRESELECTIONNEE' | 'ENTRETIEN_PLANIFIE' | 'ACCEPTEE' | 'REJETEE';
  dateDepot: string;
  offreTitre: string;
  offreId: number;
  candidatNom: string;
  cvFichierUrl: string | null;
  scoreMatching: number | null;
}