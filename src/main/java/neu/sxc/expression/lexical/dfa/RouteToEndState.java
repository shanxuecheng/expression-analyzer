package neu.sxc.expression.lexical.dfa;

/**
 * 指向结束状态的路径
 * @author shanxuecheng
 *
 */
public class RouteToEndState {
	/**
	 * 路径，使用正则表达式表示
	 */
	private String route;
	
	/**
	 * 结束状态代码
	 */
	private DFAEndStateCode endStateCode;
	
	public RouteToEndState(String route, DFAEndStateCode endStateCode) {
		this.route = route;
		this.endStateCode = endStateCode;
	}

	public DFAEndStateCode getEndStateCode() {
		return endStateCode;
	}
	
	public String getRoute() {
		return route;
	}
	
	/**
	 * 根据输入字符获取结束状态代码，如果与路径不匹配，返回null
	 * @param inputChar
	 * @return
	 */
	public DFAEndStateCode goToEndStateWithInput(Character inputChar) {
		if(inputChar.toString().matches(route))
			return endStateCode;
		return null;
	}
}
