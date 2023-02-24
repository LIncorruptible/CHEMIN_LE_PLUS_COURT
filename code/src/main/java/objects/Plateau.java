package objects;

import java.util.ArrayList;
import java.util.List;

/**
 * Classe Plateau.
 * Cette classe représente le plateau de jeu.
 */
public class Plateau {
    /**
     * Attributs
     * Matrice du plateau de jeu
     * Graphe du plateau de jeu (représentation du plateau sous forme de graphe)
     */
    int[][] matrice;
    Graphe graphe;

    /**
     * Constructeur de la classe Plateau.
     * @param matrice Matrice du plateau
     */
    public Plateau(int[][] matrice) {
        this.matrice = matrice;
        this.graphe = new Graphe();
    }

    /**
     * Cette méthode recherche les sommets voisins du sommet passé en paramètre.
     * @param sommet Sommet dont on veut connaitre le voisinage
     * @return Liste des voisins du sommet
     */
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

        return voisins;
    }

    /**
     * Cette méthode transforme la matrice en graphe.
     */
    public void matriceVersGraphe() {

        /**
         * Création des sommets
         */
        for(int i = 0; i < this.matrice.length; i++) {
            for(int j = 0; j < this.matrice[i].length; j++) {
                Sommet sommet = new Sommet("s_" + i + "_" + j);
                this.graphe.sommets.add(sommet);
            }
        }

        /**
         * Création des arcs
         * On parcourt tous les sommets et on récupère leurs voisins
         * On créé un arc entre le sommet et son voisin de poids égal à la valeur de la matrice
         */
        for(Sommet sommet : this.graphe.sommets) {
            List<Sommet> voisins = this.voisinnage(sommet);

            for (Sommet voisin : voisins) {
                Arc arc = new Arc(sommet, voisin, this.matrice[Integer.parseInt(voisin.nom.split("_")[1])][Integer.parseInt(voisin.nom.split("_")[2])]);
                sommet.arcs.add(arc);
            }
        }
    }

    /**
     * Cette méthode permet de trouver le chemin le plus court entre deux sommets.
     * On utilise l'algorithme de Dijkstra.
     * @param x_depart Coordonnée x du sommet de départ
     * @param y_depart Coordonnée y du sommet de départ
     * @param x_arrive Coordonnée x du sommet d'arrivée
     * @param y_arrive Coordonnée y du sommet d'arrivée
     */
    public void cheminLePlusCourt(int x_depart, int y_depart, int x_arrive, int y_arrive) {
        Sommet d = new Sommet("s_" + x_depart + "_" + y_depart);
        Sommet a = new Sommet("s_" + x_arrive + "_" + y_arrive);

        /**
         * On récupère les sommets correspondant aux nième sommet de la liste
         */
        Sommet depart = this.graphe.sommets.get(this.graphe.rangSommet(d));
        Sommet arrive = this.graphe.sommets.get(this.graphe.rangSommet(a));

        /**
         * On récupère le chemin le plus court entre les deux sommets
         * On affiche le chemin
         */
        List<Sommet> chemin = this.graphe.Dijsktra(depart, arrive);

        System.out.println("Chemin le plus court entre " + depart.nom + " et " + arrive.nom + " : ");
        for (Sommet sommet : chemin) {
            System.out.print(sommet.nom);

            if(sommet != chemin.get(chemin.size() - 1)) {
                System.out.print(" -> ");
            }
        }
    }

    /**
     * Cette méthode permet d'afficher la matrice.
     */
    public void afficherMatrice() {
        /**
         * Affichage de la matrice
         * Avec numéro de ligne et numéro de colonne
         */
        System.out.print("  ");
        for(int i = 0; i < this.matrice[0].length; i++) {
            System.out.print(i + " ");
        }
        System.out.println();

        for(int i = 0; i < this.matrice.length; i++) {
            System.out.print(i + " ");
            for(int j = 0; j < this.matrice[i].length; j++) {
                System.out.print(this.matrice[i][j] + " ");
            }
            System.out.println();
        }
    }
}
