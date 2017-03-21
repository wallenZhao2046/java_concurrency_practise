package com.wallen.atomic;

public class Node<E>{
	private E data;
	private Node<E> next;
	
	public Node(E item){
		this.data = item;
	}
	
	public Node<E> next(){
		return next;
	}
	
	public void setNext(Node<E> item){
		next = item;
	}
	
	public E getData(){
		return data;
	}
	
	public void setData(E data){
		this.data = data;
	}
}
