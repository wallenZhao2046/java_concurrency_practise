package com.wallen.week3;

public class ConcurrencyLong {
	
	public static void main(String[] args){
		
		SharedLong sLong = new SharedLong();
		Getter getter = new Getter(sLong);
		Setter setter = new Setter(sLong);
		
		getter.start();
		setter.start();
		
	}
	
	public static class Getter extends Thread{
		private SharedLong sLong;
		public Getter(SharedLong sLong){
			this.sLong = sLong;
		}
		public void run() {
			while(true){
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				System.out.println(sLong.getValue());
			}
		}
	}
	
	public static class Setter extends Thread{
		private SharedLong sLong;
		public Setter(SharedLong sLong) {
			this.sLong = sLong;
		}
		public void run() {
			while(true){
				for(int i = 0 ; i < 10; i++){
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					sLong.setValue(i);
				}
			}
			
		}
	}
}


class SharedInt {

   private int value;
   public void setValue(int value) {
      this.value = value;
   }
   public int getValue() {
      return this.value;
   }
}



class SharedLong {

 private long value;
    public void setValue(long  value) {
      this.value = value;
    }
    public long getValue() {
       return this.value;
    }
}
