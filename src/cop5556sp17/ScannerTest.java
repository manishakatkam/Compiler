package cop5556sp17;

import static cop5556sp17.Scanner.Kind.SEMI;

import static cop5556sp17.Scanner.Kind;
import static org.junit.Assert.assertEquals;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import cop5556sp17.Scanner.IllegalCharException;
import cop5556sp17.Scanner.IllegalNumberException;


public class ScannerTest {

	@Rule
    public ExpectedException thrown = ExpectedException.none();


	
	@Test
	public void testEmpty() throws IllegalCharException, IllegalNumberException {
		String input = "";
		Scanner scanner = new Scanner(input);
		scanner.scan();
	}

	@Test
	public void testSemiConcat() throws IllegalCharException, IllegalNumberException {
		//input string
		String input = ";;;";
		//create and initialize the scanner
		Scanner scanner = new Scanner(input);
		scanner.scan();
		//get the first token and check its kind, position, and contents
		Scanner.Token token = scanner.nextToken();
		assertEquals(SEMI, token.kind);
		assertEquals(0, token.pos);
		String text = SEMI.getText();
		assertEquals(text.length(), token.length);
		assertEquals(text, token.getText());
		//get the next token and check its kind, position, and contents
		Scanner.Token token1 = scanner.nextToken();
		assertEquals(SEMI, token1.kind);
		assertEquals(1, token1.pos);
		assertEquals(text.length(), token1.length);
		assertEquals(text, token1.getText());
		Scanner.Token token2 = scanner.nextToken();
		assertEquals(SEMI, token2.kind);
		assertEquals(2, token2.pos);
		assertEquals(text.length(), token2.length);
		assertEquals(text, token2.getText());
		//check that the scanner has inserted an EOF token at the end
		Scanner.Token token3 = scanner.nextToken();
		assertEquals(Scanner.Kind.EOF,token3.kind);
	}
	
	
	/**
	 * This test illustrates how to check that the Scanner detects errors properly. 
	 * In this test, the input contains an int literal with a value that exceeds the range of an int.
	 * The scanner should detect this and throw and IllegalNumberException.
	 * 
	 * @throws IllegalCharException
	 * @throws IllegalNumberException
	 */
	@Test
	public void testIntOverflowError() throws IllegalCharException, IllegalNumberException{
		String input = "99999999999999999";
		Scanner scanner = new Scanner(input);
		thrown.expect(IllegalNumberException.class);
		scanner.scan();		
	}

//TODO  more tests
	@Test
	public void testOtherOpsBreakCase() throws IllegalCharException, IllegalNumberException{
		
		//testing minus and arrow
		String input = "-";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		
		Scanner.Token token = scanner.nextToken();		
		
		//System.out.println(token.kind);
		//System.out.println(token.pos);
		//System.out.println(token.length);
		//System.out.println(token.getText());
		
		assertEquals(Kind.MINUS, token.kind);
		assertEquals(0, token.pos);
		
		input = "-> -";
		scanner = new Scanner(input);
		scanner.scan();
		
		Scanner.Token token2 = scanner.nextToken();
		assertEquals(Kind.ARROW,token2.kind);
		assertEquals(0,token2.pos);
		
		Scanner.Token token3 = scanner.nextToken();
		assertEquals(Kind.MINUS,token3.kind);
		assertEquals(3,token3.pos);
		
		//testing not and not equal
		input = "! != !";
		scanner = new Scanner(input);
		scanner.scan();
		
		Scanner.Token token4 = scanner.nextToken();		
		assertEquals(Kind.NOT, token4.kind);
		assertEquals(0, token4.pos);
		
		Scanner.Token token5 = scanner.nextToken();
		assertEquals(Kind.NOTEQUAL, token5.kind);
		assertEquals(2, token5.pos);
		
		Scanner.Token token6 = scanner.nextToken();
		assertEquals(Kind.NOT, token6.kind);
		assertEquals(5, token6.pos);
		
		//testing div and comment
		input = "/ /* this is comment */ /*dfdfsfd";
		scanner = new Scanner(input);
		scanner.scan();
		System.out.println(input.length());
		Scanner.Token token7 = scanner.nextToken();		
		assertEquals(Kind.DIV, token7.kind);
		assertEquals(0, token7.pos);
		
		Scanner.Token token8 = scanner.nextToken();
		System.out.println(token8.kind);
		System.out.print(token8.kind);
		System.out.println(token8.getText());
		assertEquals(Kind.EOF, token8.kind);
		assertEquals(33, token8.pos);
		
		//testing BARARROW
		input = "|-> |-";
		scanner = new Scanner(input);
		scanner.scan();
		
		Scanner.Token token9 = scanner.nextToken();		
		assertEquals(Kind.BARARROW, token9.kind);
		assertEquals(0, token9.pos);
		
		Scanner.Token token10 = scanner.nextToken();
		assertEquals(Kind.OR, token10.kind);
		assertEquals(4, token10.pos);
		
		Scanner.Token token11 = scanner.nextToken();
		assertEquals(Kind.MINUS, token11.kind);
		assertEquals(5, token11.pos);
		
		//testing LT,LE,ASSIGN
		input = "<- < <= <";
		scanner = new Scanner(input);
		scanner.scan();
		
		Scanner.Token token15 = scanner.nextToken();		
		assertEquals(Kind.ASSIGN, token15.kind);
		assertEquals(0, token15.pos);
		
		Scanner.Token token16 = scanner.nextToken();
		assertEquals(Kind.LT, token16.kind);
		assertEquals(3, token16.pos);
		
		Scanner.Token token12 = scanner.nextToken();
		assertEquals(Kind.LE, token12.kind);
		assertEquals(5, token12.pos);
		
		Scanner.Token token13 = scanner.nextToken();
		assertEquals(Kind.LT, token13.kind);
		assertEquals(8, token13.pos);
		
		Scanner.Token token14 = scanner.nextToken();
		assertEquals(Kind.EOF, token14.kind);
		assertEquals(9, token14.pos);
		
		//testing GT,GW
		input = "> >= >";
		scanner = new Scanner(input);
		scanner.scan();
		
		Scanner.Token token17 = scanner.nextToken();		
		assertEquals(Kind.GT, token17.kind);
		assertEquals(0, token17.pos);
		
		Scanner.Token token18 = scanner.nextToken();
		assertEquals(Kind.GE, token18.kind);
		assertEquals(2, token18.pos);
		
		Scanner.Token token19 = scanner.nextToken();
		assertEquals(Kind.GT, token19.kind);
		assertEquals(5, token19.pos);
	}
		//TODO  more tests
		@Test
		public void myTestCase1() throws IllegalCharException, IllegalNumberException {
			String input = "manisha><1992'#";
			Scanner scanner = new Scanner(input);
			thrown.expect(IllegalCharException.class);
			scanner.scan();
		}
		 
