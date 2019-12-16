package org.firstinspires.ftc.teamcode.utils;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

/*
Template class for main processes
 */
public class MainProcess {
    protected LinearOpMode opMode;

    public MainProcess(LinearOpMode opMode) {
        this.opMode = opMode;
    }

    public void init() {}
    public State main() { return null; }
    public void cleanup() {}
}

