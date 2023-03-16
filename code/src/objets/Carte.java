package objets;

import java.util.ArrayList;
import java.util.List;

public class Carte {

    private int[] depart, cases_obligatoires;
    private int[][] grille, cases_interets, cases_strategiques;
    private Graphe graphe;

    public Carte(int[] depart, int[] cases_olbigatoires, int[][] grille, int[][] cases_interets, int[][] cases_strategiques) {
        this.depart = depart;
        this.cases_obligatoires = cases_olbigatoires;
        this.grille = grille;
        this.cases_interets = cases_interets;
        this.cases_strategiques = cases_strategiques;

        System.out.print("\tCréation du graphe correspondant...");
        this.graphe = new Graphe(grille);
        System.out.print("[Terminé]\n");
    }

    public int[] getDepart() {
        return depart;
    }

    public void setDepart(int[] depart) {
        this.depart = depart;
    }

    public int[] getCases_obligatoires() {
        return cases_obligatoires;
    }

    public void setCases_obligatoires(int[] cases_obligatoires) {
        this.cases_obligatoires = cases_obligatoires;
    }

    public int[][] getGrille() {
        return grille;
    }

    public void setGrille(int[][] grille) {
        this.grille = grille;
    }

    public int[][] getCases_interets() {
        return cases_interets;
    }

    public void setCases_interets(int[][] cases_interets) {
        this.cases_interets = cases_interets;
    }

    public int[][] getCases_strategiques() {
        return cases_strategiques;
    }

    public void setCases_strategiques(int[][] cases_strategiques) {
        this.cases_strategiques = cases_strategiques;
    }

    public Graphe getGraphe() {
        return graphe;
    }

    public void setGraphe(Graphe graphe) {
        this.graphe = graphe;
    }

    public List<Sommet> getSommetsStrategiques() {
        List<Sommet> sommets_strategiques = new ArrayList<>();
        for (int[] coord : cases_strategiques) {
            Sommet sommet = graphe.getSommets().get(graphe.getIndexOfSommet(coord[0], coord[1]));
            if (sommet != null) {
                sommets_strategiques.add(sommet);
            }
        }
        return sommets_strategiques;
    }

    public List<Sommet> getSommetsInterets() {
        List<Sommet> sommets_interets = new ArrayList<>();
        List<Sommet> sommets_graphe = graphe.getSommets();

        for(int[] case_interet : cases_interets) {
            sommets_interets.add(sommets_graphe.get(graphe.getIndexOfSommet(case_interet[0], case_interet[1])));
        }

        return sommets_interets;
    }

    public List<Sommet> getSommetsObligatoires() {
        return new ArrayList<>(getSommetsStrategiques().subList(0, cases_obligatoires.length));
    }

    public boolean isStrategique(Sommet sommet) {

        for(Sommet sommet_a_compare : getSommetsStrategiques()) {
            if(sommet_a_compare.getNom().equals(sommet.getNom())) {
                return true;
            }
        }

        return false;
    }

    public boolean isObligatoire(Sommet sommet) {
        for(Sommet sommet_a_compare : getSommetsObligatoires()) {
            if(sommet_a_compare.getNom().equals(sommet.getNom())) {
                return true;
            }
        }

        return false;
    }

    public boolean isInteret(Sommet sommet) {

        for(Sommet sommet_a_compare : getSommetsInterets()) {
            if(sommet_a_compare.getNom().equals(sommet.getNom())) {
                return true;
            }
        }

        return false;
    }

    public int computeScoreChemin(List<Sommet> chemin) {
        int
                score_strategique = 0,
                score_interet = 0,
                score_passage = 0;

        // Pour chaque sommet du chemin
        for(Sommet sommet : chemin) {

            // On ajoute 30 si c'est un stratégique
            if(isStrategique(sommet)) score_strategique += 30;

            // On ajoute les points correspondants si c'est un intérêt
            if (isInteret(sommet)) {
                for(int ligne = 0; ligne < cases_interets.length; ligne++) {
                    if(cases_interets[ligne][0] == sommet.getX() && cases_interets[ligne][1] == sommet.getY()) {
                        score_interet += cases_interets[ligne][2];
                        break;
                    }
                }
            }

            // On retire les points de la case passée
            score_passage += grille[sommet.getX()][sommet.getY()];
        }
        return score_strategique + score_interet - score_passage;
    }

    public Graphe streamlineGraphe(boolean interets, boolean strategies) {
        List<Sommet> sommets_crees = new ArrayList<>();
        List<Sommet> sommets = (strategies ? getSommetsStrategiques() : getSommetsObligatoires());

        // On ajoute le sommet de départ sans les arcs
        sommets_crees.add(new Sommet(this.depart[0], this.depart[1]));

        // On ajoute les sommets stratégiques ou intérêts
        if (interets) {
            sommets.addAll(getSommetsInterets());
        }

        // On crée les sommets
        for (Sommet sommet : sommets) {
            sommets_crees.add(new Sommet(sommet.getX(), sommet.getY(), sommet.isUnObstacle()));
        }

        // On crée des arcs pour que chaque sommet soit relié à un autre
        for (Sommet sommet_en_cours : sommets_crees) {
            for (Sommet sommet : sommets_crees) {
                if (!sommet_en_cours.getNom().equals(sommet.getNom())) {
                    List<Sommet> chemin = graphe.dijkstra(sommet_en_cours, sommet);

                    // On ne crée pas d'arc si le chemin contient un obstacle
                    boolean contient_obstacle = false;
                    for (Sommet s : chemin) {
                        if (s.isUnObstacle()) {
                            contient_obstacle = true;
                            break;
                        }
                    }

                    if (!contient_obstacle) {
                        sommet_en_cours.getArcs().add(new Arc(
                                sommet_en_cours,
                                sommet,
                                computeScoreChemin(chemin)
                        ));
                    }
                }
            }
        }

        return new Graphe(sommets_crees);
    }

