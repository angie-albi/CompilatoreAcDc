package main;

import ast.NodeProgram;
import parser.Parser;
import parser.SyntacticException;
import scanner.Scanner;
import visitor.CodeGeneratorVisitor;
import visitor.TypeCheckingVisitor;

import java.io.File;
import java.io.FileWriter;

/**
 * Classe principale e punto d'ingresso (Entry Point) del compilatore "ac"
 * 
 * * Il software esegue le fasi di analisi lessicale, sintattica, semantica e 
 * generazione del codice, traducendo il sorgente in istruzioni per 'dc'
 */
public class Main {

    /**
     * Metodo principale per l'esecuzione del compilatore
     * 
     * @param args Argomenti da riga di comando. Il primo argomento (facoltativo) 
     * rappresenta il file sorgente.
     */
    public static void main(String[] args) {
        
        // 1. DETERMINAZIONE DEL FILE DI INPUT
        String filePath;
        
        if (args.length > 0) {
            // Se l'utente specifica un file, usiamo quello
            filePath = args[0];
        } else {
            // Default a input.txt se non viene passato alcun argomento
            filePath = "input.txt";
            System.out.println("Nessun file specificato. Utilizzo del default: " + filePath);
        }

        // 2. VERIFICA ESISTENZA FILE (Fail-Fast)
        File inputFile = new File(filePath);
        if (!inputFile.exists()) {
            System.err.println("Errore Critico: Il file sorgente '" + filePath + "' non è stato trovato");
            System.err.println("Assicurati che il file esista nella cartella del progetto");
            return; 
        }

        // Calcolo dinamico del percorso di output (es. .txt -> .dc)
        String outputFilePath = filePath.contains(".") 
            ? filePath.substring(0, filePath.lastIndexOf('.')) + ".dc" 
            : filePath + ".dc";

        try {
            System.out.println("Inizio compilazione del file: " + filePath + "\n");

            // 3. ANALISI LESSICALE E SINTATTICA (Scanner e Parser)
            Scanner scanner = new Scanner(filePath);
            Parser parser = new Parser(scanner);
            NodeProgram ast = parser.parse(); 
            System.out.println("[OK] Analisi Sintattica completata");

            // 4. ANALISI SEMANTICA (Type Checker)
            TypeCheckingVisitor typeChecker = new TypeCheckingVisitor();
            ast.accept(typeChecker);

            if (typeChecker.hasErrors()) {
                System.err.println("Compilazione fallita: Rilevati errori semantici");
                return;
            }
            System.out.println("[OK] Analisi Semantica completata");

            // 5. GENERAZIONE DEL CODICE (Code Generator)
            CodeGeneratorVisitor codeGen = new CodeGeneratorVisitor();
            ast.accept(codeGen);

            if (!codeGen.getLog().isEmpty()) {
                System.err.println("Compilazione fallita (Generazione Codice): " + codeGen.getLog());
                return;
            }
            System.out.println("[OK] Generazione del Codice completata\n");

            // 6. GESTIONE OUTPUT
            String codiceDcGenerato = codeGen.getCodiceDc();
            
            System.out.println("============= CODICE DC GENERATO =============");
            System.out.println(codiceDcGenerato);
            System.out.println("==============================================");

            // Salvataggio su file fisico
            creaFileOutput(outputFilePath, codiceDcGenerato);

        } catch (SyntacticException e) {
            // Gestione specifica per errori legati alla grammatica del linguaggio
            System.err.println("\nERRORE DI SINTASSI O LESSICO:");
            System.err.println(e.getMessage());
            
        } catch (Exception e) {
            // Gestione per errori imprevisti di sistema (I/O, permessi, ...)
            System.err.println("\nSi è verificato un errore di sistema imprevisto:");
            e.printStackTrace();
        }
    }
    
    /**
     * Scrive il contenuto generato in un nuovo file sul filesystem
     * 
     * @param path Il percorso di destinazione del file
     * @param contenuto Il codice 'dc' risultante dalla compilazione
     * @throws Exception Propaga errori di I/O per la gestione centralizzata nel main
     */
    private static void creaFileOutput(String path, String contenuto) throws Exception {
        File file = new File(path);
        
        // Uso del FileWriter per la persistenza del dato
        try (FileWriter writer = new FileWriter(file)) {
            writer.write(contenuto);
        }
        
        System.out.println("\n[SUCCESSO] File di output generato correttamente:");
        System.out.println(file.getAbsolutePath());
    }
}