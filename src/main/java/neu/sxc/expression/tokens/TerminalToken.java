package neu.sxc.expression.tokens;

/**
 * 终结符
 * @author shanxuecheng
 *
 */
public abstract class TerminalToken implements Token {

	/**
	 * 行号
	 */
	private final int line;
	
	/**
	 * 列号
	 */
	private final int column;
	
	private final String text;
	
	public TerminalToken(TokenBuilder builder) {
		line = builder.getLine();
		column = builder.getColumn();
		text = builder.getText();
	}
	
	public int getLine() {
		return line;
	}
	
	public int getColumn() {
		return column;
	}
	
	public String getText() {
		return text;
	}
	
	/**
	 * 与文法中的符号是否匹配
	 * @param target
	 * @return
	 */
	public abstract boolean equalsInGrammar(TerminalToken target);
}
