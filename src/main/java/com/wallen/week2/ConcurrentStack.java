package com.wallen.week2;

public class ConcurrentStack{
	private int[] queue;
	private int currentSize;
	private Object lock = new Object();
	
	public static void main(String[] args){
		ConcurrentStack q = new ConcurrentStack(5);
		Thread t1 = new Thread(new GetThread(q), "getT");
		Thread t2 = new Thread(new SetThread(q), "setT");
		t1.start();
		t2.start();
	}
	
	public ConcurrentStack(int length){
		if(length > 0){
			queue = new int[length];
			currentSize = 0;
		}
	}
	
	public int get(){
		int value = 0;
		synchronized(lock){
			lock.notifyAll();
			if(currentSize <= 0){
				try {
					System.out.println(Thread.currentThread().getName() + " begin to wait ...");
					lock.wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}else{
				System.out.print("get queue: ");
				printQueue();
				value = queue[--currentSize];
				
			}
		}
		
		return value;
	}
	
	public void set(int data){
		synchronized(lock){
			lock.notifyAll();
			if(currentSize >= queue.length){
				try {
					lock.wait();
					System.out.println(Thread.currentThread().getName() + " begin to wait() ...");
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}else{
				queue[currentSize++] = data;
				System.out.print("set queue: ");
				printQueue();
			}
		}
	}
	
	public void printQueue(){
		
		for(int i = 0; i< queue.length; i++){
			System.out.print(queue[i] + " => ");
		}
		System.out.println();
	}
}

class GetThread implements Runnable{

	private ConcurrentStack q;
	public GetThread(ConcurrentStack q) {
		this.q = q;
	}
	public void run() {
		while(true){
			System.out.println(q.get());
			try {
				Thread.sleep(0);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	
}

class SetThread implements Runnable{

	private ConcurrentStack q;
	public SetThread(ConcurrentStack q){
		this.q = q;
	}
	public void run() {
		while(true){
			for(int i = 1; i <= 10; i++){
				q.set(i);
				try {
					Thread.sleep(0);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	
}