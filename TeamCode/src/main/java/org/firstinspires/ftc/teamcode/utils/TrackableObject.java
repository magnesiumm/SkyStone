package org.firstinspires.ftc.teamcode.utils;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.matrices.OpenGLMatrix;
import org.firstinspires.ftc.robotcore.external.matrices.VectorF;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackableDefaultListener;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class TrackableObject {
    private int targetIdx;
    private String name;
    private VuforiaTrackable target;

    private static final float mmPerInch = 25.4f;

    public static List<TrackableObject> generateList(List<String> names, VuforiaTrackables targets) {
        List<TrackableObject> ret = new ArrayList<>();

        for (int i = 0; i < names.size(); i++) {
            ret.add(new TrackableObject(i, names.get(i), targets.get(i)));
        }

        return ret;
    }

    public TrackableObject(int targetIdx, String name, VuforiaTrackables targets) {
        this.targetIdx = targetIdx;
        this.name = name;

        this.target = targets.get(targetIdx);
        this.target.setName(name);
    }

    public TrackableObject(int targetIdx, String name, VuforiaTrackable target) {
        this.targetIdx = targetIdx;
        this.name = name;

        this.target = target;
        this.target.setName(name);
    }

    public void setLocation(OpenGLMatrix locMat) {
        target.setLocation(locMat);
    }

    public void setPhoneLocation(OpenGLMatrix robotFromCamera, VuforiaLocalizer.CameraDirection direction) {
        ((VuforiaTrackableDefaultListener) target.getListener()).setPhoneInformation(robotFromCamera, direction);
    }

    public boolean isVisible() {
        return ((VuforiaTrackableDefaultListener) target.getListener()).isVisible();
    }

    /*
    Returns relative position of robot in mm as matrix
     */
    public OpenGLMatrix getPosition() {
        return ((VuforiaTrackableDefaultListener) target.getListener()).getUpdatedRobotLocation();
    }

    /*
    Returns relative position of robot in inches as vector
     */
    public Vector getRelRobotPos() {
        VectorF translation = getPosition().getTranslation();

        return new Vector(
            translation.get(0) / mmPerInch,
            translation.get(1) / mmPerInch,
            translation.get(2) / mmPerInch
        );
    }

    /*
    Returns position of target relative to center in inches
     */
    public Vector getTargetLocation() {
        VectorF translation = target.getLocation().getTranslation();

        return new Vector(
            translation.get(0) / mmPerInch,
            translation.get(1) / mmPerInch,
            translation.get(2) / mmPerInch
        );
    }

    /*
    Returns position of robot relative to center in inches
     */
    public Vector getAbsRobotPos() {
        return getTargetLocation().subtract(getRelRobotPos());
    }

    /*
    Returns name of trackable target
     */
    public String getName() {
        return name;
    }
}