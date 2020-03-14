package com.eh7n.f1telemetry.data;

import java.util.List;

import com.eh7n.f1telemetry.data.elements.ButtonStatus;
import com.eh7n.f1telemetry.data.elements.CarTelemetryData;

import com.pi4j.io.gpio.*;
import com.pi4j.util.CommandArgumentParser;
import com.pi4j.util.Console;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.logging.Level;
import java.util.logging.Logger;
import sun.security.x509.X500Name;

public class PacketCarTelemetryData extends Packet implements Serializable {

    private List<CarTelemetryData> carTelemetryData;
    private ButtonStatus buttonStatus; // TODO, create a representation of this data properly
    //final GpioController gpio = GpioFactory.getInstance();
    //final Pin pin = CommandArgumentParser.getPin(
    //       RaspiPin.class, // pin provider class to obtain pin instance from
    //         RaspiPin.GPIO_01 // default pin if no pin argument found
    //   );
    // final GpioPinPwmOutput pwm = gpio.provisionPwmOutputPin(pin);

    public PacketCarTelemetryData() {

    }

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
    public void check_brake(GpioPinPwmOutput pwm) {

        //com.pi4j.wiringpi.Gpio.pwmSetMode(com.pi4j.wiringpi.Gpio.PWM_MODE_MS);
        //com.pi4j.wiringpi.Gpio.pwmSetRange(1000);
        //com.pi4j.wiringpi.Gpio.pwmSetClock(500);
        int pwmvalue = carTelemetryData.get(super.getHeader().getPlayerCarIndex()).getBrake() * 1024 / 100;
        pwm.setPwm(pwmvalue);
        System.out.println("pwmvalue is " + pwmvalue);
 

    }

    @Override
    public void to_short() {
        int port = 20888;
        PacketCarTelemetryDataShort newPacket = new PacketCarTelemetryDataShort();
        CarTelemetryData newData  = carTelemetryData.get(super.getHeader().getPlayerCarIndex());
        newPacket.set_all(newData.getSpeed(), newData.getEngineRpm(), newData.getGear(), newData.getBrakeTemperature(),
                newData.getEngineTemperature(),newData.isDrs());
        ByteArrayOutputStream bStream = new ByteArrayOutputStream(); 
        try {
            InetAddress ip = InetAddress.getLocalHost();
            DatagramSocket ds = new DatagramSocket();
            
            ObjectOutput oo = new ObjectOutputStream(bStream);
            oo.writeObject(newPacket);
            oo.close();
            final byte[] data = bStream.toByteArray();
            final DatagramPacket packet = new DatagramPacket(data, data.length,ip, port);
            ds.send(packet);
        } catch (IOException ex) {
            Logger.getLogger(PacketCarTelemetryData.class.getName()).log(Level.SEVERE, null, ex);
        }
       
    }


}
