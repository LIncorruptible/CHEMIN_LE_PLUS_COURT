package objets;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Classe représentant la carte du jeu.
 */
public class Carte {
    private int[]
            /**
             * Coordonnées du point de départ.
             */
            depart,
            /**
             * Indice des cases obligatoires parmis les cases stratégiques.
             */
            cases_obligatoires;
    private int[][]
            /**
             * Grille de la carte.
             */
            grille,
            /**
             * Coordonnées des cases d'intérêts et de leurs valeurs.
             */
            cases_interets,
            /**
             * Coordonnées des cases stratégiques.
             */
            cases_strategiques;

    /**
     * Graphe correspondant à la carte.
     */
    private Graphe graphe;

    /**
     * Constructeur qui prend en paramètre les informations nécessaires pour créer une carte de jeu.
     * @param depart Coordonnées du point de départ.
     * @param cases_olbigatoires Indice des cases obligatoires parmis les cases stratégiques.
     * @param grille Grille de la carte.
     * @param cases_interets Coordonnées des cases d'intérêts et de leurs valeurs.
     * @param cases_strategiques Coordonnées des cases stratégiques.
     */
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

    /**
     * Cette méthode renvoie l'objet Graphe associé à l'objet Carte.
     * @return L'objet Graphe associé.
     */
    public Graphe getGraphe() {
        return graphe;
    }

    public Sommet getDepart() {
        return graphe.getSommets().get(graphe.getIndexOfSommet(depart[0], depart[1]));
    }

    /**
     * Cette méthode permet de récupérer la liste des sommets stratégiques présents sur la carte.
     * @return La liste des sommets stratégiques.
     */
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

    /**
     * Cette méthode permet de récupérer la liste des sommets d'intérêts présents sur la carte.
     * @return La liste des sommets d'intérêts.
     */
    public List<Sommet> getSommetsInterets() {
        List<Sommet> sommets_interets = new ArrayList<>();
        List<Sommet> sommets_graphe = graphe.getSommets();

        for(int[] case_interet : cases_interets) {
            sommets_interets.add(sommets_graphe.get(graphe.getIndexOfSommet(case_interet[0], case_interet[1])));
        }

        return sommets_interets;
    }

    /**
     * Cette méthode permet de récupérer la liste des sommets obligatoires présents sur la carte.
     * @return La liste des sommets obligatoires.
     */
    public List<Sommet> getSommetsObligatoires() {

        List<Sommet> sommets_obligatoires = new ArrayList<>();

        for(int i = 0; i < cases_obligatoires.length; i++) {
            sommets_obligatoires.add(getSommetsStrategiques().get(cases_obligatoires[i]));
        }

        return sommets_obligatoires;
    }

    /**
     * Indique par un booléen si le sommet est un stratégique.
     * @param sommet Le sommet, à évaluer.
     * @return vrai ou faux.
     */
    public boolean isStrategique(Sommet sommet) {

        for(Sommet sommet_a_compare : getSommetsStrategiques()) {
            if(sommet_a_compare.getNom().equals(sommet.getNom())) {
                return true;
            }
        }

        return false;
    }

    /**
     * Indique par un booléen si le sommet est un obligatoire.
     * @param sommet Le sommet, à évaluer.
     * @return vrai ou faux.
     */
    public boolean isObligatoire(Sommet sommet) {
        for(Sommet sommet_a_compare : getSommetsObligatoires()) {
            if(sommet_a_compare.getNom().equals(sommet.getNom())) {
                return true;
            }
        }

        return false;
    }

    /**
     * Indique par un booléen si le sommet est un intérêt.
     * @param sommet Le sommet, à évaluer.
     * @return vrai ou faux.
     */
    public boolean isInteret(Sommet sommet) {

        for(Sommet sommet_a_compare : getSommetsInterets()) {
            if(sommet_a_compare.getNom().equals(sommet.getNom())) {
                return true;
            }
        }

        return false;
    }

