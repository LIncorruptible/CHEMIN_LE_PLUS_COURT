package objets;

import java.util.*;

public class Graphe {

    private List<Sommet> sommets;

    public Graphe(List<Sommet> sommets) {
        this.sommets = sommets;
    }

    public Graphe(int[][] matrice_adjacences) {

        int nb_lignes = matrice_adjacences.length;
        int nb_colonnes = matrice_adjacences[0].length;

        List<Sommet> sommets_crees = new ArrayList<>();

        for(int ligne = 0; ligne < nb_lignes; ligne++) {
            for(int colonne = 0; colonne < nb_colonnes; colonne++) {
                sommets_crees.add(
                        new Sommet(
                                colonne,
                                ligne,
                                (matrice_adjacences[colonne][ligne] < 0) ? true : false)
                );
            }
        }

        this.sommets = new ArrayList<>(sommets_crees);

        for(Sommet sommet : sommets) {
            List<Sommet> voisins = new ArrayList<>(
                    voisinnage(sommet, nb_lignes - 1, nb_colonnes - 1)
            );

            for(Sommet sommet_voisin : voisins) {

                int poids_tmp = matrice_adjacences[sommet_voisin.getX()][sommet_voisin.getY()];

                int poids = (poids_tmp >= 0) ? poids_tmp : Integer.MAX_VALUE; // Gérer les obstacles -1 par l'équivalent "infinie"

                Arc arc_cree = new Arc(sommet, sommet_voisin, poids);

                sommet.getArcs().add(arc_cree);

                this.sommets.set(
                        getIndexOfSommet(sommet),
                        sommet
                );
            }
        }
    }

    public Graphe(Graphe graphe) {
        this(graphe.getSommets());
    }

    public List<Sommet> getSommets() {
        return sommets;
    }

    public void setSommets(List<Sommet> sommets) {
        this.sommets = sommets;
    }

    public int getIndexOfSommet(Sommet sommet) {

        for(int indice = 0; indice < sommets.size(); indice++) {
            if (sommets.get(indice).getNom().equals(sommet.getNom())) {
                return indice;
            }
        }

        return -1;
    }

    public int getIndexOfSommet(int x, int y) {

        int indice = 0;

        for(Sommet sommet : sommets) {
            if (sommet.getX() == x && sommet.getY() == y) {
                return indice;
            }

            indice++;
        }

        return -1;
    }

    public List<Sommet> voisinnage(Sommet sommet, int nb_lignes, int nb_colonnes) {

        List<Sommet> sommet_voisins = new ArrayList<>();

        int x = sommet.getX();
        int y = sommet.getY();

        // Sommet de Gauche
        if(sommet.getX() > 0)
            sommet_voisins.add(sommets.get(getIndexOfSommet(new Sommet((x - 1), y))));

        // Sommet de Droite
        if(sommet.getX() < nb_lignes)
            sommet_voisins.add(sommets.get(getIndexOfSommet(new Sommet((x + 1), y))));


        // Sommet du Dessus
        if(sommet.getY() > 0)
            sommet_voisins.add(sommets.get(getIndexOfSommet(new Sommet(x, (y - 1)))));

        // Sommet du Bas
        if(sommet.getY() < nb_colonnes)
            sommet_voisins.add(sommets.get(getIndexOfSommet(new Sommet(x, (y + 1)))));

        return sommet_voisins;

    }

    public List<Sommet> dijkstra(Sommet depart, Sommet destination) {

        Map<Sommet, Integer> distances = new HashMap<>();
        Map<Sommet, Sommet> predecesseurs = new HashMap<>();
        PriorityQueue<Sommet> sommets_a_traiter = new PriorityQueue<>(Comparator.comparingInt(distances::get));

        // Initialisation
        for (Sommet sommet : sommets) {
            if (sommet != null) {
                distances.put(sommet, Integer.MAX_VALUE);
                predecesseurs.put(sommet, null);
            }
        }
        distances.put(depart, 0);
        sommets_a_traiter.offer(depart);

        while (!sommets_a_traiter.isEmpty()) {

            Sommet sommet_courant = sommets_a_traiter.poll();

            if (sommet_courant.equals(destination)) {
                break;
            }

            for (Arc arc : sommet_courant.getArcs()) {

                Sommet sommet_voisin = arc.getArrivee();

                if (!sommet_voisin.isUnObstacle()) {

                    int poids_arc = arc.getPoids();

                    if (distances.get(sommet_courant) == null) {
                        distances.put(sommet_courant, Integer.MAX_VALUE);
                    }

                    int distance_voisin = distances.get(sommet_courant) + poids_arc;

                    if (distance_voisin < distances.getOrDefault(sommet_voisin, Integer.MAX_VALUE)) {
                        sommets_a_traiter.remove(sommet_voisin);
                        distances.put(sommet_voisin, distance_voisin);
                        predecesseurs.put(sommet_voisin, sommet_courant);
                        sommets_a_traiter.offer(sommet_voisin);
                    }
                }
            }
        }

        List<Sommet> chemin = new ArrayList<>();
        Sommet sommet_courant = destination;

        while (predecesseurs.get(sommet_courant) != null) {
            chemin.add(sommet_courant);
            sommet_courant = predecesseurs.get(sommet_courant);
        }

        chemin.add(sommet_courant);
        Collections.reverse(chemin);

        return chemin;
    }

    @Override
    public String toString() {
        return "Ce graphe possède " +
                sommets.size() + " sommet" + ((sommets.size() > 1) ? "s" : "") + ".";
    }
}
