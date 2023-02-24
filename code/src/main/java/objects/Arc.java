package objects;

/**
 * Classe Arc.
 * Cette classe représente un arc du graphe.
 * Un arc est caractérisé par son sommet de départ, son sommet d'arrivée et son poids.
 */
public class Arc {
    /**
     * Attributs
     * Sommet de départ
     * Sommet d'arrivée
     * Poids de l'arc
     */
    public Sommet depart;
    public Sommet arrive;
    public int poids;

    /**
     * Constructeur de la classe Arc.
     * @param depart Sommet de départ
     * @param arrive Sommet d'arrivée
     * @param poids Poids de l'arc
     */
    public Arc(Sommet depart, Sommet arrive, int poids) {
        this.depart = depart;
        this.arrive = arrive;
        this.poids = poids;
    }
}
