package com.wallen.atomic;

import java.util.concurrent.atomic.AtomicReferenceArray;

public class LockFreeVector<E>{
	private static final int N_BUCKET = 8;
	private AtomicReferenceArray<AtomicReferenceArray<E>> buckets;
	
	public LockFreeVector(){
		buckets = new AtomicReferenceArray<AtomicReferenceArray<E>>(N_BUCKET);
	}
}
