package com.phonehalo.events;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

public class Emitter<T> {
	static private class HandlerEntry<T> {
		public HandlerEntry(EventHandler<T> handler) {
			this.handler = handler;
		}
		
		EventHandler<T> handler;
		boolean once = false;
	}
	
	final static public class Handle {
		public Handle(EventName eventName, int id) {
			this.eventName = eventName;
			this.id = id;
		}
		
		// Ensures that nobody clones a handle
		protected Object clone() throws CloneNotSupportedException {
			throw new CloneNotSupportedException();
		}
		
		EventName eventName;
		int id;
	}
	
	private HashMap<EventName, List<HandlerEntry<T>>> eventHandlers = new HashMap<EventName, List<HandlerEntry<T>>>();

	private void _clearEmptyHandler(EventName event) {
		List<HandlerEntry<T>> handlers = eventHandlers.get(event);
		if (handlers.size() == 0) {
			eventHandlers.remove(event);
		}
	}
	
	private HandlerEntry<T> _register(EventName event, EventHandler<T> handler) {
		if (!eventHandlers.containsKey(event)) {
			eventHandlers.put(event,  new LinkedList<HandlerEntry<T>>());
		}
		
		List<HandlerEntry<T>> handlers = eventHandlers.get(event);
		HandlerEntry<T> entry = new HandlerEntry<T>(handler);
		handlers.add(entry);

		return entry;
	}
	
	public Handle on(EventName event, EventHandler<T> handler) {
		HandlerEntry<T> entry = _register(event, handler);
		
		return new Handle(event, entry.hashCode());
	}
	
	public void once(EventName event, EventHandler<T> handler) {
		HandlerEntry<T> entry = _register(event, handler);
		entry.once = true;
	}
	
	public void off(Handle handles[]) {
		for (Handle handle: handles) {
			off(handle);
		}
	}
	
	public void off(Handle handle) {
		if (!eventHandlers.containsKey(handle.eventName)) {
			return;
		}
		
		List<HandlerEntry<T>> handlers = eventHandlers.get(handle.eventName);
		HandlerEntry<T> removed = null;
		for (HandlerEntry<T> entry: handlers) {
			if (entry.hashCode() == handle.id) {
				removed = entry;
				break;
			}
		}
		
		if (removed != null) {
			handlers.remove(removed);
		}
		
		_clearEmptyHandler(handle.eventName);
	}
	
	public void emit(EventName event, T items[]) {
		for(T item: items) {
			emit(event, item);
		}
	}
	
	public void emit(EventName events[], T item) {
		for (EventName event: events) {
			emit(event, item);
		}
	}
	
	public void emit(EventName event, T item) {
		if (!eventHandlers.containsKey(event)) {
			return;
		}
		
		List<HandlerEntry<T>> handlers = eventHandlers.get(event);
		for (ListIterator<HandlerEntry<T>> entryItr = handlers.listIterator(); entryItr.hasNext(); ) {
			HandlerEntry<T> entry = entryItr.next();
			entry.handler.run(event, item);
			if (entry.once) {
				entryItr.remove();
			}
		}
		
		_clearEmptyHandler(event);
	}
	
	public boolean isListening(String event) {
		return eventHandlers.containsKey(event);
	}	
}
