package com.wallen.week2;

public class ThreadWaitAndNotify {
	
	public static void main(String[] args){
		Object o = new Object();
		Thread t1 = new Thread(new Wait(o), "t1");
		t1.start();
		
		Thread t2 = new Thread(new Notify(o), "t2");
		t2.start();
	}
}

class Wait implements Runnable{

	private Object lock ;
	public Wait(Object lock) {
		this.lock = lock;
	}
	//@Override
	public void run() {
		synchronized(lock){
			System.out.println(Thread.currentThread().getName() + " is running ");
			try {
				System.out.println(Thread.currentThread().getName() + " is waiting");
				lock.wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			System.out.println(Thread.currentThread().getName() + " wake up and done.");
		}
	}
}

class Notify implements Runnable{
	private Object lock ;
	public Notify(Object lock){
		this.lock = lock;
	}
	//@Override
	public void run() {
		synchronized(lock){
			System.out.println(Thread.currentThread().getName() + " is running");
			lock.notify();
			try {
				System.out.println(Thread.currentThread().getName() + " begin to sleep......");
				Thread.sleep(1000);
				System.out.println("sleep 1 second");
				Thread.sleep(1000);
				System.out.println("sleep 2 second");
			} catch (InterruptedException e) {
				e.printStackTrace();
			}	
			System.out.println(Thread.currentThread().getName() + " wake up and done.");
		}
	}
}
