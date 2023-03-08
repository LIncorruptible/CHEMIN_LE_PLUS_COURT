package objects;

import java.util.ArrayList;
import java.util.List;

public class Plateau {
    int[][] matrice;
    public Graphe graphe;

    int[][] matrice_interets;
    int[][] matrice_strategies;
    int[] matrice_obligations;

    public int score;

    public Plateau(int[][] matrice, int[][] matrice_interets, int[][] matrice_strategies, int[] matrice_obligations, int score) {
        this.matrice = matrice;
        this.graphe = new Graphe();
        this.matrice_interets = matrice_interets;
        this.matrice_strategies = matrice_strategies;
        this.matrice_obligations = matrice_obligations;
        this.score = score;
    }

    public List<Sommet> voisinnage(Sommet sommet) {
        List<Sommet> voisins = new ArrayList<>();

        /**
         * On récupère les coordonnées du sommet
         */
        String[] coord = sommet.nom.split("_");
        int x = Integer.parseInt(coord[1]);
        int y = Integer.parseInt(coord[2]);

        /**
         * On récupère les voisins
         * Gauche, Droite, Haut, Bas
         * et en diagonale
         */

        // Gauche
        if(x > 0) {
            voisins.add(this.graphe.sommets.get(this.graphe.rangSommet(new Sommet("s_" + (x - 1) + "_" + y))));
        }

        // Droite
        if(x < this.matrice.length - 1) {
            voisins.add(this.graphe.sommets.get(this.graphe.rangSommet(new Sommet("s_" + (x + 1) + "_" + y))));
        }

        // Haut
        if(y > 0) {
            voisins.add(this.graphe.sommets.get(this.graphe.rangSommet(new Sommet("s_" + x + "_" + (y - 1)))));
        }

        // Bas
        if(y < this.matrice[x].length - 1) {
            voisins.add(this.graphe.sommets.get(this.graphe.rangSommet(new Sommet("s_" + x + "_" + (y + 1)))));
        }

        /*
        // Diagonale Haut Gauche
        if(x > 0 && y > 0) {
            voisins.add(this.graphe.sommets.get(this.graphe.rangSommet(new Sommet("s_" + (x - 1) + "_" + (y - 1)))));
        }

        // Diagonale Haut Droite
        if(x < this.matrice.length - 1 && y > 0) {
            voisins.add(this.graphe.sommets.get(this.graphe.rangSommet(new Sommet("s_" + (x + 1) + "_" + (y - 1)))));
        }

        // Diagonale Bas Gauche
        if(x > 0 && y < this.matrice[x].length - 1) {
            voisins.add(this.graphe.sommets.get(this.graphe.rangSommet(new Sommet("s_" + (x - 1) + "_" + (y + 1)))));
        }

        // Diagonale Bas Droite
        if(x < this.matrice.length - 1 && y < this.matrice[x].length - 1) {
            voisins.add(this.graphe.sommets.get(this.graphe.rangSommet(new Sommet("s_" + (x + 1) + "_" + (y + 1)))));
        }
        */
        return voisins;
    }

    public void matriceVersGraphe() {

        /**
         * Création des sommets
         */
        for(int i = 0; i < matrice.length; i++) {
            for(int j = 0; j < matrice[i].length; j++) {
                Sommet sommet = new Sommet("s_" + i + "_" + j);
                graphe.sommets.add(sommet);
            }
        }

        /**
         * Création des arcs
         * On parcourt tous les sommets et on récupère leurs voisins
         * On créé un arc entre le sommet et son voisin de poids égal à la valeur de la matrice
         */
        for(Sommet sommet : graphe.sommets) {
            List<Sommet> voisins = voisinnage(sommet);

            for (Sommet voisin : voisins) {

                Arc arc = null;

                int poids = matrice[Integer.parseInt(voisin.nom.split("_")[2])][Integer.parseInt(voisin.nom.split("_")[1])];

                if(poids >= 0) {
                    arc = new Arc(sommet, voisin, poids);
                } else {
                    arc = new Arc(sommet, voisin, Integer.MAX_VALUE);
                }

                sommet.arcs.add(arc);
            }
        }
    }

