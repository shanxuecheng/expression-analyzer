package neu.sxc.expression.test;

import java.util.Calendar;
import java.util.Date;

import neu.sxc.expression.syntax.function.Function;
import neu.sxc.expression.tokens.DataType;
import neu.sxc.expression.tokens.Valuable;

public class CurrentDate extends Function {
	@Override
	public String getName() {
		return "getDate";
	}
	
	@Override
	public int getArgumentNum() {
		return 0;
	}
	
	@Override
	public DataType[] getArgumentsDataType() {
		return null;
	}
	
	@Override
	protected Object executeFunction(Valuable[] arguments) {
		Calendar date = Calendar.getInstance();
		date.setTime(new Date());
		return date;
	}
}
