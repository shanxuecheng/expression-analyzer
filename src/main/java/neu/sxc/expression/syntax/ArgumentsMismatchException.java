package neu.sxc.expression.syntax;

import neu.sxc.expression.tokens.TerminalToken;
import neu.sxc.expression.tokens.Valuable;

/**
 * 参数不匹配异常
 * @author shanxuecheng
 *
 */
@SuppressWarnings("serial")
public class ArgumentsMismatchException extends SyntaxException {
	
	public ArgumentsMismatchException(String message) {
		super(message);
	}
	
	public ArgumentsMismatchException(String message, TerminalToken operator, Throwable cause) {
		super(message, operator, cause);
	}
	
	public ArgumentsMismatchException(String message, TerminalToken operator) {
		super(message, operator);
	}
	
	public ArgumentsMismatchException(Valuable[] arguments, String operatorName) {
		super("The operator(or method) " + operatorName + " is undefined for the arguments ("
				+ getErrorTypes(arguments) + ").");
	}
	
	private static String getErrorTypes(Valuable[] arguments) {
		if(arguments.length == 0)
			return "";
		StringBuilder types = new StringBuilder();
		for(Valuable argument : arguments)
			types.append(argument.getDataType().name()).append(',');
		return types.toString().substring(0, types.length()-1);
	}
}
