package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple.Direction;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.util.ElapsedTime;

@TeleOp(name="RunMotors")

@SuppressWarnings("unused")
public class RunMotors extends LinearOpMode
{
    private ElapsedTime runtime = new ElapsedTime();
    int tps = 1;

    private float minPow = 0.01f, maxPow = 0.1f;
    private float minTurningPow = 0.02f, maxTurningPow = 0.8f;

    private DcMotor leftDrive;
    private DcMotor rightDrive;

    private Gamepad c1;

    @Override
    public void runOpMode() throws InterruptedException
    {
        c1 = this.gamepad1;

        leftDrive = hardwareMap.get(DcMotor.class, "left_drive");
        rightDrive = hardwareMap.get(DcMotor.class, "right_drive");

        boolean run = true;
        if (c1 == null || leftDrive == null || rightDrive == null)
        {
            telemetry.addData("Status", "Hardware not found");
            telemetry.update();

            run = false;
        }

        waitForStart();
        runtime.reset();

        while (run)
        {
            sleep(1000 / tps);

            Direction dir = null;
            float pow = 1 / c1.left_stick_y * (maxPow - minPow) + minPow;
            boolean turning = true;

            if (c1.left_stick_y > 0)
            {
                dir = Direction.FORWARD;
                turning = false;
            }
            else if (c1.left_stick_y < 0)
            {
                dir = Direction.REVERSE;
                turning = false;
            }

            if (turning)
            {
                float turningPow = 1 / c1.left_stick_x * (maxTurningPow - minTurningPow) + minTurningPow;
                turn(turningPow);
            }

            setMotors(pow, dir);

            telemetry.addData("Status", "Running");
            telemetry.update();

            if (c1.x)
                break;
        }

        telemetry.addData("Status", "Terminated");
        telemetry.addData("Time elapsed", runtime.seconds() + "s");

        telemetry.update();
    }

    private void setMotors(float power, Direction dir)
    {
        leftDrive.setPower(power);
        leftDrive.setDirection(dir == null ? Direction.FORWARD : dir);

        rightDrive.setPower(power);
        rightDrive.setDirection(dir == null ? Direction.FORWARD : dir);
    }

    // -1 is left, 1 is right
    private void turn(float pow)
    {
        if (pow < 0)
        {
            leftDrive.setDirection(Direction.REVERSE);
            leftDrive.setPower(pow);

            rightDrive.setDirection(Direction.FORWARD);
            rightDrive.setPower(pow);
        }
        else if (pow > 0)
        {
            rightDrive.setDirection(Direction.REVERSE);
            rightDrive.setPower(pow);

            leftDrive.setDirection(Direction.FORWARD);
            leftDrive.setPower(pow);
        }
    }

}
