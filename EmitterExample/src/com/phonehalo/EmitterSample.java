package com.phonehalo;

import com.phonehalo.events.Emitter;
import com.phonehalo.events.EventHandler;
import com.phonehalo.events.EventName;
import com.phonehalo.events.Emitter.Handle;

public class EmitterSample {
	public static EventName CONNECTED = new EventName(EmitterSample.class, "CONNECTED");
	
	public static void main(String args[]) {
		Emitter<Object> objectEmitter = new Emitter<Object>();
		
		EventHandler<Object> handler = new EventHandler<Object>() {
			@Override
			public void run(EventName event, Object item) {
				System.out.println("Event emitted: " + event);
			}
		};
		
		Handle handle1 = objectEmitter.on(CONNECTED, handler);
				
		Handle handle2 = objectEmitter.on(CONNECTED, handler);
		
		objectEmitter.once(CONNECTED, handler);
		
		objectEmitter.emit(CONNECTED, new Object());
		objectEmitter.off(handle1);
		objectEmitter.emit(CONNECTED, new Object());
		objectEmitter.off(handle2);
		objectEmitter.emit(CONNECTED, new Object());
	}

}
