
Classe Graphe :
    
    Attributs :
        Liste de sommets : sommets
    
    Constructeur :

        Graphe :
            Paramètres :
                Matrice d entiers : grille
            Code :
                Entiers nb_lignes, nb_colonnes
                Liste de sommets_crees

                Pour chaque ligne de la grille :
                    Pour chaque case de la ligne :
                        Créer un nouveau sommet (colonne, ligne, Selon la valeur de la case < 0 : Vrai Sinon  Faux) 
                    FinPour
                FinPour

                sommets <- sommets_crees

                Pour chaque sommets dans sommets :
                    Liste de sommets voisins <- voisinnage(sommet, nb_lignes - 1, nb_colonnes - 1)

                    Pour chaque voisin dans voisins :
                        Entier poids <- grille[x de voisin][y de voisin]
                        Entier poids_tmp <- Selon poids < 0 : Infinie Sinon poids
                        Ajouter un arc entre sommet et voisin avec poids_tmp
                    FinPour
                FinPour
            FinCode
        FinConstructeur
    
    Méthodes :

        Fonction getIndexOfSommet :
            Paramètres : Sommet sommet
            Code :
                Pour chaque sommet dans sommets :
                    Si sommet est égal à sommet :
                        Retourner l index du sommet dans sommets
                    FinSi
                FinPour

                Retourner -1
            FinCode
        FinFonction

        Fonction getIndexOfSommet :
            Paramètres : Entiers x, y
            Code :
                Pour chaque sommet dans sommets :
                    Si x de sommet est égal à x et y de sommet est égal à y :
                        Retourner l index du sommet dans sommets
                    FinSi
                FinPour

                Retourner -1
            FinCode
        FinFonction

        Fonction voisinnage :
            Paramètres : 
                Sommet sommet, 
                Entiers nb_lignes, nb_colonnes
            Code :
                Liste de sommets voisins <- Liste vide

                Entiers 
                    x <- x de sommet
                    y <- y de sommet

                Si x > 0 :
                    Ajouter à voisins le sommet à l index x - 1, y
                
                Si x < nb_lignes :
                    Ajouter à voisins le sommet à l index x + 1, y

                Si y > 0 :
                    Ajouter à voisins le sommet à l index x, y - 1
                
                Si y < nb_colonnes :
                    Ajouter à voisins le sommet à l index x, y + 1

                Retourner voisins
            FinCode
        FinFonction

        Fonction dijkstra :
            Paramètres : Sommet sommet_depart, sommet_arrivee
            Code :
                Liste de correspondances (Sommet Entier) distances <- Liste vide
                Liste de correspondances (Sommet Sommet) predecesseurs <- Liste vide
                Queue de sommets sommets_non_visites <- sommets

                Pour chaque sommet dans sommets :
                    Si sommet non null :
                        Ajouter à distances (sommet, Infinie)
                        Ajouter à predecesseurs (sommet, null);
                    FinSi
                FinPour

                distances[sommet_depart] <- 0
                Retirer sommet_depart de sommets_non_visites

                Tant que sommets_non_visites n est pas vide :
                    Sommet sommet_courant <- Sommet en tête de sommets_non_visites

                    Si sommet_courant est égal à sommet_arrivee :
                        Sortir de la boucle
                    FinSi

                    Pour chaque Arc arc de sommet_courant :
                        Sommet voisin <- sommet d arrivee de arc

                        Si voisin n est pas un obstacle via isObstacle :
                            Entier poids <- poids de arc

                            Si distances[sommet_courant] non null :
                                Ajouter à distances (sommet_courant, Infinie)
                            FinSi

                            Entier distance <- distances[sommet_courant] + poids

                            Si distance < distances[voisin] :
                                Retirer voisin de sommets_non_visites
                                Ajouter à distances (voisin, distance)
                                Ajouter à predecesseurs (voisin, sommet_courant)
                                Ajouter en tête de sommets_non_visites voisin
                            FinSi
                        FinSi
                    FinPour
                FinTantQue

                Liste de sommets chemin <- Liste vide

                Sommet sommet_courant <- sommet_arrivee

                Tant que sommet_courant a un predecesseur :
                    Ajouter à chemin sommet_courant
                    sommet_courant <- predecesseur de sommet_courant
                FinTantQue
                
                Ajouter à chemin sommet_courant
                Inverser les sommets de chemin

                Retourner chemin
            FinCode
        FinFonction

                    


