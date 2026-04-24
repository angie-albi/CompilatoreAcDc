package visitor;

import ast.LangOper;
import ast.LangType;
import ast.NodeAssign;
import ast.NodeBinOp;
import ast.NodeCost;
import ast.NodeDecSt;
import ast.NodeDecl;
import ast.NodeDeref;
import ast.NodeId;
import ast.NodePrint;
import ast.NodeProgram;
import symbolTable.Attributes;
import symbolTable.SymbolTable;
import visitor.typeDescriptor.TipoTypeDescriptor;
import visitor.typeDescriptor.TypeDescriptor;

/**
 * Visitatore per l'Analisi Semantica (Type Checking)
 * Attraversa l'AST per verificare la correttezza dei tipi, popolare la 
 * Symbol Table e assicurarsi che le variabili siano dichiarate prima dell'uso
 */
public class TypeCheckingVisitor implements IVisitor{

	private TypeDescriptor resType; // mantiene il risultato della visita
	private boolean errors; // flag che indica se si è presentato un errore semantico
	
	/**
	 * Costruttore, inizializza il visitatore, la Symbol Table e il flag di stato
	 */
	public TypeCheckingVisitor() {
		resType = null;
		SymbolTable.init();
		errors = false;
	}
	
	/**
	 * Verifica se sono stati riscontrati errori semantici durante l'intera visita
	 * 
	 * @return true se c'è almeno un errore, false altrimenti
	 */
	public boolean hasErrors() {
		return errors;
	}
	
	/**
	 * Visita tutte le dichiarazioni e istruzioni sequenzialmente dall'alto verso il basso
	 */
	@Override
	public void visit(NodeProgram node) {
		
		for (NodeDecSt decSt : node.getDecSt()) {
			decSt.accept(this);
		}
	}
	
	/**
	 * Verifica che l'identificatore sia presente nella Symbol Table (già dichiarato)
	 * e ne restituisce il tipo
	 */
	@Override
	public void visit(NodeId node) {
		Attributes a = SymbolTable.lookup(node.getName());
		
		// Il nodo non è presente nella SymbolTable (non è stata dichiarata la variabile)
		if(a == null) {
			errors = true;
			String errorsMsg = "Errore Semantico: La variabile " + node.getName() + " non è stata dichiarata";
			resType = new TypeDescriptor(TipoTypeDescriptor.ERROR, errorsMsg);
			
			System.err.println(errorsMsg);
			return;
		}
		
		TipoTypeDescriptor tipo;
		
		if(a.getTipo() == LangType.INT)
			tipo = TipoTypeDescriptor.INT;
		else
			tipo = TipoTypeDescriptor.FLOAT;
		
		resType = new TypeDescriptor(tipo);
	}

	/**
	 * Analizza una dichiarazione (es. int a = 5;)
	 * Verifica che la variabile non sia già dichiarata, la aggiunge alla Symbol Table 
	 * e controlla l'eventuale compatibilità dell'espressione di inizializzazione
	 */
	@Override
	public void visit(NodeDecl node) {
		Attributes a = SymbolTable.lookup(node.getId().getName());
		
		// Il nodo è già presente nella SymbolTable (è già stata dichiarata la variabile)
		if(a != null) {
			errors = true;
			String errorsMsg = "Errore Semantico: La variabile " + node.getId().getName() + " è già stata dichiarata";
			resType = new TypeDescriptor(TipoTypeDescriptor.ERROR, errorsMsg);
			
			System.err.println(errorsMsg);
			return;
		}
		
		SymbolTable.enter(node.getId().getName(), new Attributes(node.getTipo()));
			
		// Controllo che ci sia l'inizializzazione
		if(node.getInit() != null) {
			node.getInit().accept(this);
			
			// Controllo errore
			if(resType.getTipo() != TipoTypeDescriptor.ERROR) {
				TipoTypeDescriptor tipo;
				
				if(node.getTipo() == LangType.INT)
					tipo = TipoTypeDescriptor.INT;
				else
					tipo = TipoTypeDescriptor.FLOAT;
				
				TypeDescriptor tmp = new TypeDescriptor(tipo);
				
				// Controllo della compatibilità
				if (!tmp.compatibile(resType)) {
					errors = true;
					String errorsMsg = "Errore Semantico: La dichiarazione della variabile " + node.getId().getName() + " non è compatibile con il tipo";
					
					resType = new TypeDescriptor(TipoTypeDescriptor.ERROR, errorsMsg);
					System.err.println(errorsMsg);
					return;
				}
			}	
		}
		
		resType = new TypeDescriptor(TipoTypeDescriptor.OK);
	}