    /**
     * Cette méthode permet de calculer le score d'un chemin.
     * @param chemin Le chemin, à évaluer.
     * @return vrai ou faux
     */
    public int computeScoreChemin(List<Sommet> chemin) {

        int
                score_strategique = 0,
                score_interet = 0,
                score_passage = 0;

        for(int i = 1; i < chemin.size(); i++) {
            if(isStrategique(chemin.get(i))) {
                score_strategique += 30;
            } else if (isInteret(chemin.get(i))) {
                for (int[] ligne : cases_interets) {
                    if (ligne[0] == chemin.get(i).getX() && ligne[1] == chemin.get(i).getY()) {
                        score_interet += ligne[2];
                        break;
                    }
                }
            } else {
                score_passage += grille[chemin.get(i).getX()][chemin.get(i).getY()];
            }
        }

        return (score_strategique + score_interet - score_passage);
    }

    public int maxOf(int[][] matrice) {
        int max = 0;
        for(int ligne = 0; ligne < matrice.length; ligne++) {
            for(int colonne = 0; colonne < matrice[ligne].length; colonne++) {
                if(matrice[ligne][colonne] > max) {
                    max = matrice[ligne][colonne];
                }
            }
        }
        return max;
    }

    public int computeCoutChemin(List<Sommet> chemin, Graphe graphe) {

        int cout = 0;

        for(int i = 0; i < chemin.size() - 1; i++) {

            Sommet A = graphe.getSommets().get(
                    graphe.getIndexOfSommet(chemin.get(i).getX(), chemin.get(i).getY())
            );

            Sommet B = graphe.getSommets().get(
                    graphe.getIndexOfSommet(chemin.get(i + 1).getX(), chemin.get(i + 1).getY())
            );

            for(Arc arc : A.getArcs()) {

                if(arc.getArrivee().getNom().equals(B.getNom())) {

                    cout += arc.getPoids();

                    break;
                }
            }
        }

        return cout;
    }

    public Graphe streamlineGraphe(List<Sommet> sommets, Graphe graphe) {

        System.out.println("[Minimisation du graphe]");

        List<Sommet> sommets_crees = new ArrayList<>();

        // On ajoute les sommets
        for (Sommet sommet : sommets) {
            sommets_crees.add(new Sommet(sommet.getX(), sommet.getY(), sommet.isUnObstacle()));
        }

        // On crée des arcs pour que chaque sommet soit relié à un autre
        for (Sommet sommet_en_cours : sommets_crees) {
            for (Sommet sommet : sommets_crees) {
                if (!sommet_en_cours.getNom().equals(sommet.getNom())) {

                    Sommet A = graphe.getSommets().get(
                            graphe.getIndexOfSommet(sommet_en_cours)
                    );

                    Sommet B = graphe.getSommets().get(
                            graphe.getIndexOfSommet(sommet)
                    );

                    List<Sommet> dijkstra = graphe.dijkstra(A, B);
                    int cout = computeCoutChemin(dijkstra, graphe);

                    // On ne crée pas d'arc si le chemin contient un obstacle
                    boolean contient_obstacle = false;
                    for (Sommet s : dijkstra) {
                        if (s.isUnObstacle()) {
                            contient_obstacle = true;
                            break;
                        }
                    }

                    if (!contient_obstacle) {
                        sommet_en_cours.getArcs().add(new Arc(
                                sommet_en_cours,
                                sommet,
                                cout
                        ));
                    }
                }
            }
        }

        System.out.println("\tComposition du graphe : ");
        int
                nb_sommets = 0,
                nb_arcs = 0;

        for(Sommet sommet : sommets_crees) {
            nb_sommets++;
            nb_arcs += sommet.getArcs().size();
        }

        System.out.println("\t\t- Nombre de sommets = " + nb_sommets);
        System.out.println("\t\t- Nombre d'arcs = " + nb_arcs);

        System.out.println("[Terminé]");

        return new Graphe(sommets_crees);
    }

