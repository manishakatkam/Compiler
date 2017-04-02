package cop5556sp17;
import static cop5556sp17.AST.Type.TypeName.*;
import cop5556sp17.Scanner.Token;

import cop5556sp17.AST.ASTNode;

import cop5556sp17.AST.ASTVisitor;
import cop5556sp17.AST.Tuple;
import cop5556sp17.AST.AssignmentStatement;
import cop5556sp17.AST.BinaryChain;
import cop5556sp17.AST.BinaryExpression;
import cop5556sp17.AST.Block;
import cop5556sp17.AST.BooleanLitExpression;
import cop5556sp17.AST.Chain;
import cop5556sp17.AST.ChainElem;
import cop5556sp17.AST.ConstantExpression;
import cop5556sp17.AST.Dec;
import cop5556sp17.AST.Expression;
import cop5556sp17.AST.FilterOpChain;
import cop5556sp17.AST.FrameOpChain;
import cop5556sp17.AST.IdentChain;
import cop5556sp17.AST.IdentExpression;
import cop5556sp17.AST.IdentLValue;
import cop5556sp17.AST.IfStatement;
import cop5556sp17.AST.ImageOpChain;
import cop5556sp17.AST.IntLitExpression;
import cop5556sp17.AST.ParamDec;
import cop5556sp17.AST.Program;
import cop5556sp17.AST.SleepStatement;
import cop5556sp17.AST.Statement;
import cop5556sp17.AST.Type.TypeName;
import cop5556sp17.AST.WhileStatement;
import cop5556sp17.AST.Type.*;
import java.util.List;
import java.util.ArrayList;

import cop5556sp17.Scanner.Kind;
import cop5556sp17.Scanner.LinePos;
import cop5556sp17.Scanner.Token;
import static cop5556sp17.AST.Type.TypeName.*;
import static cop5556sp17.Scanner.Kind.ARROW;
import static cop5556sp17.Scanner.Kind.KW_HIDE;
import static cop5556sp17.Scanner.Kind.KW_MOVE;
import static cop5556sp17.Scanner.Kind.KW_SHOW;
import static cop5556sp17.Scanner.Kind.KW_XLOC;
import static cop5556sp17.Scanner.Kind.KW_YLOC;
import static cop5556sp17.Scanner.Kind.OP_BLUR;
import static cop5556sp17.Scanner.Kind.OP_CONVOLVE;
import static cop5556sp17.Scanner.Kind.OP_GRAY;
import static cop5556sp17.Scanner.Kind.OP_HEIGHT;
import static cop5556sp17.Scanner.Kind.OP_WIDTH;
import static cop5556sp17.Scanner.Kind.*;
import cop5556sp17.AST.*;
import cop5556sp17.AST.Type.TypeName;
import cop5556sp17.Scanner.Token;
import static cop5556sp17.AST.Type.TypeName.*;
import static cop5556sp17.Scanner.Kind.*;

public class TypeCheckVisitor implements ASTVisitor {

	@SuppressWarnings("serial")
	public static class TypeCheckException extends Exception {
		TypeCheckException(String message) {
			super(message);
		}
	}

	SymbolTable symtab = new SymbolTable();

