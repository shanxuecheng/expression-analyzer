package neu.sxc.expression.test;

import java.io.FileInputStream;
import java.io.IOException;

import junit.framework.TestCase;
import neu.sxc.expression.Expression;
import neu.sxc.expression.ExpressionFactory;
import neu.sxc.expression.lexical.LexicalException;
import neu.sxc.expression.syntax.ArgumentsMismatchException;
import neu.sxc.expression.syntax.SyntaxException;
import neu.sxc.expression.syntax.VariableNotInitializedException;

public class TestSyntax extends TestCase{
	
	private ExpressionFactory factory = ExpressionFactory.getInstance();
	
	public void testExpression() throws IOException{
		Expression expression = new Expression(new FileInputStream("test/source.txt"));
		evaluate(expression);
//		Valuable a = expression.getVariableValue("a");
//		Printer.println(a.getValue());
	}
	
	public void testArithmetic() {
		Expression expression = factory.getExpression("a=a+1;");
		expression.initVariable("a", 2);
		
		expression.lexicalAnalysis();
		
		System.out.println("result:" + expression.evaluate().getValue());
		System.out.println("a = " + expression.getVariableValueAfterEvaluate("a").getValue());
		System.out.println("-------------------------------------");
		expression.initVariable("a", 5);
		System.out.println("result:" + expression.evaluate().getValue());
		System.out.println("a = " + expression.getVariableValueAfterEvaluate("a").getValue());
		System.out.println("-------------------------------------");
	}
	
	public void testCompare() {
		Expression expression = factory.getExpression("a=1<2 && 2>=3; b='a'<='b';" +
				"c=[2011-01-01]<=[2011-01-02]; d='a'!='a';");
		evaluate(expression);
	}
	
	public void testBoolean() {
		Expression expression = factory.getExpression("(1+2)>2 && !2>1 || TRUE;");
		evaluate(expression);
	}
	
	public void testFunction() {
		Expression expression = factory.getExpression("1 + max(1,abs(-2)) + abs(-1);");
		evaluate(expression);
	}
	
	public void testGetDate() {
		Expression expression = factory.getExpression("getDate();");
		expression.addFunction(new CurrentDate());
		evaluate(expression);
	}
	
	private void evaluate(Expression expression) {
		try {
			Printer.println(System.currentTimeMillis());
			expression.reParseAndEvaluate();
			Printer.println(System.currentTimeMillis());
		} catch (LexicalException e) {
			e.printStackTrace();
		} catch (VariableNotInitializedException e) {
			e.printStackTrace();
		} catch (ArgumentsMismatchException e) {
			e.printStackTrace();
		} catch (SyntaxException e) {
			e.printStackTrace();
		}
		PrintExpression.printExp(expression);
	}
}
