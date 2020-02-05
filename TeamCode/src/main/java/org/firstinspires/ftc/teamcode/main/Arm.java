package org.firstinspires.ftc.teamcode.main;

import android.graphics.Color;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.utils.MainProcess;
import org.firstinspires.ftc.teamcode.utils.State;

public class Arm extends MainProcess {
    private ColorSensor colorSensor;
    private DcMotor armMotor;

    public Arm(LinearOpMode opMode) {
        super(opMode);
    }

    @Override
    public void init() {
        colorSensor = opMode.hardwareMap.colorSensor.get("arm_color");
        armMotor = opMode.hardwareMap.dcMotor.get("arm_motor");

        colorSensor.enableLed(true);
    }

    @Override
    public ArmState main() {
        armMotor.setPower(0);

        float hsv[] = new float[3];
        Color.RGBToHSV(colorSensor.red(), colorSensor.green(), colorSensor.blue(), hsv);
        float angle = hsv[0];
        double power = getResistancePower(angle);

        armMotor.setPower(power);

        return new ArmState(angle, power);
    }

    private double getResistancePower(float angle) {
        return 0;
    }
}

class ArmState extends State {
    public float angle;
    public double power;

    public ArmState(float angle, double power) {
        this.angle = angle;
        this.power = power;
    }
}