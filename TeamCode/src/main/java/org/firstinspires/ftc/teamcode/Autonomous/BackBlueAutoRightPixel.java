package org.firstinspires.ftc.teamcode.Autonomous;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.trajectory.constraints.AngularVelocityConstraint;
import com.acmerobotics.roadrunner.trajectory.constraints.MinVelocityConstraint;
import com.acmerobotics.roadrunner.trajectory.constraints.TrajectoryVelocityConstraint;
import com.acmerobotics.roadrunner.trajectory.constraints.TranslationalVelocityConstraint;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.RoadRunner.drive.SampleMecanumDrive;
import org.firstinspires.ftc.teamcode.Variables.Detection;
import org.firstinspires.ftc.teamcode.Variables.VisionProcessors;

import java.util.Arrays;

@Config
@Autonomous(name = "BBRight", group = "Linear OpMode")
public class BackBlueAutoRightPixel extends MeepMeepBoilerplate{
    @Override
    public void runOpMode() {
        SampleMecanumDrive drive = new SampleMecanumDrive(hardwareMap);
        Servo passiveServo = hardwareMap.get(Servo.class, "passiveServo");
        Servo autoServo = hardwareMap.get(Servo.class, "autoServo");
        initVision(VisionProcessors.TFOD);
        Detection detection = Detection.UNKNOWN;
        TrajectoryVelocityConstraint slowConstraint = new MinVelocityConstraint(Arrays.asList(

                new TranslationalVelocityConstraint(20),

                new AngularVelocityConstraint(1)

        ));
        while (opModeInInit()) {
            detection = getDetectionsSingleTFOD();
            telemetry.addData("Detection", detection);
            telemetry.update();
        }
        autoServo.setPosition(0.52);
        switch (detection) {
            case LEFT -> drive.followTrajectorySequence(
                    drive.trajectorySequenceBuilder(getCurrentPosition(drive))
                            .forward(26.0)
                            .turn(Math.toRadians(90))
                            .forward(8)
                            .addTemporalMarker(() -> passiveServo.setPosition(0.1))
                            .waitSeconds(.25)
                            .back(8)
                            .strafeRight(25)
                            .waitSeconds(.5)
                            .forward(18)
                            .setVelConstraint(slowConstraint)
                            .waitSeconds(.25)
                            .forward(63)
                            .waitSeconds(.25)
                            .turn(Math.toRadians(90))
                            .waitSeconds(.25)
                            .forward(23)
                            .waitSeconds(.25)
                            .strafeLeft(4)
                            .addTemporalMarker(() -> autoServo.setPosition(0.75))
                            .waitSeconds(1)
                            .addTemporalMarker(() -> autoServo.setPosition(0.4))
                            .build()
            );
            case CENTER -> { drive.followTrajectorySequence(
                    drive.trajectorySequenceBuilder(getCurrentPosition(drive))
                            .forward(30)
                            .addDisplacementMarker(() -> passiveServo.setPosition(0.1))
                            .build());
            }
            case RIGHT -> drive.followTrajectorySequence(
                    drive.trajectorySequenceBuilder(getCurrentPosition(drive))
                            .forward(28.0)
                            .turn(Math.toRadians(-90))
                            .forward(2)
                            .addTemporalMarker(() -> passiveServo.setPosition(0.1))
                            .waitSeconds(.25)
                            .back(5)
                            .strafeLeft(20.5)
                            .waitSeconds(.5)
                            .back(18)
                            .setVelConstraint(slowConstraint)
                            .waitSeconds(.25)
                            .back(63)
                            .waitSeconds(.25)
                            .turn(Math.toRadians(90))
                            .waitSeconds(.25)
                            .back(19)
                            .waitSeconds(.25)
                            .strafeLeft(4)
                            .addTemporalMarker(() -> autoServo.setPosition(0.75))
                            .waitSeconds(1)
                            .addTemporalMarker(() -> autoServo.setPosition(0.4))
                            .build()
            );
            default -> {
                telemetry.addLine("Warning: Cup not detected");
                telemetry.update();
                sleep(3000);
            }
        }



//        drive.followTrajectorySequence(mergeSequences(sequences));
    }
}