    public List<Sommet> traceChemin() {

        // Minimisation du graphe général

        System.out.println("\n");

        List<Sommet> sommets = new ArrayList<>();

        System.out.print("Sommet de départ : " + getDepart().getNom());
        sommets.add(
                new Sommet(
                        getDepart().getX(),
                        getDepart().getY(),
                        getDepart().isUnObstacle()
                )
        );
        System.out.println();

        System.out.print("Sommets obligatoires : ");
        for(Sommet sommet : getSommetsObligatoires()) {
            System.out.print(sommet.getNom() + "  ");
            sommets.add(
                    new Sommet(
                            sommet.getX(),
                            sommet.getY(),
                            sommet.isUnObstacle()
                    )
            );
        }
        System.out.println();

        System.out.print("Sommets d'intérêts : ");
        for(Sommet sommet : getSommetsInterets()) {
            System.out.print(sommet.getNom() + "  ");
            sommets.add(
                    new Sommet(
                            sommet.getX(),
                            sommet.getY(),
                            sommet.isUnObstacle()
                    )
            );
        }
        System.out.println("\n");

        Graphe graphe_courant = streamlineGraphe(sommets, graphe);

        Sommet sommet_courant = graphe_courant.getSommets().get(
                graphe_courant.getIndexOfSommet(getDepart())
        );

        List<Sommet> sommet_a_visiter = new ArrayList<>();

        for(Sommet sommet : graphe_courant.getSommets()) {
            if(isObligatoire(sommet)) {
                sommet_a_visiter.add(sommet);
            }
        }

        List<Sommet> sommets_visites = new ArrayList<>();
        List<Sommet> chemin = new ArrayList<>();

        chemin.add(sommet_courant);

        while(!sommet_a_visiter.isEmpty()) {

            Map<List<Sommet>, Integer> distances = new HashMap<>();

            for(Sommet sommet : sommet_a_visiter) {

                List<Sommet> dijkstra = graphe_courant.dijkstra(sommet_courant, sommet);
                int cout = computeCoutChemin(dijkstra, graphe_courant);

                distances.put(dijkstra, cout);
            }

            List<Sommet> chemin_courant = null;
            int cout_courant = Integer.MAX_VALUE;

            for(Map.Entry<List<Sommet>, Integer> entry : distances.entrySet()) {
                if(entry.getValue() < cout_courant) {
                    chemin_courant = entry.getKey();
                    cout_courant = entry.getValue();
                }
            }

            for(int i = 1; i < chemin_courant.size(); i++) {
                chemin.add(chemin_courant.get(i));
            }

            sommet_courant = chemin.get(chemin.size() - 1);
            sommet_a_visiter.remove(sommet_courant);
            sommets_visites.add(sommet_courant);
        }

        chemin = opitmizeChemin(chemin, graphe_courant);

        List<Sommet> sommets_interets = new ArrayList<>();

        for(Sommet sommet : graphe_courant.getSommets()) {
            if(isInteret(sommet)) {
                sommets_interets.add(sommet);
            }
        }

        List<Sommet> sommets_interets_restants = new ArrayList<>();

        for(Sommet sommet_interet : sommets_interets) {
            if(!chemin.contains(sommet_interet)) {
                sommets_interets_restants.add(sommet_interet);
            }
        }

        for(Sommet sommet_interet : sommets_interets_restants) {
            List<Sommet> chemin_tmp = placeSommetRestant(sommet_interet, chemin, graphe);

            int cout_tmp = computeCoutChemin(chemin_tmp, graphe_courant);
            int cout_courant = computeCoutChemin(chemin, graphe_courant);

            if(cout_tmp <= cout_courant) {
                chemin = chemin_tmp;
            }
        }

        return chemin;
    }

