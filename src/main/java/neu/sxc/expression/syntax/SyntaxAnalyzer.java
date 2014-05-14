package neu.sxc.expression.syntax;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import neu.sxc.expression.lexical.LexicalConstants;
import neu.sxc.expression.syntax.function.Function;
import neu.sxc.expression.syntax.operator.AssignOperator;
import neu.sxc.expression.syntax.operator.Operator;
import neu.sxc.expression.tokens.ConstToken;
import neu.sxc.expression.tokens.ContextOperationToken;
import neu.sxc.expression.tokens.DataType;
import neu.sxc.expression.tokens.DelimiterToken;
import neu.sxc.expression.tokens.ExecutionToken;
import neu.sxc.expression.tokens.FunctionToken;
import neu.sxc.expression.tokens.NonterminalToken;
import neu.sxc.expression.tokens.TerminalToken;
import neu.sxc.expression.tokens.Token;
import neu.sxc.expression.tokens.TokenType;
import neu.sxc.expression.tokens.Valuable;
import neu.sxc.expression.tokens.VariableToken;
import neu.sxc.expression.utils.Stack;

/**
 * 语法分析
 * @author shanxuecheng
 *
 */
public class SyntaxAnalyzer {
	/**
	 * 文法
	 */
	private Grammar grammar = Grammar.getGrammar();
	
	/**
	 * 最终结果
	 */
	private Valuable finalResult;
	
	/**
	 * 语法栈
	 */
	private Stack<Token> syntaxStack = new Stack<Token>();
	
	/**
	 * 语义栈
	 */
	private Stack<Valuable> semanticStack = new Stack<Valuable>();
	
	/**
	 * 操作符栈
	 */
	private Stack<DelimiterToken> operatorTokenStack = new Stack<DelimiterToken>();
	
	/**
	 * 函数符号栈
	 */
	private Stack<FunctionToken> functionTokenStack = new Stack<FunctionToken>();
	
	/**
	 * 用于记录函数参数在语义栈中的开始位置
	 */
	private Stack<Integer> argumentStartIndexStack = new Stack<Integer>();
	
	/**
	 * 上下文栈
	 */
	private Stack<Context> contextStack = new Stack<Context>();
	
	/**
	 * 用于压入if-else语句各分支的条件的栈
	 */
	private Stack<Boolean> conditionStack = new Stack<Boolean>();
	
	public SyntaxAnalyzer() {}
	
	/**
	 * 获取所有参数
	 * @return
	 */
	public Map<String, Valuable> getVariableTable() {
		if(contextStack.isEmpty())
			return new HashMap<String, Valuable>();
		return contextStack.top().getVariableTable();
	}
	
	/**
	 * 解析表达式
	 * @param tokens Token序列
	 * @return
	 * @throws SyntaxException
	 */
	public Valuable analysis(List<TerminalToken> tokens) throws SyntaxException {
		return analysis(tokens, null);
	}
	
	/**
	 * 解析表达式
	 * @param tokens Token序列
	 * @param variableTable 初始变量值
	 * @return
	 * @throws SyntaxException
	 */
	public Valuable analysis(List<TerminalToken> tokens, Map<String, Valuable> variableInitialValues)
				throws SyntaxException {
		this.finalResult = null;
		
		//构造初始上下文，并压入上下文栈
		conditionStack.push(true);
		contextStack.push(new Context(true, variableInitialValues, 0, 0, 0));
		
		int index = 0;
		while(index < tokens.size()) {
			//一条语句解析结束时，返回下一语句的开始位置
			index = analysisSentence(tokens, index);
		}
		
		return finalResult;
	}
	
