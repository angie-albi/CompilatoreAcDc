package scanner;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PushbackReader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import token.*;

/**
 * Analizzatore Lessicale 
 * Gestisce la scansione di un file carattere per carattere generndo i relativi
 * token che utilizzerà il Parser
 */
public class Scanner {
	final char EOF = (char) -1; 
	private int riga;
	private PushbackReader buffer;
	private Token nextTk = null; 		//memorizza il prossimo token senza consumarlo
	
	private Set<Character> skpChars;	//lista caratteri di skip (include EOF)
	private Set<Character> letters; 	//lista lettere 
	private Set<Character> digits; 		//lista numeri 

	private Map<Character, TokenType> operTkType; 		//mappa fra caratteri operatore '+', '-', '*', '/'  e il TokenType corrispondente
	private Map<Character, TokenType> delimTkType; 		//mappa fra caratteri delimitatore '=', ';' e il e il TokenType corrispondente
	private Map<String, TokenType> keyWordsTkType; 		//mappa keyword e il TokenType  corrispondente
	
	/**
	 * Costruttore che inizializza il lettore del file e inizializza tutte le 
	 * variabili necessarie per il riconoscimento dei token
	 * 
	 * @param fileName Nome del file da scansionare
	 * @throws FileNotFoundException Eccezione se il file non è stato trovato
	 */
	public Scanner(String fileName) throws FileNotFoundException {
		buffer = new PushbackReader(new FileReader(fileName));
		riga = 1;
		
		iniz();
	}
	
	/**
	 * Inizializzazione delle variabili dello scanner
	 */
	private void iniz() {
		//Caratteri da ignorare
		skpChars = new HashSet<>();

		skpChars.add(' ');
		skpChars.add('\t');
		skpChars.add('\n');
		skpChars.add('\r');
		
		
		//Lettere
		letters = new HashSet<>();
		
		for (char c = 'a'; c <= 'z'; c++) 
            letters.add(c);
        

		//Cifre
		digits = new HashSet<>();
		
		for (char c = '0'; c <= '9'; c++) 
			digits.add(c);
		
		//Operatori
		operTkType = new HashMap<>();
		operTkType.put('+', TokenType.PLUS);
		operTkType.put('-', TokenType.MINUS);
		operTkType.put('*', TokenType.TIMES);
		operTkType.put('/', TokenType.DIVIDE);

		//Delimitatore e assegnamento
		delimTkType = new HashMap<>();
		delimTkType.put('=', TokenType.ASSIGN);
		delimTkType.put(';', TokenType.SEMI);

		//Parole chiave
		keyWordsTkType = new HashMap<>();
		keyWordsTkType.put("print", TokenType.PRINT);
		keyWordsTkType.put("float", TokenType.TYFLOAT);
		keyWordsTkType.put("int", TokenType.TYINT);
	}
	
	/**
	 * Estrae e consuma il prossimo carattere dal file
	 * 
	 * @return Il carattere letto e consumato
	 * @throws LexicalException Se si verifica un'eccezione di Input/Output durante la lettura
	 */
	private char readChar() throws LexicalException {
		try {
			return ((char) buffer.read());
		} catch (IOException e) {
			throw new LexicalException(riga, "Errore di lettura I/O durante il read: " + e.getMessage());
		}
	}

	/**
	 * Guarda il prossimo carattere dal file senza consumarlo
	 * 
	 * @return Il carattere 
	 * @throws LexicalException Se si verifica un'eccezione di Input/Output durante la lettura
	 */
	private char peekChar() throws LexicalException {
		try {
			char c = (char) buffer.read();
			buffer.unread(c);
			return c;
		} catch (IOException e) {
			throw new LexicalException(riga, "Errore di lettura I/O durante il peek: " + e.getMessage());
		}
		
	}
	
