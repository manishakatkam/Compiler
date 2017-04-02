package cop5556sp17;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import cop5556sp17.Parser.SyntaxException;
import cop5556sp17.Scanner.IllegalCharException;
import cop5556sp17.Scanner.IllegalNumberException;


public class ParserTest {

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	@Test
	public void testFactor0() throws IllegalCharException, IllegalNumberException, SyntaxException {
		String input = "abc";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		Parser parser = new Parser(scanner);
		parser.factor();
	}

	@Test
	public void testArg() throws IllegalCharException, IllegalNumberException, SyntaxException {
		String input = "  (3,5) ";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		System.out.println(scanner);
		Parser parser = new Parser(scanner);
        parser.arg();
	}

	@Test
	public void testArgerror() throws IllegalCharException, IllegalNumberException, SyntaxException {
		String input = "  (3,) ";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		Parser parser = new Parser(scanner);
		thrown.expect(Parser.SyntaxException.class);
		parser.arg();
	}


	@Test
	public void testProgram0() throws IllegalCharException, IllegalNumberException, SyntaxException{
		String input = "prog0 {}";
		Parser parser = new Parser(new Scanner(input).scan());
		parser.parse();
	}
	@Test
	public void myTestCase1() throws IllegalCharException, IllegalNumberException, SyntaxException {
		String input = "3-4";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		Parser parser = new Parser(scanner);
		//thrown.expect(Parser.SyntaxException.class);
		parser.arg();
	}
	
	@Test
	public void myTestCase2ToCheckIf() throws IllegalCharException, IllegalNumberException, SyntaxException {
		String input = "if(a>b){a;}";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		Parser parser = new Parser(scanner);
		//thrown.expect(Parser.SyntaxException.class);
		parser.arg();
		//parser.program();
	}
	
	@Test
	public void myTestCase3ToCheckWhile() throws IllegalCharException, IllegalNumberException, SyntaxException {
		String input = "while(a>b){a==b;}";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		Parser parser = new Parser(scanner);
		//thrown.expect(Parser.SyntaxException.class);
		parser.arg();
		//parser.program();
	}
	
	@Test
	public void myTestCase4ToCheckExpression() throws IllegalCharException, IllegalNumberException, SyntaxException {
		String input = "(a*c)||(b*d)||(c*e);";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		Parser parser = new Parser(scanner);
		//thrown.expect(Parser.SyntaxException.class);
		parser.arg();
	}
	
	@Test
	public void myTestCase5ToCheckProgram() throws IllegalCharException, IllegalNumberException, SyntaxException{
		String input = "hi {while(a>b){a==b;}}";
		Parser parser = new Parser(new Scanner(input).scan());
		thrown.expect(Parser.SyntaxException.class);
		parser.program();
	}
	
	@Test
	public void myTestCase6ToCheckExpression() throws IllegalCharException, IllegalNumberException, SyntaxException {
		String input = "a*b+c/(c+d)";
		Scanner scanner = new Scanner(input);
		scanner.scan();
	    Parser parser = new Parser(scanner);
        parser.expression();
	}
	
	@Test
	public void myTestCase7ToCheckFactor() throws IllegalCharException, IllegalNumberException, SyntaxException {
		String input = "(abc*100/(1160/5)+c)";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		Parser parser = new Parser(scanner);
		parser.factor();
	}
	
	@Test
	public void myTestCase8ToCheckChainElem() throws IllegalCharException, IllegalNumberException, SyntaxException {
		String input = "(a->b)->c";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		Parser parser = new Parser(scanner);
		thrown.expect(Parser.SyntaxException.class);
		parser.chainElem();
		}
	
	@Test
	public void myTestCase9ToCheckStatement() throws IllegalCharException, IllegalNumberException, SyntaxException {
		String input = "while(a>b){}";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		Parser parser = new Parser(scanner);
		parser.statement();
	}
	
	
	
	
	
	
}