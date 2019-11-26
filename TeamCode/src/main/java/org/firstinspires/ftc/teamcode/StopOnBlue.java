package org.firstinspires.ftc.teamcode;

import android.graphics.Color;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;

@Autonomous(name="StopOnBlue", group="Linear Opmode")
//@Disabled
public class StopOnBlue extends LinearOpMode {
    /*
    REPRESENTED IN UPSIDE DOWN CONE

    hue - type of color (rgb)
        measured in degrees of rotation around top of cone
    saturation - degree of richness (0->1)
        distance from center
    value - brightness level (0->1), 0 always results in black
        distance from bottom

    Color = HSV or RGB
     */

    private ElapsedTime runtime = new ElapsedTime();

    private DcMotor leftMotor = null;
    private DcMotor rightMotor = null;
    private ColorSensor cs = null;

    double power;

    @Override
    public void runOpMode() throws InterruptedException {
        telemetry.addData("Status", "Initialized");
        telemetry.update();

        leftMotor = hardwareMap.dcMotor.get("left_drive");
        leftMotor.setDirection(DcMotorSimple.Direction.FORWARD);
        rightMotor = hardwareMap.dcMotor.get("right_drive");
        rightMotor.setDirection(DcMotorSimple.Direction.FORWARD);

        hardwareMap.touchSensor.get("touch_sensor");

        cs = hardwareMap.colorSensor.get("cs");
        cs.enableLed(false);

        sleep(500);

        cs.enableLed(true);

        waitForStart();
        runtime.reset();

        updatePower(0.5);

        while (opModeIsActive()) {
            float hsv[] = new float[3];
            Color.RGBToHSV(cs.red(), cs.green(), cs.blue(), hsv);
            /*
            hsv[0] is hue [0, 360]
            hsv[1] is saturation [0, 1]
            hsv[2] is value [0, 1]
             */

            /*
            CONDITIONS:
            saturation >= .6 to ensure getting good color and not background one
            hue > 210 && hue < 275 for blue
             */
            if (hsv[1] >= .6 && hsv[0] >= 210 && hsv[0] <= 275) {
                updatePower(0.0);
                cs.enableLed(false);
            }

            telemetry.update();
        }
    }

    public void updatePower(double new_power) {
        power = new_power;

        leftMotor.setPower(power);
        rightMotor.setPower(power);
    }
}
