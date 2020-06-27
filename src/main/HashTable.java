package main;

import static main.Utils.*;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class HashTable<T> {
	
	private final Collection<T>[] buckets;
	
	@SuppressWarnings("unchecked")
	public HashTable(int capacity) {
		buckets = new List[capacity];
		
		for (int i = 0; i < capacity; i++) {
			buckets[i] = new LinkedList<T>();
		}
	}
	
	public void add(T t) {
		buckets[mod(t.hashCode(), buckets.length)].add(t);
	}
	
	public Collection<T> get(int hashCode) {
		return buckets[mod(hashCode, buckets.length)];
	}
	
	public void clear() {
		for (Collection<T> l : buckets) {
			l.clear();
		}
	}

}
