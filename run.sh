#!/bin/bash

# TP RDD - Script d'exécution
# Auteur: Votre Nom

set -e

echo "=== TP RDD - Traitement Parallèle et Distribué ==="
echo

# Vérification Java
if ! command -v java &> /dev/null; then
    echo "❌ Java n'est pas installé"
    exit 1
fi

# Vérification Maven
if ! command -v mvn &> /dev/null; then
    echo "❌ Maven n'est pas installé"
    exit 1
fi

echo "✅ Environnement vérifié"

# Compilation
echo "🔧 Compilation du projet..."
mvn clean package -q

if [ $? -eq 0 ]; then
    echo "✅ Compilation réussie"
else
    echo "❌ Erreur de compilation"
    exit 1
fi

# Exécution locale
echo "🚀 Exécution locale..."
echo
java -cp target/TP_RDD-1.0-SNAPSHOT.jar Main data/ventes.txt

echo
echo "✅ Exécution terminée"

# Option cluster Docker
read -p "Voulez-vous lancer le cluster Docker? (y/N): " -n 1 -r
echo
if [[ $REPLY =~ ^[Yy]$ ]]; then
    echo "🐳 Démarrage du cluster Docker..."
    
    # Copie du JAR
    cp target/TP_RDD-1.0-SNAPSHOT.jar apps/
    
    # Démarrage des conteneurs
    docker-compose up -d
    
    echo "⏳ Attente du démarrage des services..."
    sleep 10
    
    echo "🔥 Soumission du job sur le cluster..."
    docker exec rdd-spark-master spark-submit \
        --class Main \
        --master spark://rdd-master:7077 \
        /opt/bitnami/spark/apps/TP_RDD-1.0-SNAPSHOT.jar \
        /opt/bitnami/spark/data/ventes.txt
    
    echo
    echo "🌐 Interface Spark disponible: http://localhost:8080"
    echo "📊 Logs des workers disponibles dans l'interface"
fi