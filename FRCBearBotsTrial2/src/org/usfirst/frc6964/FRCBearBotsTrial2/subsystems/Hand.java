package org.usfirst.frc6964.FRCBearBotsTrial2.subsystems;

import org.usfirst.frc6964.FRCBearBotsTrial2.Robot;
import org.usfirst.frc6964.FRCBearBotsTrial2.RobotMap;
import org.usfirst.frc6964.FRCBearBotsTrial2.commands.HandMovement;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.command.Subsystem;

public class Hand extends Subsystem {
	
//	private DoubleSolenoid s1 = RobotMap.handSubsystemLeftSolenoid;
	private DoubleSolenoid s2 = RobotMap.handSubsystemRightSolenoid;

	@Override
	protected void initDefaultCommand() {
		// TODO Auto-generated method stub
		this.setDefaultCommand(new HandMovement());
	}
	
    @Override
    public void periodic() {
        // Put code here to be run every loop

    }
    
    public void takeJoystickInput(Joystick joystick) {
    	if (joystick.getRawButton(2)) { //if button 2 is pressed, so open
//    		s1.set(DoubleSolenoid.Value.kForward);
    		s2.set(DoubleSolenoid.Value.kForward);
    	}
    	else if (joystick.getRawButton(3)) { //if button 3 is pressed, so grab
//    		s1.set(DoubleSolenoid.Value.kReverse);
    		s2.set(DoubleSolenoid.Value.kReverse);
    	}
    	else { //buttons not pressed, so do nothing
//    		s1.set(DoubleSolenoid.Value.kOff);
    		s2.set(DoubleSolenoid.Value.kOff);
    	}
    }


}
