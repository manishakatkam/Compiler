package cop5556sp17.AST;

import cop5556sp17.AST.Type.TypeName;
import cop5556sp17.Scanner.Token;

public abstract class Expression extends ASTNode {
	private Dec decs;
	private TypeName type;
	protected Expression(Token firstToken) {
		super(firstToken);
	}
	@Override
	abstract public Object visit(ASTVisitor v, Object arg) throws Exception;
	
	public void setType(TypeName t){
		type = t;
	}
	
	public TypeName getType(){
		return type;
	}
	
	public void setDec(Dec d){
		decs=d;
	}
	public Dec getDec(){
		return decs;
	}	

	

}
