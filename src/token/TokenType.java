package token;
/**
 * Questa enumerazione contiene tutti i tipi di Token validi per il linguaggio "ac"
 */
public enum TokenType {
	INT, 
	FLOAT, 
	ID,
	TYINT,		//parola chiave "int"
	TYFLOAT,	//parola chiave "float"
	PRINT,		//parola chiave "print"
	OP_ASSIGN,
	ASSIGN,
	PLUS,
	MINUS,
	TIMES,
	DIVIDE,
	SEMI,		//delimitazione
	EOF;		//fine del file
}
