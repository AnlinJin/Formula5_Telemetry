package com.eh7n.f1telemetry.gui;

import java.awt.Toolkit;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.UIManager;


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
    static int refresh_rate = 100; //important! value in milliseconds, should be below 1000
    static int speed = 0;
    static int rpm = 0;
    static int gear = 1;
    static int milliseconds = 0;
    static int seconds = 0;
    static int minutes = 0;
    static int battery_level = 1000;
    static int distance = 1532;
    static int bat_temp = 0;
    static int brake_temp = 0;
    
    // VOOR ICONS
    static int track_id = 18;
    static boolean newIcon = false;
    //static int distance_driven = 0;
    
    /**
     * methods 
     */
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
        return speed;
    }
    
    static public int getRPM(){
        Random rand = new Random();
        if (rpm < 15000){
            rpm += 2000 * rand.nextInt(2);
        } else {
            rpm = 0;
            gear ++;
        }
        return rpm;
    }
    
    static public int getGear(){
        if (gear > 8){
            return 8;
        }
        return gear;
    }
    
    static public int getBattery(){
        battery_level -= 0.1;
        return battery_level;
    }
    
    static public int getBatTemp(){
        Random randint = new Random();
        if (bat_temp >= 60){
            bat_temp -= randint.nextInt(5);
        } else {
            bat_temp += randint.nextInt(3);
        }
        return bat_temp;
    }
    
    static public int getBrakeTemp(){
        Random randint = new Random();
        if (brake_temp >= 40){
            brake_temp -= randint.nextInt(5);
        } else {
            brake_temp += randint.nextInt(3);
        }
        return brake_temp;
    }
    
    static public String getCrrTime(){
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
        }
        return minutes + ":" + seconds + ":" + milliseconds;
    }
    
    static public int getDistance(){
        int driven = (speed * refresh_rate)/(3600);
        distance -= driven;
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
     * regenerated by the Form Editor.
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

        logoLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/formula_logo.png"))); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(44, 44, 44)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(displayButton, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(31, 31, 31)
                        .addComponent(tracksButton, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(32, 32, 32)
                        .addComponent(hScoresButton))
                    .addComponent(logoLabel))
                .addContainerGap(253, Short.MAX_VALUE))
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
                .addContainerGap(197, Short.MAX_VALUE))
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
                    dashboardScreen.rpmBar.setValue(getRPM());
                    dashboardScreen.rpmLabel.setText(getRPM()+"");
                    dashboardScreen.speedLabel.setText(getSpeed()+" kmh");
                    dashboardScreen.gearLabel.setText("G"+getGear());
                    dashboardScreen.batteryBar.setValue(getBattery()/10);
                    dashboardScreen.batteryLabel.setText(getBattery()/10+" %");
                    dashboardScreen.batTempLabel.setText(getBatTemp()+" °C");
                    dashboardScreen.brakeTempLabel.setText(getBrakeTemp()+" °C");
                    dashboardScreen.currentLabel.setText("Current: " + getCrrTime());
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
