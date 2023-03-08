package objects;

import java.util.ArrayList;
import java.util.List;

public class Sommet{

    public String nom;
    public List<Sommet> predecesseurs;
    public List<Sommet> successeurs;
    public List<Arc> arcs;

    public Sommet(String nom) {
        this.nom = nom;
        this.predecesseurs = new ArrayList<>();
        this.successeurs = new ArrayList<>();
        this.arcs = new ArrayList<>();
    }

    public Sommet(Sommet sommet) {
        this.nom = sommet.nom;
        this.predecesseurs = sommet.predecesseurs;
        this.successeurs = sommet.successeurs;
        this.arcs = sommet.arcs;
    }
}
