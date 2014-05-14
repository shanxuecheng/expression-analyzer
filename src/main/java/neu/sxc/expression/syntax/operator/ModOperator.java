package neu.sxc.expression.syntax.operator;

import java.math.BigDecimal;

import neu.sxc.expression.syntax.ArgumentsMismatchException;
import neu.sxc.expression.tokens.DataType;
import neu.sxc.expression.tokens.Valuable;


public class ModOperator extends BinaryOperator {

	public ModOperator() {
		super("MOD");
	}

	@Override
	public Object operate(Valuable[] arguments)
			throws ArgumentsMismatchException {
		Object result = null;
		Valuable a1 = arguments[0];
		Valuable a2 = arguments[1];
		if (a1.getDataType() == DataType.NUMBER
				&& a2.getDataType() == DataType.NUMBER) {
			if (a2.getNumberValue().compareTo(new BigDecimal("0")) == 0)
				throw new ArithmeticException("Divided by zero.");
			result = a1.getNumberValue().divideAndRemainder(a2.getNumberValue())[1];
		} else {
			throw new ArgumentsMismatchException(arguments, "%");
		}
		return result;
	}
}
