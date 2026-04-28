package ast;

/**
 * Rappresenta i tipi di operatori di assegnamento supportati dal linguaggio "ac".
 * E' utile per distinguere un assegnamento semplice (=) dagli assegnamenti composti (+=, -=, *=, /=)
 */
public enum AssignOper {
	ASSIGN, 
	PLUSASSIGN, 
	MINUSASSIGN, 
	TIMESASSIGN,
	DIVASSIGN
}
