package org.firstinspires.ftc.teamcode.main;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple.Direction;

import org.firstinspires.ftc.teamcode.utils.MainProcess;
import org.firstinspires.ftc.teamcode.utils.State;

public class DriveInput extends MainProcess {
    private float maxPow = 5.0f;
    private float maxTurningPow = 0.8f;

    private DcMotor leftDrive;
    private DcMotor rightDrive;

    private final int LEFT_MOTOR = 0;
    private final int RIGHT_MOTOR = 1;

    public DriveInput(LinearOpMode opMode) {
        super(opMode);
    }

    public void init() {
        leftDrive = opMode.hardwareMap.dcMotor.get("left_drive");
        rightDrive = opMode.hardwareMap.dcMotor.get("right_drive");
    }

    public State main() {
        setMotors(0, Direction.FORWARD);
        Direction dir = null;

        float y = -1 * opMode.gamepad1.left_stick_y;
        float x = opMode.gamepad1.left_stick_x;

        // drive
        float drive_pow = Math.abs(y * maxPow);
        dir = (y > 0) ? Direction.FORWARD : Direction.REVERSE;
        setMotors(drive_pow, dir);

        // turn
        float turn_pow = turn(x);

        return new DriveState(drive_pow, turn_pow, dir, x, y);
    }

    public void cleanup() {

    }

    private void setMotors(float power, Direction dir) {
        leftDrive.setPower(power);
        leftDrive.setDirection(dir);

        rightDrive.setPower(power);
        // TODO: Mechanics reverse motors
        rightDrive.setDirection((dir == Direction.FORWARD) ? Direction.REVERSE : Direction.FORWARD);
    }

    private float turn(float x)
    {
        float pow = Math.abs(x * maxTurningPow);

        double leftPow = leftDrive.getPower();
        double rightPow = rightDrive.getPower();

        if (x < 0)
        {
            // turn left
            leftPow -= pow;
            if (leftPow < 0) {
                leftPow = Math.abs(leftPow);
                reverseDirection(LEFT_MOTOR);
            }
            leftDrive.setPower(leftPow);

            rightPow += pow;
            rightDrive.setPower(rightPow);
        }
        else if (x > 0)
        {
            // turn right
            leftPow += pow;
            leftDrive.setPower(leftPow);

            rightPow -= pow;
            if (rightPow < 0) {
                rightPow = Math.abs(rightPow);
                reverseDirection(RIGHT_MOTOR);
            }
            rightDrive.setPower(rightPow);
        }

        return pow;
    }

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