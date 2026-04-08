package test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import scanner.LexicalException;
import scanner.Scanner;
import token.Token;
import token.TokenType;

/**
 * Classe di test per verificare il corretto funzionamento dell'analizzatore lessicale (Scanner)
 */
public class TestScanner {

	private static final String PATH = "test/data/testScanner/";
	private Scanner scanner;

	/**
	 * Metodo di supporto per inizializzare lo scanner puntando alla cartella dei test
	 * 
	 * @param fileName Nome del file da scansionare
	 * @throws Exception Se il file non viene trovato
	 */
	private void inizializzaScanner(String fileName) throws Exception {
		scanner = new Scanner(PATH + fileName);
	}
	
	/**
	 * Verifica che un file vuoto generi immediatamente il Token EOF
	 * File: testEOF.txt
	 */
	@Test
	void testEOF() throws Exception {
		inizializzaScanner("testEOF.txt");
		Token t = scanner.nextToken();
		
		assertEquals(TokenType.EOF, t.getTipo());
		assertTrue(t.getRiga() == 1);
	}
	
	/**
	 * Verifica che i caratteri di formattazione (spazi, tabulazioni, a capo) 
	 * vengano ignorati correttamente senza produrre token
	 * File: caratteriSkip
	 */
	@Test
	void testCaratteriSkip() throws Exception {
		inizializzaScanner("caratteriSkip");
		
		Token t = scanner.nextToken();
		assertEquals(TokenType.EOF, t.getTipo());
	}
	
	/**
	 * Verifica che lo scanner riconosca correttamente i nomi delle variabili (Identificatori)
	 * File: testId.txt
	 */
	@Test
	void testId() throws Exception {
		inizializzaScanner("testId.txt");

		Token t = scanner.nextToken();
		assertEquals(TokenType.ID, t.getTipo());
		assertEquals(1, t.getRiga());
		assertEquals("jskjdsf2jdshkf", t.getValore());

		Token t1 = scanner.nextToken();
		assertEquals(TokenType.ID, t1.getTipo());
		assertEquals(2, t1.getRiga());
		assertEquals("printl", t1.getValore());

		Token t2 = scanner.nextToken();
		assertEquals(TokenType.ID, t2.getTipo());
		assertEquals(4, t2.getRiga());
		assertEquals("ffloat", t2.getValore());
	}

	/**
	 * Verifica che lo scanner riconosca correttamente le parole chiave del linguaggio
	 * File: testKeywords.txt
	 */
	@Test
	void testKeywords() throws Exception {
		inizializzaScanner("testKeywords.txt");

		Token t = scanner.nextToken();
		assertEquals(TokenType.PRINT, t.getTipo());

		Token t1 = scanner.nextToken();
		assertEquals(TokenType.TYFLOAT, t1.getTipo());

		Token t2 = scanner.nextToken();
		assertEquals(TokenType.TYINT, t2.getTipo());
	}
	
	/**
	 * Verifica la capacità dello scanner di distinguere correttamente tra parole chiave
	 * e identificatori quando sono presenti nello stesso file
	 * File: testIdKeyWords.txt
	 */
	@Test
	void testIdKeyWords() throws Exception {
		inizializzaScanner("testIdKeyWords.txt");

		int contatoreId = 0;
		int contatoreKeyWords = 0;

		Token t;
		while ((t = scanner.nextToken()).getTipo() != TokenType.EOF) {
			TokenType tipo = t.getTipo();
			
			if (tipo == TokenType.PRINT || tipo == TokenType.TYFLOAT || tipo == TokenType.TYINT) {
				contatoreKeyWords++;
			} else if (tipo == TokenType.ID) {
				contatoreId++;
			}
		}

		assertEquals(4, contatoreKeyWords);
		assertEquals(4, contatoreId);
	}
	
	/**
	 * Verifica il corretto riconoscimento di operatori matematici, delimitatori
	 * e operatori di assegnamento composti
	 * File: testOpsDels.txt
	 */
	@Test
	void testOpsDels() throws Exception {
		inizializzaScanner("testOpsDels.txt");

		int contatoreDels = 0;
		int contatoreOp = 0;
		int contatoreOpAssign = 0;

		Token t;
		while ((t = scanner.nextToken()).getTipo() != TokenType.EOF) {
			switch (t.getTipo()) {
				case PLUS:
				case MINUS:
				case TIMES:
				case DIVIDE:
					contatoreOp++;
					break;
				case SEMI:
				case ASSIGN:
					contatoreDels++;
					break;
				case OP_ASSIGN:
					contatoreOpAssign++;
					break;
				default:
					break;
			}
		}

		assertEquals(5, contatoreOp);
		assertEquals(4, contatoreOpAssign);
		assertEquals(3, contatoreDels);
	}

	/**
	 * TEST ERRORE: Verifica che il sistema si blocchi sollevando una LexicalException
	 * se incontra un carattere non previsto dal linguaggio (es. ^)
	 * File: caratteriNonCaratteri.txt
	 */
	@Test
	void testCaratteriNonCaratteri() throws Exception {
		inizializzaScanner("caratteriNonCaratteri.txt");

		Exception e = assertThrows(LexicalException.class, () -> {
			while (true) {
				scanner.nextToken();
			}
		});

		assertTrue(e.getMessage().contains("Carattere invalido (^)"));
	}

	/**
	 * Verifica che lo scanner riconosca i numeri interi, inclusi quelli con zeri iniziali
	 * File: testInt.txt
	 */
	@Test
	void testInt() throws Exception {
		inizializzaScanner("testInt.txt");
		
		Token t = scanner.nextToken();
		assertEquals(TokenType.INT, t.getTipo());
		assertEquals(1, t.getRiga());
		assertEquals("0050", t.getValore());

		Token t1 = scanner.nextToken();
		assertEquals(TokenType.INT, t1.getTipo());
		assertEquals(2, t1.getRiga());
		assertEquals("698", t1.getValore());
	}

	/**
	 * Verifica che lo scanner riconosca i numeri in virgola mobile (Float)
	 * File: testFloat.txt
	 */
	@Test
	void testFloat() throws Exception {
		inizializzaScanner("testFloat.txt");

		Token t = scanner.nextToken();
		assertEquals(TokenType.FLOAT, t.getTipo());
		assertEquals(1, t.getRiga());
		assertEquals("098.8095", t.getValore());

		Token t1 = scanner.nextToken();
		assertEquals(TokenType.FLOAT, t1.getTipo());
		assertEquals("0.", t1.getValore());
	}

	/**
	 * TEST ERRORE: Verifica che il sistema sollevi una LexicalException
	 * se un float supera il limite massimo di 5 cifre decimali
	 * File: erroriNumbers.txt
	 */
	@Test
	void testErroriNumbers() throws Exception {
		inizializzaScanner("erroriNumbers.txt");

		scanner.nextToken();
		scanner.nextToken();

		Exception e = assertThrows(LexicalException.class, () -> {
			scanner.nextToken();
		});
		
		assertTrue(e.getMessage().contains("Troppe cifre decimali"));
	}

	/**
	 * Verifica il comportamento dello scanner su un file sorgente realistico,
	 * assicurandosi che non vengano sollevate eccezioni fino alla fine del file
	 * File: testGenerale.txt
	 */
	@Test
	void testGenerale() throws Exception {
		inizializzaScanner("testGenerale.txt");

		Token t;
		while ((t = scanner.nextToken()).getTipo() != TokenType.EOF) {
			// Nessuna operazione necessaria
		}
		
		assertEquals(TokenType.EOF, t.getTipo());
	}
}