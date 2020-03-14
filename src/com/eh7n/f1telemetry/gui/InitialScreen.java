package com.eh7n.f1telemetry.gui;

//imports
import java.util.List;
import java.awt.Toolkit;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.UIManager;

//data imports
import com.eh7n.f1telemetry.data.Packet;
import com.eh7n.f1telemetry.data.PacketCarTelemetryData;
import com.eh7n.f1telemetry.data.PacketSessionData;
import com.eh7n.f1telemetry.data.PacketLapData;
import com.eh7n.f1telemetry.data.PacketMotionData;
import com.eh7n.f1telemetry.data.PacketCarStatusData;

//elements imports
import com.eh7n.f1telemetry.data.elements.CarTelemetryData;
import com.eh7n.f1telemetry.data.elements.WheelData;
import com.eh7n.f1telemetry.data.elements.CarMotionData;
import com.eh7n.f1telemetry.data.elements.LapData;
import com.eh7n.f1telemetry.data.elements.CarStatusData;
        




/**
 *
 * @author remid_000
 */

public class InitialScreen extends javax.swing.JFrame {
    
    /**
     * variables
     */
    static boolean displaySelected = false;
    static boolean trackSelected = false;
    static InitialScreen initialScreen = new InitialScreen();
    static DashboardScreen dashboardScreen = new DashboardScreen();
    static TracksScreen tracksScreen = new TracksScreen();
    static HScoresScreen hScoresScreen = new HScoresScreen();
    static int refresh_rate = 300; //important! value in milliseconds, should be below 1000 & above 
    static int speed = 0;
    static int rpm = 0;
    static int gear = 1;
    static float crr_lap_time = 0;
    static float best_lap_time = 0;
    static int milliseconds = 0;
    static int seconds = 0;
    static int minutes = 0;
    static int battery_level = 100;
    static int distance = 1535;
    static int bat_temp = 0;
    static int brake_temp = 0;
    static int rev_lights = 0;
    
    // VOOR ICONS
    static int track_id = 18;
    static boolean newIcon = false;

    
    /**
     * methods 
     */
    
    public void readPacket(Packet p){
        switch (p.getHeader().getPacketId()) {
            
            case 0:
                // motion data packet
                System.out.println("Packet type: Car motion data");
                List<CarMotionData> carMotionData =  ((PacketMotionData) p).getCarMotionDataList();
                break;
            
            case 1:
                // session data packet
                System.out.println("Packet type: Session data");
                distance = ((PacketSessionData) p).getTrackLength();
                track_id = ((PacketSessionData) p).getTrackId();
                break;
            
            case 2:
                // lap data packet
                System.out.println("Packet type: Lap data");
                List<LapData> lapData = ((PacketLapData) p).getLapDataList();
                crr_lap_time = lapData.get(p.getHeader().getPlayerCarIndex()).getCurrentLapTime();
                best_lap_time = lapData.get(p.getHeader().getPlayerCarIndex()).getBestLaptTime();
                break;
                
            case 3:
                // event data packet
                System.out.println("Packet type: Event data");
                break;
                
            case 4:
                // participants data packet
                System.out.println("Packet type: Participants data");
                break;
                
            case 5:
                // car setup data packet
                System.out.println("Packet type: Car setup data");
                break;
                
            case 6:
                // car telemetry data packet
                System.out.println("Packet type: Car telemetry data");
                List<CarTelemetryData> carTelemetryData =  ((PacketCarTelemetryData) p).getCarTelemetryData();
                speed = carTelemetryData.get(p.getHeader().getPlayerCarIndex()).getSpeed();
                rpm = carTelemetryData.get(p.getHeader().getPlayerCarIndex()).getEngineRpm();
                gear = carTelemetryData.get(p.getHeader().getPlayerCarIndex()).getGear();
                bat_temp = carTelemetryData.get(p.getHeader().getPlayerCarIndex()).getEngineTemperature();
                brake_temp  = carTelemetryData.get(p.getHeader().getPlayerCarIndex()).getBrakeTemperature().getRearRight();
                rev_lights = carTelemetryData.get(p.getHeader().getPlayerCarIndex()).getRevLightsPercent();
                System.out.println("brakes: " + brake_temp + "\t battery: " + bat_temp);
                break;
                
            case 7:
                // car status data packet
                System.out.println("Packet type: Car status data");
                List<CarStatusData> carStatusData = ((PacketCarStatusData) p).getCarStatuses();
                float fuelInTank = carStatusData.get(p.getHeader().getPlayerCarIndex()).getFuelInTank();
                float fuelCapacity = carStatusData.get(p.getHeader().getPlayerCarIndex()).getFuelCapacity();
                battery_level = Math.round(fuelInTank / fuelCapacity);   
                System.out.println("FUEL IN TANK: " + fuelInTank);
                break;
            
            default:
                System.out.println("Unrecognised packet");
                break;
        }
        
    }
    
