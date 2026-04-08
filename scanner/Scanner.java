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
 * Gestisce la scansione di un file carattere per carattere generndo i relativi
 * token che utilizzerà il Parser
 */
public class Scanner {
	final char EOF = (char) -1; 
	private int riga;
	private PushbackReader buffer;
	
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
		skpChars.add(EOF);
		
		
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
	 * Ritorna il prossimo token nel file di input e legge i caratteri del token ritornato 
	 * 
	 * @return Il token successivo
	 * @throws LexicalException Eccezione lessicale, se il token non è valido
	 */
	public Token nextToken() throws LexicalException  {
		try {
			char nextChar = peekChar(); // guarda il prossimo carattere
			
			//Si saltano i caratteri vuoti finché non si trova qualcosa da leggere
			while (skpChars.contains(nextChar)) {
				readChar(); 	//consuma il carattere
				if (nextChar == '\n') {
					riga++;
				}
				
				nextChar = peekChar();
			}
			
			//Se è la fine del file, ritorna il token EOF
			if (nextChar == EOF) 
				return new Token(riga, TokenType.EOF);
			
			//Scansiona le parole chiave
			if (letters.contains(peekChar())) 
				return scanId();

			//Scansiona gli operatori
			if (operTkType.containsKey(peekChar())) 
				return scanOperator();
			
			//Scansiona i numeri
			if (digits.contains(peekChar())) 
				return scanNumber();
			
			//Scansiona i delimitatori
			if (delimTkType.containsKey(peekChar())) 
				return new Token(riga, delimTkType.get(readChar()));

			//Il carattere non fa parte del linguaggio
			readChar();
			throw new LexicalException(riga, "Carattere invalido (" + nextChar + ")");
		
		} catch (IOException e) {
			throw new LexicalException(riga, "Errore di lettura I/O: " + e.getMessage());
		}
	}
	
	private Token scanId() throws IOException {
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
	
	private Token scanOperator() {
		
	}
		
	private Token scanNumber() {
		
	}

	
	private char readChar() throws IOException {
		return ((char) buffer.read());
	}

	private char peekChar() throws IOException {
		char c = (char) buffer.read();
		buffer.unread(c);
		return c;
	}
}