	/**
	 * Ritorna il prossimo token nel file di input e legge i caratteri del token ritornato 
	 * 
	 * @return Il token successivo
	 * @throws LexicalException Eccezione lessicale, se il token non è valido
	 */
	public Token nextToken() throws LexicalException  {
		// Se avevamo salvato un token con peekToken, lo restituiamo e svuotiamo la memoria
		if (nextTk != null) {
			Token tokenDaRitornare = nextTk;
			nextTk = null; 
			return tokenDaRitornare;
		}
		
		char nextChar = peekChar(); 
		
		// Si saltano i caratteri vuoti finché non si trova qualcosa da leggere
		while (skpChars.contains(nextChar)) {
			readChar(); 
			if (nextChar == '\n') {
				riga++;
			}
			
			nextChar = peekChar();
		}
		
		// Se è la fine del file, ritorna il token EOF
		if (nextChar == EOF) {
			readChar();
			return new Token(riga, TokenType.EOF);
		}
		
		// Scansiona le parole chiave
		if (letters.contains(peekChar())) 
			return scanId();

		// Scansiona gli operatori
		if (operTkType.containsKey(peekChar())) 
			return scanOperator();
		
		// Scansiona i numeri
		if (digits.contains(peekChar())) 
			return scanNumber();
		
		// Scansiona i delimitatori
		if (delimTkType.containsKey(peekChar())) 
			return new Token(riga, delimTkType.get(readChar()));

		// Il carattere non fa parte del linguaggio
		readChar();
		throw new LexicalException(riga, "Carattere invalido (" + nextChar + ")");
	}
	
	/**
	 * Legge una sequenza di lettere e cifre per formare un identificatore (ID)
	 * oppure riconoscere una parola chiave (es. "int", "print", "float").
	 * 
	 * @return Il Token corrispondente all'ID o alla parola chiave
	 * @throws LexicalException Se si verifica un errore durante la lettura del file
	 */
	private Token scanId() throws LexicalException {
		StringBuilder idValore = new StringBuilder();
		char c = peekChar();

		while (letters.contains(c) || digits.contains(c)) {
			idValore.append(readChar());
			c = peekChar();
		}

		String id = idValore.toString();
		
		if (keyWordsTkType.containsKey(id)) 
			return new Token(riga, keyWordsTkType.get(id)); 
		else 
			return new Token(riga, TokenType.ID, id);
	}
	
	/**
	 * Scansiona i simboli per riconoscere operatori matematici (+, -, *, /),
	 * semplici delimitatori (;), assegnamenti (=) o operatori composti (+=, -=, ecc.).
	 * 
	 * @return Il Token corrispondente all'operatore o al delimitatore trovato
	 * @throws LexicalException Se si verifica un errore durante la lettura del file
	 */
	private Token scanOperator()  throws LexicalException{
		char c = readChar();
		char nextC = peekChar();
		
		//Operatore di assegnamento (+=., -=, /=, *=)
		if(nextC == '=') {
			readChar();
			return new Token(riga, TokenType.OP_ASSIGN, c + "=");
		}
		else
			return new Token(riga, operTkType.get(c));
	}
		
	/**
	 * Legge una sequenza numerica. Riconosce i numeri interi (INT) e i numeri 
	 * in virgola mobile (FLOAT), verificando il vincolo dei decimali.
	 * 
	 * @return Il Token del numero 
	 * @throws LexicalException Se un float ha più di 5 cifre decimali o in caso di errore I/O
	 */
	private Token scanNumber()  throws LexicalException{
		StringBuilder numero = new StringBuilder();
		char c = peekChar();
		
		//Parte intera
		while (digits.contains(c)) {
			numero.append(readChar());
			c = peekChar();
		}
		
		//Parte decimale
		if(c == '.') {
			numero.append(readChar());
			c = peekChar();
			
			int cont = 0;
			while (digits.contains(c)) {
				numero.append(readChar());
				c = peekChar();
				cont++;
			}
			
			if(cont>5)
				throw new LexicalException(riga, "Troppe cifre decimali (max 5)");
			
			return new Token(riga, TokenType.FLOAT, numero.toString());
		}
		
		return new Token(riga, TokenType.INT, numero.toString());
	}

	/**
	 * Guarda il prossimo token nel file di input senza consumarlo.
	 * Se il token è già stato "sbirciato", restituisce quello in memoria.
	 * 
	 * @return Il prossimo Token
	 * @throws LexicalException Se si verifica un errore lessicale durante la lettura
	 */
	public Token peekToken() throws LexicalException {
		if (nextTk == null) 
			nextTk = nextToken();
	
		return nextTk;
	}
	
}
