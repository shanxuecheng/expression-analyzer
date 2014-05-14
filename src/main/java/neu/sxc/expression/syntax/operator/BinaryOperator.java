package neu.sxc.expression.syntax.operator;

/**
 * 二元操作符
 * @author shanxuecheng
 *
 */
public abstract class BinaryOperator extends Operator {

	public BinaryOperator(String operator) {
		super(operator);
	}

	/**
	 * 操作数个数为2
	 */
	public final int getArgumentNum() {
		return 2;
	}

}
