#!/bin/bash

# Entra nella cartella dei sorgenti
cd src

# Cicla su tutti i file .txt che si trovano nella cartella E2E
for filepath in test/data/testE2E/*.txt; do
    echo -e "\n\033[1;34m---------------------------------------\033[0m"
    echo -e "\033[1;33m🔄 ESECUZIONE TEST: $filepath\033[0m"
    echo -e "\033[1;34m---------------------------------------\033[0m"
    
    # Passa il percorso completo del file al Main
    java main.Main "$filepath"
    
    # Genera il nome del file .dc atteso
    nome_dc="${filepath%.txt}.dc"
    if [ -f "$nome_dc" ]; then
        echo -e "\033[1;32m\n▶ OUTPUT DI DC:\033[0m"
        dc "$nome_dc"
    fi
done