package symbolTable;

import java.util.HashMap;

/**
 * Tabella dei Simboli (Symbol Table) per il compilatore "ac"
 * 
 * Memorizza le associazioni tra i nomi delle variabili (identificatori) 
 * e i loro rispettivi attributi (es. tipo di dato)
 */
public class SymbolTable {
	
	private static HashMap<String, Attributes> table;
	
	/**
	 * Inizializza la Symbol Table allocando una nuova mappa vuota
	 */
	public static void init() {
		table = new HashMap<>();
	}
	
	/**
	 * Inserisce un nuovo identificatore (nome) e i suoi attributi nella tabella
	 * 
	 * @param nome L'identificatore (nome) della variabile
	 * @param entry Gli attributi da associare alla variabile
	 * @return true se l'inserimento ha avuto successo, false se l'id era già presente
	 */
	public static boolean enter(String nome, Attributes entry) {
		if(table.containsKey(nome))
			return false;
		
		table.put(nome, entry);
		return true;
	}
	
	/**
	 * Cerca un identificatore nella Symbol Table
	 * 
	 * @param nome L'identificatore da cercare
	 * @return Gli attributi associati, oppure null se la variabile non è mai stata dichiarata
	 */
	public static Attributes lookup(String nome) {
		return table.get(nome);
	}
	/**
	 * Restituisce la quantità totale di variabili dichiarate
	 * 
	 * @return Il numero di entry attualmente presenti nella Symbol Table
	 */
	public static int size() {
		return table.size();
	}
	
	/**
	 * Metodo di supporto per generare una rappresentazione testuale della tabella
	 * 
	 * @return Una stringa formattata con tutto il contenuto della Symbol Table
	 */
	public static String toStr() {
		StringBuilder st = new StringBuilder();
		
		for (var key : table.entrySet()) {
			st.append(key.getKey()).append(",Tipo:").append(key.getValue().getTipo()).append("\n");
		}
		
		return st.toString();
	}
}