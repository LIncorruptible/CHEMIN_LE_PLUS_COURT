
Méthode main :

    Chaîne de caractères repertoire_courant <- répertoire courant

    Carte c <- importerDonneesATravailler(repertoire_courant + "/IO/in")

    Liste de sommets chemin_score_max <- c.traceMaxScoreChemin()

    Afficher chemin_score_max via afficherChemin()

    Chaîne de caractères repertoire_dataset <- répertoire_courant + "/IO/out"

    Créer le dataset via dataset(chemin_score_max, repertoire_dataset + "/dataset_chemin_strategique.txt")
    
Fin Méthode