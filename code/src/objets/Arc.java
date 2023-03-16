package objets;

import java.util.Objects;

public class Arc {

    private Sommet Depart, Arrivee;
    private int poids;

    public Arc(Sommet depart, Sommet arrivee, int poids) {
        Depart = depart;
        Arrivee = arrivee;
        this.poids = poids;
    }

    public Sommet getDepart() {
        return Depart;
    }

    public void setDepart(Sommet depart) {
        Depart = depart;
    }

    public Sommet getArrivee() {
        return Arrivee;
    }

    public void setArrivee(Sommet arrivee) {
        Arrivee = arrivee;
    }

    public int getPoids() {
        return poids;
    }

    public void setPoids(int poids) {
        this.poids = poids;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Arc arc)) return false;
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
