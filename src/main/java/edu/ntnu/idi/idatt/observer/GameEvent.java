package edu.ntnu.idi.idatt.observer;

public class GameEvent {

	private String type;
	private Object data;

	public GameEvent(String type, Object data) {
		this.type = type;
		this.data = data;
	}

	public String getType() {
		return type;
	}

	public Object getData() {
		return data;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setData(Object data) {
		this.data = data;
	}
}