    public List<Sommet> placeSommetRestant(Sommet sommet, List<Sommet> chemin, Graphe graphe) {

        int score_courant = computeScoreChemin(chemin);

        // Recherche du sommet dans le chemin, le plus proche du sommet interet
        Sommet sommet_proche_A = null;
        int distance_courante = Integer.MAX_VALUE;

        for(Sommet sommet_chemin : chemin) {
            int distance = computeCoutChemin(
                    graphe.dijkstra(
                            graphe.getSommets().get(
                                    graphe.getIndexOfSommet(sommet_chemin)
                            ),
                            graphe.getSommets().get(
                                    graphe.getIndexOfSommet(sommet)
                            )
                    ), graphe
            );
            if(distance < distance_courante) {
                sommet_proche_A = sommet_chemin;
                distance_courante = distance;
            }
        }

        // Recherche du second sommet dans le chemin le plus proche du sommet interet
        Sommet sommet_proche_B = null;
        distance_courante = Integer.MAX_VALUE;

        for(Sommet sommet_chemin : chemin) {
            int distance = computeCoutChemin(
                    graphe.dijkstra(
                            graphe.getSommets().get(
                                    graphe.getIndexOfSommet(sommet_chemin)
                            ),
                            graphe.getSommets().get(
                                    graphe.getIndexOfSommet(sommet)
                            )
                    ), graphe
            );
            if(distance < distance_courante && sommet_chemin != sommet_proche_A) {
                sommet_proche_B = sommet_chemin;
                distance_courante = distance;
            }
        }

        // A précède t-il B ?
        boolean a_precede_b = false;

        for(int i = 0; i < chemin.size(); i++) {
            if(chemin.get(i) == sommet_proche_A) {
                a_precede_b = true;
            }
            if(chemin.get(i) == sommet_proche_B) {
                a_precede_b = false;
            }
        }

        // Reconstruire le chemin en ajoutant le sommet interet entre les deux sommets les plus proches
        List<Sommet> chemin_courant = new ArrayList<>();
        for(Sommet sommet_chemin : chemin) {
            chemin_courant.add(sommet_chemin);
            if(sommet_chemin == ((a_precede_b) ? sommet_proche_A : sommet_proche_B)) {
                chemin_courant.add(sommet);
            }
        }

        int score_chemin_courant = computeScoreChemin(chemin_courant);

        return (score_chemin_courant > score_courant) ? chemin_courant : chemin;
    }

    public List<Sommet> opitmizeChemin(List<Sommet> chemin, Graphe graphe) {

        List<Sommet> chemin_optimal = new ArrayList<>();

        for(int i = 0; i < chemin.size() - 1; i++) {

            // Chemin à peut-être optimiser
            List<Sommet> chemin_courant = new ArrayList<>();
            chemin_courant.add(chemin.get(i));
            chemin_courant.add(chemin.get(i + 1));

            int score_courant = computeCoutChemin(chemin_courant, graphe);

            Map<List<Sommet>, Integer> distances = new HashMap<>();

            distances.put(chemin_courant, score_courant);

            for(Sommet sommet : graphe.getSommets()) {
                if(isInteret(sommet) && !chemin_optimal.contains(sommet)) {
                    // Chemin peut être optimisé
                    List<Sommet> chemin_tmp = new ArrayList<>();
                    chemin_tmp.add(chemin.get(i));
                    chemin_tmp.add(sommet);
                    chemin_tmp.add(chemin.get(i + 1));

                    // On récupère la valeur case intérêt
                    int cellule = 0;

                    for(int ligne[] : cases_interets) {
                        if(ligne[0] == sommet.getX() && ligne[1] == sommet.getY()) {
                            cellule = ligne[2];
                        }
                    }
                    int score_tmp = computeCoutChemin(chemin_tmp, graphe) - cellule;

                    distances.put(chemin_tmp, score_tmp);
                }
            }

            List<Sommet> chemin_optimise = null;
            int score_optimise = score_courant;

            for(Map.Entry<List<Sommet>, Integer> entry : distances.entrySet()) {
                if(entry.getValue() < score_optimise) {
                    chemin_optimise = entry.getKey();
                    score_optimise = entry.getValue();
                }
            }

            if(chemin_optimise != null) {
                chemin_optimal.addAll(chemin_optimise);
            } else {
                chemin_optimal.addAll(chemin_courant);
            }

            chemin_optimal.remove(chemin_optimal.size() - 1);
        }

        chemin_optimal.add(chemin.get(chemin.size() - 1));

        return chemin_optimal;
    }


    /**
     * Cette méthode développe le chemin passé en paramètre.
     * @param chemin Le chemin, à développer.
     * @return Le chemin développé.
     */
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
}