	@Override
	public Object visitBinaryChain(BinaryChain binaryChain, Object arg) throws Exception {
		// TODO Auto-generated method stub
		Chain chain = binaryChain.getE0();
		chain.visit(this, arg);
		Token t1 = binaryChain.getFirstToken();
		ChainElem chainElem = binaryChain.getE1();
		chainElem.visit(this, arg);
		Token t = binaryChain.getArrow();
		if(t.kind.equals(ARROW))
		{
			if (chain.getType().equals(URL) && chainElem.getType().equals(IMAGE)){
				binaryChain.setType(IMAGE);
			}else if (chain.getType().equals(FILE)  && chainElem.getType().equals(IMAGE)){
				binaryChain.setType(IMAGE);
			}else if(chain.getType().equals(FRAME)  && (chainElem.getFirstToken().isKind(KW_XLOC)|| chainElem.getFirstToken().isKind(KW_YLOC)) ){
				binaryChain.setType(INTEGER);
			}else if(chain.getType().equals(FRAME)  && (chainElem.getFirstToken().isKind(KW_SHOW)|| chainElem.getFirstToken().isKind(KW_HIDE)|| chainElem.getFirstToken().isKind(KW_MOVE))){
				binaryChain.setType(FRAME);
			}else if(chain.getType().equals(IMAGE)  && (chainElem.getFirstToken().isKind(OP_WIDTH)|| chainElem.getFirstToken().isKind(OP_HEIGHT)) ){
				binaryChain.setType(INTEGER);
			}else if(chain.getType().equals(IMAGE)  && chainElem.getType().equals(FRAME)){
				binaryChain.setType(FRAME);
			}else if(chain.getType().equals(IMAGE)  && chainElem.getType().equals(FILE)){
				binaryChain.setType(NONE);
			}else if(chain.getType().equals(IMAGE)  && chainElem.getFirstToken().isKind(OP_GRAY)|| chainElem.getFirstToken().isKind(OP_BLUR)|| chainElem.getFirstToken().isKind(OP_CONVOLVE) ){
				binaryChain.setType(IMAGE);
			}else if(chain.getType().equals(IMAGE)  && chainElem.getFirstToken().isKind(KW_SCALE)){
				binaryChain.setType(IMAGE);
			}else if(chain.getType().equals(IMAGE)  && chainElem.getFirstToken().isKind(IDENT)){
				binaryChain.setType(IMAGE);
			}else
				throw new TypeCheckException("Type Error!");
		 }
		else if(t.kind.equals(BARARROW))
		{
			if (chainElem.getType().equals(TypeName.IMAGE) && (chainElem.getFirstToken().isKind(OP_GRAY)||chainElem.getFirstToken().isKind(OP_BLUR)||chainElem.getFirstToken().isKind( OP_CONVOLVE))){
				binaryChain.setType(IMAGE);
			}else{
				throw new TypeCheckException("Type Error");
			}
		}
		else{
			throw new TypeCheckException("Type Error");
		}

		return null;
	}

		

	@Override
	public Object visitBinaryExpression(BinaryExpression binaryExpression, Object arg) throws Exception {
		// TODO Auto-generated method stub
		Expression e0,e1;
		e0= binaryExpression.getE0();
	    e1= binaryExpression.getE1();
		e0.visit(this, arg);
		e1.visit(this, arg);
		Token t = binaryExpression.getOp();
		if(t.kind.equals(PLUS) ||t.kind.equals(MINUS)){
			 	if (e0.getType().equals(INTEGER) && e1.getType().equals(INTEGER)){
						binaryExpression.setType(INTEGER);
			 	 }else if(e0.getType().equals(IMAGE) && e1.getType().equals(IMAGE)){
					   	binaryExpression.setType(IMAGE);
			 	 }else {
						throw new TypeCheckException("Type Error");
				 }
		}else if(t.kind.equals(TIMES)){
				if (e0.getType().equals(INTEGER) && e1.getType().equals(INTEGER)){
					 binaryExpression.setType(INTEGER);
			     }else if(e0.getType().equals(INTEGER) && e1.getType().equals(IMAGE)){
			    	 binaryExpression.setType(IMAGE);
			     }else if(e0.getType().equals(IMAGE) && e1.getType().equals(INTEGER)){
			    	 binaryExpression.setType(IMAGE);
			     }else{
			 		throw new TypeCheckException("Type Error");
			 	 }
	    }else if(t.kind.equals(DIV)){
	    	    if (e0.getType().equals(INTEGER) && e1.getType().equals(INTEGER)){
	    	    	binaryExpression.setType(INTEGER);
				}else {
					throw new TypeCheckException("Type Error");
				}
	    }else if(t.kind.equals(LT) || t.kind.equals(GT) || t.kind.equals(LE) || t.kind.equals(GE) ){
	    		if (e0.getType().equals(INTEGER) && e1.getType().equals(INTEGER)){
	    			binaryExpression.setType(BOOLEAN);
	    		}else if(e0.getType().equals(BOOLEAN) && e1.getType().equals(BOOLEAN)){
	    			binaryExpression.setType(BOOLEAN);
	    		}else {
					throw new TypeCheckException("Type Error");
				}
	    }else if(t.kind.equals(EQUAL) || t.kind.equals(NOTEQUAL)){
	    	    if(e0.getType().equals(e1.getType())){
	    			binaryExpression.setType(BOOLEAN);
				}else{
				throw new TypeCheckException("Type Error");
				}
	    }else{
	    	throw new TypeCheckException("Type Error");
	    }
		
		return null;
	}

