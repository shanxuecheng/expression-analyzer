package neu.sxc.expression.lexical;

import static neu.sxc.expression.lexical.LexicalConstants.*;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import neu.sxc.expression.lexical.dfa.DFADefinition;
import neu.sxc.expression.lexical.dfa.DFAEndStateCode;
import neu.sxc.expression.lexical.dfa.DFAMidState;
import neu.sxc.expression.syntax.function.Function;
import neu.sxc.expression.syntax.function.SystemFunctions;
import neu.sxc.expression.tokens.DataType;
import neu.sxc.expression.tokens.TerminalToken;
import neu.sxc.expression.tokens.TokenBuilder;
import neu.sxc.expression.tokens.VariableToken;
import neu.sxc.expression.utils.ExpressionUtil;

/**
 * 词法分析
 * @author shanxuecheng
 *
 */
public class LexicalAnalyzer {
	
	/**
	 * 有限自动机
	 */
	private DFADefinition DFA = DFADefinition.getDFA();
	
	/**
	 * 当前解析位置行号
	 */
	private int curLine = 0;
	
	/**
	 * 下次读取列号
	 */
	private int nextScanColumn = 0;
	
	/**
	 * 存放正在解析的Token的字面内容
	 */
	private StringBuilder curWord = new StringBuilder();
	
	/**
	 * 表达式中涉及的函数
	 */
	private Map<String, Function> functionDefinitions;
	
	public LexicalAnalyzer() {}
	
	/**
	 * 词法分析
	 * @param expression 表达式
	 * @return
	 * @throws LexicalException
	 */
	public List<TerminalToken> analysis(String expression) throws LexicalException {
		return analysis(expression, null);
	}
	
	/**
	 * 词法分析
	 * @param expression 表达式
	 * @param functionDefinitions 表达式涉及的函数
	 * @return
	 * @throws LexicalException
	 */
	public List<TerminalToken> analysis(String expression, Map<String, Function> functionDefinitions) throws LexicalException {
		if(expression == null || expression.length() == 0)
			throw new LexicalException("Invalid empty expression.");
		
		Scanner scanner = new Scanner(expression);
		this.functionDefinitions = functionDefinitions;
		
		try {
			List<TerminalToken> tokens = doAnalysis(scanner);
			return tokens;
		} catch(LexicalException e) {
			throw e;
		} finally {
			scanner.close();
		}
	}
	
	/**
	 * 执行词法分析
	 * @return
	 * @throws LexicalException 
	 */
	private List<TerminalToken> doAnalysis(Scanner scanner) throws LexicalException {
		//词法分析的结果，按序存放识别出的Token
		List<TerminalToken> tokens = new ArrayList<TerminalToken>();
		
		char[] curLineCharArray;	//用于存放当前行的字符数组
		char inputChar;	//当前读取的字符
		
		DFAMidState curMidState = null;		//当前到达的中间状态
		DFAMidState nextMidsState = null;	//curMidState根据inputChar所能到达的中间状态
		DFAEndStateCode endStateCode = null;	//结束状态代码
		TerminalToken curToken = null;	//识别出的Token
		curLine = 0;
		while(scanner.hasNextLine()) {
			curLineCharArray = nextLine(scanner).toCharArray();//读取下一行
			curLine++;
			nextScanColumn = 0;
			
			while(escapeBlank(curLineCharArray) < curLineCharArray.length) {
				curMidState = DFA.getDFAStartState(); //设置当前状态到开始状态，准备识别下一个Token
				curWord = curWord.delete(0, curWord.length());
				curToken = null;
				
				while(curToken == null) {
					if(nextScanColumn < curLineCharArray.length) {
						inputChar = curLineCharArray[nextScanColumn]; //取下一字符
						nextMidsState = curMidState.getNextMidState(inputChar);
						if(nextMidsState != null) {	//下一中间状态不空，追加该字符到当前Token
							curMidState = nextMidsState;
							curWord.append(inputChar);
							nextScanColumn++;
						} else {
							endStateCode = curMidState.goToEndStateWithInput(inputChar);
							if(endStateCode != null)
								//到达结束状态，一个token识别结束（当前输入的字符不追加到curWord）
								curToken = actAtEndState(endStateCode);
							else 	//发生词法错误
								throw new LexicalException(curMidState, curLine, nextScanColumn + 1);
						}
					} else if(curMidState.hasRouteToEndState()) {
						//在行尾如果curMidState存在到结束状态的路由，说明当前Token正确结束，否则存在词法错误
						curToken = actAtEndState(curMidState.getNextEndStateCode());
					} else
						throw new LexicalException(curMidState, curLine, nextScanColumn + 1);
				}
				tokens.add(curToken);
				checkVariableToBeAssigned(tokens);
			}
		}
		return tokens;
	}
	
