package org.firstinspires.ftc.teamcode.main;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.internal.android.dx.command.Main;
import org.firstinspires.ftc.teamcode.utils.MainProcess;
import org.firstinspires.ftc.teamcode.utils.State;
import org.firstinspires.ftc.teamcode.utils.TrackableObject;
import org.firstinspires.ftc.teamcode.utils.Vector;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.matrices.OpenGLMatrix;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;

import java.util.Arrays;
import java.util.List;

import static org.firstinspires.ftc.robotcore.external.navigation.AngleUnit.DEGREES;
import static org.firstinspires.ftc.robotcore.external.navigation.AxesOrder.XYZ;
import static org.firstinspires.ftc.robotcore.external.navigation.AxesOrder.YZX;
import static org.firstinspires.ftc.robotcore.external.navigation.AxesReference.EXTRINSIC;
import static org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer.CameraDirection.BACK;

public class Vuforia extends MainProcess {
    private static final String VUFORIA_KEY = "AcQ5+hb/////AAABmRx9QlQ0XkAwqC22l77KJChhFww31MgBQxZIHSnMRH3L+7j4JY0Ji/4hoCkNEfTYmDEnk/vwHmbm5+TWEBGXRIJgxKZ/xyn30akgPkZO36FAjZAF5h6hRbh1YkKE2OY8km3I2QJaq8kEcil2Suwk0P5PL4x9B6dMhBZBKbMH0EZMMjw4l/4wdIsUECGvvD1my9Wp7fb2wI5t68D8O5kWGMVyJJG9cT5qLb60yPCXp1oziHVif5L1mwqNNsqIoFSWlkYrtmw94oUmzLTTwpEVocITqVT3W0wtJG6tLLOPn3hi+Sl3F54VGetfR3G2HeVRu9ApnOFjTcRz/WrB2eFfEl7OX+KM3VVdpaLDgCWJEfkK";
    private VuforiaLocalizer vuforia = null;

    private static final VuforiaLocalizer.CameraDirection CAMERA_CHOICE = BACK;
    private static final boolean PHONE_IS_PORTRAIT = false;

    List<String> names = null;
    private List<TrackableObject> trackables;

    // Since ImageTarget trackables use mm to specifiy their dimensions, we must use
    // mm for all the physical dimension.
    // We will define some constants and conversions here
    private static final float mmPerInch = 25.4f;
    private static final float mmTargetHeight = (6) * mmPerInch; // the height of the center of the target image
    // above the floor

    // Constant for Stone Target
    private static final float stoneZ = 2.00f * mmPerInch;

    // Constants for the center support targets
    private static final float bridgeZ = 6.42f * mmPerInch;
    private static final float bridgeY = 23 * mmPerInch;
    private static final float bridgeX = 5.18f * mmPerInch;
    private static final float bridgeRotY = 59; // Units are degrees
    private static final float bridgeRotZ = 180;

    // Constants for perimeter targets
    private static final float halfField = 72 * mmPerInch;
    private static final float quadField = 36 * mmPerInch;

    private VuforiaTrackables targetsSkyStone = null;
    private OpenGLMatrix lastLocation = null;
    private boolean targetVisible = false;
    private float phoneXRotate = 0;
    private float phoneYRotate = 0;
    private float phoneZRotate = 0;

    public Vuforia(LinearOpMode opMode) {
        super(opMode);
    }

    public void init() {
        int cameraMonitorViewId = opMode.hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId",
                "id", opMode.hardwareMap.appContext.getPackageName());
        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters(cameraMonitorViewId);

        parameters.vuforiaLicenseKey = VUFORIA_KEY;
        parameters.cameraDirection = CAMERA_CHOICE;

        // instantiate vuforia
        vuforia = ClassFactory.getInstance().createVuforia(parameters);

        targetsSkyStone = this.vuforia.loadTrackablesFromAsset("Skystone");

        String[] names_arr = {
                "Stone Target",
                "Blue Rear Bridge",
                "Red Rear Bridge",
                "Red Front Bridge",
                "Blue Front Bridge",
                "Red Perimeter 1",
                "Red Perimeter 2",
                "Front Perimeter 1",
                "Front Perimeter 2",
                "Blue Perimeter 1",
                "Blue Perimeter 2",
                "Rear Perimeter 1",
                "Rear Perimeter 2"
        };
        names = Arrays.asList(names_arr);

        trackables = TrackableObject.generateList(names, targetsSkyStone);

