package com.eh7n.f1telemetry.data;

import java.util.List;

import com.eh7n.f1telemetry.data.elements.ButtonStatus;
import com.eh7n.f1telemetry.data.elements.CarTelemetryData;

import com.pi4j.io.gpio.*;
import com.pi4j.util.CommandArgumentParser;
import com.pi4j.util.Console;

public class PacketCarTelemetryData extends Packet {
	
	private List<CarTelemetryData> carTelemetryData;
	private ButtonStatus buttonStatus; // TODO, create a representation of this data properly
	
	public PacketCarTelemetryData() {}
	
	public List<CarTelemetryData> getCarTelemetryData() {
		return carTelemetryData;
	}
	public void setCarTelemetryData(List<CarTelemetryData> carTelemetryData) {
		this.carTelemetryData = carTelemetryData;
	}

	public ButtonStatus getButtonStatus() {
		return buttonStatus;
	}

	public void setButtonStatus(ButtonStatus buttonStatus) {
		this.buttonStatus = buttonStatus;
	}
        
        
        @Override
        public void check_brake() {
            System.out.println("reached");
            // create GPIO controller instance
            GpioController gpio = GpioFactory.getInstance();
            Pin pin = CommandArgumentParser.getPin(
                RaspiPin.class,    // pin provider class to obtain pin instance from
                RaspiPin.GPIO_01 // default pin if no pin argument found
              );            

            GpioPinPwmOutput pwm = gpio.provisionPwmOutputPin(pin); 
            System.out.println("brake: "+carTelemetryData.get(super.getHeader().getPlayerCarIndex()).getBrake());
            pwm.setPwm(carTelemetryData.get(super.getHeader().getPlayerCarIndex()).getBrake() / 100 * 1024);
        }       
}
