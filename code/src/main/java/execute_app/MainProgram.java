package execute_app;

import objects.Arc;
import objects.Graphe;
import objects.Plateau;
import objects.Sommet;

import java.util.List;
import java.util.Scanner;

/**
 * Classe MainProgram.
 * APPLICATION : Déterminer le chemin le plus court entre 2 cases d'une grille d'entiers.
 */
public class MainProgram {
    /**
     * Cette fonction permet de générer une matrice pour le plateau de jeu.
     * @param largeur largeur du plateau
     * @param hauteur hauteur du plateau
     * @param min valeur minimale des cases
     * @param max valeur maximale des cases
     * @return une matrice de taille hauteur x largeur avec des valeurs comprises entre min et max
     */
    public static int[][] genererMatrice(int largeur, int hauteur, int min, int max) {
        int[][] matrice = new int[hauteur][largeur];
        for(int i = 0; i < hauteur; i++) {
            for(int j = 0; j < largeur; j++) {
                matrice[i][j] = (int) (Math.random() * (max - min + 1) + min);
            }
        }
        return matrice;
    }

    /**
     * Cette fonction permet de générer des coordonnées aléatoires de départ et d'arrivée dans le plateau de jeu.
     * @param largeur largeur du plateau
     * @param hauteur hauteur du plateau
     * @return un tableau de 4 entiers : x1, y1, x2, y2
     */
    public static int[] genererCoordonnees(int largeur, int hauteur) {
        int[] coordonnees = new int[2];
        coordonnees[0] = (int) (Math.random() * (hauteur - 1));
        coordonnees[1] = (int) (Math.random() * (largeur - 1));
        return coordonnees;
    }

    /**
     * Cette fonction principale permet de lancer le programme.
     * Elle permet de choisir entre un plateau de jeu aléatoire ou un plateau de jeu prédéfini.
     * On y utilise l'algo de Dijkstra pour trouver le chemin le plus court entre deux points.
     * @param args
     */
    public static void main(String[] args) {
        int matrice[][] = {
                {   2   ,   1   ,   4},
                {   3   ,   1   ,   4},
                {   2   ,   2   ,   3},
        };

        /**
         * Choix utilisateur
         */
        int choix = 0;

        System.out.println("Que voulez-vous faire ?");
        System.out.println("1 - Chemin le plus court pour la matrice du code");
        System.out.println("2 - Chemin le plus court pour une matrice aléatoire");

        Scanner sc = new Scanner(System.in);
        choix = sc.nextInt();

        switch (choix) {
            case 1:
                sc = new Scanner(System.in);

                Plateau plateau = new Plateau(matrice);
                plateau.afficherMatrice();
                plateau.matriceVersGraphe();

                System.out.println("Choisissez un couple de coordonnées pour trouver le chemin le plus court");

                System.out.println("Coordonnées de départ : ");
                System.out.print("> ligne : ");
                int x1 = sc.nextInt();
                System.out.print("> colonne : ");
                int y1 = sc.nextInt();

                System.out.println("Coordonnées d'arrivée : ");
                System.out.print("> ligne : ");
                int x2 = sc.nextInt();
                System.out.print("> colonne : ");
                int y2 = sc.nextInt();

                plateau.cheminLePlusCourt(x1, y1, x2, y2);
                break;
            case 2:
                /**
                 * Génération d'une matrice aléatoire
                 * On génère une matrice aléatoire de
                 * -> largeur et hauteur comprises entre 3 et 32
                 * -> valeurs comprises entre 1 et largeur
                 * On génère également deux couples de coordonnées aléatoires
                 */
                int largeur = (int) (Math.random() * (32 - 3 + 1) + 3);
                int hauteur = (int) (Math.random() * (32 - 3 + 1) + 3);
                int min = 1;
                int max = largeur;

                int[][] matriceAleatoire = genererMatrice(largeur, hauteur, min, max);

                int[] coordonneesDepart = genererCoordonnees(largeur, hauteur);
                int[] coordonneesArrivee = genererCoordonnees(largeur, hauteur);

                Plateau plateauAleatoire = new Plateau(matriceAleatoire);
                plateauAleatoire.afficherMatrice();
                plateauAleatoire.matriceVersGraphe();
                plateauAleatoire.cheminLePlusCourt(coordonneesDepart[0], coordonneesDepart[1], coordonneesArrivee[0], coordonneesArrivee[1]);
                break;
            default:
                System.out.println("Choix invalide");
                break;
        }
    }
}
