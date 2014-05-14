package neu.sxc.expression.lexical.dfa;

/**
 * 有限自动机中间状态代码（包括开始状态）
 * @author shanxuecheng
 *
 */
public enum DFAMidStateCode {
	//开始状态
	START,
	
	//数字中间状态
	NUMBER_1,
	NUMBER_2,
	NUMBER_3,
	NUMBER_4,
	NUMBER_5,
	NUMBER_6,
	
	//标识符中间状态
	ID_1,
	
	//界符中间状态
	DELIMITER_1,
	DELIMITER_2,
	
	//日期中间状态
	DATE_1,
	DATE_2,
	
	//字符中间状态
	CHAR_1,
	CHAR_2,
	CHAR_3,
	CHAR_4,
	CHAR_5,
	
	//字符串中间状态
	STRING_1,
	STRING_2,
	STRING_3,
	STRING_4
}