	/**
	 * 在结束状态执行动作，识别出一个Token
	 * @param endStateCode
	 * @throws LexicalException 
	 */
	private TerminalToken actAtEndState(DFAEndStateCode endStateCode) throws LexicalException {
		TerminalToken curToken = null;	//当前识别出的Token
		String curWordText = curWord.toString();	//当前Token的字面内容
		int wordStartColumn = nextScanColumn - curWordText.length() + 1;	//当前Token开始的列位置
		
		switch(endStateCode) {
		case NUMBER_END:
			curToken = TokenBuilder.getBuilder().line(curLine).column(wordStartColumn)
							.text(curWordText).dataType(DataType.NUMBER)
							.value(new BigDecimal(curWordText))
							.buildConst();
			break;
		case ID_END:
			//依次判断是否为布尔常量、关键字、函数名，如果都不是，则判断为变量
			if("true".equals(curWordText) || "TRUE".equals(curWordText)
					|| "false".equals(curWordText) || "FALSE".equals(curWordText)) {
				//识别布尔常量
				curToken = TokenBuilder.getBuilder().line(curLine).column(wordStartColumn)
								.text(curWordText).dataType(DataType.BOOLEAN)
								.value(Boolean.valueOf(curWordText))
								.buildConst();
			} else if(KEY_WORDS.contains(curWordText)) { //识别关键字
				curToken = TokenBuilder.getBuilder().line(curLine).column(wordStartColumn)
								.text(curWordText).buildKey();
			} else if(hasFunction(curWordText)) { //函数
				curToken = TokenBuilder.getBuilder().line(curLine).column(wordStartColumn)
								.text(curWordText).function(findFunction(curWordText)).buildFunction();
			} else //变量
				curToken = TokenBuilder.getBuilder().line(curLine).column(wordStartColumn)
								.text(curWordText).buildVariable();
			break;
		case SINGLE_DELIMITER_END:
			//判断是否为合法的单字符界符，否则词法错误
			if(SINGLE_DELIMITERS.contains(curWordText))
				curToken = TokenBuilder.getBuilder().line(curLine).column(wordStartColumn)
									.text(curWordText).buildDelimiter();
			else
				throw new LexicalException("Invalid delimiter.", curLine, wordStartColumn);
			break;
		case DOUBLE_DELIMITER_END:
			if(DOUBLE_DELIMITERS.contains(curWordText)) {	//判断是否为合法的双字符界符
				curToken = TokenBuilder.getBuilder().line(curLine).column(wordStartColumn)
								.text(curWordText).buildDelimiter();
			} else {
				//取第一个字符，如果是合法的单字符界符，当前列扫描位置减1，下次扫描从第二个界符开始
				String firstDelimiter = curWordText.substring(0, 1);
				if(SINGLE_DELIMITERS.contains(firstDelimiter)) {
					curToken = TokenBuilder.getBuilder().line(curLine).column(wordStartColumn)
									.text(firstDelimiter).buildDelimiter();
					nextScanColumn--;
				} else
					throw new LexicalException("Invalid delimiter.", curLine, wordStartColumn);
			}
			break;
		case DATE_END:
			Calendar date = null;
			DateFormat dateFormate;
			try {
				if(curWordText.matches(DATE_PATTERN)) {
					//日期格式yyyy-MM-dd
					dateFormate = new SimpleDateFormat("[yyyy-MM-dd]");
					date = Calendar.getInstance();
					date.setTime(dateFormate.parse(curWordText));
				} else if(curWordText.matches(ACCURATE_DATE_PATTERN)) {
					//日期格式yyyy-MM-dd HH:mm:ss
					dateFormate = new SimpleDateFormat("[yyyy-MM-dd HH:mm:ss]");
					date = Calendar.getInstance();
					date.setTime(dateFormate.parse(curWordText));
				} else {
					throw new LexicalException("Wrong date format, please input as [yyyy-MM-dd] or [yyyy-MM-dd HH:mm:ss].",
										curLine, wordStartColumn);
				}
				if(date != null)
					curToken = TokenBuilder.getBuilder().line(curLine).column(wordStartColumn)
									.text(curWordText).dataType(DataType.DATE)
									.value(date).buildConst();
			} catch (ParseException e) {
				throw new LexicalException("Wrong date format, please input as [yyyy-MM-dd] or [yyyy-MM-dd HH:mm:ss].",
						curLine, wordStartColumn);
			}
			break;
		case CHAR_END:
			char ch;
			if(curWordText.length() == 3)
				ch = curWordText.toCharArray()[1];
			else	//识别转义字符
				ch = ExpressionUtil.getEscapedChar(curWordText.toCharArray()[2]);
			curToken = TokenBuilder.getBuilder().line(curLine).column(wordStartColumn)
							.text(curWordText).dataType(DataType.CHARACTER)
							.value(ch).buildConst();
			break;
		case STRING_END:
			String str = curWordText.substring(1, curWordText.length()-1);
			str = ExpressionUtil.transformEscapesInString(str);
			curToken = TokenBuilder.getBuilder().line(curLine).column(wordStartColumn)
							.text(curWordText).dataType(DataType.STRING)
							.value(str).buildConst();
			break;
		}
		return curToken;
	}
	
