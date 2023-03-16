import objets.Carte;
import objets.Graphe;
import objets.Sommet;

import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void afficherChemin(List<Sommet> chemin) {

        for(int i = 0; i < chemin.size() - 1; i++) {
            System.out.print(chemin.get(i).getNom() + " -> ");
        }
        System.out.print(chemin.get(chemin.size() - 1).getNom());

        System.out.println();
    }

    public static void traduirePourLeCode(int[][] matrice_interets, int[][] matrice_strategies, int[] obligatoire, int[] depart) {
        for (int i = 0; i < matrice_strategies.length; i++) {
            matrice_strategies[i][0]--;
            matrice_strategies[i][1]--;
        }

        for (int i = 0; i < matrice_interets.length; i++) {
            matrice_interets[i][0] -= 1;
            matrice_interets[i][1] -= 1;
        }

        for (int i = 0; i < obligatoire.length; i++) {
            obligatoire[i] -= 1;
        }

        depart[0]--;
        depart[1]--;
    }

    public static void main(String[] args) {

        int I = -1;

        int[][] matrice_adjacences = {
                {4, 4, 5, 4, 4, 3, 2, 4, 2, 5, 1, 4, 3, 3, 1, 3, 2, 2, 2, 2},
                {3, 4, 1, 3, 1, 4, 3, 2, 4, 3, 4, 2, 1, 5, 1, 4, 5, 4, 4, 1},
                {2, 1, 3, 3, 1, 4, 5, 1, 3, 5, 3, 2, 2, 4, 3, 3, 4, 5, 1, 4},
                {2, 4, I, 3, 2, I, I, I, I, I, 3, 2, 3, 4, 4, 4, 5, 5, 4, 4},
                {2, 1, 4, 2, 4, I, 2, 3, 1, 5, 1, 1, 5, 3, 1, 1, 2, 5, 5, 4},
                {3, 1, 5, 2, 5, I, 3, 1, 5, 1, 1, 3, 3, 5, 2, 4, 2, 4, 1, 5},
                {5, 1, 4, 5, 5, I, 3, 3, 3, 3, 4, I, 1, 1, 3, I, 5, 4, 5, 4},
                {2, 2, 1, 4, 2, I, 3, 3, 1, 4, 5, I, 4, 1, 2, I, 1, 3, 2, 4},
                {2, 5, 4, 5, 2, I, 5, 2, 4, I, I, I, 5, 1, 3, I, I, I, I, 3},
                {1, 4, 1, 3, 1, I, I, I, 3, 5, 1, 4, 5, 4, 3, I, 5, 2, 3, 4},
                {3, 3, 3, 1, 3, 4, 1, I, 1, 3, 3, 4, 5, 1, 5, I, 5, 1, 3, 4},
                {2, 2, 3, 2, 1, 5, 3, I, 5, 5, 2, 5, 5, 5, 1, 5, 4, 2, 3, 4},
                {2, 5, 1, 3, 5, 1, 4, I, 3, 2, 5, 2, 4, 1, 2, 5, 1, 4, 2, 5},
                {1, 4, 4, 3, 3, 1, 2, I, 1, 2, 3, 1, I, I, I, 4, 3, 4, 4, 1},
                {1, 5, 3, 2, 3, 3, 3, I, 4, 5, 2, 3, 1, 2, 4, 2, 5, 3, 1, 2},
                {3, 1, 1, 4, I, I, I, I, 3, 4, 3, 3, 2, 3, 3, 3, 2, 4, 4, 1},
                {5, 2, 5, 2, 2, 2, 5, 4, 2, 2, 5, 2, 2, 4, 5, 1, 3, 4, 3, 5},
                {2, 1, 5, 2, 1, 4, 3, 5, 2, 1, 5, 5, 3, 3, 5, 4, 3, 3, 4, 1},
                {2, 1, 5, 2, 5, 1, 4, 3, 3, 1, 3, 5, 1, 1, 1, 1, 1, 4, 3, 1},
                {2, 2, 1, 5, 2, 3, 4, 3, 1, 2, 3, 1, 2, 3, 3, 2, 5, 5, 4, 1}
        };

        int[][] cas_strategiques = {
                {2, 2},
                {2, 12},
                {6, 13},
                {8, 6},
                {14, 17},
                {15, 3},
                {19, 2}
        };

        int[][] cases_interets = {
                {2, 7, 18},
                {2, 19, 12},
                {7, 2, 13},
                {11, 14, 9},
                {13, 5, 25},
                {18, 6, 8},
                {19, 17, 5}
        };

        int[] cases_obligatoires = {1, 3, 6, 7};

        int[] depart = {11, 19};

        Graphe g = new Graphe(matrice_adjacences);

        System.out.println(g);

        System.out.print("Traduction pour le code ...");
        traduirePourLeCode(cases_interets, cas_strategiques, cases_obligatoires, depart);
        System.out.print("[Terminé]\n");

        System.out.println("\n[Création de la carte]");
        Carte c = new Carte(depart, cases_obligatoires, matrice_adjacences, cases_interets, cas_strategiques);
        System.out.println("\t" + c.getGraphe());
        System.out.println("[Terminée]");

        List<Sommet> chemin_score_max = new ArrayList<>(c.traceMaxScoreChemin());
        List<Sommet> chemin_dist_min = new ArrayList<>(c.traceMinDistChemin());

        List<Sommet> chemin_strategique = new ArrayList<>(c.tracePersonalizedChemin(false, true));

        System.out.println("\n[Chemin obtenu de score max]");
        System.out.print("\t");
        afficherChemin(chemin_score_max);
        System.out.println("[Fin]");

        System.out.println("\n[Chemin obtenu de dist min]");
        System.out.print("\t");
        afficherChemin(chemin_dist_min);
        System.out.println("[Fin]");

        System.out.println("\n[Chemin stratégique]");
        System.out.print("\t");
        afficherChemin(chemin_strategique);
        System.out.println("[Fin]");
    }
}