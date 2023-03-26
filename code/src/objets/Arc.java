package objets;

import java.util.Objects;

/**
 * Classe représentant un arc.
 */
public class Arc {

    private Sommet
            /**
             * Sommet de départ de l'arc.
             */
            Depart,
            /**
             * Sommet d'arrivée de l'arc.
             */
            Arrivee;

    /**
     * Poids de l'arc.
     */
    private int poids;

    /**
     * Constructeur de la classe Arc avec tous les paramètres.
     * @param depart Sommet de départ de l'arc.
     * @param arrivee Sommet d'arrivée de l'arc.
     * @param poids Poids de l'arc.
     */
    public Arc(Sommet depart, Sommet arrivee, int poids) {
        Depart = depart;
        Arrivee = arrivee;
        this.poids = poids;
    }

    /**
     * Cette méthode permet de récupérer le sommet d'arrivée de l'arc.
     * @return Sommet d'arrivée de l'arc.
     */
    public Sommet getArrivee() {
        return Arrivee;
    }

    /**
     * Cette méthode permet de récupérer le poids de l'arc.
     * @return Poids de l'arc.
     */
    public int getPoids() {
        return poids;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Arc)) return false;
        Arc arc = (Arc) o;
        return poids == arc.poids && Objects.equals(Depart, arc.Depart) && Objects.equals(Arrivee, arc.Arrivee);
    }

    @Override
    public int hashCode() {
        return Objects.hash(poids);
    }

    @Override
    public String toString() {
        return "Arc{" +
                "Depart=" + Depart +
                ", Arrivee=" + Arrivee +
                ", poids=" + poids +
                '}';
    }
}
