import objets.Carte;
import objets.Graphe;
import objets.Sommet;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static void afficherChemin(List<Sommet> chemin) {

        for(int i = 0; i < chemin.size() - 1; i++) {
            System.out.print(chemin.get(i).getNom() + " -> ");
        }
        System.out.print(chemin.get(chemin.size() - 1).getNom());

        System.out.println();
    }

    public static void traduirePourLeCode(int[][] matrice_interets, int[][] matrice_strategies, int[] obligatoire, int[] depart) {
        for (int i = 0; i < matrice_strategies.length; i++) {
            matrice_strategies[i][0]--;
            matrice_strategies[i][1]--;
        }

        for (int i = 0; i < matrice_interets.length; i++) {
            matrice_interets[i][0] -= 1;
            matrice_interets[i][1] -= 1;
        }

        for (int i = 0; i < obligatoire.length; i++) {
            obligatoire[i] -= 1;
        }

        depart[0]--;
        depart[1]--;
    }

    public static int[][] importMatrice(String separateur, String chemin_fichier) {

        int[][] matrice = null;

        try (BufferedReader br = new BufferedReader(new FileReader(chemin_fichier))) {
            String ligne;
            int nbLignes = 0;
            while ((ligne = br.readLine()) != null) {
                nbLignes++;
            }
            matrice = new int[nbLignes][];
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try (BufferedReader br = new BufferedReader(new FileReader(chemin_fichier))) {
            String ligne;
            int i = 0;
            while ((ligne = br.readLine()) != null) {
                String[] tab = ligne.split(separateur);
                int[] ligneMatrice = new int[tab.length];
                for (int j = 0; j < tab.length; j++) {
                    ligneMatrice[j] = Integer.parseInt(tab[j]);
                }
                matrice[i++] = ligneMatrice;
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return matrice;
    }

    public static Carte importerDonneesATravailler(String repertoire_des_donnees) {

        int[] depart = new int[2];

        Scanner sc = null;

        try {
            sc = new Scanner(new File(repertoire_des_donnees + "/depart.txt"));

            sc.useDelimiter("_");

            depart[0] = sc.nextInt();
            depart[1] = sc.nextInt();

            sc.close();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        int[] obligations = new int[0];

        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(repertoire_des_donnees + "/obligatoire.txt"));
            String line = br.readLine();
            String[] ligne = line.split("_");
            obligations = new int[ligne.length];

            for(int i = 0; i < ligne.length; i++) {
                obligations[i] = Integer.parseInt(ligne[i]);
            }
            br.close();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        int[][] matrice_adjacences = importMatrice("\t", repertoire_des_donnees + "/matrice.txt");

        int[][] matrice_interets = importMatrice("_", repertoire_des_donnees + "/matrice_interet.txt");

        int[][] matrice_strategies = importMatrice("_", repertoire_des_donnees + "/matrice_strategique.txt");

        System.out.print("Traduction pour le code ...");
            traduirePourLeCode(matrice_interets, matrice_strategies, obligations, depart);
        System.out.print("[Terminé]\n");

        return new Carte(depart, obligations, matrice_adjacences, matrice_interets, matrice_strategies);
    }

    public static void dataset(List<Sommet> chemin, String chemin_fichier_sorti) {

        System.out.print("Création du dataset...");
        int[][] dataset = new int[chemin.size()][2];

        for(int indice = 0; indice < chemin.size(); indice++) {
            dataset[indice][0] = chemin.get(indice).getX() + 1;
            dataset[indice][1] = chemin.get(indice).getY() + 1;
        }

        System.out.print("[Terminé]\n");

        System.out.print("Ecriture du dataset à l'adresse suivante : " + chemin_fichier_sorti + "...");
        try {
            File fichier = new File(chemin_fichier_sorti);
            fichier.createNewFile();

            FileWriter writer = new FileWriter(fichier);

            for(int[] ligne : dataset) {
                writer.write(ligne[0] + ";" + ligne[1] + "\n");
            }

            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.print("[Terminé]\n");
    }

    public static void main(String[] args) {

        String repertoire_courant = System.getProperty("user.dir");

        System.out.println("\n[Création de la carte]");
            Carte c = importerDonneesATravailler(repertoire_courant + "/IO/in");
            System.out.println("\t" + c.getGraphe());
        System.out.println("[Terminée]");

        List<Sommet> chemin_score_max = new ArrayList<>(c.traceMaxScoreChemin());
        List<Sommet> chemin_dist_min = new ArrayList<>(c.traceMinDistChemin());

        List<Sommet> chemin_strategique = new ArrayList<>(c.tracePersonalizedChemin(false, true));

        String repertoire_dataset = repertoire_courant + "/IO/out";

        System.out.println("\n[Chemin obtenu de score max]");
            System.out.print("\t");
            afficherChemin(chemin_score_max);
        System.out.println("[Fin]");

        dataset(chemin_score_max, repertoire_dataset + "/dataset_chemin_score_max.txt");

        System.out.println("\n[Chemin obtenu de dist min]");
            System.out.print("\t");
            afficherChemin(chemin_dist_min);
        System.out.println("[Fin]");

        dataset(chemin_dist_min, repertoire_dataset + "/dataset_chemin_dist_min.txt");

        System.out.println("\n[Chemin stratégique]");
            System.out.print("\t");
            afficherChemin(chemin_strategique);
        System.out.println("[Fin]");

        dataset(chemin_strategique, repertoire_dataset + "/dataset_chemin_strategique.txt");
    }
}