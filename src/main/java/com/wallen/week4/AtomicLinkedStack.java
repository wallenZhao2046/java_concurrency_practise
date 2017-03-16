package com.wallen.week4;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

public class AtomicLinkedStack<E> {
	
	public static void main(String[] args){
		final AtomicLinkedStack<Integer> stack = new AtomicLinkedStack<Integer>();
		
		final Set<Integer> set = new HashSet<Integer>();
		
		for(int i = 0; i < 100; i++){
			final int value = i;
			new Thread(){
				public void run(){
					stack.push(new Integer(value));
				}
			}.start();
			
		}
		
		for(int i = 0; i < 100; i++){
			new Thread(){
				public void run(){
					Integer item = stack.pop();
					System.out.println(item);
					if(!set.contains(item)){
						set.add(item);
					}else{
						System.out.println("duplicate item!!!!!!!!");
					}
				}
			}.start();
		}
	}
	
	private AtomicReference<Node<E>> top = new AtomicReference<Node<E>>();
	
	public void push(E item){
		Node<E> topItem;
		Node<E> newItem = new Node<E>(item);
		for(;;){
			
			topItem = top.get();
			newItem.setNext(topItem);
			if(top.compareAndSet(topItem, newItem)){
				break;
			}
		}
	}
	
	public E pop(){
		Node<E> nextItem ;
		Node<E> topItem;
		for(;;){
			topItem = top.get();
			nextItem = topItem.next();
			if(top.compareAndSet(topItem, nextItem)){
				break;
			}
		}
		return topItem.getData();
		
	}
	
	
}

