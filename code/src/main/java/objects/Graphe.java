package objects;

import java.util.*;

public class Graphe {

    public List<Sommet> sommets;

    public Graphe() {
        this.sommets = new ArrayList<>();
    }

    public Graphe(List<Sommet> sommets) {
        this.sommets = sommets;
    }

    public int rangSommet(Sommet sommet) {
        for(Sommet s : this.sommets) {
            if(s.nom.equals(sommet.nom)) {
                return this.sommets.indexOf(s);
            }
        }

        return -1;
    }

    public List<Sommet> Dijkstra(Sommet depart, Sommet arrive) {
        List<Sommet> chemin = new ArrayList<>();

        Set<Sommet> visites = new HashSet<>();
        Map<Sommet, Integer> poids = new HashMap<>();
        Map<Sommet, Sommet> predecesseurs = new HashMap<>();

        for (Sommet s : sommets) {
            poids.put(s, Integer.MAX_VALUE);
            predecesseurs.put(s, null);
        }

        poids.put(depart, 0);

        while (!visites.contains(arrive)) {
            Sommet sommetCourant = null;
            int poidsMin = Integer.MAX_VALUE;
            for (Sommet s : sommets) {
                if (!visites.contains(s) && poids.get(s) < poidsMin) {
                    poidsMin = poids.get(s);
                    sommetCourant = s;
                }
            }

            visites.add(sommetCourant);

            if(sommetCourant == null) {
                break;
            }

            for (Arc arc : sommetCourant.arcs) {
                int poidsVoisin = poids.get(sommetCourant) + arc.poids;
                if (poidsVoisin < poids.get(arc.arrive)) {
                    poids.put(arc.arrive, poidsVoisin);
                    predecesseurs.put(arc.arrive, sommetCourant);
                }
            }
        }

        Sommet sommet = arrive;
        while (sommet != null) {
            chemin.add(sommet);
            sommet = predecesseurs.get(sommet);
        }

        Collections.reverse(chemin);

        return chemin;
    }
}
