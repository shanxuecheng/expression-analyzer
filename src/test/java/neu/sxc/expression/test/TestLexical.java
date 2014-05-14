package neu.sxc.expression.test;

import java.util.List;

import junit.framework.TestCase;
import neu.sxc.expression.Expression;
import neu.sxc.expression.ExpressionFactory;
import neu.sxc.expression.lexical.LexicalAnalyzer;
import neu.sxc.expression.lexical.LexicalException;
import neu.sxc.expression.tokens.TerminalToken;


public class TestLexical extends TestCase {
	private ExpressionFactory factory = ExpressionFactory.getInstance();
	
	public void testNumber() {
		Expression expression = factory.getExpression("1 1.1 1.10 1e+2 1.1e-1");
		lexicalAnalysis(expression);
	}
	
	public void testDelimiter() {
		Expression expression = factory.getExpression("+-*/ >=<= ><,;&& ||!");
		lexicalAnalysis(expression);
	}
	
	public void testBoolean() {
		Expression expression = factory.getExpression("true false TRUE FALSE");
		lexicalAnalysis(expression);
	}
	
	public void testDate() {
		Expression expression = factory.getExpression("[2011-1-11] [2011-01-11] [2011-1-11 1:1:1] [2011-1-11 23:59:59]");
		lexicalAnalysis(expression);
	}
	
	public void testString() {
		Expression expression = factory.getExpression(" \"as\" ");
		lexicalAnalysis(expression);
	}
	
	public void testChar() {
		Expression expression = factory.getExpression(" 'a' ");
		lexicalAnalysis(expression);
	}
	
	public void testFunction() {
		Expression expression = factory.getExpression(" max abs ");
		lexicalAnalysis(expression);
	}
	
	public void testError() {
		Expression expression = factory.getExpression(" &2");
		lexicalAnalysis(expression);
	}
	
	private void lexicalAnalysis(Expression expression) {
		LexicalAnalyzer la = new LexicalAnalyzer();
		try {
			List<TerminalToken> tokens = la.analysis(expression.getExpression(), expression.getFunctionDefinitions());
			PrintExpression.printTokens(tokens);
		} catch (LexicalException e) {
			e.printStackTrace();
		}
	}
}
