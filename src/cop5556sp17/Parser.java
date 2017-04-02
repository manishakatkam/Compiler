package cop5556sp17;

import cop5556sp17.Scanner.Kind;
import cop5556sp17.AST.*;
import static cop5556sp17.Scanner.Kind.*;

import java.util.ArrayList;
import java.util.List;

import cop5556sp17.Scanner.Token;

public class Parser {

	/**
	 * Exception to be thrown if a syntax error is detected in the input.
	 * You will want to provide a useful error message.
	 *
	 */
	@SuppressWarnings("serial")
	public static class SyntaxException extends Exception {
		public SyntaxException(String message) {
			super(message);
		}
	}
	
	/**
	 * Useful during development to ensure unimplemented routines are
	 * not accidentally called during development.  Delete it when 
	 * the Parser is finished.
	 *
	 */
	@SuppressWarnings("serial")	
	public static class UnimplementedFeatureException extends RuntimeException {
		public UnimplementedFeatureException() {
			super();
		}
	}

	Scanner scanner;
	Token t;

	Parser(Scanner scanner) {
		this.scanner = scanner;
		t = scanner.nextToken();
	}

	/**
	 * parse the input using tokens from the scanner.
	 * Check for EOF (i.e. no trailing junk) when finished
	 * @return 
	 * 
	 * @throws SyntaxException
	 */
	ASTNode parse() throws SyntaxException {
		System.out.println("parse");
		ASTNode nodes = program();
		matchEOF();
		return nodes ;
	}
	

	Expression expression() throws SyntaxException {
		System.out.println("expression");
		Token first=t;
		Expression e1,e2;
		e1=term();
		Token op=t;
		while( t.isKind(LT) || t.isKind(LE) || t.isKind(GT) || t.isKind(GE) || t.isKind(EQUAL) || t.isKind(NOTEQUAL)){
			op=t;
			consume();
        	e2=term();
        	e1=new BinaryExpression(first, e1, op, e2);
		}
		return e1;
	}
	

	
	 Expression term() throws SyntaxException {
		System.out.println("term");
		Token first=t;
		Expression e1,e2;
		e1=elem();
		while( t.isKind(PLUS) || t.isKind(MINUS) || t.isKind(OR)  ){
			Token op=t;
			consume();
			e2=elem();
			e1=new BinaryExpression(first, e1, op, e2);
		}
	   return e1;	
	}
	 

	Expression elem() throws SyntaxException {
		System.out.println("elem");
		Token first=t;
		Expression e1,e2;
		e1=factor();
		while(t.isKind(TIMES) | t.isKind(DIV) | t.isKind(AND) | t.isKind(MOD)){
			Token op=t;
			consume();
			e2=factor();
			e1=new BinaryExpression(first, e1, op, e2);
		}
		return e1;
	}
	

	Expression factor() throws SyntaxException {
		System.out.println("factor");
		Token first=t;
		Expression e1;
		Kind kind = t.kind;
		switch (kind) {
		case IDENT: {
			first=t;
			e1=new IdentExpression(first);
			consume();
		}
			break;
		case INT_LIT: {
			first=t;
			e1=new IntLitExpression(first);
			consume();
		}
			break;
		case KW_TRUE:
		case KW_FALSE: {
			first=t;
			e1=new BooleanLitExpression(first);
			consume();
		}
			break;
		case KW_SCREENWIDTH:
		case KW_SCREENHEIGHT: {
			first=t;
			e1=new ConstantExpression(first);
			consume();
		}
			break;
		case LPAREN: {
			consume();
			e1=expression();
			match(RPAREN);
		}
			break;
		default:
			throw new SyntaxException("illegal factor");
		}
		return e1;
	}
	
	

	Block block() throws SyntaxException {   
		System.out.println("block");
		Token first=t;
		ArrayList<Dec> decs=new ArrayList<>();
		ArrayList<Statement> statements=new ArrayList<>();
		if(t.isKind(LBRACE)){
			consume();
			while(!t.isKind(RBRACE)){	
				if(t.isKind(KW_INTEGER) || t.isKind(KW_BOOLEAN) || t.isKind(KW_IMAGE) || t.isKind(KW_FRAME)){
					decs.add(dec());
				}
				else{
					statements.add(statement());
				}		
		     }
		    match(RBRACE);	
		} else {
			throw new SyntaxException("Illegal block");
		}
		return new Block(first,decs,statements);
	}
	
	

