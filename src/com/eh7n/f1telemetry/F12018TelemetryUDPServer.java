package com.eh7n.f1telemetry;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.DatagramChannel;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.eh7n.f1telemetry.data.Packet;
import com.eh7n.f1telemetry.util.PacketDeserializer;
import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinPwmOutput;
import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.RaspiPin;
import com.pi4j.util.CommandArgumentParser;

import com.eh7n.f1telemetry.gui.*;
import com.eh7n.f1telemetry.data.PacketCarTelemetryData;
import com.eh7n.f1telemetry.data.elements.CarTelemetryData;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinState;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.LogManager;
import org.jooq.Configuration;
import org.jooq.SQLDialect;
import org.jooq.impl.DefaultConfiguration;

/**
 * The base class for the F1 2018 Telemetry app. Starts up a non-blocking I/O
 * UDP server to read packets from the F1 2018 video game and then hands those
 * packets off to a parallel thread for processing based on the lambda function
 * defined. Leverages a fluent API for initialization.
 *
 * Also exposes a main method for starting up a default server
 *
 * @author eh7n
 *
 */
public class F12018TelemetryUDPServer {

    private static final Logger log = LoggerFactory.getLogger(F12018TelemetryUDPServer.class);

    //private static final String DEFAULT_BIND_ADDRESS = "0.0.0.0";
    private static final String DEFAULT_BIND_ADDRESS = "0.0.0.0";
    private static final int DEFAULT_PORT = 20777;
    private static final int MAX_PACKET_SIZE = 1341;

    private String bindAddress;
    private int port;
    private Consumer<Packet> packetConsumer;

    private F12018TelemetryUDPServer() {
        bindAddress = DEFAULT_BIND_ADDRESS;
        port = DEFAULT_PORT;
    }

    /**
     * Create an instance of the UDP server
     *
     * @return
     */
    public static F12018TelemetryUDPServer create() {
        return new F12018TelemetryUDPServer();
    }

    /**
     * Set the bind address
     *
     * @param bindAddress
     * @return the server instance
     */
    public F12018TelemetryUDPServer bindTo(String bindAddress) {
        this.bindAddress = bindAddress;
        return this;
    }

    /**
     * Set the bind port
     *
     * @param port
     * @return the server instance
     */
    public F12018TelemetryUDPServer onPort(int port) {
        this.port = port;
        return this;
    }

    /**
     * Set the consumer via a lambda function
     *
     * @param consumer
     * @return the server instance
     */
    public F12018TelemetryUDPServer consumeWith(Consumer<Packet> consumer) {
        packetConsumer = consumer;
        return this;
    }

    /**
     * Start the F1 2018 Telemetry UDP server
     *
     * @throws IOException if the server fails to start
     * @throws IllegalStateException if you do not define how the packets should
     * be consumed
     */
    public void start() throws IOException {

        System.getProperties().setProperty("org.jooq.no-logo", "true");
        if (packetConsumer == null) {
            throw new IllegalStateException("You must define how the packets will be consumed.");
        }

        log.info("F1 2018 - Telemetry UDP Server");
        final GpioController gpio = GpioFactory.getInstance();
        final Pin pin1 = CommandArgumentParser.getPin(
                RaspiPin.class, // pin provider class to obtain pin instance from
                RaspiPin.GPIO_01 // default pin if no pin argument found
        );
        final GpioPinDigitalOutput pin2 = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_02, "redLED", PinState.LOW);
        final GpioPinDigitalOutput pin3 = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_03, "greenLED", PinState.LOW);
      
        final GpioPinPwmOutput pwm1 = gpio.provisionPwmOutputPin(pin1);
    
        // Create an executor to process the Packets in a separate thread
        // To be honest, this is probably an over-optimization due to the use of NIO,
        // but it was done to provide a simple way of providing back pressure on the
        // incoming UDP packet handling to allow for long-running processing of the
        // Packet object, if required.

        ExecutorService executor = Executors.newSingleThreadExecutor();

        try (DatagramChannel channel = DatagramChannel.open()) {
            channel.socket().bind(new InetSocketAddress(bindAddress, port));
            log.info("Listening on " + bindAddress + ":" + port + "...");
            ByteBuffer buf = ByteBuffer.allocate(MAX_PACKET_SIZE);
            buf.order(ByteOrder.LITTLE_ENDIAN);
            while (true) {
                channel.receive(buf);
                final Packet packet = PacketDeserializer.read(buf.array());
                executor.submit(() -> {
                    packetConsumer.accept(packet);
                });
                //executor.submit(() -> {packet.to_database();});
                packet.check_brake(pwm1);
                packet.displayTractionConrtrol(pin2, pin3);
                buf.clear();
            }
        } finally {
            executor.shutdown();
        }
    }

    /**
     * Main class in case you want to run the server independently. Uses
     * defaults for bind address and port, and just logs the incoming packets as
     * a JSON object to the location defined in the logback config
     *
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException, InterruptedException {
//            InitialScreen initialScreen = new InitialScreen();
//            
//            Thread thread1 = new Thread(){
//                @Override
//                public void run(){
//                    try {
//                        initialScreen.main(null);
//                    } catch (InterruptedException ex) {
//                        System.out.println("Launching thread failed");
//                        java.util.logging.Logger.getLogger(F12018TelemetryUDPServer.class.getName()).log(Level.SEVERE, null, ex);
//                    }
//                }
//            };
//            
//            thread1.start();

        //InitialScreen initialScreen = new InitialScreen();
        //initialScreen.main(null);
        System.getProperties().setProperty("org.jooq.no-logo", "true");

        String userName = "root";
        String password = "123456";
        String url = "jdbc:mysql://localhost:3306/f1data?serverTimezone=UTC";

        try (Connection conn = DriverManager.getConnection(url, userName, password)) {
            final Configuration jooqConfig = new DefaultConfiguration().set(conn).set(SQLDialect.MYSQL);
            F12018TelemetryUDPServer.create()
                    .bindTo(DEFAULT_BIND_ADDRESS)
                    .onPort(DEFAULT_PORT)
                    .consumeWith((p) -> {
                        //System.out.println("New packet received");
                        p.to_database(conn);

                        //initialScreen.readPacket(p);
                        //log.trace(p.toJSON());
                    })
                    .start();
            System.out.println("started");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
