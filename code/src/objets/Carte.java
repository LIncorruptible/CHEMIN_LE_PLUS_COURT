package objets;

import java.util.*;
import java.util.stream.Collectors;

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
    public int computeCoutChemin(List<Sommet> chemin, Graphe graphe) {

        int cout = 0;

        for(int i = 0; i < chemin.size() - 1; i++) {
            cout += graphe.coutArc(chemin.get(i), chemin.get(i + 1));
        }

        return cout;
    }

    /**
     * Cette méthode minimise le graphe en ne gardant que les sommets spécifiés, le reste est calculé par l'algorithme de Dijkstra.
     * @param sommets Les sommets à garder.
     * @param graphe Le graphe à minimiser.
     * @return Le graphe minimisé.
     */
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
                        sommet.addPredecesseur(sommet_en_cours);
                        sommet_en_cours.addSuccesseurs(sommet);
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

    public List<List<Sommet>> findAllPaths(Sommet depart, Sommet arrivee, Graphe graphe) {
        List<List<Sommet>> chemins = new ArrayList<>();
        List<Sommet> cheminCourant = new ArrayList<>();
        cheminCourant.add(depart);
        findAllPathsUtil(arrivee, graphe, cheminCourant, chemins);
        return chemins;
    }

    private void findAllPathsUtil(Sommet arrivee, Graphe graphe, List<Sommet> cheminCourant, List<List<Sommet>> chemins) {
        Sommet sommetCourant = cheminCourant.get(cheminCourant.size() - 1);

        if (sommetCourant.equals(arrivee)) {
            chemins.add(new ArrayList<>(cheminCourant));
        } else {
            for (Arc arc : sommetCourant.getArcs()) {
                Sommet sommetVoisin = arc.getArrivee();
                if (!cheminCourant.contains(sommetVoisin)) {
                    cheminCourant.add(sommetVoisin);
                    findAllPathsUtil(arrivee, graphe, cheminCourant, chemins);
                    cheminCourant.remove(cheminCourant.size() - 1);
                }
            }
        }
    }
    public int computePathScore(List<Sommet> chemin, Graphe graphe) {
        int score = 0;
        for (int i = 0; i < chemin.size() - 1; i++) {

            Sommet arrivee = chemin.get(i + 1);

            // On retire le coût de l'arc
            score -= graphe.coutArc(chemin.get(i), arrivee);

            // On ajoute le score de l'arrivée si c'est un intérêt
            if(isInteret(arrivee)) {
                // On récupère la valeur de l'intérêt
                for(int[] ligne : cases_interets) {
                    if(ligne[0] == arrivee.getX() && ligne[1] == arrivee.getY()) {
                        score += ligne[2];
                        break;
                    }
                }
            }

            // On ajoute le score de l'arrivée si c'est un sommet stratégique
            if(isStrategique(arrivee)) {
                score += 30;
            }

        }
        return score;
    }

    public List<Sommet> pathFinder() {

        // On minimise le graphe
        List<Sommet> sommets = new ArrayList<>();
            sommets.addAll(getSommetsStrategiques());
            sommets.addAll(getSommetsInterets());
            sommets.add(getDepart());
        Graphe graphe_minimise = streamlineGraphe(sommets, graphe);

        // On récupère le depart dans le graphe minimisé
        Sommet depart = graphe_minimise.getSommets().get(
                graphe_minimise.getIndexOfSommet(getDepart())
        );

        // On récupère les sommets obligatoires dans le graphe minimisé
        List<Sommet> sommets_obligatoires = new ArrayList<>();
        for(Sommet sommet : getSommetsObligatoires()) {
            sommets_obligatoires.add(graphe_minimise.getSommets().get(
                    graphe_minimise.getIndexOfSommet(sommet)
            ));

            System.out.println("\tSommet obligatoire : " + sommet.getNom());
        }

        // On récupère tous les sommets sauf le départ
        List<Sommet> sommets_sans_depart = graphe_minimise.getSommets();
        sommets_sans_depart.remove(depart);

        // On recherche tous les chemins possibles entre le départ et les autres sommets
        System.out.println("\tRecherche des chemins possibles...");
        List<List<Sommet>> chemins = new ArrayList<>();
        for (Sommet sommet : sommets_sans_depart) {
            System.out.println("\t\tRecherche des chemins vers " + sommet.getNom() + "...");
            chemins.addAll(findAllPaths(depart, sommet, graphe_minimise));
        }

        System.out.println("\tNombre de chemins trouvés : " + chemins.size());

        // On ne garde que les chemins qui passent par tous les sommets obligatoires
        List<List<Sommet>> chemins_possibles = new ArrayList<>();
        for(List<Sommet> chemin : chemins) {
            if(chemin.containsAll(sommets_obligatoires)) {
                chemins_possibles.add(chemin);
            }
        }

        System.out.println("\tNombre de chemins possibles : " + chemins_possibles.size());

        // On calcule le score de chaque chemin
        Map<List<Sommet>, Integer> scores = new HashMap<>();
        for(List<Sommet> chemin : chemins_possibles) {
            scores.put(chemin, computePathScore(chemin, graphe_minimise));
        }

        // On sélectionne le chemin avec le meilleur score
        List<Sommet> meilleur_chemin = null;
        int meilleur_score = Integer.MIN_VALUE;
        for(Map.Entry<List<Sommet>, Integer> entry : scores.entrySet()) {
            if(entry.getValue() > meilleur_score) {
                meilleur_chemin = entry.getKey();
                meilleur_score = entry.getValue();
            }
        }

        return meilleur_chemin;
    }
}
