package neu.sxc.expression.syntax.function;

import neu.sxc.expression.syntax.ArgumentsMismatchException;
import neu.sxc.expression.syntax.Executable;
import neu.sxc.expression.tokens.DataType;
import neu.sxc.expression.tokens.TokenBuilder;
import neu.sxc.expression.tokens.Valuable;

/**
 * 函数定义抽象类
 * @author shanxuecheng
 *
 */
public abstract class Function implements Executable{

	/**
	 * 函数名
	 */
	private final String functionName = getName();
	
	/**
	 * 参数类型数组
	 */
	private final DataType[] argumentsDataType = getArgumentsDataType() == null ? new DataType[0] : getArgumentsDataType();
	
	/**
	 * 返回函数名
	 * @return
	 */
	public abstract String getName();
	
	/**
	 * 返回函数参数类型数组，当参数个数不限时，所有参数类型必须相同，本方法须提供一个参数类型
	 * @return
	 */
	public abstract DataType[] getArgumentsDataType();
	
	/**
	 * 返回函数参数个数，参数个数小于0时，表示参数个数不限
	 */
	public abstract int getArgumentNum();
	
	/**
	 * 执行函数
	 */
	public final Valuable execute(Valuable[] arguments) throws ArgumentsMismatchException {
		if(getArgumentNum() < 0) {	//可变参数
			//检查参数类型是否一致
			for(Valuable argument : arguments) {
				if(argumentsDataType[0] == DataType.ANY)
					break;
				
				if(argument.getDataType() != argumentsDataType[0])
					throw new ArgumentsMismatchException(arguments, toString());
			}
		} else if(getArgumentNum() == arguments.length) {
			int argumentNum = getArgumentNum(); 
			for(int i=0; i<argumentNum; i++) {
				if(argumentsDataType[i] == DataType.ANY) {
					continue;
				} else if (argumentsDataType[i] != arguments[i].getDataType()){
					throw new ArgumentsMismatchException(arguments, toString());
				}
			}
		} else {
			throw new ArgumentsMismatchException(arguments, toString());
		}
		//执行函数
		Object result = executeFunction(arguments);
		return TokenBuilder.buildRuntimeValue(result);
	}

	/**
	 * 函数执行逻辑
	 * @param arguments
	 * @return
	 */
	protected abstract Object executeFunction(Valuable[] arguments);
	
	/**
	 * 检查函数定义
	 */
	public final void checkFunctionDefinition() {
		if(functionName == null || "".equals(functionName))
			throw new RuntimeException("Function name can not be empty.");
		
		if(getArgumentNum() >= 0) {
			if(argumentsDataType.length != getArgumentNum()) {
				throw new RuntimeException("Function definition error:" + getName() + ".");
			}
		} else {
			//参数个数小于0，即个数不限时，需要提供一个参数类型，所有参数类型必须相同
			if(argumentsDataType.length != 1) {
				throw new RuntimeException("Function definition error:" + getName() + ".");
			}
		}
	}
	
	@Override
	public final String toString() {
		StringBuilder signature = new StringBuilder();
		signature.append(functionName).append('(');
		if(getArgumentNum() >= 0) {
			int argumentNum = getArgumentNum();
			for(int i=0; i<argumentNum; i++) {
				if(i == argumentNum-1)
					signature.append(argumentsDataType[i].name());
				else
					signature.append(argumentsDataType[i].name()).append(',');
			}
		} else {
			signature.append(argumentsDataType[0].name()).append("...");
		}
		signature.append(')');
		return signature.toString();
	}

}
