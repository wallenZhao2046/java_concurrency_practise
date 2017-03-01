package com.wallen.week2;

public class ConcurrentQueue {
	private int[] queue; 
	private int queueHeader;
	private int queueTailer;
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
		if(size > 0){
			queue = new int[size];
		}
	}
	
	public void add(int data){
		synchronized(lock){
			lock.notifyAll();
			printQueue();
			if(queueTailer >= queue.length - 1 && queueHeader != 0){
					queueTailer = 0;
					queue[queueTailer] = data;
			}else{
				if(queueTailer != queueHeader - 1 && queueTailer != queue.length - 1){
					queue[++queueTailer] = data;
				}else{
					try {
						System.out.println("queue is full, waiting for poll...");
						lock.wait();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
	}
	
	public int poll(){
		int result = -1;
		synchronized(lock){
			lock.notifyAll();
			printQueue();
			if( queueHeader >= queue.length - 1 && queueTailer != 0){
				result = queue[queueHeader];
				queueHeader = 0;
			}else{
				if(queueHeader != queueTailer - 1 && queueHeader != queue.length -1){
					result = queue[queueHeader++];
				}else{
					try {
						System.out.println("queue is empty, waiting for add ...");
						lock.wait();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
		return result;
	}
	
	public void printQueue(){
		
		System.out.println(Thread.currentThread().getName() + "Header: " + queueHeader + " Tailer: " + queueTailer);
		for(int i = 0; i < queue.length; i++){
			System.out.print(queue[i] + " -> ");
		}
		System.out.println();
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
