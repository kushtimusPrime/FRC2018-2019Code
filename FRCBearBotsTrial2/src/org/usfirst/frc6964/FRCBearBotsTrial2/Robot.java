// RobotBuilder Version: 2.0
//
// This file was generated by RobotBuilder. It contains sections of
// code that are automatically generated and assigned by robotbuilder.
// These sections will be updated in the future when you export to
// Java from RobotBuilder. Do not put any code or make any change in
// the blocks indicating autogenerated code or it will be lost on an
// update. Deleting the comments indicating the section will prevent
// it from being updated in the future.

package org.usfirst.frc6964.FRCBearBotsTrial2;

import edu.wpi.cscore.CvSink;
import edu.wpi.cscore.CvSource;
import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.vision.VisionPipeline;
import edu.wpi.first.wpilibj.vision.VisionRunner;
import edu.wpi.first.wpilibj.vision.VisionThread;

import java.util.ArrayList;
import java.util.List;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfKeyPoint;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfRect;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;
import org.opencv.objdetect.Objdetect;
import org.usfirst.frc6964.FRCBearBotsTrial2.commands.*;
import org.usfirst.frc6964.FRCBearBotsTrial2.subsystems.*;

import com.sun.javafx.font.directwrite.RECT;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the TimedRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the build.properties file in the
 * project.
 */

/**
 * Stuff to test tomorrow 1. Test autonomous program 2. Test double camera thing
 * (Delay this until we figure out how to do camera stitching 3. Test if
 * NetworkTables are printing out 4. Upload GripPipeline into the main program
 * 
 * @author BearBots Inc
 *
 */
public class Robot extends TimedRobot {

	Command autonomousCommand;
	SendableChooser<Command> chooser = new SendableChooser<>();
	String theMessage = "";
	private CascadeClassifier cascade;
	private int absoluteSize;

	public static OI oi;
	// BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DECLARATIONS
	public static DriveSubsystem driveSubsystem;
	public static ArmSubsystem armSubsystem;
	public static HandSubsystem handSubsystem;

	// END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DECLARATIONS
	UsbCamera cameraZero;
	UsbCamera cameraOne;
	CameraServer cameraServerZero;
	CameraServer cameraServerOne;
	private static final int IMG_WIDTH = 640;
	private static final int IMG_HEIGHT = 480;
	private VisionThread visionThread;
	private double centerX = 0.0;
	private DifferentialDrive drive;
	private final Object imgLock = new Object();
	NetworkTableEntry xEntry;
	NetworkTableEntry yEntry;

