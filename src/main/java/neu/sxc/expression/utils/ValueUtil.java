package neu.sxc.expression.utils;

import java.math.BigDecimal;
import java.util.Calendar;

import neu.sxc.expression.tokens.DataType;
import neu.sxc.expression.tokens.Valuable;


public class ValueUtil {
	public static BigDecimal getNumberValue(Valuable valuable) {
		if(valuable.getDataType() == DataType.NUMBER
				&& valuable.getValue() != null)
			return (BigDecimal) valuable.getValue();
		return null;
	}
	
	public static String getStringValue(Valuable valuable) {
		if(valuable.getDataType() == DataType.STRING
				&& valuable.getValue() != null)
			return (String) valuable.getValue();
		return null;
	}
	
	public static Character getCharValue(Valuable valuable) {
		if(valuable.getDataType() == DataType.CHARACTER 
				&& valuable.getValue() != null)
			return (Character) valuable.getValue();
		return null;
	}
	
	public static Calendar getDateValue(Valuable valuable) {
		if(valuable.getDataType() == DataType.DATE 
				&& valuable.getValue() != null)
			return (Calendar) valuable.getValue();
		return null;
	}
	
	public static Boolean getBooleanValue(Valuable valuable) {
		if(valuable.getDataType() == DataType.BOOLEAN
				&& valuable.getValue() != null)
			return (Boolean) valuable.getValue();
		return null;
	}

}
