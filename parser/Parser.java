package parser;

import ast.LangOper;
import ast.LangType;
import ast.NodeDecSt;
import ast.NodeDecl;
import ast.NodeExpr;
import ast.NodeProgram;
import ast.NodeStat;

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
	 * Metodo di supporto per sbirciare il prossimo token catturando 
	 * automaticamente la LexicalException e convertendola
	 * 
	 * @return Il prossimo Token
	 * @throws SyntacticException Se si verifica un errore lessicale
	 */
	private Token peek() throws SyntacticException {
		try {
			return scanner.peekToken();
		} catch (LexicalException e) {
			throw new SyntacticException(e);
		}
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
		Token t = this.peek();

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
	public NodeProgram parse() throws SyntacticException {
		return this.parsePrg();
	}

	/**
	 * Regola 0: Prg -> DSs EOF
	 */
	private NodeProgram parsePrg() throws SyntacticException {
		Token t = this.peek();

		switch (t.getTipo()) {
			case TYFLOAT, TYINT, ID, PRINT, EOF -> {
				parseDSs();
				match(TokenType.EOF);
				return null;
			}
			
			default -> throw new SyntacticException(t.getRiga(), "Token non valido per l'inizio di un programma: " + t.getTipo());
		}
	}

	/**
	 * Regole 1, 2, 3: DSs -> Dcl DSs | Stm DSs | epsilon
	 */
	private NodeDecSt parseDSs() throws SyntacticException {
		Token t = this.peek();

		switch (t.getTipo()) {
			case TYFLOAT, TYINT -> {
				parseDcl();
				parseDSs();
				return null;
			}
			case ID, PRINT -> {
				parseStm();
				parseDSs();
				return null;
			}
			case EOF -> {
				// Regola epsilon (vuoto), non facciamo nulla
				return null;
			}
			
			default -> throw new SyntacticException(t.getRiga(), "Token non valido in DSs: " + t.getTipo());
		}
	}

	/**
	 * Regola 4: Dcl -> Ty id DclP
	 */
	private NodeDecl parseDcl() throws SyntacticException {
		Token t = this.peek();

		switch (t.getTipo()) {
			case TYFLOAT, TYINT -> {
				parseTy();
				match(TokenType.ID);
				parseDclP();
				return null;
			}
			
			default -> throw new SyntacticException(t.getRiga(), "Atteso valore numerico, trovato " +  t.getTipo());
		}
	}

	/**
	 * Regole 5, 6: DclP -> ; | = Exp ;
	 */
	private NodeExpr parseDclP() throws SyntacticException {
		Token t = this.peek();

		switch (t.getTipo()) {
			case SEMI -> {
				match(TokenType.SEMI);
				return null;
			}
			case ASSIGN -> {
				match(TokenType.ASSIGN);
				parseExp();
				match(TokenType.SEMI);
				return null;
			}
			
			default -> throw new SyntacticException(t.getRiga(), "Atteso ';' oppure '=', trovato " + t.getTipo());
		}
	}

	/**
	 * Regole 7, 8: Stm -> id Op Exp ; | print id ;
	 */
	private NodeStat parseStm() throws SyntacticException {
		Token t = this.peek();

		switch (t.getTipo()) {
			case ID -> {
				match(TokenType.ID);
				parseOp();
				parseExp();
				match(TokenType.SEMI);
				return null;
			}
			case PRINT -> {
				match(TokenType.PRINT);
				match(TokenType.ID);
				match(TokenType.SEMI);
				return null;
			}
			
			default -> throw new SyntacticException(t.getRiga(), "Atteso 'print' o identificatore, trovato " + t.getTipo());
		}
	}

	/**
	 * Regola 9: Exp -> Tr ExpP
	 */
	private NodeExpr parseExp() throws SyntacticException {
		Token t = this.peek();
		
		switch (t.getTipo()) {
			case ID, FLOAT, INT -> {
				parseTr();
				parseExpP();
				return null;
			}
		
			default -> throw new SyntacticException(t.getRiga(), "Atteso valore numerico o identificatore, trovato " + t.getTipo());
		}
	}
		

	/**
	 * Regole 10, 11, 12: ExpP -> + Tr ExpP | - Tr ExpP | epsilon
	 */
	private NodeExpr parseExpP() throws SyntacticException {
		Token t = this.peek();

		switch (t.getTipo()) {
			case PLUS -> {
				match(TokenType.PLUS);
				parseTr();
				parseExpP();
				return null;
			}
			case MINUS -> {
				match(TokenType.MINUS);
				parseTr();
				parseExpP();
				return null;
			}
			case SEMI -> {
				return null;
				// Regola epsilon (vuoto), non facciamo nulla
			}
			
			default -> throw new SyntacticException(t.getRiga(), "Atteso '+', '-' o ';', trovato " + t.getTipo());
		}
	}

	/**
	 * Regola 13: Tr -> Val TrP
	 */
	private NodeExpr parseTr() throws SyntacticException {
		Token t = this.peek();

		switch (t.getTipo()) {
			case ID, FLOAT, INT -> {
				parseVal();
				parseTrP();
				return null;
			}
			
			default -> throw new SyntacticException(t.getRiga(), "Atteso valore numerico o identificatore, trovato " + t.getTipo());
		}
	}

	/**
	 * Regole 14, 15, 16: TrP -> * Val TrP | / Val TrP | epsilon
	 */
	private NodeExpr parseTrP() throws SyntacticException {
		Token t = this.peek();

		switch (t.getTipo()) {
			case TIMES -> {
				match(TokenType.TIMES);
				parseVal();
				parseTrP();
				return null;
			}
			case DIVIDE -> {
				match(TokenType.DIVIDE);
				parseVal();
				parseTrP();
				return null;
			}
			case PLUS, MINUS, SEMI -> {
				return null;
				// Regola epsilon (vuoto), non facciamo nulla
			}
			
			default -> throw new SyntacticException(t.getRiga(), "Atteso '*', '/', '+', '-' o ';', trovato " + t.getTipo());
		}
	}

	/**
	 * Regole 17, 18: Ty -> float | int
	 */
	private LangType parseTy() throws SyntacticException {
		Token t = this.peek();

		switch (t.getTipo()) {
			case TYFLOAT -> {
				match(TokenType.TYFLOAT);
				return null;
			}
			case TYINT -> {
				match(TokenType.TYINT);
				return null;
			}
			
			default -> throw new SyntacticException(t.getRiga(), "Atteso valore numerico, trovato " + t.getTipo());
		}
	}

	/**
	 * Regole 19, 20, 21: Val -> intVal | floatVal | id
	 */
	private NodeExpr parseVal() throws SyntacticException {
		Token t = this.peek();

		switch (t.getTipo()) {
			case INT -> {
				match(TokenType.INT);
				return null;
			}
			case FLOAT -> {
				match(TokenType.FLOAT);
				return null;
			}
			case ID -> {
				match(TokenType.ID);
				return null;
			}
			
			default -> throw new SyntacticException(t.getRiga(), "Atteso valore numerico o identificatore, trovato " + t.getTipo());
		}
	}

	/**
	 * Regole 22, 23: Op -> = | opAss
	 */
	private LangOper parseOp() throws SyntacticException {
		Token t = this.peek();

		switch (t.getTipo()) {
			case ASSIGN -> {
				match(TokenType.ASSIGN);
				return null;
			}
			case OP_ASSIGN -> {
				match(TokenType.OP_ASSIGN);
				return null;
			}
			
			default -> throw new SyntacticException(t.getRiga(), "Atteso '=' o operatore di assegnamento (+=, -=, ...), trovato " + t.getTipo());
		}
	}
}