	Program program() throws SyntaxException {
		System.out.println("program");
		Token first=t;
		ArrayList<ParamDec> paramList=new ArrayList<>();
		Block block;
		match(IDENT);
		 		if(t.isKind(LBRACE)){
		 				//consume();
		 				block=block();
			       }
		 		else {
		 				paramList.add(paramDec());
		 					while(t.isKind(COMMA)){
		 						 consume();
		 						paramList.add(paramDec());			 
		 					}
		 				block=block();
		 			}
		 return new Program(first, paramList,block);
	}
	

	ParamDec paramDec() throws SyntaxException {
		System.out.println("paramDec");
		Token first=t;
		Token Ident;
		if( t.isKind(KW_URL) || t.isKind(KW_FILE) || t.isKind(KW_INTEGER) || t.isKind(KW_BOOLEAN)){
			consume();
			}
		else{
			throw new SyntaxException("Illegal paramdec");
		}
		Ident=t;
		match(IDENT);                       		
		return new ParamDec(first,Ident); 
	}
	
	

	Dec dec() throws SyntaxException {
		System.out.println("dec");
		Token first=t;
		Token Ident;
		if(t.isKind(KW_INTEGER) || t.isKind(KW_BOOLEAN) || t.isKind(KW_IMAGE) || t.isKind(KW_FRAME)){
			consume();
		}
		Ident=t;
	    match(IDENT);
	    return new Dec(first,Ident);
	}
	
	

	Statement statement() throws SyntaxException {
		System.out.println("statement");
		Statement stmt;
		Token first=t;
		//statement ::=   OP_SLEEP expression ; | whileStatement | ifStatement | chain ; | assign ;
        if(t.isKind(OP_SLEEP)){
        	consume();
        	stmt=new SleepStatement(first,expression());
        	match(SEMI);
        } 
        else if(t.isKind(KW_WHILE)){                         
           stmt=whileStatement(); 
        } 
        else if(t.isKind(KW_IF)){
        	stmt=ifStatement();
        } 
        else if(t.isKind(IDENT)){
        	if(scanner.peek().isKind(ASSIGN)){
        		stmt=assign();
                }
            else{
        		stmt=chain();
                }
        match(SEMI);
        }
        else {
        	if (t.isKind(OP_BLUR) || t.isKind(OP_GRAY) || t.isKind(OP_CONVOLVE) || t.isKind(KW_SHOW) || t.isKind(KW_HIDE) || t.isKind(KW_MOVE) ||t.isKind(KW_XLOC) || t.isKind(KW_YLOC) || t.isKind(OP_WIDTH) || t.isKind(OP_HEIGHT) || t.isKind(KW_SCALE)){
	        	stmt=chain();
	        	match(SEMI);
        	}
        	else{
        		throw new SyntaxException("illegal token"+t.kind+"expected: " +" a statements");
        	}
        }
        return stmt;
       }
	
	