		@Test
		public void myTestCase2() throws IllegalCharException, IllegalNumberException {
			String input = "buhaha nene egire robo doremon < \n i am done with my plp scanner \n yey yey yey ";
			Scanner scanner = new Scanner(input);
			//thrown.expect(IllegalCharException.class);
			scanner.scan();
		}  
		
		@Test
		public void myTestCase3() throws IllegalCharException, IllegalNumberException {
			String input = "9999 \n >= manisha <- plp scanner |-> haha \n";
			Scanner scanner = new Scanner(input);
			//thrown.expect(IllegalCharException.class);
			scanner.scan();
		}
		
		@Test
		public void myTestCase4() throws IllegalCharException, IllegalNumberException {
			String input = "9999 -> \n <= >= == \n doremon 1234 integer";
			Scanner scanner = new Scanner(input);
			//thrown.expect(IllegalCharException.class);
			scanner.scan();
		} 
		
		@Test
		public void myTestCase5() throws IllegalCharException, IllegalNumberException {
			String input = "9999 != if /*while \n doremon*/ 1234 integer grey ";
			Scanner scanner = new Scanner(input);
			//thrown.expect(IllegalCharException.class);
			scanner.scan();
		} 
		
		@Test
		public void myTestCase6() throws IllegalCharException, IllegalNumberException {
			String input = " <= >= != = \n /* hahaha != /n */";
			Scanner scanner = new Scanner(input);
		    thrown.expect(IllegalCharException.class);
			scanner.scan();
		} 
		
		@Test
		public void myTestCase7() throws IllegalCharException, IllegalNumberException {
			String input = " frame\n, while,\n if  ";
			Scanner scanner = new Scanner(input);
		    //thrown.expect(IllegalCharException.class);
			scanner.scan();
		}
		
		
		
		
	
	
	
	}
	
	
	
  
