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

Questo progetto è l'implementazione di un vero e proprio **traduttore (compilatore)** per un linguaggio didattico chiamato `"ac"`. L'applicativo legge un file sorgente, ne analizza la validità formale e semantica, e genera in output un file eseguibile `.dc` contenente il codice macchina equivalente, pronto per essere calcolato dalla storica utility Unix `dc` (Desk Calculator).

Il software è stato ingegnerizzato seguendo le *best practices* della progettazione dei compilatori, separando nettamente le fasi di compilazione e implementando il **Design Pattern Visitor** per l'esplorazione dell'Albero di Sintassi Astratta (AST).

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

Il codice sorgente è rigorosamente disaccoppiato in package tematici:

```text
.
├── src/                      # Codice sorgente Java
│   ├── main/                 # Entry point del compilatore (Main.java)
│   ├── scanner/              # Analizzatore lessicale e gestione token
│   ├── parser/               # Analizzatore sintattico e regole grammaticali
│   ├── ast/                  # Nodi dell'Albero di Sintassi Astratta (AST)
│   ├── symbolTable/          # Tabella dei simboli e gestione registri
│   ├── visitor/              # Pattern Visitor (TypeChecking e CodeGeneration)
│   └── test/data/testE2E/    # File sorgenti (.txt) per i test automatici
├── docs/                     # Documentazione Javadoc per GitHub Pages
└── run_e2e_tests.sh          # Script Bash per l'automazione dei test
```

---

## 📖 Documentazione Tecnica (Javadoc)

L'intero progetto è documentato a livello di classi e metodi. La documentazione è ospitata online grazie a **GitHub Pages**.

👉 **[Consulta il Javadoc del Compilatore AcDc](https://angie-albi.github.io/CompilatoreAcDc/)**

---

## 🧪 Testing Automatizzato (End-to-End)

Il progetto include una suite di 10 test End-to-End studiati per coprire tutte le ramificazioni del compilatore (Golden path, eccezioni lessicali, sintattiche, semantiche ed esaurimento registri).

Per eseguire l'intera suite di test in modo automatico su sistemi Unix (Linux/macOS/WSL), aprire il terminale nella root del progetto ed eseguire:
```bash
./run_e2e_tests.sh
```
Lo script compilerà i file in sequenza, evidenzierà gli errori intercettati in modo pulito e, in caso di successo, invierà automaticamente il codice generato alla calcolatrice `dc` per stamparne il risultato matematico.

---

## ⚙️ Installazione e Utilizzo Manuale

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