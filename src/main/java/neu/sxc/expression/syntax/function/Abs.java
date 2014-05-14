package neu.sxc.expression.syntax.function;

import neu.sxc.expression.tokens.DataType;
import neu.sxc.expression.tokens.Valuable;

public class Abs extends Function {

	@Override
	public String getName() {
		return "abs";
	}
	
	@Override
	public int getArgumentNum() {
		return 1;
	}
	
	@Override
	public DataType[] getArgumentsDataType() {
		return new DataType[]{DataType.NUMBER};
	}

	@Override
	protected Object executeFunction(Valuable[] arguments) {
		Valuable argument = arguments[0];
		return argument.getNumberValue().abs();
	}


}