	/**
	 * 解析一条语句，解析成功后返回下一语句的开始位置
	 * @param tokens
	 * @param index
	 * @return
	 * @throws SyntaxException
	 */
	private int analysisSentence(List<TerminalToken> tokens, int index)
				throws SyntaxException {
		clearStacks();
		syntaxStack.push(grammar.getStart());//压入文法开始符号，开始解析一条语句
		TerminalToken currentToken = tokens.get(index++);
		Token syntaxStackTop = null;
		while(!syntaxStack.isEmpty()) {//栈空时一条语句分析结束
			syntaxStackTop = syntaxStack.pop();
			switch(syntaxStackTop.getTokenType()) {
			case NT: //语法栈顶为非终结符时，查找产生式
				Token[] production = ((NonterminalToken)syntaxStackTop).getProduction(currentToken);
				if(production != null)
					reverseProductionIntoSyntaxStack(production);
				else //找不到对应的产生式，存在语法错误
					throw new SyntaxException(currentToken);
				break;
			case EXECUTION:
				if(conditionStack.top()) {
					Executable executable = ((ExecutionToken)syntaxStackTop).getExecutable();
					if(executable == null) //需要执行的是函数，从函数符号栈取出函数定义
						executable = functionTokenStack.top().getFunction();
					execute(executable);
				}
				break;
			case CONTEXT_OPERATION:
				try {
					//上下文操作
					contextOperate((ContextOperationToken)syntaxStackTop);
				} catch (SyntaxException e) {
					throw new SyntaxException(e.getMessage(), currentToken, e);
				}
				break;
			default: //语法栈顶为终结符，检查是否匹配
				if(currentToken.equalsInGrammar((TerminalToken)syntaxStackTop)) {
					dealTerminalToken(currentToken);
					//语法栈不空，取后续Token继续解析
					if(!syntaxStack.isEmpty()) {
						if(index < tokens.size())
							currentToken = tokens.get(index++);
						else 	//没有后续Token，说明语句未正确结束
							throw new SyntaxException("Sentence is not properly over at line:"
									+ currentToken.getLine() + ".");
					}
				} else	//终结符未匹配，存在语法错误
					throw new SyntaxException(currentToken);
				break;
			}
		}
		
		if(!semanticStack.isEmpty())
			finalResult = semanticStack.pop();
		
		return index;
	}
	
	private void dealTerminalToken(TerminalToken currentToken) {
		switch(currentToken.getTokenType()) {
		case CONST:	//常量压入语义栈
			semanticStack.push((ConstToken)currentToken);
			break;
		case VARIABLE:	//变量设值后压入语义栈
			VariableToken variable = (VariableToken)currentToken;
			Valuable valueOfVariable = getVariableValue(variable.getText());
			if(valueOfVariable != null)
				variable.assignWith(valueOfVariable);
			semanticStack.push(variable);
			break;
		case DELIMITER:
			if(LexicalConstants.OPERATORS.contains(currentToken.getText()))
				operatorTokenStack.push((DelimiterToken)currentToken);
			break;
		case FUNCTION:
			functionTokenStack.push((FunctionToken)currentToken);	//压入函数栈
			argumentStartIndexStack.push(semanticStack.size());	//压入函数参数在语义栈中的起始位置
			break;
		default:
			break;
		}
	}
	
	/**
	 * 将产生式反序压入语法栈
	 * @param production
	 */
	private void reverseProductionIntoSyntaxStack(Token[] production) {
		if(production.length > 0)
			for(int i=production.length-1; i>=0; i--)
				syntaxStack.push(production[i]);
	}
	
	private void execute(Executable executable) {
		Valuable[] arguments = getArguments(executable);
		Valuable result = null;
		if(executable instanceof Operator)
			result = executeOperator((Operator)executable, arguments);
		else if(executable instanceof Function)
			result = executeFunction((Function)executable, arguments);
		//结果压入语义栈
		semanticStack.push(result);
	}
	
	/**
	 * 执行操作符
	 * @param operator 操作符定义
	 * @param arguments 参数
	 * @return
	 */
	private Valuable executeOperator(Operator operator, Valuable[] arguments) {
		//弹出操作符，如果发生错误，记录错误位置
		DelimiterToken operatorToken = operatorTokenStack.pop();
		try {
			Valuable result = operator.execute(arguments);
			//如果是赋值操作，则需要更新被赋值变量到variableTable
			if(operator instanceof AssignOperator){
				VariableToken variable = (VariableToken)arguments[0];
				setVariableValue(variable.getText(), result);
			}
			return result;
		} catch(ArgumentsMismatchException e) {
			throw new ArgumentsMismatchException(e.getMessage(), operatorToken, e);
		} catch(ArithmeticException e) {
			ArithmeticException arithmeticException = new ArithmeticException(e.getMessage()
					+ " At line:" + operatorToken.getLine() + ", column:" + operatorToken.getColumn() + ".");
			arithmeticException.initCause(e);
			throw arithmeticException;
		}
	}
	
