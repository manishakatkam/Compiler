package cop5556sp17;

import java.util.ArrayList;
import java.util.Collections;

import cop5556sp17.Scanner.Kind;

public class Scanner {
	/**
	 * Kind enum
	 */
	
	public static enum Kind {
		IDENT(""), INT_LIT(""), KW_INTEGER("integer"), KW_BOOLEAN("boolean"), 
		KW_IMAGE("image"), KW_URL("url"), KW_FILE("file"), KW_FRAME("frame"), 
		KW_WHILE("while"), KW_IF("if"), KW_TRUE("true"), KW_FALSE("false"), 
		SEMI(";"), COMMA(","), LPAREN("("), RPAREN(")"), LBRACE("{"), 
		RBRACE("}"), ARROW("->"), BARARROW("|->"), OR("|"), AND("&"), 
		EQUAL("=="), NOTEQUAL("!="), LT("<"), GT(">"), LE("<="), GE(">="), 
		PLUS("+"), MINUS("-"), TIMES("*"), DIV("/"), MOD("%"), NOT("!"), 
		ASSIGN("<-"), OP_BLUR("blur"), OP_GRAY("gray"), OP_CONVOLVE("convolve"), 
		KW_SCREENHEIGHT("screenheight"), KW_SCREENWIDTH("screenwidth"), 
		OP_WIDTH("width"), OP_HEIGHT("height"), KW_XLOC("xloc"), KW_YLOC("yloc"), 
		KW_HIDE("hide"), KW_SHOW("show"), KW_MOVE("move"), OP_SLEEP("sleep"), 
		KW_SCALE("scale"), EOF("eof");

		Kind(String text) {
			this.text = text;
		}

		final String text;

		String getText() {
			return text;
		}
	}
	
	
/**
 * Thrown by Scanner when an illegal character is encountered
 */
	@SuppressWarnings("serial")
	public static class IllegalCharException extends Exception {
		public IllegalCharException(String message) {
			super(message);
		}
	}
	
	/**
	 * Thrown by Scanner when an int literal is not a value that can be represented by an int.
	 */
	@SuppressWarnings("serial")
	public static class IllegalNumberException extends Exception {
	public IllegalNumberException(String message){
		super(message);
		}
	}
	

	/**
	 * Holds the line and position in the line of a token.
	 */
	static class LinePos {
		public final int line;
		public final int posInLine;
		
		public LinePos(int line, int posInLine) {
			super();
			this.line = line;
			this.posInLine = posInLine;
		}

		@Override
		public String toString() {
			return "LinePos [line=" + line + ", posInLine=" + posInLine + "]";
		}
	}
		

	

	public class Token {
		public final Kind kind;
		public final int pos;  //position in input array
		public final int length;  

		//returns the text of this Token
		public String getText() {
			//TODO IMPLEMENT THIS
			String s = new String(chars.substring(pos,pos+length));
			return s;
		}
		
		//returns a LinePos object representing the line and column of this Token
		LinePos getLinePos(){
			//TODO IMPLEMENT THIS
			int id;
			id = Collections.binarySearch(newLineStartPos, pos);
			if(id < 0){
					id=-1*(id+1)-1;
				       }
				
				return new LinePos(id, pos-newLineStartPos.get(id));
			}

			
		

		Token(Kind kind, int pos, int length) {
			this.kind = kind;
			this.pos = pos;
			this.length = length;
		}

		/** 
		 * Precondition:  kind = Kind.INT_LIT,  the text can be represented with a Java int.
		 * Note that the validity of the input should have been checked when the Token was created.
		 * So the exception should never be thrown.
		 * 
		 * @return  int value of this token, which should represent an INT_LIT
		 * @throws NumberFormatException
		 */
		public int intVal() throws NumberFormatException{
			//TODO IMPLEMENT THIS
			int v = Integer.parseInt(chars.substring(pos, pos+length));
			return v;
		}

