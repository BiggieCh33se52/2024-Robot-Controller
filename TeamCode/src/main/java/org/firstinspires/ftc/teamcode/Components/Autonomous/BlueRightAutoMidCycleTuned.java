package org.firstinspires.ftc.teamcode.Components.Autonomous;

import static org.firstinspires.ftc.teamcode.Components.Claw.ClawStates.CLAW_CLOSED;
import static org.firstinspires.ftc.teamcode.Components.Lift.LiftConstants.LIFT_HIGH_JUNCTION;
import static org.firstinspires.ftc.teamcode.Components.Lift.LiftConstants.LIFT_MED_JUNCTION;
import static org.firstinspires.ftc.teamcode.Robots.BasicRobot.logger;
import static java.lang.Math.toRadians;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.Robots.PwPRobot;
import org.firstinspires.ftc.teamcode.roadrunner.drive.DriveConstants;
import org.firstinspires.ftc.teamcode.roadrunner.drive.SampleMecanumDrive;
import org.firstinspires.ftc.teamcode.roadrunner.trajectorysequence.TrajectorySequence;

import java.util.ArrayList;

@Config
@Autonomous(name = "BlueRightAutoMidCycleTuned")


public class BlueRightAutoMidCycleTuned extends LinearOpMode {
    private SampleMecanumDrive roadrun;

    public static double dummyP = 3;

    public static double dropX = -29.5, dropY = 20, dropA = toRadians(210), dropET = toRadians(30);

    public static double pickupX1 = -46, pickupY1 = 10, pickupA1 = toRadians(180), pickupET1 = toRadians(180);
    public static double pickupX2 = -62, pickupY2 = 13, pickupA2 = toRadians(180), pickupET2 = toRadians(180);

    double[] stackPos = {440,330,245,100,0};

