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
    public boolean isUnObtascle;

    /**
     * Constructeur de la classe Sommet avec tous les paramètres.
     * @param nom Nom du sommet.
     * @param x Coordonnée x du sommet.
     * @param y Coordonnée y du sommet.
     * @param predecesseurs Liste des prédécesseurs du sommet.
     * @param successeurs Liste des successeurs du sommet.
     * @param arcs Liste des arcs du sommet.
     * @param isUnObtascle Indique si le sommet est un obstacle.
     */
    public Sommet(String nom, int x, int y, List<Sommet> predecesseurs, List<Sommet> successeurs, List<Arc> arcs, boolean isUnObtascle) {
        this.nom = nom;
        this.x = x;
        this.y = y;
        this.predecesseurs = predecesseurs;
        this.successeurs = successeurs;
        this.arcs = arcs;
        this.isUnObtascle = isUnObtascle;
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
     * @param isUnObtascle Indique si le sommet est un obstacle.
     */
    public Sommet(int x, int y, boolean isUnObtascle) {
        this(( (x + 1) + "_" + (y + 1) ), x, y, new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), isUnObtascle);
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
        return isUnObtascle;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Sommet sommet)) return false;
        return x == sommet.x && y == sommet.y && isUnObtascle == sommet.isUnObtascle && Objects.equals(predecesseurs, sommet.predecesseurs) && Objects.equals(successeurs, sommet.successeurs) && Objects.equals(arcs, sommet.arcs);
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    @Override
    public String toString() {
        return "Sommet{" +
                "x=" + x +
                ", y=" + y +
                ", predecesseurs=" + predecesseurs +
                ", successeurs=" + successeurs +
                ", arcs=" + arcs +
                ", isUnObtascle=" + isUnObtascle +
                '}';
    }
}