		public boolean isKind(Kind k) {
			// TODO Auto-generated method stub
			boolean m=kind.equals(k);
			return m;
		}
		 @Override
		  public int hashCode() {
		   final int prime = 31;
		   int result = 1;
		   result = prime * result + getOuterType().hashCode();
		   result = prime * result + ((kind == null) ? 0 : kind.hashCode());
		   result = prime * result + length;
		   result = prime * result + pos;
		   return result;
		  }

		  @Override
		  public boolean equals(Object obj) {
		   if (this == obj) {
		    return true;
		   }
		   if (obj == null) {
		    return false;
		   }
		   if (!(obj instanceof Token)) {
		    return false;
		   }
		   Token other = (Token) obj;
		   if (!getOuterType().equals(other.getOuterType())) {
		    return false;
		   }
		   if (kind != other.kind) {
		    return false;
		   }
		   if (length != other.length) {
		    return false;
		   }
		   if (pos != other.pos) {
		    return false;
		   }
		   return true;
		  }

		 

		  private Scanner getOuterType() {
		   return Scanner.this;
		  }
		
	}

	 
	ArrayList<Integer> newLineStartPos;



	Scanner(String chars) {
		this.chars = chars;
		tokens = new ArrayList<Token>();
		newLineStartPos = new ArrayList<Integer>();
		
		


	}

