package neu.sxc.expression.lexical.dfa;

import static neu.sxc.expression.lexical.LexicalConstants.*;

/**
 * 有限自动机的定义，包括所有状态和路径的初始化
 * @author shanxuecheng
 *
 */
public class DFADefinition {
	
	private DFADefinition(){
		initStartState();
		initNumberStates();
		initIDStates();
		initDelimiterStates();
		initDateStates();
		initCharStates();
		initStringStates();
	}
	
	public static DFADefinition getDFA(){
		return new DFADefinition();
	}
	
	public DFAMidState getDFAStartState(){
		return START_STATE;
	}
	
	/**
	 * initialize the start states
	 */
	private void initStartState(){
		START_STATE.setNextMidState(DIGITS_PATTERN, NUMBER_1);
		START_STATE.setNextMidState(LETTER_UNDERLINE_PATTERN, ID_1);
		START_STATE.setNextMidState(DELIMITER_PATTERN, DELIMITER_1);
		START_STATE.setNextMidState(LEFT_SQUARE_BRACKET_PATTERN, DATE_1);
		START_STATE.setNextMidState(SINGLE_QUOTES_PATTERN, CHAR_1);
		START_STATE.setNextMidState(DOUBLE_QUOTES_PATTERN, STRING_1);
		START_STATE.setErrorMessage("Illegal character.");
	}
	
	/**
	 * initialize states used to recognize numbers 
	 */
	private void initNumberStates(){
		NUMBER_1.setNextMidState(DIGITS_PATTERN, NUMBER_1);
		NUMBER_1.setNextMidState(DECIMAL_POINT_PATTERN, NUMBER_2);
		NUMBER_1.setNextMidState(EXPONENT_PATTERN, NUMBER_4);
		NUMBER_1.setRouteToEndState(NOT_LETTER_UNDERLINE_POINT_DIGIT_PATTERN, DFAEndStateCode.NUMBER_END);
		
		NUMBER_2.setNextMidState(DIGITS_PATTERN, NUMBER_3);
		
		NUMBER_3.setNextMidState(DIGITS_PATTERN, NUMBER_3);
		NUMBER_3.setNextMidState(EXPONENT_PATTERN, NUMBER_4);
		NUMBER_3.setRouteToEndState(NOT_LETTER_UNDERLINE_POINT_DIGIT_PATTERN, DFAEndStateCode.NUMBER_END);
		
		NUMBER_4.setNextMidState(POSITIVE_NEGATIVE_PATTERN, NUMBER_5);
		NUMBER_4.setNextMidState(DIGITS_PATTERN, NUMBER_6);
		
		NUMBER_5.setNextMidState(DIGITS_PATTERN, NUMBER_6);
		
		NUMBER_6.setNextMidState(DIGITS_PATTERN, NUMBER_6);
		NUMBER_6.setRouteToEndState(NOT_LETTER_UNDERLINE_POINT_DIGIT_PATTERN, DFAEndStateCode.NUMBER_END);
	}
	
	/**
	 * initialize states used to recognize variables, key words, 'true' or 'false'
	 */
	private void initIDStates(){
		ID_1.setNextMidState(LETTER_UNDERLINE_PATTERN, ID_1);
		ID_1.setNextMidState(DIGITS_PATTERN, ID_1);
		ID_1.setRouteToEndState(NOT_LETTER_UNDERLINE_DIGIT_PATTERN, DFAEndStateCode.ID_END);
	}
	
	/**
	 * initialize states used to recognize delimiters
	 * there maybe single character delimiters like '+',
	 * and double character delimiters like '>='
	 */
	private void initDelimiterStates(){
		DELIMITER_1.setNextMidState(DELIMITER_PATTERN, DELIMITER_2);
		DELIMITER_1.setRouteToEndState(NOT_DELIMITER_PATTERN, DFAEndStateCode.SINGLE_DELIMITER_END);
		
		DELIMITER_2.setRouteToEndState(ANY_CHAR_PATTERN, DFAEndStateCode.DOUBLE_DELIMITER_END);
	}
	
	/**
	 * initialize states used to recognize dates
	 * date must be inputed as [yyyy-MM-dd] or [yyyy-MM-dd HH:mm:ss],
	 * this time, we just guarantee characters between '[' and ']' are digit, '-', ':' or blank
	 */
	private void initDateStates(){
		DATE_1.setNextMidState(DATE_FORMAT_PATTERN, DATE_1);
		DATE_1.setNextMidState(RIGHT_SQUARE_BRACKET_PATTERN, DATE_2);
		DATE_1.setErrorMessage("Wrong date format, please input as [yyyy-MM-dd] or [yyyy-MM-dd HH:mm:ss].");
		
		DATE_2.setRouteToEndState(ANY_CHAR_PATTERN, DFAEndStateCode.DATE_END);
	}
	
