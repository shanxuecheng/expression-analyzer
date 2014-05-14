package neu.sxc.expression;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;

/**
 * 表达式工厂类
 * @author shanxuecheng
 *
 */
public class ExpressionFactory {
	
	private static ExpressionFactory expressionFactory = new ExpressionFactory();
	
	private ExpressionFactory() {}
	
	public static ExpressionFactory getInstance() {
		return expressionFactory;
	}
	
	public Expression getExpression(String expression) {
		return new Expression(expression);
	}
	
	public Expression getExpression(InputStream source) throws IOException {
		return new Expression(source);
	}
	
	public Expression getExpression(Reader source) throws IOException {
		return new Expression(source);
	}
}
