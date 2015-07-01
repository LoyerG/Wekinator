# Wekinator
Ce projet a pour objectif de créer un jeu type « Akinator » dont le but est de
deviner à quel personnage célèbre pense un utilisateur. J'ai décidé de
cantonner le projet aux acteurs et actrice célèbres.
Le principe du jeu est très simple : l’utilisateur pense à un acteur ou une actrice,
et l’ordinateur devra deviner de qui il s’agit en posant des questions auxquelles
l’utilisateur doit répondre par oui, non, probablement oui, probablement non, je ne sais
pas. Plus il y’a de parties jouées, plus l’ordinateur apprendra de nouveaux acteurs
pour les joueurs futurs.
J'ai choisi de développer ce projet sous la forme d’une application
Android. Pour le moteur et la stratégie de recherche, je me suis basé sur
un système de scoring qui cherchera la meilleure solution parmi les réponses
possibles. A chaque réponse de l’utilisateur, on ajoute des points au score si la
réponse donnée est proche de celle attendue et inversement, on retire des points
du score si la réponse donnée s’écarte de la réponse attendue. La première question
est sélectionnée de manière aléatoire. Pour les questions qui suivent, on sélectionne
à chaque fois la question la plus discriminante parmi celles qui ne sont pas encore
posées.
Pour la gestion des erreurs de l’utilisateur j'ai imaginé un système
d’attribution de points positifs ou négatifs selon que les réponses sont proches ou
éloignées des réponses attendues. L’heuristique de notation choisie est la suivante :
-  1 : oui
-  2 : probablement oui
-  3 : ne sais pas
-  4 : probablement non
-  5 : non
-  0 : réponse inconnue