	 //Robot constructor to initialize Network Table 

	
	/**
	 * This function is run when the robot is first started up and should be used
	 * for any initialization code.
	 */
	@Override
	public void robotInit() {
		RobotMap.init();
		// BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTRUCTORS
		driveSubsystem = new DriveSubsystem();
		armSubsystem = new ArmSubsystem();
		handSubsystem = new HandSubsystem();
		 
		// END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTRUCTORS
		// OI must be constructed after subsystems. If the OI creates Commands
		// (which it very likely will), subsystems are not guaranteed to be
		// constructed yet. Thus, their requires() statements may grab null
		// pointers. Bad news. Don't move it.
		oi = new OI();

		// Add commands to Autonomous Sendable Chooser
		// BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=AUTONOMOUS

		// chooser.addObject("Middle", new Middle());
		chooser.addDefault("Autonomous Command", new LeftSideAuto("Doesn't matter"));
		/**
		 * chooser.addDefault("Autonomous Command", new LeftSideAuto("Doesn't even
		 * matter rn"));
		 * 
		 */
		// END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=AUTONOMOUS
		SmartDashboard.putData("Auto mode", chooser);

		// Kush's attempt to make two cameras have access to OpenCV
		/*
		 * cameraServerZero=CameraServer.getInstance();
		 * cameraServerOne=CameraServer.getInstance();
		 * if(cameraServerZero.equals(cameraServerOne)) {
		 * System.out.println("You failed dummy"); } cameraZero =
		 * cameraServerZero.startAutomaticCapture(0); cameraZero.setResolution(640,
		 * 480); cameraOne = cameraServerOne.startAutomaticCapture(1);
		 * cameraOne.setResolution(640, 480); CvSink cvSinkZero =
		 * cameraServerZero.getVideo(); CvSource cvOutputZero =
		 * cameraServerZero.putVideo("Zero camera server", 640, 480); CvSink cvSinkOne =
		 * cameraServerOne.getVideo(); CvSource cvOutputOne =
		 * cameraServerOne.putVideo("One camera server", 640, 480); Mat source = new
		 * Mat(); Mat blurredImage = new Mat(); Mat hsvImage = new Mat(); Mat mask = new
		 * Mat(); Mat output = new Mat(); Thread t = new Thread(() -> { while
		 * (!Thread.interrupted()) { cvSinkZero.grabFrame(source);
		 * cvSinkOne.grabFrame(source); Imgproc.blur(source, blurredImage, new Size(7,
		 * 7)); Imgproc.cvtColor(blurredImage, hsvImage, Imgproc.COLOR_BGR2HSV); Scalar
		 * minValues = new Scalar(20, 100, 100); Scalar maxValues = new Scalar(30, 255,
		 * 255); Core.inRange(hsvImage, minValues, maxValues, mask); Mat dilateElement =
		 * Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(24, 24)); Mat
		 * erodeElement = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(12,
		 * 12)); Imgproc.erode(mask, output, erodeElement); Imgproc.erode(output,
		 * output, erodeElement); Imgproc.dilate(output, output, dilateElement);
		 * Imgproc.dilate(output, output, dilateElement);
		 * cvOutputZero.putFrame(this.findBoxes(output, source));
		 * cvOutputOne.putFrame(this.findBoxes(output, source)); } }); t.start();
		 */
		cameraZero = CameraServer.getInstance().startAutomaticCapture(0);
		cameraZero.setResolution(IMG_WIDTH, IMG_HEIGHT);
		System.out.println("Steve Miller");
		CvSink cvSinkZero=cameraServerZero.getVideo();
		CvSource cvOutputZero=cameraServerZero.putVideo("Kush got it", IMG_WIDTH, IMG_HEIGHT);
		Mat source=new Mat();
		visionThread = new VisionThread(cameraZero, new KushGripPipelineTrialOne(), new VisionRunner.Listener<VisionPipeline>() {
			@Override
			public void copyPipelineOutputs(VisionPipeline pipeline) {
				//This should allow for the GRIP Pipeline to work in the code
				pipeline.process(new Mat());
				MatOfKeyPoint output=((KushGripPipelineTrialOne) pipeline).findBlobsOutput();
				cvOutputZero.putFrame(output);
				// TODO Auto-generated method stub
			}
		});
		visionThread.start();
		/*NetworkTableInstance inst = NetworkTableInstance.getDefault();
		NetworkTable table = inst.getTable("datatable");package org.usfirst.frc6964.FRCBearBotsTrial2;

		xEntry = table.getEntry("X");
		yEntry = table.getEntry("Y");
		System.out.println("X entry: " + xEntry);
		System.out.println("Y entry: " + yEntry);*/

	}

	public void disabledInit() {
		if (theMessage.equals("")) {
			theMessage = DriverStation.getInstance().getGameSpecificMessage();
		}
	}

	public void disabledPeriodic() {
		Scheduler.getInstance().run();
		if (theMessage.equals("")) {
			theMessage = DriverStation.getInstance().getGameSpecificMessage();
		}
	}

	public void autonomousInit() {
		//autonomousCommand = new LeftSideAuto("Trolling");
		autonomousCommand = new AutonomousCommand();
		if (autonomousCommand != null) {
			//autonomousCommand.start();
		}
	}

	@Override
	public void autonomousPeriodic() {
		Scheduler.getInstance().run();
	}

	@Override
	public void teleopInit() {
		// This makes sure that the autonomous stops running when
		// teleop starts running. If you want the autonomous to
		// continue until interrupted by another command, remove
		// this line or comment it out.
		if (autonomousCommand != null)
			autonomousCommand.cancel();
		System.out.println("Steve Miller");

	}

	/**
	 * This function is called periodically during operator control
	 */
	@Override
	public void teleopPeriodic() {
		Scheduler.getInstance().run();
	}
}