	/**
	 * initialize states used to recognize character constants
	 * the character constants can be a escape sequence
	 */
	private void initCharStates(){
		CHAR_1.setNextMidState(BACKSLASH_PATTERN, CHAR_4);
		CHAR_1.setNextMidState(NOT_SINGLEQUOTES_BACKSLASH_PATTERN, CHAR_2);
		CHAR_1.setErrorMessage("Invalid character constant.");
		
		CHAR_2.setNextMidState(SINGLE_QUOTES_PATTERN, CHAR_3);
		CHAR_2.setErrorMessage("Invalid character constant.");
		
		CHAR_3.setRouteToEndState(ANY_CHAR_PATTERN, DFAEndStateCode.CHAR_END);
		
		CHAR_4.setNextMidState(ESCAPE_PATTERN, CHAR_5);
		CHAR_4.setErrorMessage("Invalid escape sequence (valid ones are \\b \\t \\n \\f \\r \\\" \\\' \\\\ ).");
		
		CHAR_5.setNextMidState(SINGLE_QUOTES_PATTERN, CHAR_3);
		CHAR_5.setErrorMessage("Invalid character constant.");
	}
	
	/**
	 * initialize states used to recognize string constants
	 * the string constants may contain one escape sequence or more
	 */
	private void initStringStates(){
		STRING_1.setNextMidState(NOT_DOUBLEQUOTES_BACKSLASH_PATTERN, STRING_1);
		STRING_1.setNextMidState(DOUBLE_QUOTES_PATTERN, STRING_2);
		STRING_1.setNextMidState(BACKSLASH_PATTERN, STRING_3);
		STRING_1.setErrorMessage("String literal is not properly closed by a double-quote.");
		
		STRING_2.setRouteToEndState(ANY_CHAR_PATTERN, DFAEndStateCode.STRING_END);
		
		STRING_3.setNextMidState(ESCAPE_PATTERN, STRING_4);
		STRING_3.setErrorMessage("Invalid escape sequence (valid ones are \\b \\t \\n \\f \\r \\\" \\\' \\\\ ).");
		
		STRING_4.setNextMidState(DOUBLE_QUOTES_PATTERN, STRING_2);
		STRING_4.setNextMidState(BACKSLASH_PATTERN, STRING_3);
		STRING_4.setNextMidState(NOT_DOUBLEQUOTES_BACKSLASH_PATTERN, STRING_1);
	}

	private DFAMidState START_STATE = new DFAMidState(DFAMidStateCode.START);
	
	private DFAMidState NUMBER_1 = new DFAMidState(DFAMidStateCode.NUMBER_1);
    
	private DFAMidState NUMBER_2 = new DFAMidState(DFAMidStateCode.NUMBER_2);
	                        
	private DFAMidState NUMBER_3 = new DFAMidState(DFAMidStateCode.NUMBER_3);
	                        
	private DFAMidState NUMBER_4 = new DFAMidState(DFAMidStateCode.NUMBER_4);
	                        
	private DFAMidState NUMBER_5 = new DFAMidState(DFAMidStateCode.NUMBER_5);
	                        
	private DFAMidState NUMBER_6 = new DFAMidState(DFAMidStateCode.NUMBER_6);
	                        
	private DFAMidState ID_1 = new DFAMidState(DFAMidStateCode.ID_1);
	                        
	private DFAMidState DELIMITER_1 = new DFAMidState(DFAMidStateCode.DELIMITER_1);
	 
	private DFAMidState DELIMITER_2 = new DFAMidState(DFAMidStateCode.DELIMITER_2);
	                         
	private DFAMidState DATE_1 = new DFAMidState(DFAMidStateCode.DATE_1);
	                         
	private DFAMidState DATE_2 = new DFAMidState(DFAMidStateCode.DATE_2);
	
	private DFAMidState CHAR_1 = new DFAMidState(DFAMidStateCode.CHAR_1);
	
	private DFAMidState CHAR_2 = new DFAMidState(DFAMidStateCode.CHAR_2);
	
	private DFAMidState CHAR_3 = new DFAMidState(DFAMidStateCode.CHAR_3);
	
	private DFAMidState CHAR_4 = new DFAMidState(DFAMidStateCode.CHAR_4);
	
	private DFAMidState CHAR_5 = new DFAMidState(DFAMidStateCode.CHAR_5);
	
	private DFAMidState STRING_1 = new DFAMidState(DFAMidStateCode.STRING_1);
	                         
	private DFAMidState STRING_2 = new DFAMidState(DFAMidStateCode.STRING_2);
	                         
	private DFAMidState STRING_3 = new DFAMidState(DFAMidStateCode.STRING_3);
	                         
	private DFAMidState STRING_4 = new DFAMidState(DFAMidStateCode.STRING_4);
}
