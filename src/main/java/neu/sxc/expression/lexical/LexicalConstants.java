package neu.sxc.expression.lexical;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import neu.sxc.expression.tokens.DelimiterToken;
import neu.sxc.expression.tokens.TokenBuilder;

/**
 * 词法分析常量
 * @author shanxuecheng
 *
 */
public class LexicalConstants {
	private LexicalConstants() {}
	
	/**
	 * 数字
	 */
	public static final String DIGITS_PATTERN = "\\d";
	
	/**
	 * 字母、下划线
	 */
	public static final String LETTER_UNDERLINE_PATTERN = "[a-zA-Z_]";
	
	/**
	 * 界符 ; ( ) , + - * / = > < | & ! { } . %
	 */
	public static final String DELIMITER_PATTERN = "[;(),\\+\\-\\*/=><|&!{}\\.%]";
	
	/**
	 * 非界符
	 */
	public static final String NOT_DELIMITER_PATTERN = "[^;(),\\+\\-\\*/=><|&!{}\\.]";
	
	/**
	 * 左方括号，作为日期常量的开始字符
	 */
	public static final String LEFT_SQUARE_BRACKET_PATTERN = "\\[";
	
	/**
	 * 右方括号，作为日期常量的结束字符
	 */
	public static final String RIGHT_SQUARE_BRACKET_PATTERN = "]";
	
	/**
	 * 单引号，作为日期常量的开始和结束字符
	 */
	public static final String SINGLE_QUOTES_PATTERN = "'";
	
	/**
	 * 双引号, 作为字符串常量的开始和结束字符
	 */
	public static final String DOUBLE_QUOTES_PATTERN = "\"";
	
	/**
	 * 小数点
	 */
	public static final String DECIMAL_POINT_PATTERN = "\\.";
	
	/**
	 * 指数符号, E/e
	 */
	public static final String EXPONENT_PATTERN = "[Ee]";
	
	/**
	 * 除字母、下划线、小数点、数字之外的任意字符
	 */
	public static final String NOT_LETTER_UNDERLINE_POINT_DIGIT_PATTERN = "[^a-zA-Z_\\.\\d]";
	
	/**
	 * 除字母、下划线、数字之外的任意字符
	 */
	public static final String NOT_LETTER_UNDERLINE_DIGIT_PATTERN = "[^a-zA-Z_\\d]";
	
	/**
	 * 正负号
	 */
	public static final String POSITIVE_NEGATIVE_PATTERN = "[\\+\\-]";
	
	/**
	 * 任意字符
	 */
	public static final String ANY_CHAR_PATTERN = ".";
	
	/**
	 * 数字、横线、冒号和空白字符（日期常量中的字符）
	 */
	public static final String DATE_FORMAT_PATTERN = "[\\d\\-\\s:]";
	
	/**
	 * 反斜杠，用于标识转义字符
	 */
	public static final String BACKSLASH_PATTERN = "\\\\";
	
	/**
	 * 非单引号、非反斜杠
	 */
	public static final String NOT_SINGLEQUOTES_BACKSLASH_PATTERN = "[^'\\\\]";
	
	/**
	 * 有效的转义字符: \\ \b \t \n \f \r \' \"
	 */
	public static final String ESCAPE_PATTERN = "[\\\\btnfr\'\"]";
	
	/**
	 * 非双引号、非反斜杠
	 */
	public static final String NOT_DOUBLEQUOTES_BACKSLASH_PATTERN = "[^\"\\\\]";
	
	/**
	 * 空白字符
	 */
	public static final String BLANK_PATTERN = "\\s";
	
	/**
	 * 非空白字符
	 */
	public static final String NOT_BLANK_PATTERN =  "\\S";
	
	/**
	 * 日期格式, [yyyy-MM-dd]
	 */
	public static final String DATE_PATTERN = "\\[\\d{4}-\\d{1,2}-\\d{1,2}\\]";
	
	/**
	 * 日期格式，精确到秒, [yyyy-MM-dd HH:mm:ss]
	 */
	public static final String ACCURATE_DATE_PATTERN = "\\[\\d{4}-\\d{1,2}-\\d{1,2}\\s+\\d{1,2}:\\d{1,2}:\\d{1,2}\\]";
	
	/**
	 * 注释（注释以##开始到行末结束，不能跨行）
	 */
	public static final String COMMENT_PATTERN = "##.*";
	
	/**
	 * 赋值符号
	 */
	public static final DelimiterToken ASSIGN_TOKEN = TokenBuilder.getBuilder().text("=").buildDelimiter();
	
	/**
	 * 关键字
	 */
	public static final List<String> KEY_WORDS;
	
	/**
	 * 单字符界符
	 */
	public static final List<String> SINGLE_DELIMITERS;
	
	/**
	 * 双字符界符
	 */
	public static final List<String> DOUBLE_DELIMITERS;
	
	public static final List<String> OPERATORS;
	
	static {
		List<String> keys = new ArrayList<String>();
		keys.add("if");
		keys.add("else");
		keys.add("then");
		keys.add("endif");
		keys.add("var");
		keys.add("while");
		keys.add("for");
		keys.add("do");
		KEY_WORDS = Collections.unmodifiableList(keys);
		
		List<String> doubleDelimiters = new ArrayList<String>();
		doubleDelimiters.add(">=");
		doubleDelimiters.add("<=");
		doubleDelimiters.add("==");
		doubleDelimiters.add("!=");
		doubleDelimiters.add("&&");
		doubleDelimiters.add("||");
		DOUBLE_DELIMITERS = Collections.unmodifiableList(doubleDelimiters);
		
		List<String> singleDelimiters = new ArrayList<String>();
		singleDelimiters.add("+");
		singleDelimiters.add("-");
		singleDelimiters.add("*");
		singleDelimiters.add("/");
		singleDelimiters.add("%");
		singleDelimiters.add("=");
		singleDelimiters.add(">");
		singleDelimiters.add("<");
		singleDelimiters.add("!");
		singleDelimiters.add(";");
		singleDelimiters.add(",");
		singleDelimiters.add("(");
		singleDelimiters.add(")");
		singleDelimiters.add("{");
		singleDelimiters.add("}");
		singleDelimiters.add(".");
		SINGLE_DELIMITERS = Collections.unmodifiableList(singleDelimiters);
		
		List<String> operators = new ArrayList<String>();
		operators.add("+");
		operators.add("-");
		operators.add("*");
		operators.add("/");
		operators.add("%");
		operators.add(">");
		operators.add("<");
		operators.add(">=");
		operators.add("<=");
		operators.add("==");
		operators.add("!=");
		operators.add("&&");
		operators.add("||");
		operators.add("!");
		operators.add("=");
		OPERATORS = Collections.unmodifiableList(operators);
	}
}
