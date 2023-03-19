
Classe Sommet :

    Attributs :
        Chaîne de caractères : nom
        Entiers : x, y
        Liste de sommets : predecesseurs, successeurs
        Liste d arcs : arcs
        Booléen : isUnObstacle

    Constructeur :

        Sommet :
            Paramètres :
                Chaîne de caractères : nom
                Entiers : x, y
                Liste de sommets : predecesseurs, successeurs
                Booléen : isUnObstacle
            Code :
                nom de notre sommet <- nom
                x de notre sommet <- x
                y de notre sommet <- y
                predecesseurs de notre sommet <- predecesseurs
                successeurs de notre sommet <- successeurs
                arcs de notre sommet <- Liste vide
                isUnObstacle de notre sommet <- isUnObstacle
            FinCode
        FinConstructeur

        Sommet :
            Paramètres : Entiers x, y
            Code :
                Surcharger le constructeur avec :
                    nom = (x + 1) + "_" + (y + 1) , x, y, predecesseurs = Liste vide, successeurs = Liste vide, isUnObstacle = faux
            FinCode
        FinConstructeur

        Sommet : 
            Paramètres : 
                Entiers x, y
                Booléen isUnObstacle
            Code : 
                Surcharger le constructeur avec :
                    nom = (x + 1) + "_" + (y + 1) , x, y, predecesseurs = Liste vide, successeurs = Liste vide, isUnObstacle
            FinCode
        FinConstructeur

    Méthodes :

        Fonction isUnObstacle : 
            Paramètres : aucun
            Code :
                Retourner isUnObstacle de notre sommet
            FinCode
        FinFonction

        Fonction getArcs :
            Paramètres : aucun
            Code :
                Retourner arcs de notre sommet
            FinCode
        FinFonction
