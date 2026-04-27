package visitor;

import ast.LangOper;
import ast.NodeAssign;
import ast.NodeBinOp;
import ast.NodeCost;
import ast.NodeDecSt;
import ast.NodeDecl;
import ast.NodeDeref;
import ast.NodeId;
import ast.NodePrint;
import ast.NodeProgram;
import symbolTable.Attributes;
import symbolTable.Registri;
import symbolTable.SymbolTable;

/**
 * Visitatore per la Generazione del Codice (Code Generator)
 * 
 * Traduce l'AST in stringhe di comandi compatibili con la macchina a stack 'dc'
 */
public class CodeGeneratorVisitor implements IVisitor {

	private String codiceDc; // mantiene il codice della visita
	private String log;   // raccoglie i messaggi di errore
	
	/**
	 * Costruttore: inizializza le variabili per una nuova visita 
	 * e resetta il generatore dei registri
	 */
	public CodeGeneratorVisitor() {
		super();
		this.codiceDc = "";
		this.log = "";
		Registri.init();
	}
	
	/**
	 * Restituisce il codice 'dc' generato alla fine della visita
	 * 
	 * @return Una stringa contenente le istruzioni per la calcolatrice dc
	 */
	public String getCodiceDc() {
		return codiceDc;
	}

	/**
	 * Restituisce l'eventuale log degli errori
	 * 
	 * @return Una stringa con l'errore, oppure una stringa vuota se non ci sono errori
	 */
	public String getLog() {
		return log;
	}
	
	/**
	 * Visita il nodo radice del programma
	 * Itera su tutte le dichiarazioni e le istruzioni, generando il codice sequenzialmente
	 * Si interrompe immediatamente se viene rilevato un errore (es. registri esauriti)
	 * 
	 * @param node Il nodo principale del programma da visitare
	 */
	@Override
	public void visit(NodeProgram node) {
		for (NodeDecSt decSt: node.getDecSt()) {
			if(!log.isEmpty())
				return;
			
			decSt.accept(this);
		}
	}

	/**
	 * Visita un identificatore
	 * Nella generazione del codice, questo metodo rimane vuoto poiché le istruzioni 
	 * 'dc' per i registri (load/store) vengono generate direttamente dai nodi genitore 
	 * (NodeDeref, NodeAssign, NodeDecl) in base al loro specifico contesto
	 *
	 * @param node Il nodo identificatore da visitare
	 */
	@Override
	public void visit(NodeId node) {
		// Nessuna operazione
	}

	/**
	 * Visita un nodo di dichiarazione (es. int a = 5; oppure float b;)
	 * Richiede un nuovo registro, lo salva nella Symbol Table e genera il codice
	 * per l'inizializzazione della variabile
	 * 
	 * @param node Il nodo di dichiarazione da visitare
	 */
	@Override
	public void visit(NodeDecl node) {
		// Controllo iniziale: se c'è già un errore propagato dall'alto, fermiamo subito la visita
		if (!log.isEmpty()) 
			return;
		
		char reg = Registri.newRegister();
		
		// Controllo se il registro è valido (
		if (reg == 0) {
			log = "Errore: Registri esauriti per " + node.getId().getName(); 
			return; 
		}
		
		// Recupero gli attributi dalla Symbol Table e salvo il registro assegnato
		Attributes attr = SymbolTable.lookup(node.getId().getName());
		attr.setRegistro(reg);
		
		// Gestione del valore di inizializzazione
		if (node.getInit() != null) {
			node.getInit().accept(this);
		} else {
			// se non c'è inizializzazione (es. "int a;"), lo inizializzo a 0
			codiceDc += "0 ";
		}
		
		// Salvo il valore dal top dello stack nel registro usando il comando 's' (es. "sa ")
		codiceDc += "s" + reg + " ";
	}

	/**
	 * Visita un'operazione binaria (+, -, *, /)
	 * Visita i due figli in notazione postfissa e aggiunge l'operatore corrispondente
	 * 
	 * @param node Il nodo dell'operazione binaria da visitare.
	 */
	@Override
	public void visit(NodeBinOp node) {
		// Controllo iniziale: se c'è già un errore propagato dall'alto, fermiamo subito la visita
		if (!log.isEmpty()) 
			return;
		
		// Visita il figlio sinistro (accoda automaticamente il suo codice in codiceDc)
		node.getSx().accept(this);
		if (!log.isEmpty()) 
			return;
		
		// Visita il figlio destro
		node.getDx().accept(this);
		if (!log.isEmpty()) 
			return;
		
		//Identifica l'operatore e lo accoda
		LangOper op = node.getOp();
		
		switch (op) {
			case PLUS -> codiceDc += "+ ";
			case MINUS -> codiceDc += "- ";
			case TIMES -> codiceDc += "* ";
			case DIVIDE -> codiceDc += "/ ";
			
			// Divisione float: setta precisione a 5, divide, resetta precisione a 0
			case DIV_FLOAT -> codiceDc += " 5 k / 0 k ";
		}
	}

	/**
	 * Visita un nodo di assegnamento (es. a = espressione;)
	 * 
	 * @param node Il nodo di assegnamento da visitare
	 */
	@Override
	public void visit(NodeAssign node) {
		// Controllo iniziale: se c'è già un errore propagato dall'alto, fermiamo subito la visita
		if (!log.isEmpty()) 
			return;

		// Recupero il registro della variabile dalla Symbol Table 
		Attributes attr = SymbolTable.lookup(node.getId().getName());
		char reg = attr.getRegistro();
		
		// Visito l'espressione a destra dell'uguale
		node.getExpr().accept(this);
		if (!log.isEmpty()) 
			return;

		codiceDc += "s" + reg + " ";
	}

	/**
	 * Visita un nodo di stampa (print)
	 * Carica il valore, lo stampa e svuota la cima dello stack.
	 * 
	 * @param node Il nodo di stampa da visitare
	 */
	@Override
	public void visit(NodePrint node) {
		// Controllo iniziale: se c'è già un errore propagato dall'alto, fermiamo subito la visita
		if (!log.isEmpty()) 
			return;
				
		// Recupero il registro della variabile da stampare
		Attributes attr = SymbolTable.lookup(node.getId().getName());
		char reg = attr.getRegistro();
		
		// 1. Carico il valore (es. "la ")
		// 2. Stampo (p)
		// 3. Pulisco lo stack (P) e vado a capo
		codiceDc += "l" + reg + " p P\n";
	}

	/**
	 * Visita un nodo di dereferenziazione (utilizzo di una variabile in un calcolo)
	 * 
	 * @param node Il nodo di dereferenziazione da visitare
	 */
	@Override
	public void visit(NodeDeref node) {
		// Controllo iniziale: se c'è già un errore propagato dall'alto, fermiamo subito la visita
		if (!log.isEmpty()) {
			return;
		}
				
		// Recupero il registro della variabile dalla Symbol Table
		Attributes attr = SymbolTable.lookup(node.getId().getName());
		char reg = attr.getRegistro();
		
		// Genero il comando di load
		codiceDc += "l" + reg + " ";
	}

	/**
	 * Visita un nodo di valore costante (es. un numero)
	 * 
	 * @param node Il nodo costante da visitare
	 */
	@Override
	public void visit(NodeCost node) {
		// Controllo iniziale: se c'è già un errore propagato dall'alto, fermiamo subito la visita
		if (!log.isEmpty()) 
			return;
		
		codiceDc += node.getValore() + " ";
	}
}