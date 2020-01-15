package org.firstinspires.ftc.teamcode.main;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple.Direction;

import org.firstinspires.ftc.teamcode.utils.MainProcess;
import org.firstinspires.ftc.teamcode.utils.State;

/*
Class to handle input from controller 1 for driving
 */
public class DriveInput extends MainProcess {
    // power limits
    private float maxPow = 5.0f;
    private float maxTurningPow = 0.8f;

    // motor objects
    private DcMotor leftDrive;
    private DcMotor rightDrive;

    // motor identifiers
    private final int LEFT_MOTOR = 0;
    private final int RIGHT_MOTOR = 1;

    public DriveInput(LinearOpMode opMode) {
        super(opMode);
    }

    /*
    get motor objects from hardware
     */
    @Override
    public void init() {
        leftDrive = opMode.hardwareMap.dcMotor.get("left_drive");
        rightDrive = opMode.hardwareMap.dcMotor.get("right_drive");
    }

    /*
    get input from controller and translate into motion
     */
    @Override
    public State main() {
        float x = opMode.gamepad1.left_stick_x;
        float y = opMode.gamepad1.left_stick_y;
        float drive_pow = -1 * y * maxPow;
        float turn_pow = -1 * x * maxTurningPow;

        float left_pow, right_pow;

        left_pow = drive_pow + turn_pow;
        right_pow = drive_pow + turn_pow;

        setLeftMotor(left_pow);
        setRightMotor(right_pow);

        return new DriveState(drive_pow, turn_pow, x, y);
    }

    /*
    set left motor power/direction
     */
    public void setLeftMotor(float pow) {
        leftDrive.setDirection(pow >= 0 ? Direction.FORWARD : Direction.REVERSE);
        // Direction.FORWARD if pow >= 0 else Direction.REVERSE
        leftDrive.setPower(Math.abs(pow));
    }

    /*
    set right motor power/direction
     */
    public void setRightMotor(float pow) {
        rightDrive.setDirection(pow >= 0 ? Direction.FORWARD : Direction.REVERSE);
        rightDrive.setPower(Math.abs(pow));
    }
}

class DriveState extends State {
    public float drivePower;
    public float turningPower;
    public float stickX;
    public float stickY;

    public DriveState(float drivePower, float turningPower, float stickX, float stickY) {
        this.drivePower = drivePower;
        this.turningPower = turningPower;
        this.stickX = stickX;
        this.stickY = stickY;
    }
}