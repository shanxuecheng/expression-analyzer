package neu.sxc.expression.utils;

public class ExpressionUtil {
	/**
	 * 根据反斜杠后接的字符，返回转义字符
	 * @param escape
	 * @return
	 */
	public static char getEscapedChar(char escape) {
		char escapedChar = 0;
		switch(escape){
		case 'b':
			escapedChar = '\b';
			break;
		case 't':
			escapedChar = '\t';
			break;
		case 'n':
			escapedChar = '\n';
			break;
		case 'f':
			escapedChar = '\f';
			break;
		case 'r':
			escapedChar = '\r';
			break;
		case '\"':
			escapedChar = '\"';
			break;
		case '\'':
			escapedChar = '\'';
			break;
		case '\\':
			escapedChar = '\\';
			break;
		}
		return escapedChar;
	}
	
	/**
	 * 替换字符串中的转义字符
	 * @param target
	 * @return
	 */
	public static String transformEscapesInString(String target) {
		target = target.replaceAll("\\\\\\\\", "\\");
		target = target.replaceAll("\\\\b", "\b");
		target = target.replaceAll("\\\\t", "\t");
		target = target.replaceAll("\\\\n", "\n");
		target = target.replaceAll("\\\\f", "\f");
		target = target.replaceAll("\\\\r", "\r");
		target = target.replaceAll("\\\\'", "\'");
		target = target.replaceAll("\\\\\"", "\"");
		return target;
	}
}
