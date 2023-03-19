 
 Classe Carte :

    Attributs :

        Vecteur d entiers : depart, cases_obligatoire
        Matrice d entiers : grille, cases_interets, cases_strategiques
        Graphe : graphe
    
    Constructeur :

        Carte :
            Paramètres : 
                Vecteur d entiers : depart, cases_obligatoires
                Matrice d entiers : grille, cases_interets, cases_strategiques
            Code :
                depart de notre carte <- depart
                cases_obligatoires de notre carte <- cases_obligatoires
                grille de notre carte <- grille
                cases_interets de notre carte <- cases_interets
                cases_strategiques de notre carte <- cases_strategiques
                graphe de notre carte <- nouveau graphe à partir de la grille
            FinCode
    
    Méthodes :

        Fonction getSommetsStrategiques :
            Paramètres : aucun
            Code :
                Retourner les sommets de notre carte qui correspondent à des cases stratégiques
            FinCode
        FinFonction
        
        Fonction getSommetsInterets :
            Paramètres : aucun
            Code :
                Retourner les sommets de notre carte qui correspondent à des cases d interets
            FinCode
        FinFonction
        
        Fonction getSommetsObligatoires :
            Paramètres : aucun
            Code :
                Récupèrer les sommets stratégiques via getSommetsStrategiques
                Retourner les sommets stratégiques qui correspondent à des cases cases_obligatoires
            FinCode
        FinFonction

        Fonction isStrategique :
            Paramètres : Sommet sommet
            Code :
                Récupèrer les sommets stratégiques via getSommetsStrategiques
                Retourner si vrai si sommet est dans les sommets stratégiques, faux sinon
            FinCode
        FinFonction

        Fonction isInteret :
            Paramètres : Sommet sommet
            Code :
                Récupèrer les sommets d interets via getSommetsInterets
                Retourner si vrai si sommet est dans les sommets d interets, faux sinon
            FinCode
        FinFonction
        
        Fonction isObligatoire :
            Paramètres : Sommet sommet
            Code :
                Récupèrer les sommets obligatoires via getSommetsObligatoires
                Retourner si vrai si sommet est dans les sommets obligatoires, faux sinon
            FinCode
        FinFonction

        Fonction computeScoreChemin :
            Paramètres : Liste de sommets chemin
            Code :
                Entiers (score_strategique, score_interet, score_passage) <- 0
                Pour chaque sommet dans chemin :
                    Si isStrategique(sommet) : score_strategique <- score_strategique + 30
                    Si isInteret(sommet) : score_interet <- score_interet + valeur de la case correspondante dans matrice_interet
                    Sinon : score_passage <- score_passage + valeur de la case correspondante dans grille
                Retourner score_strategique + score_interet - score_passage
            FinCode
        FinFonction

        Fonction streamLineGraphe :
            Paramètres : 
                Booléen interets
                Booléen strategies
            Code : 
                Liste de sommets sommet_crees <- Liste vide
                Liste de sommets sommets <- Selon si strategies est vrai, 
                    Récupèrer les sommets stratégiques via getSommetsStrategiques, 
                    sinon Récupèrer les sommets obligatoires via getSommetsObligatoires

                Ajouter à sommet_crees le sommet de départ de notre carte

                Si interets est vrai :
                    Ajouter à sommets les sommets d interets via getSommetsInterets
                FinSi
                
                Pour chaque sommet dans sommets :
                    Ajouter à sommet_crees un nouveau sommet avec les coordonnées du sommet
                FinPour
                
                Pour chaque sommet dans sommet_crees :
                    Pour chaque sommet2 dans sommet_crees :
                        Si sommet est différent de sommet2 :
                            Liste de sommets chemin <- chemin le plus court entre sommet et sommet2 via Dijkstra

                            Booléen contient_obstacle <- faux
                            Pour chaque sommet3 dans chemin :   
                                Si sommet3 est un obstacle via isObstacle :
                                    contient_obstacle <- vrai
                                    Sortir de la boucle
                            
                            Si contient_obstacle est faux :
                                Ajouter un arc entre sommet et sommet2 dans notre graphe avec le score du chemin comme poids via computeScoreChemin
                            FinSi
                        FinSi
                    FinPour
                FinPour

                Retourner notre graphe
            FinCode
        FinFonction

        Fonction buildChemin :
            Paramètres : Liste de sommets chemin
            Code :
                Liste de sommets chemin_final <- Liste vide
                Liste de sommets chemin_temp <- Liste vide

                Ajouter à chemin_final le premier sommet de chemin

                Pour chaque sommet dans chemin :
                    Sommet sommetA <- sommet
                    Sommet sommetB <- sommet suivant dans chemin
                    chemin_temp <- chemin le plus court entre sommetA et sommetB via Dijkstra

                    Retirer le premier élément de chemin_temp
                    Ajouter à chemin_final chemin_temp
                    Vider chemin_temp
                FinPour

                Ajouter à chemin_final le dernier sommet de chemin

                Retourner chemin_final
            FinCode
        FinFonction

        Fonction browseChemin :
            Paramètres : Graphe graphe_minimise
            Code :
                Liste de sommets chemin <- Liste vide

                Sommet sommet_courant <- sommet de départ de notre carte

                Ajouter à chemin sommet_courant

                Liste de sommets sommet_à_parcourir <- Liste vide

                Ajouter à sommet_à_parcourir les sommets obligatoires de graphe_minimise

                Tant que sommet_à_parcourir n est pas vide :

                    Arc arc_de_poids_max <- arc de poids maximal de sommet_courant si l arrivée de l arc n est pas dans chemin

                    Si arc_de_poids_max a un poids supérieur à 0 :
                        Ajouter à chemin l arrivée de arc_de_poids_max
                        sommet_courant <- l arrivée de arc_de_poids_max
                        Retirer l arrivée de arc_de_poids_max de sommet_à_parcourir si elle y est
                    Sinon :
                        Si sommet_courant est un sommet obligatoire :
                            Ajouter à chemin l arrivée de arc_de_poids_max
                            sommet_courant <- l arrivée de arc_de_poids_max
                            Retirer l arrivée de arc_de_poids_max de sommet_à_parcourir

                            Pour chaque sommet_restant dans sommet_à_parcourir :
                                Ajouter à chemin sommet_restant
                                Retirer sommet_restant de sommet_à_parcourir
                            FinPour
                        Sinon :
                            Sortir de la boucle
                        FinSi
                    FinSi
                FinTantQue

                Retourner buildChemin(chemin)

            FinCode
        FinFonction
                        
        Fonction traceMaxScoreChemin :
            Paramètres : aucun
            Code :
                Graphe graphe_minimise <- streamLineGraphe(Vrai, Vrai)
                Retourner browseChemin(graphe_minimise)
            FinCode
        FinFonction