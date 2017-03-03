package com.wallen.week2;

public class ConcurrentQueue {
	private int[] queue; 
	private int queueHeader;
	private int queueTailer;
	private int queueSize;
	
	/**
	 * 开启5个线程进行出队操作
	 * 开启一个线程进行入队操作
	 * 出队执行慢, 入队执行快
	 *
	 * 如果出队按照1~100 顺序则证明队列线程安全
	 * 
	 * TODO: 需要补充test case测试入队也是线程安全的
	 * @param args
	 */
	public static void main(String[] args){
		ConcurrentQueue q = new ConcurrentQueue(10);
		
		Thread t1 = new Thread(new GetterThread(q), "getT1");
		Thread t2 = new Thread(new GetterThread(q), "getT2");
		Thread t3 = new Thread(new GetterThread(q), "getT3");
		Thread t4 = new Thread(new GetterThread(q), "getT4");
		Thread t5 = new Thread(new GetterThread(q), "getT5");
		
		Thread st1 = new Thread(new SetterThread(q), "setT1");
		//Thread st2 = new Thread(new SetterThread(q), "setT2");
		t1.start();
		t2.start();
		t3.start();
		t4.start();
		t5.start();
		st1.start();
		//st2.start();
	}
	
	public ConcurrentQueue(int size){
		if(size <= 0){
			throw new IllegalArgumentException("queue size should be a positive number");
		}
		queue = new int[size];
		// 队列头指针: -1 表明队列刚刚初始化
		queueHeader = -1;
		// 队列尾指针
		queueTailer = 0;
		// 队列长度
		queueSize = 0;
	}
	
	/**
	 * 线程安全的入队操作
	 * synchronized加在实例方法上, 等待在当前对象this上
	 * @param data
	 */
	public synchronized void add(int data) {
		System.out.println("----------------- begin add --");
		printQueue();

		// 使用while, 可以重复判断队列是否已满, 只到队列不满才跳出循环, 进入入队操作
		while (queueSize == queue.length) {
			try {
				System.out.println("queue is full, waiting for poll...");
				// 当队列已满时, 在此阻塞, 释放锁资源, 等待被其他线程唤醒
				this.wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		// 已经可以入队了, 唤醒其他等待线程, 但是其他线程不会马上执行, 需要当前线程释放锁资源
		this.notifyAll();
		queue[queueTailer] = data;
		queueTailer = movePointer(queueTailer);
		queueSize++;
		printQueue();
		System.out.println("-- end add ---------------------------");
	}
	
	/**
	 * 线程安全的出队操作
	 * 
	 * @return
	 */
	public synchronized int poll() {
		System.out.println("------------------- begin poll --");
		printQueue();
		int result = -1;
		// 使用while, 可以重复判断队列是否已空, 只到队列不空才跳出循环, 进入出队操作
		while (queueSize == 0) {
			try {
				System.out.println("queue is empty, waiting for add ...");
				// 当队列已空时, 在此阻塞, 释放锁资源, 等待被其他线程唤醒
				this.wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		// 已经可以出队了, 唤醒其他等待线程, 但是其他线程不会马上执行, 需要当前线程释放锁资源
		// 当锁资源释放后, 所有被唤醒的线程竞争锁资源, 只有一个线程会得到锁资源
		// 也可以将notifyAll放入finally中,保证唤醒必被执行?
		this.notifyAll();
		queueHeader = movePointer(queueHeader);
		result = queue[queueHeader];
		// 将出队的元素制空, 非必要,仅便于查看打印信息
		queue[queueHeader] = 0;
		queueSize--;
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
	
	private synchronized void printQueue() {
		System.out.println(Thread.currentThread().getName() + " Header: " + queueHeader + " Tailer: " + queueTailer);
		for (int i = 0; i < queue.length; i++) {
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
			System.out.println(Thread.currentThread().getName() + ": !!!!! GET " + q.poll());
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
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
		for (int i = 1; i <= 100; i++) {
			q.add(i);
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
}