	@Override
	public Object visitBlock(Block block, Object arg) throws Exception {
		// TODO Auto-generated method stub
		symtab.enterScope();
		int i,j;
		ArrayList<Dec> dec = block.getDecs();
		ArrayList<Statement>stat = block.getStatements();
		for ( i=0, j=0; i<dec.size() && j<stat.size();){
			 if (dec.get(i).firstToken.pos > stat.get(j).firstToken.pos){
				 	stat.get(j).visit(this, arg);
				 	j++;
			 }else{
				 	dec.get(i).visit(this, arg);
				 	i++;
			}
		}
		for (; i<dec.size(); i++){
			dec.get(i).visit(this, arg);
		}
		for (; j<stat.size(); j++){
			stat.get(j).visit(this, arg);
		}
		symtab.leaveScope();
		return null;
	}

	@Override
	public Object visitBooleanLitExpression(BooleanLitExpression booleanLitExpression, Object arg) throws Exception {
		// TODO Auto-generated method stub
		booleanLitExpression.setType(BOOLEAN);
		return null;
	}

	@Override
	public Object visitFilterOpChain(FilterOpChain filterOpChain, Object arg) throws Exception {
		// TODO Auto-generated method stub
		if (filterOpChain.getArg().getExprList().size() != 0)
			throw new TypeCheckException("Type Error");
		filterOpChain.setType(TypeName.IMAGE);
		return null;
	}

	@Override
	public Object visitFrameOpChain(FrameOpChain frameOpChain, Object arg) throws Exception {
		// TODO Auto-generated method stub
		Token t = frameOpChain.firstToken;
		Tuple tuple = frameOpChain.getArg();
		if (t.isKind(KW_SHOW)||t.isKind(KW_HIDE)){
				if (tuple.getExprList().size() != 0)
				throw new TypeCheckException("Type Error");
				frameOpChain.setType(NONE);
		}else if(t.isKind(KW_XLOC)||t.isKind(KW_YLOC)){
				if (tuple.getExprList().size()!= 0)
				throw new TypeCheckException("Type Error");
				frameOpChain.setType(TypeName.INTEGER);
		}else if(t.isKind(KW_MOVE)){
		    	if (tuple.getExprList().size()!= 2)
				throw new TypeCheckException("Type Error");
				frameOpChain.setType(NONE);
		}else{
			    throw new TypeCheckException("Type Error");
		}
		
		return null;
	}

	@Override
	public Object visitIdentChain(IdentChain identChain, Object arg) throws Exception {
		// TODO Auto-generated method stub
		Dec dec = symtab.lookup(identChain.getFirstToken().getText());
		if (dec == null)
		throw new TypeCheckException("Type Error");
		identChain.setType(dec.getType());
		return null;
	}

	@Override
	public Object visitIdentExpression(IdentExpression identExpression, Object arg) throws Exception {
		// TODO Auto-generated method stub
		Dec d = symtab.lookup(identExpression.getFirstToken().getText());
		if (d == null)
			throw new TypeCheckException("Type Error");
		identExpression.setType(d.getType() );
		identExpression.setDec(d);
	    return null;
	}

	@Override
	public Object visitIfStatement(IfStatement ifStatement, Object arg) throws Exception {
		// TODO Auto-generated method stub
		Expression exp = ifStatement.getE();
		exp.visit(this, arg);
		if (!exp.getType().equals(TypeName.BOOLEAN) )
			throw new TypeCheckException(" Type Error");
		Block block = ifStatement.getB();
		block.visit(this, arg);
		return null;
	}

