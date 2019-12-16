package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple.Direction;
import com.qualcomm.robotcore.util.ElapsedTime;

@TeleOp(name="ManualController", group="Linear")

@SuppressWarnings("unused")
public class ManualController extends LinearOpMode
{
    private ElapsedTime runtime = new ElapsedTime();

    private float maxPow = 5.0f;
    private float maxTurningPow = 0.8f;

    private DcMotor leftDrive;
    private DcMotor rightDrive;

    @Override
    public void runOpMode() throws InterruptedException
    {
        leftDrive = hardwareMap.get(DcMotor.class, "left_drive");
        rightDrive = hardwareMap.get(DcMotor.class, "right_drive");

        telemetry.addData("Status", "Waiting for start");
        telemetry.update();

        waitForStart();
        runtime.reset();

        while (opModeIsActive())
        {
            setMotors(0, Direction.FORWARD);
            Direction dir = null;

            float y = -1 * this.gamepad1.left_stick_y; // driving power
            float x = this.gamepad1.left_stick_x; // turning power

            float pow = Math.abs(y * maxPow);

            // drive forward or reverse
            setMotors(pow, ((y > 0) ? Direction.FORWARD : Direction.REVERSE));

            // turn robot
            turn(x);

            telemetry.addData("Status", "Running");
            telemetry.addData("Power", pow);
            telemetry.addData("Stick values", String.format("X: %.2f, Y: %.2f", this.gamepad1.left_stick_x, this.gamepad1.left_stick_y));

            telemetry.update();
        }

        telemetry.addData("Status", "Terminated");
        telemetry.addData("Time elapsed", runtime.seconds() + "s");

        telemetry.update();
    }

    private void setMotors(float power, Direction dir)
    {
        leftDrive.setPower(power);
        leftDrive.setDirection(dir);

        rightDrive.setPower(power);
        // TODO: Mechanics reverse motors
        rightDrive.setDirection((dir == Direction.FORWARD) ? Direction.REVERSE : Direction.FORWARD);
    }

    private void turn(float x)
    {
        float pow = Math.abs(x * maxTurningPow);
        if (x < 0)
        {
            // turn left
            leftDrive.setPower(leftDrive.getPower() - pow);
            rightDrive.setPower(rightDrive.getPower() + pow);
        }
        else if (x > 0)
        {
            // turn right
            leftDrive.setPower(leftDrive.getPower() + pow);
            rightDrive.setPower(rightDrive.getPower() - pow);
        }
    }
}