    public List<Sommet> recupererLesSommetsStrategiques() {
        List<Sommet> sommets_strategiques = new ArrayList<>();

        for(int[] ligne : matrice_strategies) {
            Sommet sommet_a_recherche = new Sommet("s_" + ligne[0] + "_" + ligne[1]);

            for(Sommet sommet : graphe.sommets) {
                if(sommet_a_recherche.nom.equals(sommet.nom)) {
                    sommets_strategiques.add(sommet);
                }
            }
        }

        return sommets_strategiques;
    }

    public List<Sommet> recupererLesSommetsInterets() {
        List<Sommet> sommets_interets = new ArrayList<>();

        for(int[] ligne : matrice_interets) {
            Sommet sommet_a_recherche = new Sommet("s_" + ligne[0] + "_" + ligne[1]);

            for(Sommet sommet : graphe.sommets) {
                if(sommet_a_recherche.nom.equals(sommet.nom)) {
                    sommets_interets.add(sommet);
                }
            }
        }

        return sommets_interets;
    }

    public List<Sommet> recupererLesSommetsObligatoires() {
        List<Sommet> sommets_obligatoires = new ArrayList<>();
        List<Sommet> sommets_strategiques = new ArrayList<>(recupererLesSommetsStrategiques());

        for(int indice : matrice_obligations) {
            sommets_obligatoires.add(sommets_strategiques.get(indice));
        }

        return sommets_obligatoires;
    }

    public int calculerScore(List<Sommet> trajectoire) {
        int score_tmp = 0;

        List<Sommet> sommets_strategiques = new ArrayList<>(recupererLesSommetsStrategiques());
        List<Sommet> sommets_interets = new ArrayList<>(recupererLesSommetsInterets());

        // Les sommets stratégiques rapportent 30 points
        // Les sommets d'intérêts rapportent x points
        for(Sommet sommet : trajectoire) {
            for(Sommet sommet_strategique : sommets_strategiques) {
                if(sommet.nom.equals(sommet_strategique.nom)) {
                    score_tmp += 30;
                    break;
                }
            }

            for(Sommet sommet_interet : sommets_interets) {
                if(sommet.nom.equals(sommet_interet.nom)) {

                    int x = 0;

                    for(int i = 0; i < matrice_interets.length; i++) {
                        if(sommet.nom.equals("s_" + matrice_interets[i][0] + "_" + matrice_interets[i][1])) {
                            x = matrice_interets[i][2];
                            break;
                        }
                    }

                    score_tmp += x;
                    break;
                }
            }
        }

        // On retire les points des arc parcourus
        for (Sommet sommet : trajectoire) {
            // On récupère les coordonnées du sommet en décomposant le nom
            String[] coordonnees = sommet.nom.split("_");
            int x = Integer.parseInt(coordonnees[1]);
            int y = Integer.parseInt(coordonnees[2]);

            score_tmp -= matrice[x][y];
        }

        return score_tmp;
    }