    public List<Sommet> buildChemin(List<Sommet> chemin) {

        List<Sommet> chemin_developpe = new ArrayList<>();

        List<Sommet> chemin_tmp = new ArrayList<>();

        // On ajoute le départ
        chemin_developpe.add(chemin.get(0));

        // Entre chaque sommet et leur voisin, on cherche le chemin le plus court
        for(int indice = 0; indice < chemin.size() - 1; indice++) {

            Sommet
                    sommet_A = chemin.get(indice),
                    sommet_B = chemin.get(indice + 1);

            chemin_tmp.addAll(
                    graphe.dijkstra(
                            graphe.getSommets().get(
                                    graphe.getIndexOfSommet(sommet_A.getX(), sommet_A.getY())
                            ),
                            graphe.getSommets().get(
                                    graphe.getIndexOfSommet(sommet_B.getX(), sommet_B.getY())
                            )
                    )
            );

            chemin_tmp.remove(0);
            chemin_developpe.addAll(chemin_tmp);
            chemin_tmp.clear();
        }

        // On ajoute l'arrivée
        chemin.add(chemin.get(chemin.size() - 1));

        return chemin_developpe;
    }

    public List<Sommet> browseChemin(Graphe graphe_minimise) {

        List<Sommet> chemin = new ArrayList<>();

        // Sommet curseur partant du depart
        Sommet sommet_en_cours = graphe_minimise.getSommets().get(
                graphe_minimise.getIndexOfSommet(this.depart[0], this.depart[1])
        );

        chemin.add(sommet_en_cours);

        // On déclare les sommets à parcourir obligatoirement
        List<Sommet> sommets_a_parcourir = new ArrayList<>();

        for(Sommet sommet : graphe_minimise.getSommets()) {
            if(isObligatoire(sommet))
                sommets_a_parcourir.add(sommet);
        }

        while(!sommets_a_parcourir.isEmpty()) {

            Arc arc_poids_max = new Arc(sommet_en_cours, sommet_en_cours, -Integer.MAX_VALUE);

            for(Arc arc : sommet_en_cours.getArcs()) {
                if(arc.getPoids() > arc_poids_max.getPoids() && !chemin.contains(arc.getArrivee()))
                    arc_poids_max = arc;
            }

            if(arc_poids_max.getPoids() >= 0) {
                sommet_en_cours = arc_poids_max.getArrivee();
                chemin.add(sommet_en_cours);

                if(sommets_a_parcourir.contains(sommet_en_cours))
                    sommets_a_parcourir.remove(sommet_en_cours);
            } else {
                // Si la destination est un sommet à parcourir on y va
                if(sommets_a_parcourir.contains(sommet_en_cours)) {
                    sommet_en_cours = arc_poids_max.getArrivee();
                    chemin.add(sommet_en_cours);
                    sommets_a_parcourir.remove(sommet_en_cours);

                    // On voyage aux autres sommets à parcourir restant
                    for(Sommet sommet_restant : sommets_a_parcourir) {
                        chemin.add(sommet_restant);
                        sommets_a_parcourir.remove(sommet_restant);
                    }
                } else {
                    break;
                }
            }
        }

        return buildChemin(chemin);
    }

    public List<Sommet> traceMaxScoreChemin() {

        System.out.println("\n[Calcul du tracé]");

        System.out.println("\t(Note : on cherche le chemin le plus court mais qui rapporte le plus de points.)");

        System.out.print("\tRéduction du graphe...");
        Graphe graphe_minimise = new Graphe(
                streamlineGraphe(true, true)
        );
        System.out.print("[Terminée]\n");
        System.out.println("\t" + graphe_minimise);

        return browseChemin(graphe_minimise);
    }

    public List<Sommet> traceMinDistChemin() {

        System.out.println("\n[Calcul du tracé]");

        System.out.println("\t(Note : on cherche le chemin le plus court. Seul les sommets obligatoires sont pris en compte.)");

        System.out.print("\tRéduction du graphe...");
        Graphe graphe_minimise = new Graphe(
                streamlineGraphe(false, false)
        );
        System.out.print("[Terminée]\n");
        System.out.println("\t" + graphe_minimise);

        return browseChemin(graphe_minimise);
    }

    public List<Sommet> tracePersonalizedChemin(boolean interets, boolean strategies) {

        System.out.println("\n[Calcul du tracé]");

        System.out.println(
                "\t(Note : ce chemin prend en compte" +
                        ((interets) ? " les points d'intérêts" : "") +
                        ((strategies) ? " les points stratégiques" : " les points obligatoires") + "."
        );

        System.out.print("\tRéduction du graphe...");
        Graphe graphe_minimise = new Graphe(
                streamlineGraphe(interets, strategies)
        );
        System.out.print("[Terminée]\n");
        System.out.println("\t" + graphe_minimise);

        return browseChemin(graphe_minimise);
    }
}