    public void setSpeed(int speed){
        if (speed != -1){
            this.speed = speed;
        }
    }
    
    public void setRpm(int rpm){
        if (rpm != -1){
            this.rpm = rpm;
        }
    }
    
    static public void closeDashboard(){
        displaySelected = false;
        dashboardScreen.dispose();
        initialScreen.setVisible(true);
        resetDashboardValues();
    }
    
    static public void closeTracks(){
        trackSelected = false;
        tracksScreen.dispose();
        initialScreen.setVisible(true);
    }
    
    static public void closeHScores(){
        hScoresScreen.dispose();
        initialScreen.setVisible(true);
    }
    
    static public void resetDashboardValues(){
        speed = 0;
        rpm = 0;
        gear = 1;
        battery_level = 1000;
        milliseconds = 0;
        seconds = 0;
        minutes = 0;
        distance = 1532;
        bat_temp = 0;
        brake_temp = 0;              
    }
    
    static public int getSpeed(){
        /*
        Random rand = new Random();
        //slower acceleration at higher speeds
        if (speed < 60){
            speed += rand.nextInt(20);
        } else if (speed < 90){
            speed += rand.nextInt(10);
        } else if (speed >= 115 && speed <= 120){
            speed -= rand.nextInt(5);
        } else {
            speed += rand.nextInt(5);
        }
        */
        //speed = CarTelemetryData.getSpeed();
        return speed;
    }
    
    static public int getRevLights(){
        return rev_lights;
    }
    
    static public int getRPM(){
        /*
        Random rand = new Random();
        if (rpm < 15000){
            rpm += 2000 * rand.nextInt(2);
        } else {
            rpm = 0;
            gear ++;
        }
        */
        return rpm;
    }
    
    static public int getGear(){
        /*
        if (gear > 8){
            return 8;
        }*/
        return gear;
    }
    
    static public int getBattery(){
        /*battery_level -= 0.1;*/
        return battery_level;
    }
    
    static public int getBatTemp(){
        /*
        Random randint = new Random();
        if (bat_temp >= 60){
            bat_temp -= randint.nextInt(5);
        } else {
            bat_temp += randint.nextInt(3);
        }*/
        return bat_temp;
    }
    
    static public int getBrakeTemp(){
        /*
        Random randint = new Random();
        if (brake_temp >= 40){
            brake_temp -= randint.nextInt(5);
        } else {
            brake_temp += randint.nextInt(3);
        }*/
        return brake_temp;
    }
    
    static public String getBestTime(){
        return best_lap_time + "";
    }
    
    static public String getCrrTime(){
        /*
        if (milliseconds < 1000-refresh_rate){
            milliseconds += refresh_rate;
        } else {
            milliseconds = 0;
            if (seconds < 60){
                seconds ++;
            } else {
                seconds = 0;
                minutes += 1;
            }          
        }*/
        return crr_lap_time + "";
        //return minutes + ":" + seconds + ":" + milliseconds;
    }
    
