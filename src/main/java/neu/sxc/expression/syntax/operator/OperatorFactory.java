package neu.sxc.expression.syntax.operator;

import java.util.HashMap;
import java.util.Map;

public class OperatorFactory {

	private static Map<String, Operator> operators = new HashMap<String, Operator>();

	private static void addOperator(Operator operator) {
		operators.put(operator.getOperatorName(), operator);
	}

	public static Operator getOperator(String name) {
		return operators.get(name);
	}

	static {
		addOperator(new AddOperator());
		addOperator(new MinusOperator());
		addOperator(new MultiplyOperator());
		addOperator(new DivideOperator());
		addOperator(new NegativeOperator());
		addOperator(new ModOperator());

		addOperator(new AndOperator());
		addOperator(new OrOperator());
		addOperator(new NotOperator());

		addOperator(new GreatOperator());
		addOperator(new GreatEOperator());
		addOperator(new LessOperator());
		addOperator(new LessEOperator());
		addOperator(new EqualOperator());
		addOperator(new NotEqualOperator());

		addOperator(new AssignOperator());
	}
}
