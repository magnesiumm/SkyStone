package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.OpticalDistanceSensor;
import com.qualcomm.robotcore.util.ElapsedTime;

/*
Test class for Optical Distance Sensor (ODS)
 */

@TeleOp(name="Using ODS", group="Linear Opmode")
//@Disabled
public class UsingODS extends LinearOpMode {
    private ElapsedTime runtime = new ElapsedTime();

    OpticalDistanceSensor ods;

    @Override
    public void runOpMode() throws InterruptedException {
        telemetry.addData("Status", "Initialized");
        telemetry.update();

        // name of ODS in configuration on DS must be "ods"
        ods = hardwareMap.opticalDistanceSensor.get("ods");
        ods.enableLed(true);

        waitForStart();
        runtime.reset();

        while (opModeIsActive()) {
            telemetry.addData("Light Detected: ", ods.getLightDetected());
            telemetry.update();

            /*
            ods.getLightDetected returns value between 0 and 1 based on amount of light
            if closer, value is higher
            if color is lighter, value is higher (white sheet returns more than darker colors)
            */
        }
    }
}
