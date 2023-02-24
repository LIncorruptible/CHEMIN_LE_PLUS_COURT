package objects;

import java.util.ArrayList;
import java.util.List;

/**
 * Classe Sommet.
 * Cette classe représente un sommet du graphe.
 * Un sommet est caractérisé par son nom, ses prédécesseurs, ses successeurs et ses arcs.
 */
public class Sommet{
    /**
     * Attributs
     * Nom du sommet
     * Liste des prédécesseurs du sommet
     * Liste des successeurs du sommet
     * Liste des arcs du sommet
     */
    public String nom;
    public List<Sommet> predecesseurs;
    public List<Sommet> successeurs;
    public List<Arc> arcs;

    /**
     * Constructeur de la classe Sommet.
     * On initialise les listes de prédécesseurs, de successeurs et d'arcs à vide.
     * @param nom Nom du sommet
     */
    public Sommet(String nom) {
        this.nom = nom;
        this.predecesseurs = new ArrayList<>();
        this.successeurs = new ArrayList<>();
        this.arcs = new ArrayList<>();
    }

    /**
     * Constructeur de la classe Sommet.
     * On initialise ce sommet à partir d'un autre sommet.
     * @param sommet
     */
    public Sommet(Sommet sommet) {
        this.nom = sommet.nom;
        this.predecesseurs = sommet.predecesseurs;
        this.successeurs = sommet.successeurs;
        this.arcs = sommet.arcs;
    }
}
