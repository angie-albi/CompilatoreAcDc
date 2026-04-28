package test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import ast.NodeProgram;
import parser.Parser;
import parser.SyntacticException;
import scanner.Scanner;

/**
 * Classe di test per verificare il corretto funzionamento dell'analizzatore sintattico
 */
class TestParser {

	private static final String PATH = "test/data/testParser/";
	private Scanner scanner;
	private Parser parser;

	/**
	 * Metodo di supporto per inizializzare il parser puntando alla cartella dei test
	 * 
	 * @param fileName Nome del file da parsare
	 * @throws Exception Se il file non viene trovato
	 */
	private void inizializzaParser(String fileName) throws Exception {
		scanner = new Scanner(PATH + fileName);
		parser = new Parser(scanner);
	}

	/**
	 * Verifica che il parser analizzi correttamente un programma con sintassi valida
	 * File: testParserCorretto1.txt
	 */
	@Test
	void testParserCorretto1() throws Exception {
		inizializzaParser("testParserCorretto1.txt");
		assertDoesNotThrow(() -> parser.parse());
	}

	/**
	 * Verifica che il parser analizzi correttamente un altro programma con sintassi valida
	 * File: testParserCorretto2.txt
	 */
	@Test
	void testParserCorretto2() throws Exception {
		inizializzaParser("testParserCorretto2.txt");
		assertDoesNotThrow(() -> parser.parse());
	}

	/**
	 * TEST ERRORE: Verifica che il sistema sollevi una SyntacticException
	 * se manca un operatore di assegnamento valido
	 * File: testParserEcc_0.txt
	 */
	@Test
	void testParserEcc_0() throws Exception {
		inizializzaParser("testParserEcc_0.txt");
		SyntacticException e = assertThrows(SyntacticException.class, () -> parser.parse());
		assertTrue(e.getMessage().contains("Atteso '=' o operatore di assegnamento"));
	}

	/**
	 * TEST ERRORE: Verifica che il sistema sollevi una SyntacticException
	 * se c'è un operatore matematico in una posizione non valida
	 * File: testParserEcc_1.txt
	 */
	@Test
	void testParserEcc_1() throws Exception {
		inizializzaParser("testParserEcc_1.txt");
		SyntacticException e = assertThrows(SyntacticException.class, () -> parser.parse());
		assertTrue(e.getMessage().contains("Atteso valore numerico o identificatore, trovato TIMES"));
	}

	/**
	 * TEST ERRORE: Verifica che il sistema sollevi una SyntacticException
	 * se viene trovata un'istruzione malformata
	 * File: testParserEcc_2.txt
	 */
	@Test
	void testParserEcc_2() throws Exception {
		inizializzaParser("testParserEcc_2.txt");
		SyntacticException e = assertThrows(SyntacticException.class, () -> parser.parse());
		assertTrue(e.getMessage().contains("Token non valido in DSs: INT"));
	}

	/**
	 * TEST ERRORE: Verifica che il sistema sollevi una SyntacticException
	 * se un assegnamento è scritto in modo errato
	 * File: testParserEcc_3.txt
	 */
	@Test
	void testParserEcc_3() throws Exception {
		inizializzaParser("testParserEcc_3.txt");
		SyntacticException e = assertThrows(SyntacticException.class, () -> parser.parse());
		assertTrue(e.getMessage().contains("Atteso '=' o operatore di assegnamento"));
	}

	/**
	 * TEST ERRORE: Verifica che il sistema sollevi una SyntacticException
	 * se manca un identificatore dopo un comando come print
	 * File: testParserEcc_4.txt
	 */
	@Test
	void testParserEcc_4() throws Exception {
		inizializzaParser("testParserEcc_4.txt");
		SyntacticException e = assertThrows(SyntacticException.class, () -> parser.parse());
		assertTrue(e.getMessage().contains("Atteso token ID, trovato INT"));
	}

	/**
	 * TEST ERRORE: Verifica che il sistema sollevi una SyntacticException
	 * se un tipo di dato viene usato come nome di variabile
	 * File: testParserEcc_5.txt
	 */
	@Test
	void testParserEcc_5() throws Exception {
		inizializzaParser("testParserEcc_5.txt");
		SyntacticException e = assertThrows(SyntacticException.class, () -> parser.parse());
		assertTrue(e.getMessage().contains("Atteso token ID, trovato INT"));
	}

