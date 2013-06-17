import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import javax.bluetooth.RemoteDevice;

import lejos.nxt.*;
import lejos.nxt.comm.BTConnection;
import lejos.nxt.comm.Bluetooth;
public class NXTRobotCDIO {
	static DataInputStream dis = null;
    static DataOutputStream dos = null;

	/**
	 * @param args
	 * @author Casper
	 * 
	 * This program runs on the NXT brick, and controls the NXT motors depending on data received over bluetooth.
	 * The commands are 6 chars that determines the speed of the motor, the first 3 chars is motor a and the next 3 is motor b 
	 */
	public static void main(String[] args) {
		//First we wait for a PC to connect to the NXT Brick
		connect();
		//We start the interpreter
		run();
			
	}
	
	public static void connect(){
		LCD.drawString("Connecting...", 0, 0);
		BTConnection btConnection = Bluetooth.waitForConnection();
		Sound.buzz();
		dis = btConnection.openDataInputStream();
        dos = btConnection.openDataOutputStream();
        LCD.drawString("Connected!", 0, 0);
	}
	
	private static void run(){
		byte bytes[] = new byte[12];
		int motorA = 0;
		int motorB = 0;
		LCD.scroll();
		while(true){
			try{
				LCD.drawString("Reading", 0, 0);
				dis.read(bytes, 0, 12);
				
				StringBuilder buffer = new StringBuilder(); 
			
				for(int i=0;i < bytes.length ;i++){
					buffer.append(bytes[i]);
				}
				String commands = new String(bytes);
				
				try {
					motorA = Integer.parseInt(commands.substring(0, 3));
					motorB = Integer.parseInt(commands.substring(3, 6));
				} catch (IllegalArgumentException e1) {
					LCD.scroll();
					LCD.scroll();
					LCD.scroll();
					LCD.drawString("Error: Could not parse", 0, 0);
				}
				
				try{
					LCD.drawString("Val A: " + motorA, 0, 1);
					LCD.drawString("Val B: " + motorB, 0, 2);
					if (motorA == 0){
						Motor.A.flt();
					}
					else if(motorB == 0){
						Motor.B.flt();
					}
					else{
						Motor.A.setSpeed(motorA);
						Motor.B.setSpeed(motorB);
						Motor.A.backward();
						Motor.B.backward();
					}
				}
				catch(Exception e){
					while (true){
						LCD.drawString("Command not", 0, 0);
						LCD.drawString("Understod", 0, 1);
						
					}
					
				}
			}
			catch (IOException e){
				while(true){
					LCD.drawString("Read Error", 0, 0);
				}
			}
		}
	}
}
