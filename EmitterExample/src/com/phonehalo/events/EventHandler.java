package com.phonehalo.events;

public interface EventHandler<T> {
	void run(EventName event, T item);
}
