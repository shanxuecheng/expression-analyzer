package neu.sxc.expression.syntax;

import neu.sxc.expression.tokens.TerminalToken;

/**
 * 语法异常
 * @author shanxuecheng
 *
 */
@SuppressWarnings("serial")
public class SyntaxException extends RuntimeException{
	
	public SyntaxException(String message) {
		super(message);
	}
	
	public SyntaxException(String message, TerminalToken token) {
		super(message + showTokenLocation(token));
	}
	
	public SyntaxException(String message, TerminalToken token, Throwable cause) {
		this(message, token);
		this.initCause(cause);
	}
	
	public SyntaxException(TerminalToken token) {
		super("Syntax error for token \"" + token.getText() + "\"." + showTokenLocation(token));
	}
	
	private static String showTokenLocation(TerminalToken token) {
		return " At line:" + token.getLine() + ", column:" + token.getColumn() + ".";
	}
}
