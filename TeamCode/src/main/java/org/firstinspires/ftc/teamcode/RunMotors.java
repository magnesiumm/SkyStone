package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple.Direction;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.util.ElapsedTime;

@TeleOp(name="RunMotors")

// This may or may not work, I really don't know.

@SuppressWarnings("unused")
public class RunMotors extends LinearOpMode
{
    private ElapsedTime runtime = new ElapsedTime();
    int tps = 1;

    private float minPow = 0.01f, maxPow = 0.1f;

    private String[] motorNames = { "left_drive", "right_drive" };
    private DcMotor[] motors = new DcMotor[motorNames.length];

    private Gamepad c1;

    @Override
    public void runOpMode() throws InterruptedException
    {
        c1 = this.gamepad1;
        initMotors();

        boolean run = true;
        if (c1 == null)
        {
            telemetry.addData("Status", "Controller not found");
            telemetry.update();

            run = false;
        }

        waitForStart();
        runtime.reset();

        while (run)
        {
            sleep(1000/tps);

            Direction dir = null;
            float pow = 1 / c1.left_stick_y * (maxPow - minPow) + minPow;

            if (c1.left_stick_y > 0)
                dir = Direction.FORWARD;
            else if (c1.left_stick_y < 0)
                dir = Direction.REVERSE;

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

    private void initMotors()
    {
        for (int i = 0; i < motors.length; i++)
            motors[i] = hardwareMap.get(DcMotor.class, motorNames[i]);

        telemetry.addData("Status", "Initialized");
        telemetry.update();
    }

    private void setMotors(float power, Direction dir)
    {
        for (DcMotor motor : motors)
        {
            motor.setPower(power);
            motor.setDirection(dir);
        }
    }


}
