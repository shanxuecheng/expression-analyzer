package neu.sxc.expression.syntax.operator;

import neu.sxc.expression.syntax.ArgumentsMismatchException;
import neu.sxc.expression.tokens.DataType;
import neu.sxc.expression.tokens.Valuable;

public class MultiplyOperator extends BinaryOperator {

	public MultiplyOperator() {
		super("MULTIPLY");
	}

	@Override
	public Object operate(Valuable[] arguments)
			throws ArgumentsMismatchException {
		Object result = null;
		Valuable a1 = arguments[0];
		Valuable a2 = arguments[1];
		if (a1.getDataType() == DataType.NUMBER
				&& a2.getDataType() == DataType.NUMBER) {
			result = a1.getNumberValue().multiply(a2.getNumberValue());
		} else {
			throw new ArgumentsMismatchException(arguments, "*");
		}
		return result;
	}

}
