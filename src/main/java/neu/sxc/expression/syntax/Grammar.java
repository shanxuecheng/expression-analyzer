package neu.sxc.expression.syntax;

import neu.sxc.expression.syntax.operator.OperatorFactory;
import neu.sxc.expression.tokens.ConstToken;
import neu.sxc.expression.tokens.ContextOperation;
import neu.sxc.expression.tokens.ContextOperationToken;
import neu.sxc.expression.tokens.DelimiterToken;
import neu.sxc.expression.tokens.ExecutionToken;
import neu.sxc.expression.tokens.FunctionToken;
import neu.sxc.expression.tokens.KeyToken;
import neu.sxc.expression.tokens.NonterminalToken;
import neu.sxc.expression.tokens.TerminalToken;
import neu.sxc.expression.tokens.Token;
import neu.sxc.expression.tokens.TokenBuilder;
import neu.sxc.expression.tokens.VariableToken;

/**
 * 文法及文法符号初始化
 * @author shanxuecheng
 *
 */
public class Grammar {
	
	private Grammar() {
		start.addProduction(new TerminalToken[]{variableToBeAssigned, variable, constant, minusMark, leftBracket, function, notMark}, 
								new Token[]{sentence});
		start.addProduction(new TerminalToken[]{ifKey}, 
								new Token[]{ifStatement});
		
		ifStatement.addProduction(new TerminalToken[]{ifKey}, 
								new Token[]{ifKey, leftBracket, bolExpression, ifConditionCo, rightBracket,  
											newContextCo, block, endContextCo, elseSection, endIFCo, endIfKey});
		
		block.addProduction(new TerminalToken[]{variableToBeAssigned, variable, constant, minusMark, leftBracket, function, notMark}, 
								new Token[]{sentence, block});
		block.addProduction(new TerminalToken[]{ifKey}, 
								new Token[]{ifStatement, block});
		block.addProduction(new TerminalToken[]{elseKey, endIfKey}, 
								new Token[]{});
		
		elseSection.addProduction(new TerminalToken[]{elseKey}, 
								new Token[]{elseKey, elseConditionCo, newContextCo, block, endContextCo});
		elseSection.addProduction(new TerminalToken[]{endIfKey}, 
								new Token[]{});
		
		sentence.addProduction(new TerminalToken[]{variableToBeAssigned},
								new Token[]{variableToBeAssigned, assignMark, bolExpression, assignExe, semicolon});
		sentence.addProduction(new TerminalToken[]{variable, constant, minusMark, leftBracket, function, notMark}, 
								new Token[]{bolExpression, semicolon});
		
		bolExpression.addProduction(new TerminalToken[]{variable, constant, minusMark, leftBracket, function, notMark},
								new Token[]{bolTerm, _bolExpression});
		
		_bolExpression.addProduction(new TerminalToken[]{orMark},
								new Token[]{orMark, bolTerm, orExe, _bolExpression});
		_bolExpression.addProduction(new TerminalToken[]{rightBracket, comma, semicolon},
								new Token[]{});
		
		bolTerm.addProduction(new TerminalToken[]{variable, constant, minusMark, leftBracket, function, notMark},
								new Token[]{bolFactor, _bolTerm});
		
		_bolTerm.addProduction(new TerminalToken[]{andMark},
								new Token[]{andMark, bolFactor, andExe, _bolTerm});
		_bolTerm.addProduction(new TerminalToken[]{orMark, rightBracket, comma, semicolon},
								new Token[]{});
		
		bolFactor.addProduction(new TerminalToken[]{variable, constant, minusMark, leftBracket, function},
								new Token[]{compare});
		bolFactor.addProduction(new TerminalToken[]{notMark},
								new Token[]{notMark, bolFactor, notExe});
		
		compare.addProduction(new TerminalToken[]{variable, constant, minusMark, leftBracket, function},
								new Token[]{expression, _compare});
		
		_compare.addProduction(new TerminalToken[]{equalMark},
								new Token[]{equalMark, expression, equalExe});
		_compare.addProduction(new TerminalToken[]{notEMark},
								new Token[]{notEMark, expression, notEqualExe});
		_compare.addProduction(new TerminalToken[]{greatMark},
								new Token[]{greatMark, expression, greatExe});
		_compare.addProduction(new TerminalToken[]{greatEMark},
								new Token[]{greatEMark, expression, greatEExe});
		_compare.addProduction(new TerminalToken[]{lessMark},
								new Token[]{lessMark, expression, lessExe});
		_compare.addProduction(new TerminalToken[]{lessEMark},
								new Token[]{lessEMark, expression, lessEExe});
		_compare.addProduction(new TerminalToken[]{andMark, orMark, rightBracket, comma, semicolon},
								new Token[]{});
		
		expression.addProduction(new TerminalToken[]{variable, constant, minusMark, leftBracket, function},
								new Token[]{term, _expression});
		
		_expression.addProduction(new TerminalToken[]{addMark},
								new Token[]{addMark, term, addExe, _expression});
		_expression.addProduction(new TerminalToken[]{minusMark},
								new Token[]{minusMark, term, minusExe, _expression});
		_expression.addProduction(new TerminalToken[]{equalMark, notEMark, greatMark, greatEMark, lessMark, lessEMark, andMark, orMark, rightBracket, comma, semicolon},
								new Token[]{});
		
		term.addProduction(new TerminalToken[]{variable, constant, minusMark, leftBracket, function},
								new Token[]{factor, _term});
		
		_term.addProduction(new TerminalToken[]{multiplyMark},
								new Token[]{multiplyMark, factor, multiplyExe, _term});
		_term.addProduction(new TerminalToken[]{divideMark},
								new Token[]{divideMark, factor, divideExe, _term});
		_term.addProduction(new TerminalToken[]{modMark},
								new Token[]{modMark, factor, modExe, _term});
		_term.addProduction(new TerminalToken[]{addMark, minusMark, equalMark, notEMark, greatMark, greatEMark, lessMark, lessEMark, andMark, orMark, rightBracket, comma, semicolon},
								new Token[]{});
		
		factor.addProduction(new TerminalToken[]{variable},
								new Token[]{variable});
		factor.addProduction(new TerminalToken[]{constant},
								new Token[]{constant});
		factor.addProduction(new TerminalToken[]{minusMark},
								new Token[]{minusMark, factor, negativeExe});
		factor.addProduction(new TerminalToken[]{leftBracket},
								new Token[]{leftBracket, bolExpression, rightBracket});
		
		factor.addProduction(new TerminalToken[]{function}, 
								new Token[]{function, leftBracket, parameters, rightBracket, functionExe});
		
		parameters.addProduction(new TerminalToken[]{variable, constant, minusMark, leftBracket, function},
								new Token[]{bolExpression, _parameters});
		parameters.addProduction(new TerminalToken[]{rightBracket},
								new Token[]{});
		
		_parameters.addProduction(new TerminalToken[]{comma},
								new Token[]{comma, bolExpression, _parameters});
		_parameters.addProduction(new TerminalToken[]{rightBracket},
								new Token[]{});
	}
	
