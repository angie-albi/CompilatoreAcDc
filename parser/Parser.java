package parser;

import scanner.LexicalException;
import scanner.Scanner;
import token.Token;
import token.TokenType;

/**
 * Analizzatore Sintattico Controlla che la sequenza dei token rispetti la
 * grammatica del linguaggio "ac"
 */
public class Parser {

	private Scanner scanner;

	/**
	 * Costruttore del Parser
	 * 
	 * @param scanner Lo scanner da cui leggere i token
	 */
	public Parser(Scanner scanner) {
		this.scanner = scanner;
	}

	/**
	 * Verifica se il prossimo token è del tipo atteso e lo consuma
	 * 
	 * @param expected Il tipo di token atteso
	 * @return Il token appena consumato
	 * @throws SyntacticException Se il token non corrisponde o c'è un errore
	 *                            lessicale
	 */
	private Token match(TokenType expected) throws SyntacticException {
		Token t;

		try {
			t = scanner.peekToken();
		} catch (LexicalException e) {
			throw new SyntacticException("Lexical Exception", e);
		}

		if (expected.equals(t.getTipo())) {
			try {
				return scanner.nextToken();
			} catch (LexicalException e) {
				throw new SyntacticException("Lexical Exception", e);
			}
		} else {
			throw new SyntacticException(t.getRiga(), "Atteso token " + expected + ", trovato " + t.getTipo() + " (" + t.getValore() + ")");
		}
	}

	/**
	 * Punto d'ingresso principale per l'analisi
	 * 
	 * @throws SyntacticException Se ci sono errori nel codice
	 */
	public void parse() throws SyntacticException {
		this.parsePrg();
	}

	/**
	 * Regola 0: Prg -> DSs EOF
	 */
	private void parsePrg() throws SyntacticException {
		Token t;
		
		try {
			t = scanner.peekToken();
		} catch (LexicalException e) {
			throw new SyntacticException(e);
		}

		switch (t.getTipo()) {
			case TYFLOAT, TYINT, ID, PRINT, EOF -> {
				parseDSs();
				match(TokenType.EOF);
				return;
			}
			
			default -> throw new SyntacticException(t.getRiga(), "Token non valido per l'inizio di un programma: " + t.getTipo());
		}
	}

	/**
	 * Regole 1, 2, 3: DSs -> Dcl DSs | Stm DSs | epsilon
	 */
	private void parseDSs() throws SyntacticException {
		Token t;
		
		try {
			t = scanner.peekToken();
		} catch (LexicalException e) {
			throw new SyntacticException(e);
		}

		switch (t.getTipo()) {
			case TYFLOAT, TYINT -> {
				parseDcl();
				parseDSs();
			}
			case ID, PRINT -> {
				parseStm();
				parseDSs();
			}
			case EOF -> {
				// Regola epsilon (vuoto), non facciamo nulla
			}
			
			default -> throw new SyntacticException(t.getRiga(), "Token non valido in DSs: " + t.getTipo());
		}
	}

	/**
	 * Regola 4: Dcl -> Ty id DclP
	 */
	private void parseDcl() throws SyntacticException {
		parseTy();
		match(TokenType.ID);
		parseDclP();
	}

	/**
	 * Regole 5, 6: DclP -> ; | = Exp ;
	 */
	private void parseDclP() throws SyntacticException {
		Token t;
		
		try {
			t = scanner.peekToken();
		} catch (LexicalException e) {
			throw new SyntacticException(e);
		}

		switch (t.getTipo()) {
			case SEMI -> match(TokenType.SEMI);
			case ASSIGN -> {
				match(TokenType.ASSIGN);
				parseExp();
				match(TokenType.SEMI);
			}
			
			default -> throw new SyntacticException(t.getRiga(), "Atteso ';' oppure '=', trovato " + t.getTipo());
		}
	}

