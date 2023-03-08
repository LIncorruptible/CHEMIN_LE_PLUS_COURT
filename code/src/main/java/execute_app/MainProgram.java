package execute_app;
import objects.Plateau;
import objects.Sommet;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainProgram {
    static int I = 1024;
    public static int[][] genererMatrice(int largeur, int hauteur, int min, int max) {
        int[][] matrice = new int[hauteur][largeur];
        for(int i = 0; i < hauteur; i++) {
            for(int j = 0; j < largeur; j++) {
                matrice[i][j] = (int) (Math.random() * (max - min + 1) + min);
            }
        }
        return matrice;
    }
    public static int[] genererCoordonnees(int largeur, int hauteur) {
        int[] coordonnees = new int[2];
        coordonnees[0] = (int) (Math.random() * (hauteur - 1));
        coordonnees[1] = (int) (Math.random() * (largeur - 1));
        return coordonnees;
    }

    public static void afficherTrajectoire(List<List<Sommet>> trajectoire) {
        for(List<Sommet> sous_trajectoire : trajectoire) {
            for(Sommet sommet : sous_trajectoire) {
                System.out.print(sommet.nom + " ");

                if(sommet != sous_trajectoire.get(sous_trajectoire.size() - 1)) {
                    System.out.print("-> ");
                }
            }
            System.out.println();
        }
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

    public static void retranscriptionPourLeCode(List<Sommet> chemin) {
        for (int i = 0; i < chemin.size(); i++) {
            String[] nom = chemin.get(i).nom.split("_");

            int x = Integer.parseInt(nom[1]);
            int y = Integer.parseInt(nom[2]);

            x += 1;
            y += 1;

            chemin.get(i).nom = "s_" + x + "_" + y;
        }
    }
    public static void main(String[] args) {
        int[][] matrice = {
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

        int[][] matrice_strategies = {
                {2, 2},
                {2, 12},
                {6, 13},
                {8, 6},
                {14, 17},
                {15, 3},
                {19, 2}
        };

        int[][] matrice_interets = {
                {2, 7, 18},
                {2, 19, 12},
                {7, 2, 13},
                {11, 14, 9},
                {13, 5, 25},
                {18, 6, 8},
                {19, 17, 5}
        };

        int[] obligatoire = {1, 3, 6, 7};

        int[] depart = {11, 19};

        System.out.println("Traduction pour le code...");
        traduirePourLeCode(matrice_interets, matrice_strategies, obligatoire, depart);
        System.out.println("Traduction pour le code terminée.");

        System.out.println("Création du plateau...");
        Plateau plateau = new Plateau(matrice, matrice_interets, matrice_strategies, obligatoire, 0);
        System.out.println("Création du plateau terminée.");

        System.out.println("Création du graphe...");
        plateau.matriceVersGraphe();
        System.out.println("Création du graphe terminée.");

        System.out.println("Calcul de la trajectoire globale...");
        List<Sommet> chemin = new ArrayList<>(plateau.algorithme(depart[0], depart[1]));
        System.out.println("Calcul de la trajectoire globale terminée.");

        System.out.println("Trajectoire simplifiée :");

        for (Sommet sommet : chemin) {
            System.out.print(sommet.nom);
            if (sommet != chemin.get(chemin.size() - 1)) {
                System.out.print(" -> ");
            }
        }
        System.out.println();

        List<Sommet> chemin_complet = new ArrayList<>();

        System.out.println("Sous trajectoires :");
        //Pour chaque sommet, on affiche le chemin le plus court menant au sommet qui suit
        for (int i = 0; i < chemin.size() - 1; i++) {
            System.out.println("Trajectoire " + (i + 1) + " :");
            System.out.println("Sommet de départ : " + chemin.get(i).nom);
            System.out.println("Sommet d'arrivée : " + chemin.get(i + 1).nom);

            Sommet depart_t = plateau.graphe.sommets.get(plateau.graphe.rangSommet(new Sommet(chemin.get(i).nom)));

            Sommet arrivee_t = plateau.graphe.sommets.get(plateau.graphe.rangSommet(new Sommet(chemin.get(i + 1).nom)));

            List<Sommet> chemin_tmp = plateau.graphe.Dijkstra(depart_t, arrivee_t);

            for (Sommet sommet : chemin_tmp) {
                System.out.print(sommet.nom);
                if (sommet != chemin_tmp.get(chemin_tmp.size() - 1)) {
                    System.out.print(" -> ");
                }

                if(!chemin_complet.contains(sommet)){
                    chemin_complet.add(sommet);
                }
            }

            System.out.println("\n");
        }
        System.out.println("Fin des sous trajectoires.");

        System.out.println("Trajectoire globale :");

        for (Sommet sommet : chemin_complet) {
            System.out.print(sommet.nom);
            if (sommet != chemin_complet.get(chemin_complet.size() - 1)) {
                System.out.print(" -> ");
            }
        }

        System.out.println("\nFin de la trajectoire globale.");

        System.out.println("Collection des coorodonnées du chemin :");

        File fichier = new File("coords.txt");

        try {
            FileWriter fw = new FileWriter(fichier);
            BufferedWriter bw = new BufferedWriter(fw);

            for (Sommet sommet : chemin_complet) {

                int x = Integer.parseInt(sommet.nom.split("_")[1]);
                int y = Integer.parseInt(sommet.nom.split("_")[2]);

                bw.write(x + "_" + y);
                bw.newLine();
            }

            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Fin de la collection des coorodonnées du chemin.");

        System.out.println("Fin du programme.");
    }
}