	public static Grammar getGrammar() {
		return new Grammar();
	}
	
	public NonterminalToken getStart() {
		return start;
	}
	
	public TerminalToken getGrammarEnd() {
		return semicolon;
	}
	
	public TerminalToken getAssignMark() {
		return assignMark;
	}
	
	//常量
	private ConstToken constant = TokenBuilder.getBuilder().buildConst();
	
	//变量
	private VariableToken variable = TokenBuilder.getBuilder().buildVariable();
	
	//要被赋值的变量
	private VariableToken variableToBeAssigned = TokenBuilder.getBuilder().toBeAssigned(true).buildVariable();
	
	//函数
	private FunctionToken function = TokenBuilder.getBuilder().buildFunction();
	
	//界符
	private DelimiterToken addMark =  TokenBuilder.getBuilder().text("+").buildDelimiter();
	private DelimiterToken minusMark = TokenBuilder.getBuilder().text("-").buildDelimiter();
	private DelimiterToken multiplyMark = TokenBuilder.getBuilder().text("*").buildDelimiter();
	private DelimiterToken divideMark = TokenBuilder.getBuilder().text("/").buildDelimiter();
	private DelimiterToken modMark = TokenBuilder.getBuilder().text("%").buildDelimiter();
	private DelimiterToken greatMark = TokenBuilder.getBuilder().text(">").buildDelimiter();
	private DelimiterToken greatEMark = TokenBuilder.getBuilder().text(">=").buildDelimiter();
	private DelimiterToken lessMark = TokenBuilder.getBuilder().text("<").buildDelimiter();
	private DelimiterToken lessEMark = TokenBuilder.getBuilder().text("<=").buildDelimiter();
	private DelimiterToken equalMark = TokenBuilder.getBuilder().text("==").buildDelimiter();
	private DelimiterToken notEMark = TokenBuilder.getBuilder().text("!=").buildDelimiter();
	private DelimiterToken andMark = TokenBuilder.getBuilder().text("&&").buildDelimiter();
	private DelimiterToken orMark = TokenBuilder.getBuilder().text("||").buildDelimiter();
	private DelimiterToken notMark = TokenBuilder.getBuilder().text("!").buildDelimiter();
	private DelimiterToken comma = TokenBuilder.getBuilder().text(",").buildDelimiter();
	private DelimiterToken semicolon = TokenBuilder.getBuilder().text(";").buildDelimiter();
	private DelimiterToken leftBracket = TokenBuilder.getBuilder().text("(").buildDelimiter();
	private DelimiterToken rightBracket = TokenBuilder.getBuilder().text(")").buildDelimiter();
	private DelimiterToken assignMark = TokenBuilder.getBuilder().text("=").buildDelimiter();
	
	//关键字
	private KeyToken ifKey = TokenBuilder.getBuilder().text("if").buildKey();
	private KeyToken elseKey = TokenBuilder.getBuilder().text("else").buildKey();
	private KeyToken endIfKey = TokenBuilder.getBuilder().text("endif").buildKey();
	
