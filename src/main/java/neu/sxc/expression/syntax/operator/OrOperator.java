package neu.sxc.expression.syntax.operator;

import neu.sxc.expression.syntax.ArgumentsMismatchException;
import neu.sxc.expression.tokens.DataType;
import neu.sxc.expression.tokens.Valuable;

public class OrOperator extends BinaryOperator {

	public OrOperator() {
		super("OR");
	}

	@Override
	public Object operate(Valuable[] arguments)
			throws ArgumentsMismatchException {
		Object result = null;
		Valuable a1 = arguments[0];
		Valuable a2 = arguments[1];
		if (a1.getDataType() == DataType.BOOLEAN
				&& a2.getDataType() == DataType.BOOLEAN) {
			result = a1.getBooleanValue() || a2.getBooleanValue();
		} else {
			throw new ArgumentsMismatchException(arguments, "||");
		}
		return result;
	}

}
