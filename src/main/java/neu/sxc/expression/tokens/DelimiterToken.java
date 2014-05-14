package neu.sxc.expression.tokens;

/**
 * 界符
 * @author shanxuecheng
 *
 */
public final class DelimiterToken extends TerminalToken {

	public DelimiterToken(TokenBuilder builder) {
		super(builder);
	}
	
	public TokenType getTokenType() {
		return TokenType.DELIMITER;
	}

	@Override
	public boolean equalsInGrammar(TerminalToken target) {
		if(!(target instanceof DelimiterToken))
			return false;
		return this.getText().equals(target.getText());
	}

}
