package neu.sxc.expression.syntax.function;

import java.math.BigDecimal;

import neu.sxc.expression.tokens.DataType;
import neu.sxc.expression.tokens.Valuable;

public class Max extends Function {

	@Override
	public int getArgumentNum() {
		return -1;
	}

	@Override
	public Object executeFunction(Valuable[] arguments) {
		BigDecimal result;
		if(arguments.length == 0) {
			result = new BigDecimal("0");
		} else {
			result = arguments[0].getNumberValue();
			for(int i=1; i<arguments.length; i++)
				if(result.compareTo(arguments[i].getNumberValue()) < 0)
					result = arguments[i].getNumberValue();
		}
		return result;
	}

	@Override
	public String getName() {
		return "max";
	}

	@Override
	public DataType[] getArgumentsDataType() {
		return new DataType[]{DataType.NUMBER};
	}
}
