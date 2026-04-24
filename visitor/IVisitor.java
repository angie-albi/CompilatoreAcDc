package visitor;

public interface IVisitor {

}


/*
openScope() 		// apre un nuovo scope in cui inserire i nomi
closeScope() 		// chiude lo scope piu’ recente (quindi i nomi sono interpretati nel precedente scope)
enter(nome,infNome) // inserisce il nome nello scope corrente con l’informazione sulla dichiarazione
lookup(nome) 		
dichLocale(nome) 	// true se il nome e’ dichiarato nello scope corrente false altrimenti
*/