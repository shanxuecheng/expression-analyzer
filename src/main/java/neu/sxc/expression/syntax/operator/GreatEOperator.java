package neu.sxc.expression.syntax.operator;

import neu.sxc.expression.syntax.ArgumentsMismatchException;
import neu.sxc.expression.tokens.DataType;
import neu.sxc.expression.tokens.Valuable;

public class GreatEOperator extends BinaryOperator {

	public GreatEOperator() {
		super("GREATE");
	}

	@Override
	public Object operate(Valuable[] arguments)
			throws ArgumentsMismatchException {
		Object result = null;
		Valuable a1 = arguments[0];
		Valuable a2 = arguments[1];
		if (a1.getDataType() == DataType.NUMBER
				&& a2.getDataType() == DataType.NUMBER) {
			result = a1.getNumberValue().compareTo(a2.getNumberValue()) >= 0;
		} else if (a1.getDataType() == DataType.STRING
				&& a2.getDataType() == DataType.STRING) {
			result = a1.getStringValue().compareTo(a2.getStringValue()) >= 0;
		} else if (a1.getDataType() == DataType.CHARACTER
				&& a2.getDataType() == DataType.CHARACTER) {
			result = a1.getCharValue().compareTo(a2.getCharValue()) >= 0;
		} else if (a1.getDataType() == DataType.DATE
				&& a2.getDataType() == DataType.DATE) {
			result = a1.getDateValue().compareTo(a2.getDateValue()) >= 0;
		} else {
			throw new ArgumentsMismatchException(arguments, ">=");
		}
		return result;
	}

}
