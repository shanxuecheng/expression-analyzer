package neu.sxc.expression.lexical.dfa;

import java.util.HashMap;
import java.util.Map;

/**
 * 有限自动机的中间状态（包括开始状态）
 * @author shanxuecheng
 *
 */
public class DFAMidState {
	/**
	 * 中间状态代码
	 */
	private DFAMidStateCode midStateCode = null;
	
	/**
	 * 状态转换表，定义了路径与其对应的下一个中间状态的映射，路径使用正则表达式定义
	 */
	private Map<String,DFAMidState> nextMidStateMap = new HashMap<String,DFAMidState>();
	
	/**
	 * 本中间状态到结束状态的路径
	 */
	private RouteToEndState routeToEndState;
	
	/**
	 * 根据输入字符无法找到下一个状态时的错误信息，即词法错误信息
	 */
	private String errorMessage = "Lexical error.";
	
	public DFAMidState(DFAMidStateCode midStateCode) {
		this.midStateCode = midStateCode;
	}
	
	public void setNextMidState(String pattern,DFAMidState nextState) {
		nextMidStateMap.put(pattern, nextState);
	}
	
	/**
	 * 根据输入字符获取下一中间状态，如果不匹配，放回null
	 * @param inputChar
	 * @return
	 */
	public DFAMidState getNextMidState(Character inputChar) {
		DFAMidState nextState = null;
		for(String pattern : nextMidStateMap.keySet()){
			if(inputChar.toString().matches(pattern)){
				nextState = nextMidStateMap.get(pattern);
				break;
			}
		}
		return nextState;
	}
	
	public void setRouteToEndState(String route, DFAEndStateCode endStateCode) {
		routeToEndState = new RouteToEndState(route, endStateCode);
	}
	
	/**
	 * 根据输入字符获取结束状态代码
	 * @param inputChar
	 * @return
	 */
	public DFAEndStateCode goToEndStateWithInput(Character inputChar) {
		if(routeToEndState == null)
			return null;
		return routeToEndState.goToEndStateWithInput(inputChar);
	}
	
	/**
	 * 取得与本中间状态存在路径的结束状态代码，
	 * 如果不存在指向结束状态的路径，返回null
	 * @return
	 */
	public DFAEndStateCode getNextEndStateCode() {
		if(routeToEndState == null)
			return null;
		return routeToEndState.getEndStateCode();
	}
	
	public boolean hasRouteToEndState() {
		return routeToEndState != null;
	}
	
	public DFAMidStateCode getMidStateCode() {
		return midStateCode;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
}
