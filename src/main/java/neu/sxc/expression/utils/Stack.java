package neu.sxc.expression.utils;

import java.util.*;

/**
 * 栈
 * @author shanxuecheng
 *
 * @param <T>
 */
public class Stack<T> {
	private ArrayList<T> arrayList = new ArrayList<T>();
	
	public void push(T e){
		arrayList.add(e);
	}
	
	public T pop(){
		if(isEmpty())
			return null;
		else
			return arrayList.remove(arrayList.size()-1);
	}
	
	public T top(){
		if(isEmpty())
			return null;
		else
			return arrayList.get(arrayList.size()-1);
	}
	
	public boolean isEmpty(){
		return arrayList.isEmpty();
	}
	
	public int size(){
		return arrayList.size();
	}
	
	public void clear() {
		arrayList.clear();
	}
}