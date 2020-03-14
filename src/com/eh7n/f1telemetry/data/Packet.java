package com.eh7n.f1telemetry.data;

import com.eh7n.f1telemetry.data.elements.Header;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pi4j.io.gpio.GpioPinPwmOutput;

public abstract class Packet {
	
	private Header header;
        
        protected  static final int MAX_LENGTH_SHORT_PACKET = 1000;
        
	public Header getHeader() {
		return header;
	}

	public void setHeader(Header header) {
		this.header = header;
	}

	public String toJSON() {
		ObjectMapper mapper = new ObjectMapper();
		String json = "";
		try {
			json = mapper.writeValueAsString(this);
		}catch(Exception e) {
			//TODO: Handle this exception
		}
		return json;
	}
        
        public void check_brake(GpioPinPwmOutput pwm) {
        }
        
        public void to_short() {}

}
