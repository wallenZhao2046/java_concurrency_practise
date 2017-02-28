package com.wallen.week2;

public class ConcurrentStack{
	private int[] queue;
	private int currentSize;
	private Object lock = new Object();
	
	public static void main(String[] args){
		ConcurrentStack q = new ConcurrentStack(5);
		Thread t1 = new Thread(new GetThread(q), "getT1");
		Thread t2 = new Thread(new GetThread(q), "getT2");
		Thread t3 = new Thread(new GetThread(q), "getT3");
		Thread t4 = new Thread(new GetThread(q), "getT4");
		Thread t5 = new Thread(new GetThread(q), "getT5");
		Thread st1 = new Thread(new SetThread(q), "setT");
		t1.start();
		t2.start();
		t3.start();
		t4.start();
		t5.start();
		st1.start();
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
			// 通知其他阻塞线程可以竞争锁了
			lock.notifyAll();
			if(currentSize <= 0){
				try {
					System.out.println(Thread.currentThread().getName() + " begin to wait ...");
					// 当前线程阻塞, 释放锁
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
			// 通知其他线程可以竞争锁了
			lock.notifyAll();
			if(currentSize >= queue.length){
				try {
					// 阻塞, 并释放锁
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