	//非终结符
	private NonterminalToken start = TokenBuilder.getBuilder().buildNT();
	private NonterminalToken ifStatement = TokenBuilder.getBuilder().buildNT();
	private NonterminalToken block = TokenBuilder.getBuilder().buildNT();
	private NonterminalToken elseSection = TokenBuilder.getBuilder().buildNT();
	private NonterminalToken sentence = TokenBuilder.getBuilder().buildNT();
	private NonterminalToken bolExpression = TokenBuilder.getBuilder().buildNT();
	private NonterminalToken _bolExpression = TokenBuilder.getBuilder().buildNT();
	private NonterminalToken bolTerm = TokenBuilder.getBuilder().buildNT();
	private NonterminalToken _bolTerm = TokenBuilder.getBuilder().buildNT();
	private NonterminalToken bolFactor = TokenBuilder.getBuilder().buildNT();
	private NonterminalToken compare = TokenBuilder.getBuilder().buildNT();
	private NonterminalToken _compare = TokenBuilder.getBuilder().buildNT();
	private NonterminalToken expression = TokenBuilder.getBuilder().buildNT();
	private NonterminalToken _expression = TokenBuilder.getBuilder().buildNT();
	private NonterminalToken term = TokenBuilder.getBuilder().buildNT();
	private NonterminalToken _term = TokenBuilder.getBuilder().buildNT();
	private NonterminalToken factor = TokenBuilder.getBuilder().buildNT();
	private NonterminalToken parameters = TokenBuilder.getBuilder().buildNT();
	private NonterminalToken _parameters = TokenBuilder.getBuilder().buildNT();
	
	//语义动作
	private ExecutionToken addExe = TokenBuilder.getBuilder().executable(OperatorFactory.getOperator("ADD")).buildExecution();
	private ExecutionToken minusExe = TokenBuilder.getBuilder().executable(OperatorFactory.getOperator("MINUS")).buildExecution();
	private ExecutionToken multiplyExe = TokenBuilder.getBuilder().executable(OperatorFactory.getOperator("MULTIPLY")).buildExecution();
	private ExecutionToken divideExe = TokenBuilder.getBuilder().executable(OperatorFactory.getOperator("DIVIDE")).buildExecution();
	private ExecutionToken modExe = TokenBuilder.getBuilder().executable(OperatorFactory.getOperator("MOD")).buildExecution();
	private ExecutionToken negativeExe = TokenBuilder.getBuilder().executable(OperatorFactory.getOperator("NEGATIVE")).buildExecution();
	private ExecutionToken andExe = TokenBuilder.getBuilder().executable(OperatorFactory.getOperator("AND")).buildExecution();
	private ExecutionToken orExe = TokenBuilder.getBuilder().executable(OperatorFactory.getOperator("OR")).buildExecution();
	private ExecutionToken notExe = TokenBuilder.getBuilder().executable(OperatorFactory.getOperator("NOT")).buildExecution();
	private ExecutionToken greatExe = TokenBuilder.getBuilder().executable(OperatorFactory.getOperator("GREAT")).buildExecution();
	private ExecutionToken greatEExe = TokenBuilder.getBuilder().executable(OperatorFactory.getOperator("GREATE")).buildExecution();
	private ExecutionToken lessExe = TokenBuilder.getBuilder().executable(OperatorFactory.getOperator("LESS")).buildExecution();
	private ExecutionToken lessEExe = TokenBuilder.getBuilder().executable(OperatorFactory.getOperator("LESSE")).buildExecution();
	private ExecutionToken equalExe = TokenBuilder.getBuilder().executable(OperatorFactory.getOperator("EQUAL")).buildExecution();
	private ExecutionToken notEqualExe = TokenBuilder.getBuilder().executable(OperatorFactory.getOperator("NOTEQUAL")).buildExecution();
	private ExecutionToken assignExe = TokenBuilder.getBuilder().executable(OperatorFactory.getOperator("ASSIGN")).buildExecution();
	private ExecutionToken functionExe = TokenBuilder.getBuilder().executable(null).buildExecution();//函数执行标志
	
	//上下文操作符号
	private ContextOperationToken ifConditionCo = TokenBuilder.getBuilder().contextOperation(ContextOperation.IF_CONDITION).buildContextOperation();
	private ContextOperationToken elseConditionCo = TokenBuilder.getBuilder().contextOperation(ContextOperation.ELSE_CONDITION).buildContextOperation();
	private ContextOperationToken newContextCo = TokenBuilder.getBuilder().contextOperation(ContextOperation.NEW_CONTEXT).buildContextOperation();
	private ContextOperationToken endContextCo = TokenBuilder.getBuilder().contextOperation(ContextOperation.END_CONTEXT).buildContextOperation();
	private ContextOperationToken endIFCo = TokenBuilder.getBuilder().contextOperation(ContextOperation.END_IF).buildContextOperation();
}