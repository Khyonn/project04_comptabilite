# MyERP 
*Ce projet est à but pédagogique*.

## Enoncé
Votre équipe est en train de réaliser un système de facturation et de comptabilité pour un client. Le développement a débuté depuis quelques temps et vous devez commencer à vérifier que l'application fonctionne correctement, qu'elle répond bien aux règles de gestion et les respecte.

## Résultat attendu 
* Les 4 erreurs de développements dans le projet fourni sont identifiées et résolues
* Le développement a été complété en suivant les TODO
* Les tests unitaires ont été réalisés à l’aide de JUnit
* Les tests d’intégration ont été réalisés à l’aide des “profiles” Maven
* L'ensemble des modules a un code coverage de 75% minimum
* Un serveur d'intégration est installé et configuré (Jenkins / Travis CI / GitLab CI au choix). 
* Un rapport d'exécution des tests est automatiquement généré à chaque commit. 
* Un logiciel de versionning a été correctement utilisé 

## Environnement
Le projet maven écrit en Java utilise Spring et est découpé en quatre module (model, technical, consumer et business)  
Le SGBD PostgreSQL et les données sont dockerisés  
Les requêtes SQL sont stockées sur le fichier sqlContext.xml

## Avancement
:white_square_button: erreurs corrigées  
:white_square_button: fonctionnalités complétées  
:white_square_button: tests réalisés avec JUnit et Mockito  
:white_square_button: serveur d'intégration Jenkins mis en place  
:white_square_button: rapports de tests et de couverture disponibles sur le serveur SonarQube  
:white_square_button: couverture du code par les tests : 84.8 %  

## Organisation du répertoire

*   `doc` : documentation
*   `docker` : répertoire relatifs aux conteneurs _docker_ utiles pour le projet
    *   `dev` : environnement de développement
*   `src` : code source de l'application

 
## Environnement de développement

Les composants nécessaires lors du développement sont disponibles via des conteneurs _docker_.
L'environnement de développement est assemblé grâce à _docker-compose_
(cf docker/dev/docker-compose.yml).

Il comporte :

*   une base de données _PostgreSQL_ contenant un jeu de données de démo (`postgresql://127.0.0.1:9032/db_myerp`)



### Lancement

    cd docker/dev
    docker-compose up


### Arrêt

    cd docker/dev
    docker-compose stop


### Remise à zero

    cd docker/dev
    docker-compose stop
    docker-compose rm -v
    docker-compose up
