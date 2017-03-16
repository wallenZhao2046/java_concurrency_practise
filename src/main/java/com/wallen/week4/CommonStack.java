package com.wallen.week4;

public class CommonStack<E> {
	
	public static void main(String[] args){
		CommonStack<Integer> stack = new CommonStack<Integer>();
		for(int i = 0; i < 10; i++){
			stack.push(new Integer(i));
		}
		
		for(int i = 0; i < 20; i++){
			System.out.println(stack.pop());
		}
	}
	private Node<E> top;
	
	public E pop(){
		if (top == null){
			return null;
		}
		Node<E> result = top;
		top = top.next();
		return result.getData();
		
	}
	
	public void push(E data){
		Node<E> item = new Node<E>(data);
		if (top == null){
			top = item;
		}else{
			item.setNext(top);
			top = item;
		}
	}

}


