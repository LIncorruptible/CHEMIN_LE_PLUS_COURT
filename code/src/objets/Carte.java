package objets;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
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
     * Cette méthode permet de calculer le score d'un chemin.
     * @param chemin Le chemin, à évaluer.
     * @param graphe Le graphe référent.
     * @return le score du chemin.
     */
    public int computePathScore(List<Sommet> chemin, Graphe graphe) {
        int score = 0;

        Map<Sommet, Integer> interets = new HashMap<>();
        for (int[] ligne : cases_interets) {
            Sommet sommet = new Sommet(ligne[0], ligne[1]);
            int valeur = ligne[2];
            interets.put(sommet, valeur);
        }

        Set<Sommet> sommetsStrategiques = new HashSet<>();

        for(Sommet sommet_strategique : getSommetsStrategiques()) {
            for(Sommet sommet : chemin) {
                if(sommet_strategique.getNom().equals(sommet.getNom())) sommetsStrategiques.add(sommet);
            }
        }

        for (int i = 0; i < chemin.size() - 1; i++) {
            Sommet depart = chemin.get(i);
            Sommet arrivee = chemin.get(i + 1);

            // On retire le coût de l'arc
            score -= graphe.coutArc(depart, arrivee);

            // On ajoute le score de l'arrivée si c'est un intérêt
            if (interets.containsKey(arrivee)) {
                score += interets.get(arrivee);
            }

            // On ajoute le score de l'arrivée si c'est un sommet stratégique
            if (sommetsStrategiques.contains(arrivee)) {
                score += 30;
            }
        }
        return score;
    }

    /**
     * Cette méthode minimise le graphe en ne gardant que les sommets spécifiés. Chaque arc est de poids -> dijkstra entre les sommets.
     * @param sommets Les sommets à garder.
     * @param graphe Le graphe à minimiser.
     * @return Le graphe minimisé.
     */
    public Graphe streamlineGraphe(List<Sommet> sommets, Graphe graphe) {

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

                    if (!contient_obstacle && !B.getNom().equals(getDepart().getNom())) {
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

        return new Graphe(sommets_crees);
    }

    /**
     * Cette méthode permet de réduire le graphe :
     * - en supprimant les arcs à forte distance.
     * - en supprimant les sommets considérés comme onéreux.
     * @param graphe Le graphe à réduire.
     */
    public void reduceGraphe(Graphe graphe) {
        Map<Sommet, Integer> distances = new HashMap<>();
        Sommet sommetLePlusLoin = null;
        int lePLusLoin = -Integer.MAX_VALUE;

        Arc arc_a_supprimer = null;

        List<Sommet> chemin_tmp = new ArrayList<>();
        List<Sommet> sommets_a_supprimer = new ArrayList<>();

        // Suppression des arcs à fortes distances
        for(Sommet sommet : graphe.getSommets()) {

            distances.clear();

            for(Sommet sommet_voisin : sommet.getSuccesseurs()) {
                distances.put(sommet_voisin, computeCoutChemin(graphe.dijkstra(sommet, sommet_voisin), graphe));
            }

            for(Map.Entry<Sommet, Integer> entry : distances.entrySet()) {
                if (entry.getValue() > lePLusLoin) {
                    sommetLePlusLoin = entry.getKey();
                    lePLusLoin = entry.getValue();
                }
            }

            // On supprime l'arc entre le sommet en cours et son sommet le plus loin
            for(Arc arc : sommet.getArcs()) {
                if(arc.getArrivee().equals(sommetLePlusLoin)) {
                    arc_a_supprimer = arc;
                    break;
                }
            }
            sommet.getArcs().remove(arc_a_supprimer);

            // On retire le sommet le plus loin des successeurs du sommet en cours
            sommet.getSuccesseurs().remove(sommetLePlusLoin);

            // De même pour le sommet le plus loin du sommet en cours
            for(Arc arc : sommetLePlusLoin.getArcs()) {
                if(arc.getArrivee().equals(sommet)) {
                    arc_a_supprimer = arc;
                    break;
                }
            }
            sommetLePlusLoin.getArcs().remove(arc_a_supprimer);
            sommetLePlusLoin.getPredecesseurs().remove(sommet);
        }

        // Suppression des sommets onéreux : stratégiques et intérêts
        Map<Sommet, Boolean> couts_des_sommets = new HashMap<>();
        boolean onereux = false;

        Sommet depart = graphe.getSommets().get(
                graphe.getIndexOfSommet(getDepart())
        );

        List<Sommet> sommets_obligatoires = new ArrayList<>();

        for(Sommet sommet_obligatoires : getSommetsObligatoires()) {
            for(Sommet sommet : graphe.getSommets()) {
                if (sommet_obligatoires.getNom().equals(sommet.getNom())) {
                    sommets_obligatoires.add(sommet);
                }
            }
        }

        Map<Sommet, Integer> apports = new HashMap<>();

        for(Sommet sommet_strategique : getSommetsStrategiques()) {
            for(Sommet sommet : graphe.getSommets()) {
                if (sommet_strategique.getNom().equals(sommet.getNom())) {
                    apports.put(sommet, 30);
                }
            }
        }

        for(Sommet sommet_interets : getSommetsInterets()) {
            for(Sommet sommet : graphe.getSommets()) {
                if (sommet_interets.getNom().equals(sommet.getNom())) {
                    for(int ligne[] : cases_interets) {
                        if(ligne[0] == sommet.getX() && ligne[1] == sommet.getY()) {
                            apports.put(sommet, ligne[2]);
                        }
                    }
                }
            }
        }

        for(Sommet sommet_en_cours : graphe.getSommets()) {

            if(!sommets_obligatoires.contains(sommet_en_cours) && !sommet_en_cours.equals(depart)) {
                couts_des_sommets.put(sommet_en_cours, false);

                for(Sommet sommet : graphe.getSommets()) {

                    if(sommet != sommet_en_cours) {

                        for(Arc arc : sommet.getArcs()) {
                            if(arc.getArrivee().equals(sommet_en_cours)) {

                                if(arc.getPoids() > apports.get(sommet_en_cours)) {
                                    // on modifie à true si le sommet est onéreux
                                    couts_des_sommets.put(sommet_en_cours, true);
                                    break;
                                }
                            }
                        }
                    }

                }
            }
        }

        for(Map.Entry<Sommet, Boolean> entry : couts_des_sommets.entrySet()) {
            if(entry.getValue() == true) {
                sommets_a_supprimer.add(entry.getKey());
            }
        }

        for(Sommet sommet_a_supprimer : sommets_a_supprimer) {

            for(Sommet sommet : graphe.getSommets()) {
                for(Arc arc : sommet.getArcs()) {
                    if(arc.getArrivee().equals(sommet_a_supprimer)) {
                        arc_a_supprimer = arc;
                        break;
                    }
                }
                sommet.getArcs().remove(arc_a_supprimer);

                sommet.getSuccesseurs().remove(sommet_a_supprimer);
            }

            graphe.getSommets().remove(sommet_a_supprimer);
        }
    }

    /**
     * Cette méthode permet de trouver tous les chemins entre deux sommets de la carte.
     * @param depart Le sommet de départ.
     * @param arrivee Le sommet d'arrivée.
     * @param sommets_obligatoires Les sommets obligatoires à passer.
     * @return La liste des chemins.
     */
    public List<List<Sommet>> findAllPaths(Sommet depart, Sommet arrivee, List<Sommet> sommets_obligatoires) {
        List<List<Sommet>> chemins = new ArrayList<>();

        List<Sommet> cheminCourant = new ArrayList<>();
        Set<Sommet> tabou = new HashSet<>();

        explore(depart, arrivee, cheminCourant, tabou, chemins, sommets_obligatoires);

        return chemins;
    }

    /**
     * Fonction récursive qui explore tous les chemins possibles entre deux sommets selon l'algorithme de Tabou.
     * @param sommetCourant Le sommet en cours.
     * @param arrivee Le sommet d'arrivée.
     * @param cheminCourant Le chemin en cours.
     * @param tabou La liste des sommets tabous.
     * @param chemins La liste des chemins.
     * @param sommets_obligatoires Les sommets obligatoires à passer.
     */
    private void explore(Sommet sommetCourant, Sommet arrivee, List<Sommet> cheminCourant, Set<Sommet> tabou, List<List<Sommet>> chemins, List<Sommet> sommets_obligatoires) {
        cheminCourant.add(sommetCourant);

        int MAX_SIZE = 4096;

        if (sommetCourant.equals(arrivee)) {
            if (chemins.size() < MAX_SIZE) {
                chemins.add(new ArrayList<>(cheminCourant));
            } else {
                List<List<Sommet>> chemins_possibles = new ArrayList<>();

                for(List<Sommet> chemin : chemins) {
                    if(chemin.containsAll(sommets_obligatoires)) chemins_possibles.add(chemin);
                }

                Map<List<Sommet>, Integer> scores = new HashMap<>();
                for(List<Sommet> chemin : chemins_possibles) {
                    scores.put(chemin, computePathScore(chemin, this.graphe));
                }

                List<Sommet> meilleur_chemin = null;
                int meilleur_score = Integer.MIN_VALUE;
                for(Map.Entry<List<Sommet>, Integer> entry : scores.entrySet()) {
                    if(entry.getValue() > meilleur_score) {
                        meilleur_chemin = entry.getKey();
                        meilleur_score = entry.getValue();
                    }
                }

                chemins.clear();
                chemins.add(meilleur_chemin);
            }
        } else {
            tabou.add(sommetCourant);

            for (Arc arc : sommetCourant.getArcs()) {
                Sommet sommetVoisin = arc.getArrivee();
                if (!tabou.contains(sommetVoisin) && !cheminCourant.contains(sommetVoisin)) {
                    explore(sommetVoisin, arrivee, cheminCourant, tabou, chemins, sommets_obligatoires);
                }
            }

            tabou.remove(sommetCourant);
        }

        cheminCourant.remove(cheminCourant.size() - 1);
    }

    /**
     * Cette méthode permet de développer un chemin en un plus complet dans le graphe.
     * @param sommets La liste des sommets du chemin.
     * @param graphe Le graphe.
     * @return Le chemin développé.
     */
    public List<Sommet> buildPath(List<Sommet> sommets, Graphe graphe) {
        List<Sommet> chemin = new ArrayList<>();
        List<Sommet> chemin_tmp = new ArrayList<>();

        for(int i = 0; i < sommets.size() - 1; i++) {

            Sommet
                    A = null,
                    B = null;

            for(Sommet sommet : graphe.getSommets()) {
                if(sommet.getNom().equals(sommets.get(i).getNom())) A = sommet;
                if(sommet.getNom().equals(sommets.get(i + 1).getNom())) B = sommet;
            }

            chemin_tmp.clear();
            chemin_tmp = graphe.dijkstra(A, B);
            chemin_tmp.remove(0);

            chemin.addAll(chemin_tmp);
        }

        chemin_tmp.clear();
        chemin_tmp.addAll(chemin);

        chemin.clear();
        chemin.add(sommets.get(0));
        chemin.addAll(chemin_tmp);

        return chemin;
    }

    /**
     * Cette méthode affiche la composition d'un graphe.
     * @param graphe Le graphe.
     */
    public void printGraphe(Graphe graphe) {
        // Composition du graphe
        System.out.println("\t\tComposition du graphe : ");
        int
                nb_sommets = 0,
                nb_arcs = 0;

        for(Sommet sommet : graphe.getSommets()) {
            nb_sommets++;
            nb_arcs += sommet.getArcs().size();
        }

        System.out.println("\t\t\t- Nombre de sommets = " + nb_sommets);
        System.out.println("\t\t\t- Nombre d'arcs = " + nb_arcs);
    }

    /**
     * Cette méthode permet de trouver le chemin le plus court partant du départ et passant par tous les sommets obligatoires.
     * Il maximise le passage par des sommets stratégiques et d'intérêts.
     * @return Le chemin le plus court répondant aux critères.
     */
    public List<Sommet> pathFinder() {

        // On minimise le graphe
        System.out.print("\n\tMinimisation du graphe...");
        List<Sommet> sommets = new ArrayList<>();
            sommets.addAll(getSommetsStrategiques());
            sommets.addAll(getSommetsInterets());
            sommets.add(getDepart());
        Graphe graphe_minimise = streamlineGraphe(sommets, graphe);
        System.out.print("[Terminé]\n");

        printGraphe(graphe_minimise);

        // Réduction du graphe
        System.out.print("\n\tRéduction du graphe...");
        reduceGraphe(graphe_minimise);
        System.out.print("[Terminé]\n");

        printGraphe(graphe_minimise);

        // On récupère le depart dans le graphe minimisé
        Sommet depart = graphe_minimise.getSommets().get(
                graphe_minimise.getIndexOfSommet(getDepart())
        );

        // On récupère les sommets obligatoires dans le graphe minimisé
        System.out.println("\n\tSommet(s) obligatoire(s) :");
        List<Sommet> sommets_obligatoires = new ArrayList<>();
        for(Sommet sommet : getSommetsObligatoires()) {
            sommets_obligatoires.add(graphe_minimise.getSommets().get(
                    graphe_minimise.getIndexOfSommet(sommet)
            ));

            System.out.println("\t\t- " + sommet.getNom());
        }
        System.out.println();

        // On récupère tous les sommets sauf le départ
        List<Sommet> sommets_sans_depart = graphe_minimise.getSommets();
        sommets_sans_depart.remove(depart);

        System.out.print("\tRecherche des chemins possibles...");
        List<List<Sommet>> chemins = Collections.synchronizedList(new ArrayList<>());
        List<Thread> threads = new ArrayList<>();

        for(Sommet sommet : sommets_sans_depart) {
            Thread thread = new Thread(() -> {
                List<List<Sommet>> cheminsSommet = findAllPaths(depart, sommet, sommets_obligatoires);
                chemins.addAll(cheminsSommet);
            });

            threads.add(thread);
            thread.start();
        }

        // Attendre la fin de tous les threads
        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        System.out.print("[Terminé]\n");
        System.out.println("\tNombre de chemins trouvés : " + chemins.size());

        System.out.print("\tFiltrage des chemins possibles...");

        // On ne garde que les chemins qui passent par tous les sommets obligatoires
        List<List<Sommet>> chemins_possibles = new ArrayList<>();
        for(List<Sommet> chemin : chemins) {
            if(chemin.containsAll(sommets_obligatoires)) chemins_possibles.add(chemin);
        }

        System.out.print("[Terminé]\n");
        System.out.println("\tNombre de chemins après filtrage : " + chemins_possibles.size());

        System.out.print("\tCalcul des scores des chemins...");
        // On calcule le score de chaque chemin
        Map<List<Sommet>, Integer> scores = new HashMap<>();
        for(List<Sommet> chemin : chemins_possibles) {
            scores.put(chemin, computePathScore(chemin, graphe_minimise));
        }
        System.out.print("[Terminé]\n");

        System.out.print("\tSélection du chemin au plus grand score...");
        // On sélectionne le chemin avec le meilleur score
        List<Sommet> meilleur_chemin = null;
        int meilleur_score = Integer.MIN_VALUE;
        for(Map.Entry<List<Sommet>, Integer> entry : scores.entrySet()) {
            if(entry.getValue() > meilleur_score) {
                meilleur_chemin = entry.getKey();
                meilleur_score = entry.getValue();
            }
        }
        System.out.print("[Terminé]\n");

        return buildPath(meilleur_chemin, graphe);
    }
}