	/**
	 * 判断新识别出的token是否是被赋值的变量
	 */
	private void checkVariableToBeAssigned(List<TerminalToken> tokens) {
		int size = tokens.size();
		if(size < 2)
			return;
		TerminalToken first = tokens.get(size-2);
		TerminalToken second = tokens.get(size - 1);
		if(!second.equalsInGrammar(ASSIGN_TOKEN))
			return;
		if(first instanceof VariableToken)
			((VariableToken)first).setToBeAssigned(true);
	}
	
	private int escapeBlank(char[] curLineCharArray) {
		while(nextScanColumn < curLineCharArray.length 
				&& ((Character)curLineCharArray[nextScanColumn]).toString()
				.matches(BLANK_PATTERN) ) 
			nextScanColumn++;
			
		return nextScanColumn;
	}
	
	/**
	 * @return 下一行，去掉注释
	 */
	private String nextLine(Scanner scanner){
		String nextLine = scanner.nextLine();
		//仅支持行注释，注释以##开头
		if(nextLine.indexOf("##") < 0)
			return nextLine;
		Pattern commentPattern = Pattern.compile("##.*");
		Matcher matcher = commentPattern.matcher(nextLine);
		return matcher.replaceFirst("");
	}
	
	/**
	 * 判断函数是否存在
	 * @param functionName
	 * @return
	 */
	private boolean hasFunction(String functionName) {
		return hasCustomizedFunction(functionName)
				|| SystemFunctions.hasFunction(functionName);
	}
	
	/**
	 * 查找函数，先判断函数是否为自定义函数，如果不是再判断是否为系统函数
	 * @param functionName
	 * @return
	 */
	private Function findFunction(String functionName) {
		if(hasCustomizedFunction(functionName))
			return functionDefinitions.get(functionName);
		else
			return SystemFunctions.getFunction(functionName);
	}
	
	/**
	 * 判断函数是否为自定义函数
	 * @param functionName
	 * @return
	 */
	private boolean hasCustomizedFunction(String functionName) {
		if(functionDefinitions == null || functionDefinitions.size() == 0)
			return false;
		return functionDefinitions.keySet().contains(functionName);
	}
}
