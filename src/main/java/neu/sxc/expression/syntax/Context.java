package neu.sxc.expression.syntax;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import neu.sxc.expression.tokens.Valuable;

/**
 * 上下文
 * 
 * @author shanxuecheng
 * 
 */
public class Context {

	/**
	 * 有效标志（对应上下文所在分支结构的条件）
	 */
	private boolean effective;

	/**
	 * 上下文中的变量和其对应的值
	 */
	private Map<String, Valuable> variableTable = new HashMap<String, Valuable>();

	/**
	 * 上下文的开始位置
	 */
	private int semanticStackStartIndex = -1;

	/**
	 * 函数栈开始位置
	 */
	private int functionStackStartIndex;

	/**
	 * 操作符栈开始位置
	 */
	private int operatorStackStartIndex;

	public Context(boolean effective, Map<String, Valuable> variableTable,
			int semanticStackStartIndex, int functionStackStartIndex,
			int operatorStackStartIndex) {
		this.effective = effective;
		if (variableTable == null)
			variableTable = new HashMap<String, Valuable>();
		this.variableTable.putAll(variableTable);
		this.semanticStackStartIndex = semanticStackStartIndex;
		this.functionStackStartIndex = functionStackStartIndex;
		this.operatorStackStartIndex = operatorStackStartIndex;
	}

	public boolean isEffective() {
		return effective;
	}

	public Map<String, Valuable> getVariableTable() {
		return this.variableTable;
	}

	public int getSemanticStackStartIndex() {
		return semanticStackStartIndex;
	}

	public Valuable getVariableValue(String variableName) {
		return variableTable.get(variableName);
	}

	public void setVariableValue(String variableName, Valuable value) {
		variableTable.put(variableName, value);
	}

	/**
	 * 基于本上下文创建新上下文
	 * @param effective
	 * @param semanticStackStartIndex
	 * @param functionStackStartIndex
	 * @param operatorStackStartIndex
	 * @return
	 */
	public Context constructUpon(boolean effective,
			int semanticStackStartIndex, int functionStackStartIndex,
			int operatorStackStartIndex) {
		// 将本上下文的变量全部复制到新上下文
		return new Context(effective, variableTable, semanticStackStartIndex,
				functionStackStartIndex, operatorStackStartIndex);
	}

	/**
	 * 更新本上下文
	 * 
	 * @param context
	 */
	public void update(Context context) {
		Set<String> variableNames = variableTable.keySet();
		Set<String> targetVariableNames = context.getVariableTable().keySet();
		// 如果参数context中的某变量在本上下文中也有定义，则将该变量的值覆盖到本上下文中
		for (String variableName : variableNames)
			if (targetVariableNames.contains(variableName))
				setVariableValue(variableName,
						context.getVariableValue(variableName));
	}

	public int getFunctionStackStartIndex() {
		return functionStackStartIndex;
	}

	public int getOperatorStackStartIndex() {
		return operatorStackStartIndex;
	}
}
