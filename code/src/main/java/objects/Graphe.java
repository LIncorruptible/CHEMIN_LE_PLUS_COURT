package objects;

import java.util.ArrayList;
import java.util.List;

/**
 * Classe Graphe.
 * Cette classe représente un graphe.
 * Un graphe est caractérisé par une liste de sommets.
 * (Note : le graphe est celui représentant le plateau de jeu, il est pondéré de poids positifs et orienté)
 */
public class Graphe {
    /**
     * Attributs
     * Liste des sommets du graphe (représentation du plateau sous forme de graphe)
     */
    public List<Sommet> sommets;

    /**
     * Constructeur de la classe Graphe.
     * On initialise la liste des sommets du graphe à vide.
     */
    public Graphe() {
        this.sommets = new ArrayList<>();
    }

    /**
     * Cette méthode recherche le rang d'un sommet dans la liste des sommets du graphe.
     * @param sommet Sommet dont on veut connaitre le rang
     * @return Rang du sommet dans la liste des sommets du graphe (-1 si le sommet n'est pas dans la liste)
     */
    public int rangSommet(Sommet sommet) {
        for(Sommet s : this.sommets) {
            if(s.nom.equals(sommet.nom)) {
                return this.sommets.indexOf(s);
            }
        }

        return -1;
    }

    /**
     * Cette méthode applique l'algorithme de Dijkstra pour trouver le plus court chemin entre deux sommets d'un graphe pondéré de poids positive :
     * 1. On initialise le sommet de départ à 0 et les autres sommets à l'infini
     * 2. On ajoute le sommet de départ à la liste des sommets parcourus
     * 3. On parcourt les arcs du sommet courant
     * 4. Pour chaque arc, on vérifie si le poids du sommet d'arrivée est supérieur au poids du sommet courant + le poids de l'arc
     * 5. Si c'est le cas, on met à jour le poids du sommet d'arrivée
     * 6. On ajoute le sommet d'arrivée à la liste des sommets parcourus
     * 7. On répète les étapes 3 à 6 jusqu'à ce que le sommet d'arrivée soit dans la liste des sommets parcourus
     * 8. On remonte le chemin en partant du sommet d'arrivée jusqu'au sommet de départ
     * (Note : le graphe est celui représentant le plateau de jeu)
     * @param depart Sommet de départ
     * @param arrive Sommet d'arrivée
     * @return Plus court chemin entre le sommet de départ et le sommet d'arrivée
     */
    public List<Sommet> Dijsktra(Sommet depart, Sommet arrive) {
        List<Sommet> chemin = new ArrayList<>();

        Sommet sommet_courant = new Sommet(depart);

        int[] poids = new int[this.sommets.size()];
        String[] predecesseur = new String[this.sommets.size()];

        for(Sommet s : sommets) {
            poids[this.rangSommet(s)] = Integer.MAX_VALUE;
            predecesseur[this.rangSommet(s)] = "-";
        }

        poids[this.rangSommet(sommet_courant)] = 0;

        while(!sommet_courant.nom.equals(arrive.nom)) {
            int min = Integer.MAX_VALUE;
            for(Sommet s : sommets) {
                if(poids[this.rangSommet(s)] < min && !chemin.contains(s)) {
                    min = poids[this.rangSommet(s)];
                    sommet_courant = s;
                }
            }
            chemin.add(sommet_courant);

            for(Arc arc : sommet_courant.arcs) {
                if(poids[this.rangSommet(arc.arrive)] > poids[this.rangSommet(sommet_courant)] + arc.poids) {
                    poids[this.rangSommet(arc.arrive)] = poids[this.rangSommet(sommet_courant)] + arc.poids;
                    predecesseur[this.rangSommet(arc.arrive)] = sommet_courant.nom;
                }
            }
        }

        List<Sommet> chemin_tmp = new ArrayList<>(chemin);
        chemin.clear();

        Sommet sommet = arrive;
        while(!sommet.nom.equals(depart.nom)) {
            chemin.add(sommet);
            sommet = this.sommets.get(this.rangSommet(new Sommet(predecesseur[this.rangSommet(sommet)])));
        }

        chemin.add(depart);
        chemin_tmp.clear();

        for(int i = chemin.size() - 1; i >= 0; i--) {
            chemin_tmp.add(chemin.get(i));
        }

        chemin.clear();
        chemin = chemin_tmp;

        return chemin;
    }
}