        getTarget("Stone Target")
                .setLocation(generateLocation(0, 0, stoneZ, 90, 0, -90));
        getTarget("Blue Front Bridge")
                .setLocation(generateLocation(-bridgeX, bridgeY, bridgeZ, 0, bridgeRotY, bridgeRotZ));
        getTarget("Blue Rear Bridge")
                .setLocation(generateLocation(-bridgeX, bridgeY, bridgeZ, 0, -bridgeRotY, bridgeRotZ));
        getTarget("Red Front Bridge")
                .setLocation(generateLocation(-bridgeX, -bridgeY, bridgeZ, 0, -bridgeRotY, 0));
        getTarget("Red Rear Bridge")
                .setLocation(generateLocation(bridgeX, -bridgeY, bridgeZ, 0, bridgeRotY, 0));
        getTarget("Red Perimeter 1")
                .setLocation(generateLocation(quadField, -halfField, mmTargetHeight, 90, 0, 180));
        getTarget("Red Perimeter 2")
                .setLocation(generateLocation(-quadField, -halfField, mmTargetHeight, 90, 0, 180));
        getTarget("Front Perimeter 1")
                .setLocation(generateLocation(0-halfField, -quadField, mmTargetHeight, 90, 0, 90));
        getTarget("Front Perimeter 2")
                .setLocation(generateLocation(-halfField, quadField, mmTargetHeight, 90, 0, 90));
        getTarget("Blue Perimeter 1")
                .setLocation(generateLocation(-quadField, halfField, mmTargetHeight, 90, 0, 0));
        getTarget("Blue Perimeter 2")
                .setLocation(generateLocation(quadField, halfField, mmTargetHeight, 90, 0, 0));
        getTarget("Rear Perimeter 1")
                .setLocation(generateLocation(halfField, quadField, mmTargetHeight, 90, 0, -90));
        getTarget("Rear Perimeter 2")
                .setLocation(generateLocation(halfField, -quadField, mmTargetHeight, 90, 0, -90));

        // We need to rotate the camera around it's long axis to bring the correct
        // camera forward.
        if (CAMERA_CHOICE == BACK) {
            phoneYRotate = -90;
        } else {
            phoneYRotate = 90;
        }

        // Rotate the phone vertical about the X axis if it's in portrait mode
        if (PHONE_IS_PORTRAIT) {
            phoneXRotate = 90;
        }

        // Next, translate the camera lens to where it is on the robot.
        // In this example, it is centered (left to right), but forward of the middle of
        // the robot, and above ground level.
        final float CAMERA_FORWARD_DISPLACEMENT = 4.0f * mmPerInch; // eg: Camera is 4 Inches in front of robot
        // center
        final float CAMERA_VERTICAL_DISPLACEMENT = 8.0f * mmPerInch; // eg: Camera is 8 Inches above ground
        final float CAMERA_LEFT_DISPLACEMENT = 0; // eg: Camera is ON the robot's center line

        OpenGLMatrix robotFromCamera = OpenGLMatrix
                .translation(CAMERA_FORWARD_DISPLACEMENT, CAMERA_LEFT_DISPLACEMENT,
                        CAMERA_VERTICAL_DISPLACEMENT)
                .multiplied(Orientation.getRotationMatrix(EXTRINSIC, YZX, DEGREES, phoneYRotate,
                        phoneZRotate, phoneXRotate));

        /** Let all the trackable listeners know where the phone is. */
        for (TrackableObject trackable : trackables) {
            trackable.setPhoneLocation(robotFromCamera, parameters.cameraDirection);
        }

        targetsSkyStone.activate();
    }

    public State main() {
        // check all the trackable targets to see which one (if any) is visible.
        targetVisible = false;
        TrackableObject visibleTarget = null;
        for (TrackableObject trackable : trackables) {
            if (trackable.isVisible()) {
                targetVisible = true;
                visibleTarget = trackable;

                // getUpdatedRobotLocation() will return null if no new information is available
                // since
                // the last time that call was made, or if the trackable is not currently
                // visible.
                OpenGLMatrix robotLocationTransform = trackable.getPosition();
                if (robotLocationTransform != null) {
                    lastLocation = robotLocationTransform;
                }
                break;
            }
        }

        if (targetVisible) {
            return new VuforiaState(visibleTarget);
        } else {
            return VuforiaState.targetNotFound();
        }
    }

    public void cleanup() {
        targetsSkyStone.deactivate();
    }

    private TrackableObject getTarget(String name) {
        return trackables.get(names.indexOf(name));
    }

    private OpenGLMatrix generateLocation(float dx, float dy, float dz, float firstAngle, float secondAngle, float thirdAngle) {
        return OpenGLMatrix.translation(dx, dy, dz).
                multiplied(Orientation.getRotationMatrix(EXTRINSIC, XYZ, DEGREES, firstAngle, secondAngle, thirdAngle));
    }
}

class VuforiaState extends State {
    public boolean targetFound;
    public TrackableObject visibleTarget;
    public Vector absPosition;

    public static VuforiaState targetNotFound() {
        return new VuforiaState();
    }

    private VuforiaState() {
        this.targetFound = false;
        this.visibleTarget = null;
        this.absPosition = null;
    }

    public VuforiaState(TrackableObject visibleTarget) {
        this.targetFound = true;
        this.visibleTarget = visibleTarget;
        this.absPosition = visibleTarget.getAbsRobotPos();
    }
}