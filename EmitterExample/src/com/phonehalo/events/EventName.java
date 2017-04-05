package com.phonehalo.events;

public class EventName {
	private String _name;
	
	public EventName(Class<?> clazz, String name) {
		_name = clazz.getName() + "." + name;
	}

	public String toString() {
		return _name;
	}
}