	/**
	 * 执行函数
	 * @param function 函数定义
	 * @param arguments 参数
	 * @return
	 */
	private Valuable executeFunction(Function function, Valuable[] arguments) {
		//弹出函数符号，如果发生错误，记录错误位置
		FunctionToken functionToken = functionTokenStack.pop();
		try {
			Valuable result = function.execute(arguments);
			return result;
		} catch(ArgumentsMismatchException e) {
			throw new ArgumentsMismatchException(e.getMessage(), functionToken, e);
		}
	}

	/**
	 * 获取参数
	 * @param executable 操作
	 * @return 参数数组
	 */
	private Valuable[] getArguments(Executable executable) {
		int argumentNum = 0; //参数个数
		boolean isAssignOperator = false;//是否为赋值操作
		
		if(executable instanceof Operator) {
			argumentNum = ((Operator)executable).getArgumentNum();
			isAssignOperator = executable instanceof AssignOperator;
		} else if(executable instanceof Function) {
			//参数个数为当前语义栈大小减去参数起始位置
			argumentNum = semanticStack.size() - argumentStartIndexStack.pop();
		}
		Valuable[] arguments = new Valuable[argumentNum];
		for(int i=argumentNum-1; i>=0; i--) {
			arguments[i] = semanticStack.pop();
			if(arguments[i].getTokenType() == TokenType.VARIABLE) {
				//如果参数是变量，则检查变量是否已定义，赋值操作中被赋值变量除外
				if(isAssignOperator && i == 0)
					break;
				else if(arguments[i].getValue() == null) 
					throw new VariableNotInitializedException((VariableToken)arguments[i]);
			}
		}
		return arguments;
	}
	
	/**
	 * 上下文操作
	 * @param contextOperationToken
	 * @throws SyntaxException
	 */
	private void contextOperate(ContextOperationToken contextOperationToken) throws SyntaxException {
		switch(contextOperationToken.getContextOperation()) {
		case IF_CONDITION:
			//取if后的条件，并压入条件栈
			Valuable condition = semanticStack.pop();
			if(condition.getDataType() != DataType.BOOLEAN)
				throw new SyntaxException("Type mismatch: cannot convert from " +
						condition.getDataType().name() + " to BOOLEAN.");
			else
				conditionStack.push(condition.getBooleanValue());
			break;
		case ELSE_CONDITION:
			//设置else部分的条件，即从条件栈中弹出其对应的if部分的条件，取反重新压入
			conditionStack.push(!conditionStack.pop());
			break;
		case END_IF:
			//if语句结束，从条件栈中弹出条件
			conditionStack.pop();
			break;
		case NEW_CONTEXT:	//新建上下文
			//取条件栈顶作为新建上下文是否有效的标志
			boolean effective = conditionStack.top();
			//从上下文栈中取当前上下文
			Context currentContext = contextStack.top();
			//基于当前上下文创建新的上下文，并压入上下文栈
			contextStack.push(currentContext.constructUpon(effective, semanticStack.size(),
					functionTokenStack.size(), operatorTokenStack.size()));
			break;
		case END_CONTEXT:
			//上下文结束，从上下文栈弹出
			Context topContext = contextStack.pop();
			if(topContext.isEffective()) {
				//如果该上下文有效，即条件为真true，则将其更新到当前栈顶上下文
				contextStack.top().update(topContext);
			} else {
				//如果该上下文无效，即条件为false，清理栈
				clearStacksAfterInvalidContext(topContext);
			}
			break;
		}
	}
	
	private Valuable getVariableValue(String variableName) {
		Context currentContext = contextStack.top();
		return currentContext.getVariableValue(variableName);
	}
	
	private void setVariableValue(String text, Valuable value) {
		Context currentContext = contextStack.top();
		currentContext.setVariableValue(text, value);
	}
	
	private void clearStacksAfterInvalidContext(Context invalidContext) {
		//弹出语义栈在指定位置之后的所有元素
		while(semanticStack.size() > invalidContext.getSemanticStackStartIndex())
			semanticStack.pop();
		while(functionTokenStack.size() > invalidContext.getFunctionStackStartIndex()) {
			functionTokenStack.pop();
			argumentStartIndexStack.pop();
		}
		while(operatorTokenStack.size() > invalidContext.getOperatorStackStartIndex())
			operatorTokenStack.pop();
	}
	
	private void clearStacks() {
		syntaxStack.clear();
		semanticStack.clear();
		operatorTokenStack.clear();
		functionTokenStack.clear();
		argumentStartIndexStack.clear();
	}
}