	Chain chain() throws SyntaxException {
		System.out.println("chain");
		Token first=t;
		Chain c=chainElem();
		Token op=t;
		ChainElem ce;
				if(t.isKind(ARROW) || t.isKind(BARARROW)){
					op=t;
                    consume();
				}
					ce=chainElem();
					c= new BinaryChain(first, c, op, ce);
				while(t.isKind(ARROW) || t.isKind(BARARROW)){
					op=t;
					consume();
					ce=chainElem();
					c= new BinaryChain(first, c, op, ce);
				}
		return c;
		}

	
	ChainElem chainElem() throws SyntaxException {
		System.out.println("chainelem");
		Token first=t;
		ChainElem ce;
		if(t.isKind(IDENT)){
			first=t;
			consume();
			ce=new IdentChain(first);
		}
		else if(t.isKind(OP_BLUR) ||t.isKind(OP_GRAY) || t.isKind(OP_CONVOLVE)){
			first=t;
			consume();
			ce=new FilterOpChain(first,arg());
		}
		else if(t.isKind(KW_SHOW) || t.isKind(KW_HIDE) || t.isKind(KW_MOVE) || t.isKind(KW_XLOC) ||t.isKind(KW_YLOC)){
	    first=t;
	    consume();
	    ce=new FrameOpChain(first, arg());
		}
		else if(t.isKind(OP_WIDTH) ||t.isKind(OP_HEIGHT) || t.isKind(KW_SCALE)){
			first=t;
			consume();
			ce=new ImageOpChain(first, arg());
		}
		else{
			throw new SyntaxException("Illegal chain element");
		}
		return ce;
	}
	

	
	Tuple arg() throws SyntaxException {
		System.out.println("arg");
		Token first=t;		
		List<Expression> argList=new ArrayList<>();
		if(t.isKind(LPAREN)){
			first=t;
			consume();
			argList.add(expression());
				while(t.isKind(COMMA)){
						match(COMMA);
						argList.add(expression());
				}
			match(RPAREN);
			return new Tuple(first, argList);
		}
		else 
		    return new Tuple(first, argList);
	}
	
	
	AssignmentStatement assign() throws SyntaxException {
		System.out.println("assign");
		Token first=t;
		IdentLValue ident;
		Expression e;
            if(t.isKind(IDENT)){
            	ident=new IdentLValue(first);
            	consume();
            	}
            	match(ASSIGN);
            	ident=new IdentLValue(first);
            	e=expression();
            
            return new AssignmentStatement(first, ident, e);
    } 
	
	
	WhileStatement whileStatement() throws SyntaxException {
		System.out.println("whilestatement");
		Token first=t;
		Expression exp;
		Block block;
		if (t.isKind(KW_WHILE)) {
			consume();
		}
			match(LPAREN);
			exp=expression();
			match(RPAREN);
			block=block();
		return new WhileStatement(first,exp,block);
	} 
	
	
	IfStatement ifStatement() throws SyntaxException {
    	System.out.println("ifstatement");
    	Token first=t;
		Expression exp;
		Block block;
		if (t.isKind(KW_IF)) {
			consume();
		}
			match(LPAREN);
			exp=expression();
			match(RPAREN);
			block=block();
			return new IfStatement(first,exp,block);
		}

	/**
	 * Checks whether the current token is the EOF token. If not, a
	 * SyntaxException is thrown.
	 * 
	 * @return
	 * @throws SyntaxException
	 */
	private Token matchEOF() throws SyntaxException {
		System.out.println("matcheof");
		if (t.isKind(EOF)) {
			return t;
		}
		throw new SyntaxException("expected EOF");
	}

	/**
	 * Checks if the current token has the given kind. If so, the current token
	 * is consumed and returned. If not, a SyntaxException is thrown.
	 * 
	 * Precondition: kind != EOF
	 * 
	 * @param kind=
	 * @return
	 * @throws SyntaxException
	 */
	private Token match(Kind kind) throws SyntaxException {
	
		if (t.isKind(kind)) {
			return consume();
		}
		
		throw new SyntaxException("saw " + t.kind + "expected " + kind);
	}

	/**
	 * Checks if the current token has one of the given kinds. If so, the
	 * current token is consumed and returned. If not, a SyntaxException is
	 * thrown.
	 * 
	 * * Precondition: for all given kinds, kind != EOF
	 * 
	 * @param kinds
	 *            list of kinds, matches any one
	 * @return
	 * @throws SyntaxException
	 */
	private Token match(Kind... kinds) throws SyntaxException {
		// TODO. Optional but handy
		return null; //replace this statement
	}

	/**
	 * Gets the next token and returns the consumed token.
	 * 
	 * Precondition: t.kind != EOF
	 * 
	 * @return
	 * 
	 */
	private Token consume() throws SyntaxException {
		Token tmp = t;
		t = scanner.nextToken();
		return tmp;
	}

}