	/**
	 * Analizza un assegnamento (es. a = 5;)
	 * Verifica che la variabile di destinazione e l'espressione siano di tipi compatibili
	 */
	@Override
	public void visit(NodeAssign node) {
		node.getId().accept(this);
		TypeDescriptor idType = resType;
		
		// Controllo errore
		if(idType.getTipo() == TipoTypeDescriptor.ERROR)
			return;
		
		node.getExpr().accept(this);
		TypeDescriptor expType = resType;
		
		// Controllo errore
		if(expType.getTipo() == TipoTypeDescriptor.ERROR)
			return;
		
		// Controllo compatibilità
		if (!idType.compatibile(expType)) {
			errors = true;
			String errorsMsg = "Errore Semantico: L'assegnamento della variabile " + node.getId().getName() + " non è compatibile con il tipo";
			
			resType = new TypeDescriptor(TipoTypeDescriptor.ERROR, errorsMsg);
			System.err.println(errorsMsg);
			return;
		}
		
		resType = new TypeDescriptor(TipoTypeDescriptor.OK);
	}

	/**
	 * Analizza l'istruzione di stampa
	 * Si assicura che la variabile da stampare esista
	 */
	@Override
	public void visit(NodePrint node) {
		node.getId().accept(this);

		// Controllo errore
		if(resType.getTipo() == TipoTypeDescriptor.ERROR)
			return;
		
		resType = new TypeDescriptor(TipoTypeDescriptor.OK);
	}

	/**
	 * Dereferenziazione: estrae il valore di una variabile in un'espressione
	 * Delega al NodeId la verifica
	 */
	@Override
	public void visit(NodeDeref node) {
		node.getId().accept(this);
		// resType assumerà automaticamente il valore o l'errore generato dall'id
	}

	/**
	 * Assegna il tipo nativo alla costante (numero)
	 */
	@Override
	public void visit(NodeCost node) {
		TipoTypeDescriptor tipo;
		
		if (node.getTipo() == LangType.INT)
			tipo = TipoTypeDescriptor.INT;
		else
			tipo = TipoTypeDescriptor.FLOAT;
		
		resType = new TypeDescriptor(tipo);
	}

	/**
	 * Analizza le operazioni binarie (+, -, *, /)
	 * Calcola il tipo risultante dell'operazione e decora l'AST convertendo
	 * la divisione normale in divisione float se necessario
	 */
	@Override
	public void visit(NodeBinOp node) {
		node.getSx().accept(this);
		TypeDescriptor sxTD = this.resType;
		TipoTypeDescriptor tipoSx = sxTD.getTipo();
		
		// Controllo errore: figlio sinistro
		if (tipoSx == TipoTypeDescriptor.ERROR) {
			return;
		}
		
		node.getDx().accept(this);
		TypeDescriptor dxTD = this.resType;
		TipoTypeDescriptor tipoDx = dxTD.getTipo();
		
		// Controllo errore: figlio destro
		if (tipoDx == TipoTypeDescriptor.ERROR) {
			return;
		}
		
		TipoTypeDescriptor tipo = TipoTypeDescriptor.FLOAT;
		if (tipoSx == TipoTypeDescriptor.INT && tipoDx == TipoTypeDescriptor.INT) {
			tipo = TipoTypeDescriptor.INT;
		} else if (node.getOp() == LangOper.DIVIDE) {
			node.setOp(LangOper.DIV_FLOAT);
		}
			
		this.resType = new TypeDescriptor(tipo);
	}
}