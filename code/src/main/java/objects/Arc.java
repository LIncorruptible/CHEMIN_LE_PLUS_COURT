package objects;

public class Arc {
    public Sommet depart;
    public Sommet arrive;
    public int poids;

    public Arc(Sommet depart, Sommet arrive, int poids) {
        this.depart = depart;
        this.arrive = arrive;
        this.poids = poids;
    }

    public Arc() {
        this.depart = null;
        this.arrive = null;
        this.poids = 0;
    }
}
