package parser;

import java.util.ArrayList;

import ast.LangOper;
import ast.LangType;
import ast.NodeDecSt;
import ast.NodeDecl;
import ast.NodeExpr;
import ast.NodeId;
import ast.NodePrint;
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
			throw new SyntacticException(t.getRiga(), "Atteso token " + expected + ", trovato " + t.getTipo().toString() + " (" + t.getValore() + ")");
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
			//Prg -> DSs EOF
			case TYFLOAT, TYINT, ID, PRINT, EOF -> {
				ArrayList<NodeDecSt> dss = parseDSs();
				parseDSs();
				match(TokenType.EOF);
				return new NodeProgram(dss);
			}
			
			default -> throw new SyntacticException(t.getRiga(), "Token non valido per l'inizio di un programma: " + t.getTipo().toString());
		}
	}

	/**
	 * Regole 1, 2, 3: DSs -> Dcl DSs | Stm DSs | epsilon
	 */
	private ArrayList<NodeDecSt> parseDSs() throws SyntacticException {
		Token t = this.peek();

		switch (t.getTipo()) {
			//DSs -> Dcl DSs 
			case TYFLOAT, TYINT -> {
				NodeDecl dec = parseDcl();
				ArrayList<NodeDecSt> dss = parseDSs();
				dss.add(0, dec);
				return dss;
			}
			//DSs -> Stm DSs
			case ID, PRINT -> {
				NodeStat stm = parseStm();
				ArrayList<NodeDecSt> dss = parseDSs();
				dss.add(0, stm);;
				return dss;
			}
			//DSs -> epsilon
			case EOF -> {
				return new ArrayList<NodeDecSt>();
			}
			
			default -> throw new SyntacticException(t.getRiga(), "Token non valido in DSs: " + t.getTipo().toString());
		}
	}

	/**
	 * Regola 4: Dcl -> Ty id DclP
	 */
	private NodeDecl parseDcl() throws SyntacticException {
		Token t = this.peek();

		switch (t.getTipo()) {
			//Dcl -> Ty id DclP
			case TYFLOAT, TYINT -> {
				LangType tipo = parseTy();
				NodeId nome = new NodeId(match(TokenType.ID).getValore());
				NodeExpr init = parseDclP();
				
				return new NodeDecl( nome, tipo, init);
			}
			
			default -> throw new SyntacticException(t.getRiga(), "Atteso valore numerico, trovato " +  t.getTipo().toString());
		}
	}

	/**
	 * Regole 5, 6: DclP -> ; | = Exp ;
	 */
	private NodeExpr parseDclP() throws SyntacticException {
		Token t = this.peek();

		switch (t.getTipo()) {
			// DclP -> ;
			case SEMI -> {
				match(TokenType.SEMI);
				return null;
			}
			//DclP -> = Exp ;
			case ASSIGN -> {
				match(TokenType.ASSIGN);
				parseExp();
				match(TokenType.SEMI);
				return null;
			}
			
			default -> throw new SyntacticException(t.getRiga(), "Atteso ';' oppure '=', trovato " + t.getTipo().toString());
		}
	}

	/**
	 * Regole 7, 8: Stm -> id Op Exp ; | print id ;
	 */
	private NodeStat parseStm() throws SyntacticException {
		Token t = this.peek();

		switch (t.getTipo()) {
			//Stm -> id Op Exp ;
			case ID -> {
				NodeId nome = new NodeId(match(TokenType.ID).getValore());
				parseOp();
				parseExp();
				match(TokenType.SEMI);
				return null;
			}
			//Stm ->  print id ;
			case PRINT -> {
				match(TokenType.PRINT);
				NodeId nome =new NodeId(match(TokenType.ID).getValore());
				match(TokenType.SEMI);
				return new NodePrint(nome);
			}
			
			default -> throw new SyntacticException(t.getRiga(), "Atteso 'print' o identificatore, trovato " + t.getTipo().toString());
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
		
			default -> throw new SyntacticException(t.getRiga(), "Atteso valore numerico o identificatore, trovato " + t.getTipo().toString());
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
				return parseExpP();
			}
			case MINUS -> {
				match(TokenType.MINUS);
				parseTr();
				return parseExpP();
			}
			case SEMI -> {
				return null;
				// Regola epsilon (vuoto), non facciamo nulla
			}
			
			default -> throw new SyntacticException(t.getRiga(), "Atteso '+', '-' o ';', trovato " + t.getTipo().toString());
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
			
			default -> throw new SyntacticException(t.getRiga(), "Atteso valore numerico o identificatore, trovato " + t.getTipo().toString());
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
				return LangType.Float;
			}
			case TYINT -> {
				match(TokenType.TYINT);
				return LangType.Int;
			}
			
			default -> throw new SyntacticException(t.getRiga(), "Atteso valore numerico, trovato " + t.getTipo().toString());
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
			
			default -> throw new SyntacticException(t.getRiga(), "Atteso valore numerico o identificatore, trovato " + t.getTipo().toString());
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
			
			default -> throw new SyntacticException(t.getRiga(), "Atteso '=' o operatore di assegnamento (+=, -=, ...), trovato " + t.getTipo().toString());
		}
	}
}
