package com.wallen.week2;

public class ConcurrentQueue {
	private int[] queue; 
	private int queueHeader;
	private int queueTailer;
	private int queueSize;
	private Object lock = new Object();
	
	public static void main(String[] args){
		ConcurrentQueue q = new ConcurrentQueue(10);
		Thread t1 = new Thread(new GetterThread(q), "getT1");
//		Thread t2 = new Thread(new GetThread(q), "getT2");
//		Thread t3 = new Thread(new GetThread(q), "getT3");
//		Thread t4 = new Thread(new GetThread(q), "getT4");
//		Thread t5 = new Thread(new GetThread(q), "getT5");
		Thread st1 = new Thread(new SetterThread(q), "setT");
		t1.start();
//		t2.start();
//		t3.start();
//		t4.start();
//		t5.start();
		st1.start();
	}
	
	public ConcurrentQueue(int size){
		if(size <= 0){
			throw new IllegalArgumentException("queue size should be a positive number");
		}
		queue = new int[size];
		queueHeader = 0;
		queueTailer = -1;
		queueSize = 0;
	}
	
	public void add(int data){
		System.out.println("----------------- begin add --");
		printQueue();
		synchronized(lock){
			
			if(queueSize == queue.length){
				try {
					System.out.println("queue is full, waiting for poll...");
					lock.wait();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}else{
				lock.notifyAll();
				queueTailer = movePointer(queueTailer);
				queue[queueTailer] = data;
				queueSize++;
				printQueue();
			}
		}
		System.out.println("-- end add ---------------------------");
	}
	
	public int poll(){
		System.out.println("------------------- begin poll --");
		printQueue();
		int result = -1;
		synchronized(lock){
			if(queueSize == 0){
				try {
					System.out.println("queue is empty, waiting for add ...");
					lock.wait();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}else{
				lock.notifyAll();
				result = queue[queueHeader];
				queueHeader = movePointer(queueHeader);	
				queueSize--;
			}
		}
		printQueue();
		System.out.println("-- end poll ----------------------------");
		return result;
	}
	
	private int movePointer(int pointer){
		if(pointer >= queue.length - 1){
			pointer = 0;
		}else{
			pointer++;
		}
		return pointer;
	}
	
	private void printQueue(){
		synchronized(lock){
			System.out.println(Thread.currentThread().getName() + " Header: " + queueHeader + " Tailer: " + queueTailer);
			for(int i = 0; i < queue.length; i++){
				System.out.print(queue[i] + " -> ");
			}
			System.out.println();
		}
		
	}
}

class GetterThread implements Runnable{

	private ConcurrentQueue q;
	public GetterThread(ConcurrentQueue q) {
		this.q = q;
	}
	public void run() {
		while(true){
			System.out.println("GET ---- " + q.poll());
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	
}

class SetterThread implements Runnable{

	private ConcurrentQueue q;
	public SetterThread(ConcurrentQueue q){
		this.q = q;
	}
	public void run() {
		//while(true){
			for(int i = 1; i <= 100; i++){
				q.add(i);
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		//}
	}
	
}
