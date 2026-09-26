export interface OffreResponse {
  id: number;
  titre: string;
  description: string;
  dateCreation: string;
  dateLimite: string | null;
  statut: 'BROUILLON' | 'PUBLIEE' | 'FERMEE';
  recruteurNom: string;
  competences: string[];
}

export interface OffreRequest {
  titre: string;
  description: string;
  dateLimite?: string;
  competences: string[];
}