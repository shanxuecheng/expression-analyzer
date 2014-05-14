package neu.sxc.expression.syntax.operator;

import neu.sxc.expression.syntax.ArgumentsMismatchException;
import neu.sxc.expression.tokens.DataType;
import neu.sxc.expression.tokens.Valuable;

public class AddOperator extends BinaryOperator {

	public AddOperator() {
		super("ADD");
	}

	@Override
	public Object operate(Valuable[] arguments)
			throws ArgumentsMismatchException {
		Object result = null;
		Valuable a1 = arguments[0];
		Valuable a2 = arguments[1];
		if (a1.getDataType() == DataType.NUMBER
				&& a2.getDataType() == DataType.NUMBER) {
			result = a1.getNumberValue().add(a2.getNumberValue());
		} else if (a1.getDataType() == DataType.STRING
				&& a2.getDataType() == DataType.STRING) {
			result = a1.getStringValue() + a2.getStringValue();
		} else if (a1.getDataType() == DataType.STRING
				&& a2.getDataType() == DataType.CHARACTER) {
			result = a1.getStringValue() + a2.getCharValue();
		} else if (a1.getDataType() == DataType.CHARACTER
				&& a2.getDataType() == DataType.STRING) {
			result = a1.getCharValue() + a2.getStringValue();
		} else {
			throw new ArgumentsMismatchException(arguments, "+");
		}
		return result;
	}

}
