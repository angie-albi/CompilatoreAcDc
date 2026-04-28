<div align="center">
  <h1>⚡ Compilatore AcDc</h1>
  <p>
    Un compilatore completo sviluppato in Java che traduce il codice sorgente del linguaggio "ac" in istruzioni per la calcolatrice a stack "dc" di Linux.
    <br />
    <br />
    <img src="https://img.shields.io/badge/Java-17+-orange?style=for-the-badge&logo=openjdk" alt="Java Version">
    <img src="https://img.shields.io/badge/Architettura-AST-red?style=for-the-badge" alt="Abstract Syntax Tree">
    <img src="https://img.shields.io/badge/Pattern-Visitor-blueviolet?style=for-the-badge" alt="Visitor Pattern">
    <img src="https://img.shields.io/badge/Test-Bash_Script-25A162?style=for-the-badge&logo=gnu-bash" alt="Bash Testing">
  </p>
</div>

---

## 🧐 Di cosa si tratta?

Questo progetto implementa un **traduttore (compilatore)** per il linguaggio didattico `"ac"`. Il software analizza un file sorgente per verificarne la correttezza lessicale, sintattica e semantica, producendo un file eseguibile `.dc`. Questo output contiene il codice macchina in notazione postfissa, eseguibile tramite l'utility Unix `dc` (Desk Calculator).

L'architettura segue i principi della moderna ingegneria dei linguaggi, utilizzando il **Design Pattern Visitor** per separare le fasi di analisi e generazione dall'Albero di Sintassi Astratta (AST).

---

## ✨ Funzionalità Principali (Fasi di Compilazione)

### 🔍 1. Analisi Lessicale (Scanner)
* **Tokenizzazione:** Lettura del file sorgente carattere per carattere e raggruppamento in unità logiche (Token).
* **Gestione Errori:** Riconoscimento immediato di caratteri non supportati o numeri malformati (es. restrizione a max 5 cifre decimali).

### 🌳 2. Analisi Sintattica (Parser)
* **Recursive-Descent:** Implementazione di un parser a discesa ricorsiva (Top-Down) predittivo.
* **Generazione AST:** Costruzione dell'Albero di Sintassi Astratta (`NodeProgram`, `NodeAssign`, `NodeBinOp`, ecc.) che rappresenta la struttura del codice in memoria.

### 🧠 3. Analisi Semantica (Type Checker)
* **Symbol Table:** Gestione degli scope e verifica delle dichiarazioni (blocco delle variabili non dichiarate o dichiarate due volte).
* **Type Checking:** Controllo della compatibilità dei tipi (es. impedisce l'assegnazione di un `float` a una variabile `int`).

### ⚙️ 4. Generazione del Codice (Code Generator)
* **Traduzione Postfissa:** Conversione delle espressioni matematiche infisse nell'AST in notazione postfissa (RPN) supportata da `dc`.
* **Gestione Registri:** Allocazione dinamica dei registri della calcolatrice e controllo sul limite fisico degli stessi.
* **Precisione Floating Point:** Generazione di comandi specifici (`5 k`) per garantire la corretta precisione decimale durante le divisioni tra float.

---

## 🏗️ Struttura del Progetto

L'organizzazione dei package segue rigorosamente le fasi di compilazione:

```text
.
├── src/                      # Codice sorgente Java
│   ├── main/                 # Punto di ingresso (Main.java)
│   ├── scanner/              # Lexer e gestione eccezioni lessicali
│   ├── parser/               # Analizzatore sintattico
│   ├── ast/                  # Nodi dell'Albero di Sintassi Astratta
│   ├── symbolTable/          # Gestione simboli e registri dc
│   ├── visitor/              # Implementazione del Pattern Visitor
│   └── test/data/            # Dataset per i test (Scanner, Parser, E2E)
├── docs/                     # Documentazione Javadoc per GitHub Pages
└── runTestCompletoE2E.sh     # Script di automazione per test End-to-End
```

---

## 📖 Documentazione Tecnica (Javadoc)

Il progetto è interamente documentato. La documentazione ipertestuale è consultabile online tramite GitHub Pages:

👉 **[Consulta il Javadoc del Compilatore AcDc](https://angie-albi.github.io/CompilatoreAcDc/)**

---

## 🧪 Testing Automatizzato

Il sistema include una suite di test **End-to-End** che copre casi di successo, errori lessicali, sintattici, semantici e limiti fisici dei registri.

Per eseguire i test su Linux/WSL/macOS:
```bash
chmod +x runTestCompletoE2E.sh
./runTestCompletoE2E.sh
```

---

## 🚀 Guida all'Utilizzo Manuale

Segui questi passaggi per scaricare e testare il compilatore in locale:

1. **Clona la repository:**
   ```bash
   git clone [https://github.com/angie-albi/CompilatoreAcDc.git](https://github.com/angie-albi/CompilatoreAcDc.git)
   ```
2. **Entra nella cartella dei sorgenti:**
   ```bash
   cd CompilatoreAcDc/src
   ```
3. **Compila l'intero progetto:**
   ```bash
   javac main/Main.java
   ```
4. **Esegui il compilatore passando un file sorgente:**
   ```bash
   java main.Main test/data/testE2E/01_test_corretto_completo.txt
   ```
   *Il sistema genererà un file `.dc` con lo stesso nome nella directory del file sorgente.*

5. **(Opzionale) Esegui il codice generato con dc (richiede Linux/WSL/macOS):**
   ```bash
   dc test/data/testE2E/01_test_corretto_completo.dc
   ```

---

### 👤 Autore
Sviluppato da **Angie Albitres**