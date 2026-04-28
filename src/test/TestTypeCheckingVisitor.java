package test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import ast.NodeProgram;
import parser.Parser;
import scanner.Scanner;
import visitor.TypeCheckingVisitor;

/**
 * Classe di test per verificare il corretto funzionamento dell'analizzatore semantico 
 */
class TestTypeCheckingVisitor {

	private static final String PATH = "src/test/data/testTypeChecking/";

	/**
	 * Metodo di supporto per analizzare un file ed eseguire il Type Checking
	 * 
	 * @param fileName Il nome del file da testare
	 * @return Il visitatore dopo aver completato l'analisi, utile per interrogare lo stato degli errori
	 * @throws Exception In caso di errori lessicali o sintattici imprevisti
	 */
	private TypeCheckingVisitor eseguiTypeChecking(String fileName) throws Exception {
		Scanner scanner = new Scanner(PATH + fileName);
		Parser parser = new Parser(scanner);
		NodeProgram ast = parser.parse();
		
		TypeCheckingVisitor typeChecker = new TypeCheckingVisitor();
		ast.accept(typeChecker);
		
		return typeChecker;
	}

	/**
	 * TEST ERRORE: Verifica che il compilatore intercetti una variabile dichiarata più volte
	 * File: 1_dicRipetute.txt
	 */
	@Test
	void testDichiarazioniRipetute() throws Exception {
		TypeCheckingVisitor tc = eseguiTypeChecking("1_dicRipetute.txt");
		assertTrue(tc.hasErrors(), "Il test dovrebbe fallire per dichiarazioni ripetute");
	}

	/**
	 * TEST ERRORE: Verifica che il compilatore intercetti l'uso di variabili non dichiarate
	 * File: 2_idNonDec.txt
	 */
	@Test
	void testIdNonDichiarato1() throws Exception {
		TypeCheckingVisitor tc = eseguiTypeChecking("2_idNonDec.txt");
		assertTrue(tc.hasErrors(), "Il test dovrebbe fallire per uso di variabile non dichiarata");
	}

	/**
	 * TEST ERRORE: Verifica che il compilatore intercetti l'uso di variabili non dichiarate dentro un'espressione
	 * File: 3_idNonDec
	 */
	@Test
	void testIdNonDichiarato2() throws Exception {
		TypeCheckingVisitor tc = eseguiTypeChecking("3_idNonDec");
		assertTrue(tc.hasErrors(), "Il test dovrebbe fallire per variabile non dichiarata in espressione");
	}

	/**
	 * TEST ERRORE: Verifica che il compilatore intercetti un assegnamento di tipo non compatibile (es. float in int)
	 * File: 4_tipoNonCompatibile.txt
	 */
	@Test
	void testTipoNonCompatibile() throws Exception {
		TypeCheckingVisitor tc = eseguiTypeChecking("4_tipoNonCompatibile.txt");
		assertTrue(tc.hasErrors(), "Il test dovrebbe fallire per incompatibilità di tipi");
	}

	/**
	 * TEST CORRETTO: Verifica un programma corretto con conversioni implicite valide (int -> float)
	 * File: 5_corretto.txt
	 */
	@Test
	void testCorretto1() throws Exception {
		TypeCheckingVisitor tc = eseguiTypeChecking("5_corretto.txt");
		assertFalse(tc.hasErrors(), "Il test NON dovrebbe presentare errori semantici");
	}

	/**
	 * TEST CORRETTO: Verifica espressioni matematiche complesse ma sintatticamente e semanticamente valide
	 * File: 6_corretto.txt
	 */
	@Test
	void testCorretto2() throws Exception {
		TypeCheckingVisitor tc = eseguiTypeChecking("6_corretto.txt");
		assertFalse(tc.hasErrors(), "Il test NON dovrebbe presentare errori semantici");
	}

	/**
	 * TEST CORRETTO: Verifica assegnamenti composti (es. -=) e divisioni
	 * File: 7_corretto.txt
	 */
	@Test
	void testCorretto3() throws Exception {
		TypeCheckingVisitor tc = eseguiTypeChecking("7_corretto.txt");
		assertFalse(tc.hasErrors(), "Il test NON dovrebbe presentare errori semantici");
	}
}