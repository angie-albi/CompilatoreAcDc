package test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import ast.NodeProgram;
import parser.Parser;
import scanner.Scanner;
import visitor.CodeGeneratorVisitor;
import visitor.TypeCheckingVisitor;

/**
 * Classe di test per verificare la corretta generazione del codice per la macchina 'dc'
 */
class TestCodeGeneratorVisitor {

	private static final String PATH = "src/test/data/testCodeGenerator/";

	/**
	 * Metodo di supporto per eseguire l'intera pipeline fino alla generazione del codice
	 *
	 * @param fileName Il nome del file da testare
	 * @return Il CodeGeneratorVisitor dopo aver completato l'analisi
	 * @throws Exception In caso di errori imprevisti
	 */
	private CodeGeneratorVisitor eseguiGenerazione(String fileName) throws Exception {
		Scanner scanner = new Scanner(PATH + fileName);
		Parser parser = new Parser(scanner);
		NodeProgram ast = parser.parse();
		
		TypeCheckingVisitor typeChecker = new TypeCheckingVisitor();
		ast.accept(typeChecker);
		assertFalse(typeChecker.hasErrors());
		
		CodeGeneratorVisitor codeGenerator = new CodeGeneratorVisitor();
		ast.accept(codeGenerator);
		
		return codeGenerator;
	}

	/**
	 * TEST CORRETTO: Verifica la generazione del codice per assegnamenti base
	 * File: 1_assign.txt
	 */
	@Test
	void testAssign() throws Exception {
		CodeGeneratorVisitor cg = eseguiGenerazione("1_assign.txt");
		assertEquals("", cg.getLog());
		assertNotNull(cg.getCodiceDc());
		assertFalse(cg.getCodiceDc().isEmpty());
	}

	/**
	 * TEST CORRETTO: Verifica la generazione del codice per divisioni (inclusa float)
	 * File: 2_divsioni.txt
	 */
	@Test
	void testDivisioni() throws Exception {
		CodeGeneratorVisitor cg = eseguiGenerazione("2_divsioni.txt"); 
		assertEquals("", cg.getLog());
		assertNotNull(cg.getCodiceDc());
		assertTrue(cg.getCodiceDc().contains("5 k / 0 k"));
	}

	/**
	 * TEST CORRETTO: Verifica un programma generale con espressioni matematiche miste
	 * File: 3_generale.txt
	 */
	@Test
	void testGenerale() throws Exception {
		CodeGeneratorVisitor cg = eseguiGenerazione("3_generale.txt");
		assertEquals("", cg.getLog());
		assertNotNull(cg.getCodiceDc());
		assertFalse(cg.getCodiceDc().isEmpty());
	}

	/**
	 * TEST ERRORE: Verifica che il compilatore intercetti l'esaurimento dei registri
	 * File: 4_registriFiniti.txt
	 */
	@Test
	void testRegistriFiniti() throws Exception {
		CodeGeneratorVisitor cg = eseguiGenerazione("4_registriFiniti.txt");
		
		assertNotEquals("", cg.getLog());
		assertTrue(cg.getLog().contains("Registri esauriti"));
	}
}