    public void runOpMode() {
        PwPRobot robot = new PwPRobot(this, false);
        robot.roadrun.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        Pose2d startPose = new Pose2d(-29.6, 62.25, toRadians(90));
        robot.roadrun.setPoseEstimate(startPose);

        //detectSignal();
        //store in variable
        robot.cv.observeSleeve();
        TrajectorySequence initialtrajectory = robot.roadrun.trajectorySequenceBuilder(new Pose2d(-29.6, 63.25, toRadians(90)))
                .setReversed(true)
                .splineToSplineHeading(new Pose2d(-32, 51, toRadians(70)), toRadians(250))
                .splineToSplineHeading(new Pose2d(-34, 9, toRadians(225)), toRadians(270))
                .lineToLinearHeading(new Pose2d(-28,17, toRadians(225)))
                .addTemporalMarker(robot::done)
                .build();
        TrajectorySequence pickupTrajectory = robot.roadrun.trajectorySequenceBuilder(new Pose2d(-28,17, toRadians(225)))
                .setReversed(false)
                .splineToSplineHeading(new Pose2d(pickupX2+5-0.5,pickupY2-0.5,toRadians(180)),toRadians(180))
//                .splineToSplineHeading()
                .splineToSplineHeading(new Pose2d(pickupX2-0.5,pickupY2-0.5,pickupA2),pickupET2)
//                .addTemporalMarker(()->{robot.done(); robot.roadrun.breakFollowing();})
                .addTemporalMarker(robot::done)
                .build();
//        TrajectorySequence dropTrajectory = robot.roadrun.trajectorySequenceBuilder(new Pose2d(pickupX2, pickupY2, pickupA2))
//                .setReversed(true)
////                .splineToSplineHeading(new Pose2d(pickupX2+15, pickupY2,pickupA2),0)
//                .splineToSplineHeading(new Pose2d(dropX,dropY,dropA),dropET)
//                .UNSTABLE_addTemporalMarkerOffset(0.4,robot::done)
//                .build();
        ArrayList<TrajectorySequence> dropTrajectory = new ArrayList<>();
        for(int i=0;i<5;i++){
            dropTrajectory.add(robot.roadrun.trajectorySequenceBuilder(new Pose2d(pickupX2-0.5, pickupY2-0.5, pickupA2))
                    .setReversed(true)
//                .splineToSplineHeading(new Pose2d(pickupX2+15, pickupY2,pickupA2),0)
                    .splineToSplineHeading(new Pose2d(dropX+(i+1)*0.4,dropY-(i+1)*0.7,dropA),dropET)
//                    .UNSTABLE_addTemporalMarkerOffset(0.4,robot::done)
                            .addTemporalMarker(robot::done)
                    .build());
        }
        TrajectorySequence pickupTrajectory2 = robot.roadrun.trajectorySequenceBuilder(new Pose2d(dropX,dropY, dropA))
                .setReversed(false)
                .splineToSplineHeading(new Pose2d(pickupX2-0.5, pickupY2-0.5, pickupA2), pickupET2)
//                .addTemporalMarker(()->{robot.done(); robot.roadrun.breakFollowing();})
                .addTemporalMarker(robot::done)
                .build();
        TrajectorySequence parkTrajectory = robot.roadrun.trajectorySequenceBuilder(new Pose2d(dropX,dropY, dropA))
                .splineToSplineHeading(new Pose2d(-36, 33, toRadians(90)), toRadians(90))
                .build();
        TrajectorySequence park1trajectory = robot.roadrun.trajectorySequenceBuilder(new Pose2d(dropX,dropY, dropA))
                .setReversed(false)
//                .splineToSplineHeading(new Pose2d(-50, 10, toRadians(180)), toRadians(180))
//                .setReversed(true)
//                .lineTo(new Vector2d(-10, 9))
                .splineToLinearHeading(new Pose2d(-10,9,toRadians(270)), toRadians(135))
                .build();
        Trajectory park2trajectory = robot.roadrun.trajectoryBuilder(new Pose2d(dropX,dropY, dropA))
                .lineToLinearHeading(new Pose2d(-33, 13, toRadians(180)))
                .build();
        TrajectorySequence park3trajectory = robot.roadrun.trajectorySequenceBuilder(new Pose2d(dropX,dropY, dropA))
                .setReversed(false)
                .splineToSplineHeading(new Pose2d(-58, 12, toRadians(180)), toRadians(180))
                .build();
        while(!isStarted()){
            telemetry.addData("pos",robot.cv.getPosition());
            telemetry.addData("CLAW_CLOSED:", CLAW_CLOSED.getStatus());
            telemetry.update();
            robot.updateClawStates();
            robot.updateLiftArmStates();
        }
        resetRuntime();
        dummyP = robot.cv.getPosition();

        if (isStopRequested()) return;


        while (opModeIsActive() && !isStopRequested() && getRuntime()<29.8) {
            logger.loopcounter++;
            robot.followTrajectorySequenceAsync(initialtrajectory);
            robot.delay(0.3);
            robot.raiseLiftArmToOuttake(true);
            robot.delay(0.5);
            robot.liftToPosition(LIFT_MED_JUNCTION);
            robot.openClaw(false);
            robot.delay(0.4);
            robot.cycleLiftArmToCycle(true);
            robot.delay(0.5);
            robot.wideClaw();
            robot.delay(0.5);
            robot.liftToPosition((int) stackPos[0]);
            robot.followTrajectorySequenceAsync(pickupTrajectory);
            robot.closeClaw(false);
            robot.followTrajectorySequenceAsync(dropTrajectory.get(0));
            robot.liftToPosition((int)LIFT_MED_JUNCTION.getValue(), true);
            robot.delay(0.55);
            robot.raiseLiftArmToOuttake(true);
            robot.delay(0.25);
            robot.openClaw(false);
            for (int i = 0; i < 4; i++) {
                robot.followTrajectorySequenceAsync(pickupTrajectory2);
                if(i!=3) {
                    robot.cycleLiftArmToCycle(true);
                }else{
                    robot.lowerLiftArmToIntake(true);
                }
                robot.delay(0.5);
                robot.wideClaw();
                robot.delay(0.5);
                robot.liftToPosition((int) stackPos[i + 1]);
                robot.closeClaw(false);
                robot.followTrajectorySequenceAsync(dropTrajectory.get(i));
                robot.delay(0.0+0.005*(3-i));
                robot.liftToPosition(LIFT_MED_JUNCTION);
                robot.delay(0.3+0.005*(3-i));
                robot.raiseLiftArmToOuttake(true);
                robot.delay(0.15);
                robot.openClaw(false);
            }
//
//            robot.lowerLiftArmToIntake(false);
//            robot.delay(1);
//            robot.wideClaw();
//            robot.delay(0.5);
//            robot.liftToPosition((int) stackPos[4]);
//            robot.followTrajectorySequenceAsync(pickupTrajectory2);
//            robot.waitForFinish();
//            robot.closeClaw(false);
//            robot.waitForFinish();
//            robot.raiseLiftArmToOuttake(true);
//            robot.delay(0.3);
//            robot.liftToPosition(LIFT_HIGH_JUNCTION);
//            robot.followTrajectorySequenceAsync(dropTrajectory);
//            robot.delay(1.5);
//            robot.openClaw();
//            robot.waitForFinish();
//            robot.lowerLiftArmToIntake(true);
//            robot.delay(1);
//            robot.liftToPosition(0);
//            robot.delay(0.7);

            robot.delay(0.9);
            robot.lowerLiftArmToIntake(true);
            robot.delay(1.6);
            robot.wideClaw();
            robot.delay(2.0);
            robot.liftToPosition(0);


            if (dummyP == 1) {
                robot.followTrajectorySequenceAsync(park1trajectory);
            } else if (dummyP == 3) {
                robot.followTrajectorySequenceAsync(park3trajectory);
            } else {
                robot.followTrajectoryAsync(park2trajectory);
            }

            robot.setFirstLoop(false);
            robot.liftToTargetAuto();
            robot.roadrun.update();
            robot.updateClawStates();
            robot.updateLiftArmStates();

        }
        robot.stop();
        if (getRuntime() > 29.8) {
            stop();
        }
    }
}