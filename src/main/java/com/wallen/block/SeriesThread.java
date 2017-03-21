package com.wallen.block;

import java.util.Date;

public class SeriesThread{
	public static void main(String[] args) throws InterruptedException{
		System.out.println(new Date() + " : " + " begin ...");
		Thread1 t1 = new Thread1();
		Thread2 t2 = new Thread2(t1);
		t2.start();
		t1.start();
		t2.join();
		System.out.println(new Date() + " : " + "T3 done");
	}
	
	static class Thread1 extends Thread{
		public void run() {
			try {
				Thread.sleep(2000);
				System.out.println(new Date() + " : " +  "T1 done");
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	static class Thread2 extends Thread{
		private Thread preThread;
		public Thread2(Thread preThread) {
			this.preThread = preThread;
		}
		public void run() {
			try {
				preThread.join();
				Thread.sleep(1000);
				System.out.println(new Date() + " : " + "T2 done");
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}