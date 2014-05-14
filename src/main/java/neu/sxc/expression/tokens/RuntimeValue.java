package neu.sxc.expression.tokens;

import java.math.BigDecimal;
import java.util.Calendar;

import neu.sxc.expression.utils.ValueUtil;

/**
 * 运行时的中间值
 * @author shanxuecheng
 *
 */
public final class RuntimeValue implements Valuable {

	/**
	 * 数据类型
	 */
	private final DataType dataType;
	
	/**
	 * 值
	 */
	private final Object value;
	
	public RuntimeValue(TokenBuilder builder) {
		dataType = builder.getDataType();
		value = builder.getValue();
	}
	
	public DataType getDataType() {
		return dataType;
	}
	
	public TokenType getTokenType() {
		return TokenType.RUNTIME_VALUE;
	}
	
	public BigDecimal getNumberValue() {
		return ValueUtil.getNumberValue(this);
	}
	
	public String getStringValue() {
		return ValueUtil.getStringValue(this);
	}
	
	public Character getCharValue() {
		return ValueUtil.getCharValue(this);
	}
	
	public Calendar getDateValue() {
		return ValueUtil.getDateValue(this);
	}
	
	public Boolean getBooleanValue() {
		return ValueUtil.getBooleanValue(this);
	}
	
	public Object getValue() {
		return value;
	}
}
