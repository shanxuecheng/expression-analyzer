package neu.sxc.expression;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import neu.sxc.expression.lexical.LexicalAnalyzer;
import neu.sxc.expression.lexical.LexicalException;
import neu.sxc.expression.syntax.SyntaxAnalyzer;
import neu.sxc.expression.syntax.SyntaxException;
import neu.sxc.expression.syntax.function.Function;
import neu.sxc.expression.tokens.TerminalToken;
import neu.sxc.expression.tokens.TokenBuilder;
import neu.sxc.expression.tokens.TokenType;
import neu.sxc.expression.tokens.Valuable;

/**
 * 表达式
 * 
 * @author shanxuecheng
 * 调用addFunction、removeFunction后，为更新词法分析结果，需要重新调用lexicalAnalysis()，
 * 或者直接调用reParseAndEvaluate()，重新执行词法分析并计算结果
 */
public class Expression {
	
	private String expression;
	
	/**
	 * Token序列
	 */
	private List<TerminalToken> tokens;
	
	/**
	 * 变量初始值
	 */
	private Map<String, Valuable> variableInitialValues = new HashMap<String, Valuable>();
	
	/**
	 * 表达式执行后变量的值
	 */
	private Map<String, Valuable> variableResult = new HashMap<String, Valuable>();
	
	/**
	 * 函数名及其对应的函数定义
	 */
	private Map<String, Function> functionDefinitions = new HashMap<String, Function>();
	
	/**
	 * 执行结果
	 */
	private Valuable finalResult;
	
	/**
	 * 词法分析器
	 */
	private LexicalAnalyzer lexicalAnalyzer = new LexicalAnalyzer();
	
	/**
	 * 语法分析器
	 */
	private SyntaxAnalyzer syntaxAnalyzer= new SyntaxAnalyzer();
	
	/**
	 * 除法运算默认采用的scale
	 */
	public static int DEFAULT_DIVISION_SCALE = 16;
	
	/**
	 * 除法运算默认使用的舍入方式
	 */
	public static RoundingMode DEFAULT_DIVISION_ROUNDING_MODE = RoundingMode.HALF_UP;
	
	public Expression() {}
	
	public Expression(String expression) {
		setExpression(expression);
	}
	
	public Expression(InputStream source) throws IOException {
		setExpression(source);
	}
	
	public Expression(Reader source) throws IOException {
		setExpression(source);
	}
	
	public void setExpression(String expression) {
		this.expression = expression;
	}
	
	public void setExpression(InputStream source) throws IOException {
		StringBuilder sb = new StringBuilder();
	    try {
	      int c;
	      while ((c = source.read()) != -1)
	        sb.append((char)c);
	      setExpression(sb.toString());
	    } finally {
	    	source.close();
	    }
	}
	
	public void setExpression(Reader source) throws IOException {
		StringBuilder sb = new StringBuilder();
	    try {
	      int c;
	      while ((c = source.read()) != -1)
	        sb.append((char)c);
	      setExpression(sb.toString());
	    } finally {
	    	source.close();
	    }
	}
	
	public String getExpression() {
		return expression;
	}
	
	public List<TerminalToken> getTokens() {
		return tokens;
	}
	
	/**
	 * 获取所有变量名，调用此方法的前提是已进行词法分析
	 * @return
	 * @throws LexicalException
	 */
	public Set<String> getVariableNames() throws LexicalException {
		if(this.tokens == null) 
			throw new RuntimeException("The 'tokens' is null, Please go for lexical analysis by invoking 'lexicalAnalysis()' first.");
		Set<String> variableNames = new HashSet<String>();
		for(TerminalToken terminalToken : tokens)
			if(terminalToken.getTokenType() == TokenType.VARIABLE)
				variableNames.add(terminalToken.getText());
		return variableNames;
	}
	
	/**
	 * 初始化变量
	 * @param name
	 * @param value
	 */
	public void initVariable(String name, Object value) {
		variableInitialValues.put(name, TokenBuilder.buildRuntimeValue(value));
	}
	
	/**
	 * 获取表达式执行之后变量值
	 * @param name 变量名
	 * @return 变量值
	 */
	public Valuable getVariableValueAfterEvaluate(String name) {
		return variableResult.get(name);
	}
	
	/**
	 * 获取表达式执行之后所有变量值
	 * @return 所有变量值
	 */
	public Map<String, Valuable> getAllVariableValueAfterEvaluate() {
		return variableResult;
	}
	
	/**
	 * 新增函数定义
	 * @param function
	 */
	public void addFunction(Function function) {
		function.checkFunctionDefinition();
		functionDefinitions.put(function.getName(), function);
	}
	
	/**
	 * 获取函数定义
	 * @param functionName
	 * @return
	 */
	public Function getFunction(String functionName) {
		return functionDefinitions.get(functionName);
	}
	
	/**
	 * 获取所有函数定义
	 * @return
	 */
	public Map<String, Function> getFunctionDefinitions() {
		return functionDefinitions;
	}
	
	/**
	 * 删除函数
	 * @param functionName
	 */
	public void removeFunction(String functionName) {
		functionDefinitions.remove(functionName);
	}
	
	public Valuable getFinalResult() {
		return finalResult;
	}
	
	/**
	 * 词法分析，初始化Token序列
	 * @throws LexicalException
	 * @return token序列
	 */
	public List<TerminalToken> lexicalAnalysis() throws LexicalException {
		tokens = lexicalAnalyzer.analysis(expression, functionDefinitions);
		return tokens;
	}
	
	/**
	 * 解析表达式，调用此方法的前提是已进行词法分析
	 * @return 解析结果
	 * @throws SyntaxException 语法错误异常
	 */
	public Valuable evaluate() throws SyntaxException {
		if(this.tokens == null) 
			throw new RuntimeException("The 'tokens' is null, Please go for lexical analysis by invoking 'lexicalAnalysis()' first.");
		
		//语法分析，返回最终结果
		finalResult = syntaxAnalyzer.analysis(tokens, variableInitialValues);
		//设置执行之后变量值
		variableResult = syntaxAnalyzer.getVariableTable();
		return finalResult;
	}
	
	/**
	 * 解析表达式，先执行词法分析，然后计算表达式
	 * @return 解析结果
	 * @throws LexicalException 词法错误异常
	 * @throws SyntaxException 语法错误异常
	 */
	public Valuable reParseAndEvaluate() throws LexicalException, SyntaxException {
		lexicalAnalysis();
		return evaluate();
	}
	
	/**
	 * 重置表达式
	 */
	public void clear() {
		tokens = null;
		finalResult = null;
		variableInitialValues.clear();
		variableResult.clear();
		functionDefinitions.clear();
	}
	
	/**
	 * 清除词法分析结果
	 */
	public void clearTokens() {
		tokens = null;
	}
	
	/**
	 * 清除所有变量初始值
	 */
	public void clearVariableInitialValues() {
		variableInitialValues.clear();
	}
	
	/**
	 * 清除所有函数定义
	 */
	public void clearFunctionDefinitions() {
		functionDefinitions.clear();
	}
}