	public static enum State{
		START,IN_DIGIT,IN_IDENT,AFTER_EQ;
		
	}

	
	/**
	 * Initializes Scanner object by traversing chars and adding tokens to tokens list.
	 * 
	 * @return this scanner
	 * @throws IllegalCharException
	 * @throws IllegalNumberException
	 */
	public Scanner scan() throws IllegalCharException, IllegalNumberException {
		
		int pos = 0; 
		int length=chars.length();
		State state= State.START;
		newLineStartPos.add(0);
		int startPos=0;
		int ch;
		while (pos < length) {
	        ch = chars.charAt(pos);
	        switch(state) {
	        case START: {
	        	pos = skipWhiteSpace(pos);
	        	if(pos >= length)
	        		break;
	            ch = chars.charAt(pos);
	            startPos=pos;
	            switch (ch) {

	            
	            case -1: {tokens.add(new Token(Kind.EOF, pos, 0));pos++;}   break;         
	            case '&': {tokens.add(new Token(Kind.AND, pos, 1));pos++;}   break;
	            case '+': {tokens.add(new Token(Kind.PLUS, pos, 1));pos++;}  break;
	            case '*': {tokens.add(new Token(Kind.TIMES, pos, 1));pos++;} break; 
	            case '%': {tokens.add(new Token(Kind.MOD, pos, 1));pos++;}   break;
	            case ';': {tokens.add(new Token(Kind.SEMI, pos, 1));pos++;}  break;
	            case ',': {tokens.add(new Token(Kind.COMMA, pos, 1));pos++;} break;
	            case '(': {tokens.add(new Token(Kind.LPAREN, pos, 1));pos++;}break;
	            case ')': {tokens.add(new Token(Kind.RPAREN, pos, 1));pos++;} break;
	            case '{': {tokens.add(new Token(Kind.LBRACE, pos, 1));pos++;} break;
	            case '}': {tokens.add(new Token(Kind.RBRACE, pos, 1));pos++;} break;
	            case '0': {tokens.add(new Token(Kind.INT_LIT,pos, 1));pos++;} break;
	            case '=': {state=State.AFTER_EQ;pos++;} break;
	            
	            case '\n': {pos++; newLineStartPos.add(pos);} break;
	            
	            
	            case '-': 
	            {

	            	if (pos < length-1 && chars.charAt(pos+1) =='>')
	            	{
	            		tokens.add(new Token(Kind.ARROW, startPos, 2));
	            		pos=pos+2;
	            		
	            		}
	            	
	            	else{ 
	            		tokens.add(new Token(Kind.MINUS, startPos, 1));
	            		pos++;
	            	    }
	            	
	            	state=State.START;
				
	            }   break;
	            
	            case '!': 
	            {
	            	
	            	if (pos < length-1 && chars.charAt(pos+1) == '='){
	            		tokens.add(new Token(Kind.NOTEQUAL, startPos, 2));
	            		pos=pos+2;
	            		
	            	}
	            	else{ 
	            		tokens.add(new Token(Kind.NOT, startPos, 1));
	            		pos++;
	            	}
	            	state=State.START;
	            } break;
	            
	            
	            
	            case '/': 
	            {
	            	
	            	if (pos < length-1 && chars.charAt(pos+1) == '*'){
	            		//tokens.add(new Token(Kind.ARROW, startPos, 2));
	            		//state=State.
	            		//pos=pos+2;
	            		while(pos<length-1 && (chars.charAt(pos)!='*' || chars.charAt(pos+1)!='/'))
	            		{
	            			pos++;
	            			if(chars.charAt(pos)=='\n')
	            				newLineStartPos.add(pos+1);
	            		}
	            		
	            		
	            		if (pos == length-1)
	            			pos++;
	            		else
	            			pos=pos+2;
	            		state=State.START;	
	            		
	            	}
	            	else{ 
	            		tokens.add(new Token(Kind.DIV, startPos, 1));
	            		pos++;
	            	}
	            	state=State.START;
	            } break;
	            
	            
	            case '<': 
	            {
	            	
	            	if (pos<length-1 && chars.charAt(pos+1) =='-'){
	            		tokens.add(new Token(Kind.ASSIGN, startPos, 2));
	            		pos=pos+2;
	            	}
	            	else if(pos < length-1 && chars.charAt(pos+1) == '='){ 
	            		tokens.add(new Token(Kind.LE, startPos, 2));
	            		pos=pos+2;
	            	}
	            	else{
	            		tokens.add(new Token(Kind.LT, startPos, 1));
	            		pos++;
	            	}
	            	state=State.START;
	            } 
	            break;
	            
	            case '>': 
	            {
	            	//pos++;
	            	if (pos < length-1 && chars.charAt(pos+1) == '='){
	            		tokens.add(new Token(Kind.GE, startPos, 2));
	            		pos=pos+2;
	            	}
	            	
	            	else{
	            		tokens.add(new Token(Kind.GT, startPos, 1));
	            		pos++;
	            	}
	            	state=State.START;
	            } 
	            break;
	            
	            case '|': 
	            {
	            	
	            	if (pos <length-2 && chars.charAt(pos+1) =='-' && chars.charAt(pos+2)=='>'){
	            		tokens.add(new Token(Kind.BARARROW, startPos, 3));
	            		pos=pos+3;
	            	}
	            	
	            	else{
	            		tokens.add(new Token(Kind.OR, startPos, 1));
	            		pos++;
	            	}
	            	state=State.START;
	            } 
	            break;
	            
	            
	            default: { 
	            	if (Character.isJavaIdentifierStart(ch)) 
					     {
							state = State.IN_IDENT;
							pos++;
					     }
							
	            	else if (Character.isDigit(ch))
							{
								state = State.IN_DIGIT;
								pos++;
							}
							
							else 
							{
								throw new IllegalCharException( "illegal char " +ch+" at pos "+pos);
							}
					   }     
	            
	            
	            
	            
	            
	            
	            
	            
	            
	            }//start state switch ending brace    
	            
	       }//  start state case  ending brace 
	        break;
	       
	        case IN_IDENT: {
	                        if (Character.isJavaIdentifierPart(ch)) 
	                            {
	                        	pos++;
	                        	} 
	                        else {
								 	int flag = 0;
								 	String str = chars.substring(startPos, pos);
								 	if(!str.equals("eof")){
								 	for (Kind k : Kind.values()){
								 			if (k.getText().equals(str)){
								 				tokens.add(new Token(k, startPos, pos - startPos));
								 				flag = 1;
								 				break;
								 			    }
								          }		
								 	}
								
								if(flag== 0)
									tokens.add(new Token(Kind.IDENT, startPos, pos - startPos));
								state = State.START;
	                            
	                             } 
	        }
	        break;  
	                       
	        case IN_DIGIT: {
                				if (Character.isDigit(ch)) 
                				{
                					pos++;
                				} 
                				else
                			{
                				try{
                					Integer.parseInt(chars.substring( startPos,pos));
                				   }
                				catch(Exception e){
                						throw new IllegalNumberException(" out of range "+chars.substring(startPos, pos));
                				   				  }
                                
                					tokens.add(new Token(Kind.INT_LIT, startPos, pos - startPos));
                					state = State.START;
                		   }
	                     }
                				
                           
	                       break; 
	                       
	        case AFTER_EQ: {
	        	             	if(chars.charAt(pos)=='=')
	        	             		{
	        	             			pos++;
	        	             			tokens.add(new Token(Kind.EQUAL, startPos, pos - startPos));
	        	             			state=State.START;
	        	            	 
	        	             		}
	        	                else
	        	             		{
	        	            	 		throw new IllegalCharException( "illegal char " +ch+" at pos "+pos);
	        	             		}
	                       }break;
	        	            	 

								
               
	                       
	                       
	        } // switch ending brace
		
	    }   // while loop ending brace
		//TODO IMPLEMENT THIS!!!!
		
		switch(state){    //break cases handle switch case
		case IN_IDENT: 
		    	{   
		    		int flag = 0;
				 	String str = chars.substring(startPos, pos);
				 	for (Kind tk : Kind.values()){
				 			if (tk.getText().equals(str)){
				 				tokens.add(new Token(tk, startPos, pos - startPos));
				 				flag = 1;
				 				break;
				 			    }
				          }		
            
				
				if(flag== 0)
					tokens.add(new Token(Kind.IDENT, startPos, pos - startPos));
				state = State.START;
		    		
		    		
		    		
		    		
                } 
                    break;
		case IN_DIGIT: 
		{
			
		
			try{
				Integer.parseInt(chars.substring(startPos,pos));
			   }
			catch(Exception e){
					throw new IllegalNumberException(" out of range "+chars.substring(startPos, pos));
			   				  }
            
				tokens.add(new Token(Kind.INT_LIT, startPos, pos - startPos));
				state = State.START;
	   
		}  break;
		
		case AFTER_EQ: {
         	
                       throw new IllegalCharException( "illegal char "+chars.charAt(pos-1)+" at pos " +pos);
                       
         		
					    }
		
		default :
			break;
		}
		tokens.add(new Token(Kind.EOF,pos,0));
		return this;  
	}



	public int skipWhiteSpace(int pos) {
		// TODO Auto-generated method stub
		
		while(pos<chars.length() && Character.isWhitespace(chars.charAt(pos)) && chars.charAt(pos) != '\n'){
			pos++;
		}
		
		return pos;
	}



	final ArrayList<Token> tokens;
	final String chars;
	int tokenNum;

	/*
	 * Return the next token in the token list and update the state so that
	 * the next call will return the Token..  
	 */
	public Token nextToken() {
		if (tokenNum >= tokens.size())
			return null;
		return tokens.get(tokenNum++);
	}
	
	/*
	 * Return the next token in the token list without updating the state.
	 * (So the following call to next will return the same token.)
	 */
	public Token peek(){
		if (tokenNum >= tokens.size())
			return null;
		return tokens.get(tokenNum);		
	}
    
	
	
	

	/**
	 * Returns a LinePos object containing the line and position in line of the 
	 * given token.  
	 * 
	 * Line numbers start counting at 0
	 * 
	 * @param t
	 * @return
	 */
	
	public LinePos getLinePos(Token t) {  
		
		//TODO IMPLEMENT THIS
		return t.getLinePos();
	}


}
