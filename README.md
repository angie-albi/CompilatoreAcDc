<div align="center">
  <h1>⚡ Compilatore AcDc</h1>
  <p>
    Un compilatore completo sviluppato in Java che traduce il codice sorgente del linguaggio "ac" in istruzioni per la calcolatrice a stack "dc" di Linux.
    <br />
    <br />
    <img src="https://img.shields.io/badge/Java-17+-orange?style=for-the-badge&logo=openjdk" alt="Java Version">
    <img src="https://img.shields.io/badge/Architettura-AST-red?style=for-the-badge" alt="Abstract Syntax Tree">
    <img src="https://img.shields.io/badge/Pattern-Visitor-blueviolet?style=for-the-badge" alt="Visitor Pattern">
    <img src="https://img.shields.io/badge/Test-JUnit_5-25A162?style=for-the-badge&logo=junit5" alt="JUnit 5">
    <img src="https://img.shields.io/badge/Test-Bash_Script-4EAA25?style=for-the-badge&logo=gnu-bash" alt="Bash Testing">
  </p>
</div>

---

## 🧐 Di cosa si tratta?

Questo progetto implementa un **traduttore (compilatore)** per il linguaggio didattico `ac`. Il software analizza un file sorgente per verificarne la correttezza lessicale, sintattica e semantica, producendo un file eseguibile `.dc`. Questo output contiene il codice macchina in notazione postfissa, eseguibile tramite l'utility Unix `dc` (Desk Calculator).

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
│   ├── scanner/              # Analizzatore Lessicale
│   ├── parser/               # Analizzatore Sintattico
│   ├── ast/                  # Nodi dell'Albero di Sintassi Astratta
│   ├── symbolTable/          # Gestione simboli e registri dc
│   ├── token/                # Definizione dei token testuali
│   ├── visitor/              # Implementazione del Pattern Visitor
│   │   └── typeDescriptor/   # Descrittori dei tipi per l'analisi semantica
│   ├── test/                 # Classi di test JUnit
│   │   └── data/             # File di testo per i test (Scanner, Parser, E2E, ecc.)
│   └── input.txt             # File sorgente di default per testare il compilatore
├── docs/                     # Documentazione Javadoc per GitHub Pages
└── runTestCompletoE2E.sh     # Script di automazione per test End-to-End
```

---

## 📖 Documentazione Tecnica (Javadoc)

Il progetto è interamente documentato. È possibile consultare la documentazione tecnica completa in formato ipertestuale in due modi:

* **Online:** Tramite GitHub Pages 👉 **[Consulta il Javadoc](https://angie-albi.github.io/CompilatoreAcDc/)**
* **In locale:** Navigando all'interno della cartella `doc/` del progetto e aprendo il file **`index.html`** con un qualsiasi browser web.

---

## 🧪 Testing e Qualità

La stabilità del compilatore è garantita da un doppio livello di test:

* ✅ **Test Unitari (JUnit 5):** Verifica isolata di ogni componente (`TestScanner`, `TestParser`, `TestTypeCheckingVisitor`, `TestCodeGeneratorVisitor`) per assicurare la corretta costruzione dell'AST e la propagazione degli errori.
* ✅ **Test End-to-End (Bash Script):** Automazione che compila file sorgente complessi e verifica l'output finale del programma, inclusa la gestione dei limiti fisici dei registri.

---

## 💻 Importazione in Eclipse IDE

Per integrare rapidamente il progetto nell'ambiente di sviluppo Eclipse:

1. **Apertura**: Selezionare il menu **File** e cliccare sulla voce **Open Projects from File System...**.
2. **Selezione della directory**: Nella finestra di dialogo, cliccare sul pulsante **Directory...** e individuare la cartella radice del progetto `CompilatoreAcDc` (quella contenente le cartelle `src` e `doc`).
3. **Conferma**: Verificare che la casella corrispondente al progetto sia selezionata e premere **Finish**. Eclipse configurerà automaticamente l'ambiente e riconoscerà i package Java.

---

## 🚀 Modalità di Esecuzione (da Eclipse)

Una volta importato il progetto, puoi testare il compilatore in due modi:

* **Esecuzione dell'intero Compilatore:** Cliccare con il tasto destro su `src/main/Main.java` e selezionare **Run As > Java Application**. Assicurati di aver impostato il percorso del file di input (`.txt`) all'interno del codice del Main.
* **Esecuzione della Suite di Test:** Cliccare con il tasto destro sulla cartella `src/test` (oppure su una singola classe di test) e selezionare **Run As > JUnit Test** per verificare che tutti i componenti abbiano la "barra verde".

---

## ⚙️ Guida all'Utilizzo Manuale (Terminale)

Se preferisci utilizzare la riga di comando senza un IDE:

1. **Clona la repository:**
   ```bash
   git clone https://github.com/angie-albi/CompilatoreAcDc.git
   
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
   java main.Main input.txt
   ```
5. **(Opzionale) Esegui il codice generato con dc (richiede Linux/WSL/macOS):**
   ```bash
   dc input.dc
   ```

*Per eseguire i test automatizzati Bash:*
```bash
cd ..
chmod +x runTestCompletoE2E.sh
./runTestCompletoE2E.sh
```

---

### 👤 Autore
Sviluppato da **Angie Albitres**