package test;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import token.Token;
import token.TokenType;

/**
 * Classe di test per verificare il corretto funzionamento della classe Token
 */
class TestToken {

	/**
	 * Testa la creazione e la stampa di un Token che NON ha un valore testuale
	 */
	@Test
	void testTokenSenzaValore() {
		Token tokenPlus = new Token(1, TokenType.PLUS);
		
		assertEquals(1, tokenPlus.getRiga());
		assertEquals(TokenType.PLUS, tokenPlus.getTipo());
		assertNull(tokenPlus.getValore());
		
		assertEquals("<PLUS , r:1>", tokenPlus.toString());
	}
	
	/**
	 * Testa la creazione e la stampa di un Token che HA un valore testuale
	 */
	@Test
	void testTokenConValore() {
		Token tokenId = new Token(5, TokenType.ID, "tempa");
		
		assertEquals(5, tokenId.getRiga());
		assertEquals(TokenType.ID, tokenId.getTipo());
		assertEquals("tempa", tokenId.getValore());
		
		assertEquals("<ID , r:5 , tempa>", tokenId.toString());
	}

}