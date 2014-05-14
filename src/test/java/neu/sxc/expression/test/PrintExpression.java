package neu.sxc.expression.test;

import java.util.List;
import java.util.Map.Entry;

import neu.sxc.expression.Expression;
import neu.sxc.expression.tokens.RuntimeValue;
import neu.sxc.expression.tokens.TerminalToken;
import neu.sxc.expression.tokens.Token;
import neu.sxc.expression.tokens.Valuable;
import neu.sxc.expression.tokens.ValueToken;


public class PrintExpression {
	public static void printExp(Expression exp) {
		
		printTokens(exp.getTokens());
		
		if(exp.getFinalResult() != null) {
			Printer.println("----------Final Result-----------");
			printToken(exp.getFinalResult());
		}
		
		if(exp.getAllVariableValueAfterEvaluate().size()>0) {
			Printer.println("----------Variable Values-----------");
			for(Entry<String, Valuable> variable : exp.getAllVariableValueAfterEvaluate().entrySet()) {
				Printer.print(variable.getKey() + ":");
				printToken(variable.getValue());
			}
		}
	}
	
	public static void printTokens(List<TerminalToken> tokens) {
		if(tokens.size() > 0) {
			Printer.println("----------Tokens-----------");
			for(Token token : tokens)
				printToken(token);
		}
	}
	
	private static void printToken(Token token) {
		Printer.print("[" + token.getTokenType().name() + "]");
		switch(token.getTokenType()){
		case CONST:
			ValueToken constToken = (ValueToken)token;
			Printer.print(", DateType:" + constToken.getDataType().name());
			Printer.println(", line:" + constToken.getLine() + ", column:" + constToken.getColumn() + ", text: " + constToken.getText()
					+ ", value: " + constToken.getValue());
			break;
		case VARIABLE:
			ValueToken variableToken = (ValueToken)token;
			if(variableToken.getDataType() != null)
				Printer.print(", DateType:" + variableToken.getDataType().name());
			Printer.print(", line:" + variableToken.getLine() + ", column:" + variableToken.getColumn() + ", text: " + variableToken.getText());
			if(variableToken.getValue() != null)
				Printer.println(", index: " + variableToken.getValue());
			else
				Printer.println();
			break;
		case RUNTIME_VALUE:
			RuntimeValue resultToken = (RuntimeValue)token;
			Printer.print(", DateType:" + resultToken.getDataType().name());
			Printer.print(", value: " + resultToken.getValue());
			Printer.println();
			break;
		default:
			TerminalToken terminaltoken = (TerminalToken)token;
			Printer.println(", line:" + terminaltoken.getLine() + ", column:" + terminaltoken.getColumn() + ", text: " + terminaltoken.getText());
			break;
		}
	}
}
