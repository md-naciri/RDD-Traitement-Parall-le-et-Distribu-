# TP RDD - Traitement Parallèle et Distribué

Application Java utilisant Apache Spark pour l'analyse des données de ventes avec les RDD (Resilient Distributed Datasets).

## Objectifs

Le projet implémente deux analyses principales:

1. **Analyse par ville**: Calcul du total des ventes par ville
2. **Analyse par ville et année**: Calcul du total des ventes par ville et par année

## Structure des données

Le fichier `ventes.txt` contient des données au format:
```
date ville produit prix
27/01/2025 Tangier AsusZenBook 5950
10/02/2024 Tangier HPEnvy 6126
```

## Exécution locale

```bash
# Compilation
mvn clean package

# Exécution
java -cp target/TP_RDD-1.0-SNAPSHOT.jar Main
```

## Exécution sur cluster Docker

```bash
# Démarrage du cluster
docker-compose up -d

# Soumission du job
docker exec rdd-spark-master spark-submit \
  --class Main \
  --master spark://rdd-master:7077 \
  /opt/bitnami/spark/apps/TP_RDD-1.0-SNAPSHOT.jar

# Interface web
http://localhost:8080
```

## Structure du projet

```
TP_RDD/
├── src/main/java/Main.java
├── data/ventes.txt
├── apps/
├── docker-compose.yml
├── pom.xml
└── README.md
```

## Fonctionnalités RDD utilisées

- **Transformation**: `map`, `filter`, `mapToPair`
- **Actions**: `reduceByKey`, `sortByKey`, `foreach`
- **Optimisations**: Traitement parallèle et distribué