	/**
	 * Regole 7, 8: Stm -> id Op Exp ; | print id ;
	 */
	private void parseStm() throws SyntacticException {
		Token t;
		
		try {
			t = scanner.peekToken();
		} catch (LexicalException e) {
			throw new SyntacticException(e);
		}

		switch (t.getTipo()) {
			case ID -> {
				match(TokenType.ID);
				parseOp();
				parseExp();
				match(TokenType.SEMI);
			}
			case PRINT -> {
				match(TokenType.PRINT);
				match(TokenType.ID);
				match(TokenType.SEMI);
			}
			
			default -> throw new SyntacticException(t.getRiga(), "Atteso 'print' o identificatore, trovato " + t.getTipo());
		}
	}

	/**
	 * Regola 9: Exp -> Tr ExpP
	 */
	private void parseExp() throws SyntacticException {
		parseTr();
		parseExpP();
	}

	/**
	 * Regole 10, 11, 12: ExpP -> + Tr ExpP | - Tr ExpP | epsilon
	 */
	private void parseExpP() throws SyntacticException {
		Token t;
		
		try {
			t = scanner.peekToken();
		} catch (LexicalException e) {
			throw new SyntacticException(e);
		}

		switch (t.getTipo()) {
			case PLUS -> {
				match(TokenType.PLUS);
				parseTr();
				parseExpP();
			}
			case MINUS -> {
				match(TokenType.MINUS);
				parseTr();
				parseExpP();
			}
			case SEMI -> {
				// Regola epsilon (vuoto), non facciamo nulla
			}
			
			default -> throw new SyntacticException(t.getRiga(), "Atteso '+', '-' o ';', trovato " + t.getTipo());
		}
	}

	/**
	 * Regola 13: Tr -> Val TrP
	 */
	private void parseTr() throws SyntacticException {
		parseVal();
		parseTrP();
	}

	/**
	 * Regole 14, 15, 16: TrP -> * Val TrP | / Val TrP | epsilon
	 */
	private void parseTrP() throws SyntacticException {
		Token t;
		
		try {
			t = scanner.peekToken();
		} catch (LexicalException e) {
			throw new SyntacticException(e);
		}

		switch (t.getTipo()) {
			case TIMES -> {
				match(TokenType.TIMES);
				parseVal();
				parseTrP();
			}
			case DIVIDE -> {
				match(TokenType.DIVIDE);
				parseVal();
				parseTrP();
			}
			case PLUS, MINUS, SEMI -> {
				// Regola epsilon (vuoto), non facciamo nulla
			}
			
			default -> throw new SyntacticException(t.getRiga(), "Atteso '*', '/', '+', '-' o ';', trovato " + t.getTipo());
		}
	}

	/**
	 * Regole 17, 18: Ty -> float | int
	 */
	private void parseTy() throws SyntacticException {
		Token t;
		
		try {
			t = scanner.peekToken();
		} catch (LexicalException e) {
			throw new SyntacticException(e);
		}

		switch (t.getTipo()) {
			case TYFLOAT -> match(TokenType.TYFLOAT);
			case TYINT -> match(TokenType.TYINT);
			
			default -> throw new SyntacticException(t.getRiga(), "Atteso tipo 'int' o 'float', trovato " + t.getTipo());
		}
	}

	/**
	 * Regole 19, 20, 21: Val -> intVal | floatVal | id
	 */
	private void parseVal() throws SyntacticException {
		Token t;
		
		try {
			t = scanner.peekToken();
		} catch (LexicalException e) {
			throw new SyntacticException(e);
		}

		switch (t.getTipo()) {
			case INT -> match(TokenType.INT);
			case FLOAT -> match(TokenType.FLOAT);
			case ID -> match(TokenType.ID);
			
			default -> throw new SyntacticException(t.getRiga(), "Atteso valore numerico o identificatore, trovato " + t.getTipo());
		}
	}

	/**
	 * Regole 22, 23: Op -> = | opAss
	 */
	private void parseOp() throws SyntacticException {
		Token t;
		
		try {
			t = scanner.peekToken();
		} catch (LexicalException e) {
			throw new SyntacticException(e);
		}

		switch (t.getTipo()) {
			case ASSIGN -> match(TokenType.ASSIGN);
			case OP_ASSIGN -> match(TokenType.OP_ASSIGN);
			
			default -> throw new SyntacticException(t.getRiga(), "Atteso '=' o operatore di assegnamento (+=, -=, ...), trovato " + t.getTipo());
		}
	}

}
