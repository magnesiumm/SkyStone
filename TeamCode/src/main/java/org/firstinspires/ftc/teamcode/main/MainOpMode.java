package org.firstinspires.ftc.teamcode.main;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

@TeleOp(name="Main", group="Linear Opmode")
//@Disabled
public class MainOpMode extends LinearOpMode {
    private ElapsedTime runtime = new ElapsedTime();

    private Vuforia vuforia = null;
    private DriveInput drive = null;

    @Override
    public void runOpMode() throws InterruptedException {
        telemetry.addData("Status", "Initialized");
        telemetry.update();

        // instantiate processes
        vuforia = new Vuforia(this);
        drive = new DriveInput(this);

        // initialize processes
        vuforia.init();
        drive.init();

        waitForStart();
        runtime.reset();

        while (opModeIsActive()) {
            telemetry.addData("Status", "Run Time: " + runtime.toString());

            // get state from processes
            VuforiaState vuforiaState = (VuforiaState)vuforia.main();
            DriveState driveState = (DriveState)drive.main();

            telemetry.addData("Stick Input", String.format("%.2f %.2f", driveState.stickX, driveState.stickY));
            telemetry.addData("Power", driveState.drivePower);
            telemetry.addData("Direction", driveState.direction);

            telemetry.update();
        }

        vuforia.cleanup();
    }
}
