package neu.sxc.expression.syntax.operator;

/**
 * 一元操作符
 * @author shanxuecheng
 *
 */
public abstract class UnaryOperator extends Operator {

	public UnaryOperator(String operatorName) {
		super(operatorName);
	}

	/**
	 * 操作数个数为1
	 */
	public final int getArgumentNum() {
		return 1;
	}

}
