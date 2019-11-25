package org.firstinspires.ftc.teamcode;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;

/*

    The system in place for autonomous movement requires the robot to have it's own position.
    It also requires the robot to know it's angle of rotation, so it might not even be possible.
    Given the necessary variables, it, in all likelihood, still will not work.

*/

@Autonomous(name="Autonomous Contoller", group="LinearOpMode")

@SuppressWarnings("unused")
public class AutonomousController extends LinearOpMode
{
    // How close (in grid units) the robot needs to be to the point (dx, dy).
    private final float AUTOPILOT_SENSITIVITY = 0.2f;
    // How close the angle of the robot has to be to the angle between points (x, y) and (dx, dy), in radians.
    private final float AUTOPILOT_TURN_SENSITIVITY = (float) (Math.PI / 16d);

    private ElapsedTime runtime = new ElapsedTime();
    int tps = 1;

    private float minPow = 0.01f, maxPow = 0.1f;
    private float minTurningPow = 0.02f, maxTurningPow = 0.8f;

    private DcMotor leftDrive;
    private DcMotor rightDrive;

    private float dx = 5, dy = 5;
    private float x = 0, y = 0, angle = (float) (Math.PI / 2d);

    @Override
    public void runOpMode()
    {
        leftDrive = hardwareMap.get(DcMotor.class, "left_drive");
        rightDrive = hardwareMap.get(DcMotor.class, "right_drive");

        if (leftDrive == null || rightDrive == null)
        {
            telemetry.addData("Status", "Hardware not found");
            telemetry.update();

            return;
        }

        waitForStart();
        runtime.reset();

        while (opModeIsActive())
        {
            sleep(1000 / tps);

            double distance = Math.sqrt((dx - x) * (dx - x) + (dy - y) * (dy - y));
            if (distance > AUTOPILOT_SENSITIVITY)
            {
                updateAngle();

                // Gets the direction "vector."
                double[] dir = {dx - x, dy - y};

                // Calculates the reference and actual angles between the points (from the x-axis).
                double alpha = Math.abs(Math.atan(dir[1] / dir[0]));
                double theta = getAngle(alpha, dir[0], dir[1]);

                float turningPow = (maxTurningPow + minTurningPow) / 2f;
                if (angle > theta + AUTOPILOT_TURN_SENSITIVITY)
                    turn(turningPow);
                else if (angle < theta - AUTOPILOT_TURN_SENSITIVITY)
                    turn(-turningPow);
                else
                    setMotors((maxPow + minPow) / 2f, DcMotorSimple.Direction.FORWARD);

            }


            telemetry.addData("Status", "Running");
            telemetry.update();
        }

        telemetry.addData("Status", "Terminated");
        telemetry.addData("Time elapsed", runtime.seconds() + "s");

        telemetry.update();
    }

    private void setMotors(float power, DcMotorSimple.Direction dir)
    {
        leftDrive.setPower(power);
        leftDrive.setDirection(dir == null ? DcMotorSimple.Direction.FORWARD : dir);

        rightDrive.setPower(power);
        rightDrive.setDirection(dir == null ? DcMotorSimple.Direction.FORWARD : dir);
    }

    // -1 is left, 1 is right
    private void turn(float pow)
    {
        if (pow < 0)
        {
            leftDrive.setDirection(DcMotorSimple.Direction.REVERSE);
            leftDrive.setPower(pow);

            rightDrive.setDirection(DcMotorSimple.Direction.FORWARD);
            rightDrive.setPower(pow);
        }
        else if (pow > 0)
        {
            rightDrive.setDirection(DcMotorSimple.Direction.REVERSE);
            rightDrive.setPower(pow);

            leftDrive.setDirection(DcMotorSimple.Direction.FORWARD);
            leftDrive.setPower(pow);
        }
    }

    private void updateAngle()
    {
        double alpha = Math.atan(y / x);
        angle = (float) (getAngle(alpha, x, y));
    }

    // in radians
    private double getAngle(double refAngle, double x, double y)
    {
        if (y < Math.PI / 2 && x < Math.PI / 2)
            return refAngle;
        else if (y < Math.PI && x < Math.PI)
            return 180d - refAngle;
        else if (y < 2 * Math.PI / 3 && x < 2 * Math.PI / 3)
            return 180d + refAngle;
        else if (y < 2 * Math.PI && x < 2 * Math.PI)
            return 360d - refAngle;

        return 0;
    }

}