	/**
	 * TEST ERRORE: Verifica che il sistema sollevi una SyntacticException
	 * se una dichiarazione ha due tipi e nessun identificatore
	 * File: testParserEcc_6.txt
	 */
	@Test
	void testParserEcc_6() throws Exception {
		inizializzaParser("testParserEcc_6.txt");
		SyntacticException e = assertThrows(SyntacticException.class, () -> parser.parse());
		assertTrue(e.getMessage().contains("Atteso token ID, trovato TYFLOAT"));
	}

	/**
	 * TEST ERRORE: Verifica che il sistema sollevi una SyntacticException
	 * se un tipo di dato è seguito immediatamente da un assegnamento
	 * File: testParserEcc_7.txt
	 */
	@Test
	void testParserEcc_7() throws Exception {
		inizializzaParser("testParserEcc_7.txt");
		SyntacticException e = assertThrows(SyntacticException.class, () -> parser.parse());
		assertTrue(e.getMessage().contains("Atteso token ID, trovato ASSIGN"));
	}

	/**
	 * Verifica che il parser analizzi correttamente un file contenente solo dichiarazioni
	 * File: testSoloDich.txt
	 */
	@Test
	void testParserSoloDich() throws Exception {
		inizializzaParser("testSoloDich.txt");
		assertDoesNotThrow(() -> parser.parse());
	}

	/**
	 * Verifica che il parser analizzi correttamente un file con dichiarazioni e print
	 * File: testSoloDichPrint.txt
	 */
	@Test
	void testParserSoloDichPrint() throws Exception {
		inizializzaParser("testSoloDichPrint.txt");
		assertDoesNotThrow(() -> parser.parse());
	}

	/**
	 * Verifica la corretta costruzione dell'AST per un programma con sole dichiarazioni
	 * File: testSoloDich.txt
	 */
	@Test
	void testASTSoloDichiarazioni() throws Exception {
		inizializzaParser("testSoloDich.txt");
		NodeProgram ast = parser.parse();

		assertNotNull(ast);
		String astAtteso = "<Program,[<Decl,id=<Id=x>,tipo=INT,init=null>, <Decl,id=<Id=floati>,tipo=FLOAT,init=null>]>";
		assertEquals(astAtteso, ast.toString());
	}

	/**
	 * Verifica la corretta costruzione dell'AST per un programma con dichiarazioni e print
	 * File: testSoloDichPrint.txt
	 */
	@Test
	void testASTDichiarazioniEPrint() throws Exception {
		inizializzaParser("testSoloDichPrint.txt");
		NodeProgram ast = parser.parse();

		assertNotNull(ast);
		String astAtteso = "<Program,[<Decl,id=<Id=temp>,tipo=INT,init=null>, <Print,id=<Id=abc>>]>";
		assertEquals(astAtteso, ast.toString());
	}

	/**
	 * Verifica la corretta costruzione dell'AST generale includendo operazioni matematiche,
	 * assegnamenti composti e rispetto delle precedenze
	 * File: testParserCorretto1.txt
	 */
	@Test
	void testASTGenerale() throws Exception {
		inizializzaParser("testParserCorretto1.txt");
		NodeProgram ast = parser.parse();

		assertNotNull(ast);
		String astAtteso = "<Program,[<Print,id=<Id=stampa>>, <Decl,id=<Id=numberfloat>,tipo=FLOAT,init=<BinOp,PLUS:sx=<Cost,valore=3.5,tipo=FLOAT>,dx=<Cost,valore=8,tipo=INT>>>, <Decl,id=<Id=floati>,tipo=INT,init=null>, <Assign,id=<Id=a>,expr=<BinOp,PLUS:sx=<Deref,id=<Id=a>>,dx=<BinOp,PLUS:sx=<Cost,valore=5,tipo=INT>,dx=<Cost,valore=3,tipo=INT>>>>, <Assign,id=<Id=b>,expr=<BinOp,PLUS:sx=<Deref,id=<Id=a>>,dx=<Cost,valore=5,tipo=INT>>>]>";
		assertEquals(astAtteso, ast.toString());
	}
}