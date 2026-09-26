export interface UtilisateurResponse {
  id: number;
  nom: string;
  email: string;
  role: 'ADMIN' | 'RECRUTEUR' | 'CANDIDAT';
  actif: boolean;
  dateCreation: string;
}

export interface LogActionResponse {
  id: number;
  action: string;
  details: string | null;
  adresseIp: string | null;
  dateHeure: string;
  utilisateurNom: string;
}

export interface StatistiquesResponse {
  nbOffresTotal: number;
  nbOffresPubliees: number;
  nbCandidats: number;
  nbRecruteurs: number;
  nbCandidaturesTotal: number;
  nbCandidaturesAcceptees: number;
  nbCandidaturesRejetees: number;
  tauxAcceptationPourcent: number;
}