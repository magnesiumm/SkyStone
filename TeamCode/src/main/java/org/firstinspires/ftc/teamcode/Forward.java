package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;

@Autonomous(name="Forward", group="Linear Opmode")
//@Disabled
public class Forward extends LinearOpMode {
    private ElapsedTime runtime = new ElapsedTime();

    private DcMotor leftMotor = null;

    double power = 0.5;

    @Override
    public void runOpMode() throws InterruptedException {
        telemetry.addData("Status", "Initialized");
        telemetry.update();

        leftMotor = hardwareMap.dcMotor.get("left_drive");
        leftMotor.setDirection(DcMotorSimple.Direction.FORWARD);

        waitForStart();
        runtime.reset();

        leftMotor.setPower(power);

        sleep(2000);

        power = 0.0;

        leftMotor.setPower(power);
    }
}
