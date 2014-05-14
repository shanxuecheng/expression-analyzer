package neu.sxc.expression.tokens;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 非终结符
 * @author shanxuecheng
 *
 */
public final class NonterminalToken implements Token {
	
	private Map<TerminalToken[], Token[]> productions = new HashMap<TerminalToken[], Token[]>();
	
	/**
	 * 添加产生式
	 * @param selectCollection 选择集
	 * @param production 产生式
	 */
	public void addProduction(TerminalToken[] selectCollection, Token[] production) {
		productions.put(selectCollection, production);
	}
	
	/**
	 * 根据输入的终结符查找产生式
	 * @param target 输入的终结符
	 * @return
	 */
	public Token[] getProduction(TerminalToken target) {
		//本非终结符所有产生式的选择集
		Set<TerminalToken[]> selectCollections = productions.keySet();
		for(TerminalToken[] selectCollection : selectCollections) {
			//如果输入的终结符存在于某一选择集，则返回该选择集对应的产生式
			for(TerminalToken token : selectCollection)
				if(token.equalsInGrammar(target))
					return productions.get(selectCollection);
		}
		return null;
	}
	
	public TokenType getTokenType() {
		return TokenType.NT;
	}
	
}
