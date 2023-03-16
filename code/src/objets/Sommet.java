package objets;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Sommet {

    private String nom;
    private int x, y;
    private List<Sommet>  predecesseurs, successeurs;
    private List<Arc> arcs;
    public boolean isUnObtascle;

    public Sommet(String nom, int x, int y, List<Sommet> predecesseurs, List<Sommet> successeurs, List<Arc> arcs, boolean isUnObtascle) {
        this.nom = nom;
        this.x = x;
        this.y = y;
        this.predecesseurs = predecesseurs;
        this.successeurs = successeurs;
        this.arcs = arcs;
        this.isUnObtascle = isUnObtascle;
    }

    public Sommet(int x, int y) {
        this(( (x + 1) + "_" + (y + 1) ), x, y, new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), false);
    }

    public Sommet(int x, int y, boolean isUnObtascle) {
        this(( (x + 1) + "_" + (y + 1) ), x, y, new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), isUnObtascle);
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public List<Sommet> getPredecesseurs() {
        return predecesseurs;
    }

    public void setPredecesseurs(List<Sommet> predecesseurs) {
        this.predecesseurs = predecesseurs;
    }

    public List<Sommet> getSuccesseurs() {
        return successeurs;
    }

    public void setSuccesseurs(List<Sommet> successeurs) {
        this.successeurs = successeurs;
    }

    public List<Arc> getArcs() {
        return arcs;
    }

    public void setArcs(List<Arc> arcs) {
        this.arcs = arcs;
    }

    public boolean isUnObstacle() {
        return isUnObtascle;
    }

    public void setUnObtascle(boolean unObtascle) {
        isUnObtascle = unObtascle;
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
