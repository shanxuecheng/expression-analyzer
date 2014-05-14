package neu.sxc.expression.tokens;

/**
 * 符号接口
 * @author shanxuecheng
 *
 */
public interface Token {
	/**
	 * 返回符号类型
	 * @return
	 */
	public abstract TokenType getTokenType();
}
