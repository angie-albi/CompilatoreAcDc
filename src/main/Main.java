package main;

import ast.NodeProgram;
import parser.Parser;
import scanner.Scanner;
import visitor.CodeGeneratorVisitor;
import visitor.TypeCheckingVisitor;

/**
 * Classe principale che funge da punto d'ingresso (Entry Point) del compilatore "ac"
 * Legge un file sorgente, lo analizza e genera il codice macchina per 'dc'
 */
public class Main {

    /**
     * Metodo principale eseguito all'avvio del programma
     * 
     * @param args Argomenti da riga di comando (non utilizzati in questo esempio base)
     */
    public static void main(String[] args) {
    	
        String filePath = "testCompleto.txt"; 

        try {
            System.out.println("Inizio compilazione del file: " + filePath + "\n");

            // 1. ANALISI LESSICALE (Scanner)
            Scanner scanner = new Scanner(filePath);
            
            // 2. ANALISI SINTATTICA (Parser)
            Parser parser = new Parser(scanner);
            NodeProgram ast = parser.parse(); // costruisce l'Albero di Sintassi Astratta (AST)
            System.out.println("Analisi Sintattica completata con successo!");

            // 3. ANALISI SEMANTICA (Type Checker)
            TypeCheckingVisitor typeChecker = new TypeCheckingVisitor();
            ast.accept(typeChecker);

            // Verifichiamo se ci sono stati errori (es. variabili non dichiarate)
            if (typeChecker.hasErrors()) {
                System.err.println("Compilazione fallita: Rilevati errori semantici.");
                return;
            }
            System.out.println("Analisi Semantica completata con successo!");

            // 4. GENERAZIONE DEL CODICE (Code Generator)
            CodeGeneratorVisitor codeGen = new CodeGeneratorVisitor();
            ast.accept(codeGen);

            // Verifichiamo se i registri si sono esauriti
            if (!codeGen.getLog().isEmpty()) {
                System.err.println("Compilazione fallita durante la generazione del codice: " + codeGen.getLog());
                return;
            }
            
            System.out.println("Generazione del Codice completata con successo!\n");

            // 5. RISULTATO FINALE
            System.out.println("============= CODICE DC GENERATO =============");
            System.out.println(codeGen.getCodiceDc());
            System.out.println("==============================================");

        } catch (Exception e) {
            System.err.println("Si è verificato un errore critico durante la compilazione:");
            e.printStackTrace();
        }
    }
}