    static public int getDistance(){
        /*
        int driven = (speed * refresh_rate)/(3600);
        distance -= driven;*/
        return (distance);
    }
   
    
    
    /**
     * Creates new form InitialScreen
     */
    public InitialScreen() {
        initComponents();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.r
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        displayButton = new javax.swing.JButton();
        tracksButton = new javax.swing.JButton();
        hScoresButton = new javax.swing.JButton();
        logoLabel = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Formula Simulator");
        setPreferredSize(new java.awt.Dimension(480, 320));
        setSize(new java.awt.Dimension(480, 320));

        displayButton.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        displayButton.setText("Display");
        displayButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                displayButtonActionPerformed(evt);
            }
        });

        tracksButton.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        tracksButton.setText("Tracks");
        tracksButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tracksButtonActionPerformed(evt);
            }
        });

        hScoresButton.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        hScoresButton.setText("Highscores");
        hScoresButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                hScoresButtonActionPerformed(evt);
            }
        });

        logoLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/eh7n/f1telemetry/gui/images/formula_logo.png"))); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(44, 44, 44)
                        .addComponent(logoLabel))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(24, 24, 24)
                        .addComponent(displayButton, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(tracksButton, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(hScoresButton)))
                .addContainerGap(51, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(logoLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(tracksButton, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(hScoresButton, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(displayButton, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(70, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void displayButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_displayButtonActionPerformed
        // TODO add your handling code here:
        this.dispose();
        displaySelected = true;
        dashboardScreen.setVisible(true);
    }//GEN-LAST:event_displayButtonActionPerformed

    private void tracksButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tracksButtonActionPerformed
        // TODO add your handling code here:
        this.dispose();
        newIcon = false;
        trackSelected = true;
        tracksScreen.setVisible(true);
    }//GEN-LAST:event_tracksButtonActionPerformed

    private void hScoresButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_hScoresButtonActionPerformed
        // TODO add your handling code here:
        this.dispose();
        hScoresScreen.setVisible(true);
    }//GEN-LAST:event_hScoresButtonActionPerformed


    /**
     * @param args the command line arguments
     * @throws java.lang.InterruptedException
     */
    public static void main(String args[]) throws InterruptedException {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(InitialScreen.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        
        //add code that doesn't have to be refreshed below
        //...
        //add code that doesn't have to be refreshed above
        System.out.println("GUI Launched");
        initialScreen.setVisible(true);
        
        /* Create and display the form */
        while(true){
            TimeUnit.MILLISECONDS.sleep(refresh_rate);
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                // add your code to be looped below
                if (trackSelected)
                {
                    
                    if(newIcon == false)
                    {
                    tracksScreen.tracksLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Tracks/T" + track_id +".jpg")));
                    newIcon = true;
                    }
                    
                }
                // add your code to be looped above 
                if (displaySelected){
                    dashboardScreen.rpmBar.setValue(getRevLights());
                    dashboardScreen.rpmLabel.setText(getRPM()+"");
                    dashboardScreen.speedLabel.setText(getSpeed()+" kmh");
                    
                    if (gear == -1){
                        dashboardScreen.gearLabel.setText("R");
                    } else {
                        dashboardScreen.gearLabel.setText("G"+getGear());
                    }
                    
                    dashboardScreen.batteryBar.setValue(getBattery());
                    dashboardScreen.batteryLabel.setText(getBattery()+" %");
                    dashboardScreen.batTempLabel.setText(getBatTemp()+" °C");
                    dashboardScreen.brakeTempLabel.setText(getBrakeTemp()+" °C");
                    dashboardScreen.currentLabel.setText("Current: " + getCrrTime());
                    dashboardScreen.bestLabel.setText("Best: " + getBestTime());
                    dashboardScreen.distanceLabel.setText(getDistance()+" m");
                }
            }
        });

    }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton displayButton;
    private javax.swing.JButton hScoresButton;
    private javax.swing.JLabel logoLabel;
    private javax.swing.JButton tracksButton;
    // End of variables declaration//GEN-END:variables
}
