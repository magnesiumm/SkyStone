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
        // reset
        //setMotors(0, Direction.FORWARD);
        Direction dir = null;

        // get stick values
        float y = -1 * opMode.gamepad1.left_stick_y;
        float x = opMode.gamepad1.left_stick_x;

        // drive
        float drive_pow = Math.abs(y * maxPow);

        // TODO: subtract or add for turning

        dir = (y >= 0) ? Direction.FORWARD : Direction.REVERSE;
        setMotors(drive_pow, dir);

        // turn
        float turn_pow = turn(x, drive_pow);

        return new DriveState(drive_pow, 0, dir, x, y);
    }

    /*
    set power to both motors
     */
    private void setMotors(float power, Direction dir) {
        leftDrive.setPower(power);
        leftDrive.setDirection(dir);
        //leftDrive.setDirection(Direction.REVERSE);

        rightDrive.setPower(power);
        // TODO: Mechanics reverse motors
        rightDrive.setDirection((dir == Direction.FORWARD) ? Direction.REVERSE : Direction.FORWARD);
    }

    /*
    turn the robot by decreasing power in one wheel and increasing in other
     */
    private float turn(float x, double drive_pow)
    {
        if (Math.abs(x) < .05) {
            return 0;
        }

        // get current demanded power
        float pow = Math.abs(x * maxTurningPow);

        // get current power on motors
        double leftPow = drive_pow;
        double rightPow = drive_pow;

        if (leftDrive.getDirection() == Direction.REVERSE) {
            leftPow *= -1;
        }
        if (rightDrive.getDirection() == Direction.REVERSE) {
            rightPow *= -1;
        }

        if (x < 0) {
            // turn left

            leftPow -= pow;
            rightPow += pow;
        } else if (x > 0) {
            // turn right

            leftPow += pow;
            rightPow -= pow;
        }

        leftDrive.setDirection(leftPow >= 0 ? Direction.FORWARD : Direction.REVERSE);
        rightDrive.setDirection(rightPow >= 0 ? Direction.FORWARD : Direction.REVERSE);

        leftDrive.setPower(Math.abs(leftPow));
        rightDrive.setPower(Math.abs(rightPow));

        return pow;
    }

    /*
    reverse direction of specified motor
     */
    private void reverseDirection(int motor) {
        if (motor == LEFT_MOTOR) {
            leftDrive.setDirection((leftDrive.getDirection() == Direction.FORWARD) ? Direction.REVERSE : Direction.FORWARD);
        } else if (motor == RIGHT_MOTOR) {
            rightDrive.setDirection((rightDrive.getDirection() == Direction.FORWARD) ? Direction.REVERSE : Direction.FORWARD);
        }
    }
}

class DriveState extends State {
    public float drivePower;
    public float turningPower;
    public Direction direction;
    public float stickX;
    public float stickY;

    public DriveState(float drivePower, float turningPower, Direction direction, float stickX, float stickY) {
        this.drivePower = drivePower;
        this.turningPower = turningPower;
        this.direction = direction;
        this.stickX = stickX;
        this.stickY = stickY;
    }
}