    public Graphe minimiseGraphe(Sommet depart) {
        List<Sommet> sommets = new ArrayList<>();

        // On ajoute le départ
        sommets.add(new Sommet(depart.nom));

        // On ajoute les sommets stratégiques
        for(Sommet sommet : recupererLesSommetsStrategiques()) {
            sommets.add(new Sommet(sommet.nom));
        }

        // On ajoute les sommets d'intérêts
        for(Sommet sommet : recupererLesSommetsInterets()) {
            sommets.add(new Sommet(sommet.nom));
        }

        // On parcourt chaque sommet de référence et on calcule les arcs correspondants
        for(Sommet sommet_de_reference : sommets) {
            for(Sommet sommet : sommets) {
                if(!sommet_de_reference.nom.equals(sommet.nom)) {

                    Sommet sommet_depart = null;

                    for (Sommet sommet_tmp : graphe.sommets) {
                        if(sommet_tmp.nom.equals(sommet_de_reference.nom)) {
                            sommet_depart = sommet_tmp;
                            break;
                        }
                    }

                    Sommet sommet_arrivee = null;

                    for (Sommet sommet_tmp : graphe.sommets) {
                        if(sommet_tmp.nom.equals(sommet.nom)) {
                            sommet_arrivee = sommet_tmp;
                            break;
                        }
                    }

                    List<Sommet> chemin = graphe.Dijkstra(sommet_depart, sommet_arrivee);

                    int poids = calculerScore(chemin);

                    sommet_de_reference.arcs.add(new Arc(sommet_de_reference, sommet, poids));
                }
            }
        }

        return new Graphe(sommets);
    }

    public List<Sommet> algorithme(int x_depart, int y_depart) {

        Sommet sommet_depart = new Sommet("s_" + x_depart + "_" + y_depart);
        sommet_depart = graphe.sommets.get(graphe.rangSommet(sommet_depart));

        Graphe graphe_minimise = minimiseGraphe(sommet_depart);

        List<Sommet> sommets_obligatoires_a_parcourir = new ArrayList<>();

        for(Sommet sommet : recupererLesSommetsObligatoires()) {
            Sommet sommet_a_ajouter = new Sommet(graphe_minimise.sommets.get(graphe_minimise.rangSommet(sommet)));

            if(!sommets_obligatoires_a_parcourir.contains(sommet_a_ajouter)) {
                sommets_obligatoires_a_parcourir.add(sommet_a_ajouter);
            }
        }

        List<Sommet> chemin = new ArrayList<>();

        Sommet sommet_en_cours = graphe_minimise.sommets.get(graphe_minimise.rangSommet(sommet_depart));
        chemin.add(sommet_en_cours);
        Sommet sommet_precedent = sommet_en_cours;

        while(!sommets_obligatoires_a_parcourir.isEmpty()) {

            Arc arc_de_poids_max = new Arc(sommet_en_cours, sommet_en_cours, -Integer.MAX_VALUE);

            for(Arc arc : sommet_en_cours.arcs) {
                if(arc.poids > arc_de_poids_max.poids) {
                    arc_de_poids_max = arc;
                }
            }

            Sommet sommet_a_ajouter = new Sommet(graphe_minimise.sommets.get(graphe_minimise.rangSommet(arc_de_poids_max.arrive)));

            sommet_precedent = sommet_en_cours;
            sommet_en_cours = sommet_a_ajouter;
            chemin.add(sommet_en_cours);

            score += arc_de_poids_max.poids;
            System.out.println("Ajout de " + arc_de_poids_max.poids + " au score : " + score + " | " + arc_de_poids_max.depart.nom + " -> " + arc_de_poids_max.arrive.nom);

            for(int i = 0; i < sommets_obligatoires_a_parcourir.size(); i++) {
                if(sommets_obligatoires_a_parcourir.get(i).nom.equals(sommet_en_cours.nom)) {
                    sommets_obligatoires_a_parcourir.remove(i);
                    break;
                }
            }

            for(int i = 0; i < graphe_minimise.sommets.size(); i++) {
                for(int j = 0; j < graphe_minimise.sommets.get(i).arcs.size(); j++) {
                    if(graphe_minimise.sommets.get(i).arcs.get(j).arrive.nom.equals(sommet_precedent.nom)) {
                        graphe_minimise.sommets.get(i).arcs.remove(j);
                    }
                }
            }

            for(int i = 0; i < graphe_minimise.sommets.size(); i++) {
                if(graphe_minimise.sommets.get(i).arcs.isEmpty()) {
                    graphe_minimise.sommets.remove(i);
                }
            }
        }

        System.out.println("Score : " + score);

        return chemin;
    }
}
