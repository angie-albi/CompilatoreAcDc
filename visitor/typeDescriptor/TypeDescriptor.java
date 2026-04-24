package visitor.typeDescriptor;

/**
 * Descrittore di tipo utilizzato durante la fase di Type-Checking
 * Trasporta l'informazione sul tipo risultante di un'espressione o l'esito di un'istruzione
 */
public class TypeDescriptor {
	private TipoTypeDescriptor tipo;
	private String msg;
	
	/**
	 * Costruttore per descrittori di tipo semplici
	 * 
	 * @param tipo Il tipo associato (INT, FLOAT, OK)
	 */
	public TypeDescriptor(TipoTypeDescriptor tipo) {
		super();
		this.tipo = tipo;
		this.msg = null;
	}

	/**
	 * Costruttore per descrittori di errore
	 * 
	 * @param tipo Deve essere ERROR
	 * @param msg Messaggio descrittivo dell'errore semantico
	 */
	public TypeDescriptor(TipoTypeDescriptor tipo, String msg) {
		super();
		this.tipo = tipo;
		this.msg = msg;
	}
	
	/**
	 * Ritorna il tipo dell'espressione
	 * 
	 * @return Il tipo associato
	 */
	public TipoTypeDescriptor getTipo() {
		return tipo;
	}

	/**
	 * Ritorna il messaggio dell'ERRORE
	 * 
	 * @return Il messaggio associato (oppure null se non è un errore)
	 */
	public String getMsg() {
		return msg;
	}

	/**
	 * Verifica se il tipo corrente è compatibile con un altro tipo td (INT è compatibile con FLOAT)
	 * 
	 * @param tD Il tipo con cui verificare la compatibilità
	 * @return true se i tipi sono compatibili, false altrimenti
	 */
	public boolean compatibile(TypeDescriptor td) {
		boolean cond1 = td.getTipo() == this.tipo;
		boolean cond2 = this.tipo == TipoTypeDescriptor.FLOAT && td.getTipo() == TipoTypeDescriptor.INT;
		
		return cond1 || cond2;
	}
	
	/**
	 * Restituisce una rappresentazione testuale del descrittore di tipo
	 * 
	 * @return Una stringa nel formato <TypeDescriptor,tipo=...> per tipo semplice
	 * 		   altrimenti, <TypeDescriptor,tipo=...,msg=...>
	 */
	@Override
	public String toString() {
		if(this.tipo == TipoTypeDescriptor.ERROR)
			return "<TypeDescriptor,tipo=" + tipo + ",msg=" + msg + ">";
		
		return "<TypeDescriptor,tipo=" + tipo + ">";
	}
}