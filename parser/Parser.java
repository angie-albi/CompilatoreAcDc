package parser;

import java.util.ArrayList;

import ast.AssignOper;
import ast.LangOper;
import ast.LangType;
import ast.NodeAssign;
import ast.NodeBinOp;
import ast.NodeCost;
import ast.NodeDecSt;
import ast.NodeDecl;
import ast.NodeDeref;
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
	 * @throws SyntacticException Se il token non corrisponde o c'è un errore lessicale
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
	 * 
	 * @return Il nodo NodeProgram contenente tutte le istruzioni
	 * @throws SyntacticException Se il token iniziale non è valido
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
	 * 
	 * @return Una lista di dichiarazioni e istruzioni
	 * @throws SyntacticException Se si incontra un token non valido nel corpo del programma
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
				dss.add(0, stm);
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
	 * 
	 * @return Il nodo AST per la dichiarazione della variabile
	 * @throws SyntacticException Se la sintassi della dichiarazione è errata
	 */
	private NodeDecl parseDcl() throws SyntacticException {
		Token t = this.peek();

		switch (t.getTipo()) {
			//Dcl -> Ty id DclP
			case TYFLOAT, TYINT -> {
				LangType tipo = parseTy();
				NodeId nome = new NodeId(match(TokenType.ID).getValore());
				NodeExpr init = parseDclP();
				
				return new NodeDecl(nome, tipo, init);
			}
			
			default -> throw new SyntacticException(t.getRiga(), "Atteso valore numerico, trovato " +  t.getTipo().toString());
		}
	}

	/**
	 * Regole 5, 6: DclP -> ; | = Exp ;
	 * 
	 * @return L'espressione di inizializzazione, oppure null se non inizializzata
	 * @throws SyntacticException Se manca il punto e virgola o l'espressione è malformata
	 */
	private NodeExpr parseDclP() throws SyntacticException {
		Token t = this.peek();

		switch (t.getTipo()) {
			//DclP -> ;
			case SEMI -> {
				match(TokenType.SEMI);
				return null;
			}
			//DclP -> = Exp ;
			case ASSIGN -> {
				match(TokenType.ASSIGN);
				NodeExpr expr = parseExp();
				match(TokenType.SEMI);
				return expr;
			}
			
			default -> throw new SyntacticException(t.getRiga(), "Atteso ';' oppure '=', trovato " + t.getTipo().toString());
		}
	}

	/**
	 * Regole 7, 8: Stm -> id Op Exp ; | print id ;
	 * 
	 * @return Il nodo AST che rappresenta l'istruzione (assegnamento o stampa)
	 * @throws SyntacticException Se l'istruzione è malformata
	 */
	private NodeStat parseStm() throws SyntacticException {
		Token t = this.peek();

		switch (t.getTipo()) {
			//Stm -> id Op Exp ;
			case ID -> {
				NodeId nome = new NodeId(match(TokenType.ID).getValore());
				AssignOper op = parseOp();
				NodeExpr expr = parseExp();
				match(TokenType.SEMI);
				
				if(op != AssignOper.ASSIGN) {
					LangOper operMat = switch (op) {
						case PLUSASSIGN -> LangOper.PLUS;
						case MINUSASSIGN -> LangOper.MINUS;
						case TIMESASSIGN -> LangOper.TIMES;
						case DIVASSIGN -> LangOper.DIVIDE;
						default -> throw new SyntacticException(t.getRiga(), "Assegnamento non valido, trovato " + t.getTipo().toString());
					};
					
					NodeExpr sx = new NodeDeref(nome);
					NodeBinOp binOp = new NodeBinOp(operMat, sx, expr);
					
					return new NodeAssign(nome, binOp);
				}
				
				return new NodeAssign(nome, expr);
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
	 * 
	 * @return Il nodo radice dell'espressione valutata
	 * @throws SyntacticException Se l'espressione è malformata
	 */
	private NodeExpr parseExp() throws SyntacticException {
		Token t = this.peek();
		
		switch (t.getTipo()) {
		    //Exp -> Tr ExpP
			case ID, FLOAT, INT -> {
				NodeExpr sx = parseTr();
				return parseExpP(sx);
			}
		
			default -> throw new SyntacticException(t.getRiga(), "Atteso valore numerico o identificatore, trovato " + t.getTipo().toString());
		}
	}
		

	/**
	 * Regole 10, 11, 12: ExpP -> + Tr ExpP | - Tr ExpP | epsilon
	 * 
	 * @param sx L'espressione già valutata alla sinistra dell'operatore
	 * @return Il nodo dell'espressione risultante
	 * @throws SyntacticException Se si verifica un errore sintattico
	 */
	private NodeExpr parseExpP(NodeExpr sx) throws SyntacticException {
		Token t = this.peek();

		switch (t.getTipo()) {
		 	//ExpP -> + Tr ExpP
			case PLUS -> {
				match(TokenType.PLUS);
				NodeExpr tr = parseTr();
				NodeBinOp binOp = new NodeBinOp(LangOper.PLUS, sx, tr);
				return parseExpP(binOp);
			}
			//ExpP -> - Tr ExpP
			case MINUS -> {
				match(TokenType.MINUS);
				NodeExpr tr = parseTr();
				NodeBinOp binOp = new NodeBinOp(LangOper.MINUS, sx, tr);
				return parseExpP(binOp);
			}
			//ExpP -> epsilon
			case SEMI -> {
				return sx;
			}
			
			default -> throw new SyntacticException(t.getRiga(), "Atteso '+', '-' o ';', trovato " + t.getTipo().toString());
		}
	}

	/**
	 * Regola 13: Tr -> Val TrP
	 * 
	 * @return Il nodo termine valutato
	 * @throws SyntacticException Se il termine è malformato
	 */
	private NodeExpr parseTr() throws SyntacticException {
		Token t = this.peek();

		switch (t.getTipo()) {
			//Tr -> Val TrP 
			case ID, FLOAT, INT -> {
				NodeExpr sx = parseVal();
				return parseTrP(sx);
			}
			
			default -> throw new SyntacticException(t.getRiga(), "Atteso valore numerico o identificatore, trovato " + t.getTipo().toString());
		}
	}

	/**
	 * Regole 14, 15, 16: TrP -> * Val TrP | / Val TrP | epsilon
	 * 
	 * @param sx L'espressione già valutata alla sinistra dell'operatore
	 * @return Il nodo dell'espressione risultante
	 * @throws SyntacticException Se si verifica un errore sintattico
	 */
	private NodeExpr parseTrP(NodeExpr sx) throws SyntacticException {
		Token t = this.peek();

		switch (t.getTipo()) {
			//TrP -> * Val TrP
			case TIMES -> {
				match(TokenType.TIMES);
				NodeExpr tr =parseVal();
				NodeBinOp binOp = new NodeBinOp(LangOper.TIMES, sx, tr);
				return parseTrP(binOp);
			}
			//TrP -> / Val TrP
			case DIVIDE -> {
				match(TokenType.DIVIDE);
				NodeExpr tr = parseVal();
				NodeBinOp binOp = new NodeBinOp(LangOper.DIVIDE, sx, tr);
				return parseTrP(binOp);
			}
			//TrP -> epsilon
			case PLUS, MINUS, SEMI -> {
				return sx;
			}
			
			default -> throw new SyntacticException(t.getRiga(), "Atteso '*', '/', '+', '-' o ';', trovato " + t.getTipo());
		}
	}

	/**
	 * Regole 17, 18: Ty -> float | int
	 * 
	 * @return Il tipo enumerato LangType corrispondente
	 * @throws SyntacticException Se il tipo non è valido
	 */
	private LangType parseTy() throws SyntacticException {
		Token t = this.peek();

		switch (t.getTipo()) {
			//Ty -> float
			case TYFLOAT -> {
				match(TokenType.TYFLOAT);
				return LangType.FLOAT;
			}
			//Ty -> int
			case TYINT -> {
				match(TokenType.TYINT);
				return LangType.INT;
			}
			
			default -> throw new SyntacticException(t.getRiga(), "Atteso valore numerico, trovato " + t.getTipo().toString());
		}
	}

	/**
	 * Regole 19, 20, 21: Val -> intVal | floatVal | id
	 * 
	 * @return Il nodo rappresentante un valore costante (NodeCost) o una variabile (NodeDeref)
	 * @throws SyntacticException Se il token non è un id o un numero
	 */
	private NodeExpr parseVal() throws SyntacticException {
		Token t = this.peek();

		switch (t.getTipo()) {
			//Val -> intVal 
			case INT -> {
				String nome = match(TokenType.INT).getValore();
				return new NodeCost(nome, LangType.INT);
			}
			//Val -> floatVal
			case FLOAT -> {
				String nome = match(TokenType.FLOAT).getValore();
				return new NodeCost(nome, LangType.FLOAT);
			}
			//Val -> id
			case ID -> {
				NodeId nome = new NodeId(match(TokenType.ID).getValore());
				return new NodeDeref(nome);
			}
			
			default -> throw new SyntacticException(t.getRiga(), "Atteso valore numerico o identificatore, trovato " + t.getTipo().toString());
		}
	}

	/**
	 * Regole 22, 23: Op -> = | opAss
	 * 
	 * @return Il tipo di operazione di assegnamento (AssignOper)
	 * @throws SyntacticException Se il token non è un operatore di assegnamento valido
	 */
	private AssignOper parseOp() throws SyntacticException {
		Token t = this.peek();

		switch (t.getTipo()) {
			//Op -> =
			case ASSIGN -> {
				match(TokenType.ASSIGN);
				return AssignOper.ASSIGN;
			}
			//Op -> opAss (+=, -=, *=, /=)
			case OP_ASSIGN -> {
				String oper = match(TokenType.OP_ASSIGN).getValore();
				
				switch (oper) {
					case "+=":
						return AssignOper.PLUSASSIGN;
					case "-=":
						return AssignOper.MINUSASSIGN;
					case "*=": 
						return AssignOper.TIMESASSIGN;
					case "/=":
						return AssignOper.DIVASSIGN;
				}
			}
			
			default -> throw new SyntacticException(t.getRiga(), "Atteso '=' o operatore di assegnamento (+=, -=, ...), trovato " + t.getTipo().toString());
		}
		
		return null;
	}
}