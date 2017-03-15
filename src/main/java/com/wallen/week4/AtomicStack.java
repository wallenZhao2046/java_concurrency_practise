package com.wallen.week4;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicIntegerArray;
import java.util.concurrent.atomic.AtomicReferenceArray;

public class AtomicStack {
	
	public static void main(String[] args){
		AtomicStack s = new AtomicStack(8);
		for( int i = 0; i < 10; i++)
			s.push(i);
		for( int j = 0; j < 10 ; j++){
			System.out.print(s.pop());
		}
	}
	
	private int top = -1;
	private AtomicIntegerArray array;
	public AtomicStack(int defaultSize){
		array = new AtomicIntegerArray(defaultSize);
	}
	
	public void push(Integer item){
		if(top >= array.length() - 1){
			System.out.println("stack is full");
			return;
		}
		array.set(++top, item);
	}
	
	public Integer pop(){
		if(top > -1){
			return array.get(top--);
		}else{
			return null;
		}
		
	}
	
	public int length() {
		return array.length();
	}
}