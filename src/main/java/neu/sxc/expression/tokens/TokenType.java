package neu.sxc.expression.tokens;

/**
 * Token类型
 * @author shanxuecheng
 *
 */
public enum TokenType {
	/**
	 * 非终结符
	 */
	NT,
	
	/**
	 * 动作
	 */
	EXECUTION,
	
	/**
	 * 上下文操作
	 */
	CONTEXT_OPERATION,
	
	/**
	 * 关键字
	 */
	KEY,
	
	/**
	 * 界符
	 */
	DELIMITER,
	
	/**
	 * 函数
	 */
	FUNCTION,
	
	/**
	 * 常量
	 */
	CONST,
	
	/**
	 * 变量
	 */
	VARIABLE,
	
	/**
	 * 运行时结果
	 */
	RUNTIME_VALUE
}
