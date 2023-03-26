package objets;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Classe représentant un sommet.
 */
public class Sommet {

    /**
     * Nom du sommet.
     */
    private String nom;


    private int
            /**
             * Coordonnée x du sommet.
             */
            x,
            /**
             * Coordonnée y du sommet.
             */
            y;
    private List<Sommet>
            /**
             * Liste des prédécesseurs du sommet.
             */
            predecesseurs,
            /**
             * Liste des successeurs du sommet.
             */
            successeurs;
    /**
     * Liste des arcs du sommet.
     */
    private List<Arc> arcs;
    /**
     * Indique si le sommet est un obstacle.
     */
    public boolean isUnObstacle;

    /**
     * Constructeur de la classe Sommet avec tous les paramètres.
     * @param nom Nom du sommet.
     * @param x Coordonnée x du sommet.
     * @param y Coordonnée y du sommet.
     * @param predecesseurs Liste des prédécesseurs du sommet.
     * @param successeurs Liste des successeurs du sommet.
     * @param arcs Liste des arcs du sommet.
     * @param isUnObstacle Indique si le sommet est un obstacle.
     */
    public Sommet(String nom, int x, int y, List<Sommet> predecesseurs, List<Sommet> successeurs, List<Arc> arcs, boolean isUnObstacle) {
        this.nom = nom;
        this.x = x;
        this.y = y;
        this.predecesseurs = predecesseurs;
        this.successeurs = successeurs;
        this.arcs = arcs;
        this.isUnObstacle = isUnObstacle;
    }

    /**
     * Surcharge du constructeur de la classe Sommet.
     * @param x Coordonnée x du sommet.
     * @param y Coordonnée y du sommet.
     */
    public Sommet(int x, int y) {
        this(( (x + 1) + "_" + (y + 1) ), x, y, new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), false);
    }

    /**
     * Surcharge du constructeur de la classe Sommet.
     * @param x Coordonnée x du sommet.
     * @param y Coordonnée y du sommet.
     * @param isUnObstacle Indique si le sommet est un obstacle.
     */
    public Sommet(int x, int y, boolean isUnObstacle) {
        this(( (x + 1) + "_" + (y + 1) ), x, y, new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), isUnObstacle);
    }

    /**
     * Cette méthode permet de récupérer le nom du sommet.
     * @return
     */
    public String getNom() {
        return nom;
    }

    /**
     * Cette méthtode permet de récupérer la coordonnée x du sommet.
     * @return Coordonnée x du sommet.
     */
    public int getX() {
        return x;
    }

    /**
     * Cette méthode permet de récupérer la coordonnée y du sommet.
     * @return Coordonnée y du sommet.
     */
    public int getY() {
        return y;
    }

    /**
     * Cette méthode permet de récupérer la liste des arcs du sommet.
     * @return Liste des arcs du sommet.
     */
    public List<Arc> getArcs() {
        return arcs;
    }

    /**
     * Cette permet de connaitre l'état du sommet.
     * @return true si le sommet est un obstacle, false sinon.
     */
    public boolean isUnObstacle() {
        return isUnObstacle;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Sommet)) return false;
        Sommet sommet = (Sommet) o;
        return x == sommet.x && y == sommet.y && isUnObstacle == sommet.isUnObstacle &&
                Objects.equals(predecesseurs.hashCode(), sommet.predecesseurs.hashCode()) &&
                Objects.equals(successeurs.hashCode(), sommet.successeurs.hashCode()) &&
                Objects.equals(arcs.hashCode(), sommet.arcs.hashCode());
    }


    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    @Override
    public String toString() {
        return "Sommet{" +
                "x=" + x +
                ", y=" + y + " possède " + arcs.size() + " arcs" + "." +
                ((isUnObstacle == true) ? " C'est un obstacle." : "");
    }
}