	@Override
	public Object visitIntLitExpression(IntLitExpression intLitExpression, Object arg) throws Exception {
		// TODO Auto-generated method stub
		intLitExpression.setType(INTEGER);
		return null;
	}

	@Override
	public Object visitSleepStatement(SleepStatement sleepStatement, Object arg) throws Exception {
		// TODO Auto-generated method stub
		Expression exp = sleepStatement.getE();
		exp.visit(this, arg);
		if (!(exp.getType().equals(TypeName.INTEGER))){
			throw new TypeCheckException("Type Error");
		}	
		return null;
	}

	@Override
	public Object visitWhileStatement(WhileStatement whileStatement, Object arg) throws Exception {
		// TODO Auto-generated method stub
		Expression exp = whileStatement.getE();
		exp.visit(this, arg);
		if (!(exp.getType().equals(TypeName.BOOLEAN))){
			throw new TypeCheckException("Type Error");
		}	
		Block block = whileStatement.getB();
		block.visit(this, arg);
		return null;
	}
	
	

	@Override
	public Object visitDec(Dec declaration, Object arg) throws Exception {
		// TODO Auto-generated method stub
		symtab.insert(declaration.getIdent().getText(), declaration);
		return null;
	}

	@Override
	public Object visitProgram(Program program, Object arg) throws Exception {
		// TODO Auto-generated method stub
		ArrayList<ParamDec> p = program.getParams();
		for (int i=0; i<p.size(); i++)
		p.get(i).visit(this, arg);
		Block block = program.getB();
		block.visit(this, arg);
		return null;
	}

	@Override
	public Object visitAssignmentStatement(AssignmentStatement assignStatement, Object arg) throws Exception {
		// TODO Auto-generated method stub
		IdentLValue i= assignStatement.getVar();
		i.visit(this, arg);
		Expression exp = assignStatement.getE();
		exp.visit(this, arg);
		if ( !(exp.getType().equals( i.getType())) ){
			throw new TypeCheckException("Type Error");
		}
		return null;
	}

	@Override
	public Object visitIdentLValue(IdentLValue identX, Object arg) throws Exception {
		// TODO Auto-generated method stub
		Dec d = symtab.lookup(identX.getText());
		if (d == null){
			throw new TypeCheckException("Error");
		}else{
			identX.setDec(d);
		}	
		return null;
	}

	@Override
	public Object visitParamDec(ParamDec paramDec, Object arg) throws Exception {
		// TODO Auto-generated method stub
		
		if(symtab.insert(paramDec.getIdent().getText(), paramDec))
			return null;
		else throw new TypeCheckException("Type Error")	;			
		
	}

	@Override
	public Object visitConstantExpression(ConstantExpression constantExpression, Object arg) {
		// TODO Auto-generated method stub
		constantExpression.setType(INTEGER);
		return null;
	}

	@Override
	public Object visitImageOpChain(ImageOpChain imageOpChain, Object arg) throws Exception {
		// TODO Auto-generated method stub
    	Token t = imageOpChain.firstToken;
		Tuple tuple = imageOpChain.getArg();
//		imageOpChain.setType(Type.getTypeName(t));
		if (t.isKind(OP_WIDTH) ||t.isKind(OP_HEIGHT)){
			if (tuple.getExprList().size()!= 0)
			throw new TypeCheckException("Type Error");
			imageOpChain.setType(TypeName.INTEGER);
		}else if (t.isKind(KW_SCALE)){
			if (tuple.getExprList().size()!= 1)
			throw new TypeCheckException("Type Error");
			imageOpChain.setType(TypeName.IMAGE);
		}
		return null;
	}

	@Override
	public Object visitTuple(Tuple tuple, Object arg) throws Exception {
		// TODO Auto-generated method stub
		List<Expression> exp = tuple.getExprList();
		Expression e;
		for (int i=0;i<exp.size();i++){
			e=exp.get(i);
			e.visit(this, arg);
			if(!e.getType().equals(TypeName.INTEGER))
				throw new TypeCheckException("Type Error");
		}
		return null;
	}


}
