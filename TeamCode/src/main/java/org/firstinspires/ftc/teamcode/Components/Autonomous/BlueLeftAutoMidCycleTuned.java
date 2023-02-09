package org.firstinspires.ftc.teamcode.Components.Autonomous;

import static org.firstinspires.ftc.teamcode.Components.Claw.ClawStates.CLAW_CLOSED;
import static org.firstinspires.ftc.teamcode.Components.Lift.LiftConstants.LIFT_MED_JUNCTION;
import static org.firstinspires.ftc.teamcode.Robots.BasicRobot.logger;
import static java.lang.Math.toRadians;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.Robots.PwPRobot;
import org.firstinspires.ftc.teamcode.roadrunner.trajectorysequence.TrajectorySequence;

@Config
@Autonomous(name = "BlueLeftAutoMidCycleTuned")


public class BlueLeftAutoMidCycleTuned extends LinearOpMode {

    public static double dummyP = 3;

    public static double dropX = 30.25, dropY = 22.2 , dropA = toRadians(330), dropET = toRadians(150);

    public static double pickupX1 = -46, pickupY1 = 10, pickupA1 = toRadians(180), pickupET1 = toRadians(180);
    public static double pickupX2 = 64, pickupY2 = 14.25, pickupA2 = toRadians(0), pickupET2 = toRadians(180);

    double[] stackPos = {400,330,235,80,0};

    public void runOpMode() {
        PwPRobot robot = new PwPRobot(this, false);
        robot.roadrun.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        Pose2d startPose = new Pose2d(42, 63.25, Math.toRadians(90));
        robot.roadrun.setPoseEstimate(startPose);

        //detectSignal();
        //store in variable
        robot.cv.observeSleeve();
        TrajectorySequence initialTrajectoryPart2 = robot.roadrun.trajectorySequenceBuilder(new Pose2d(37, 47, Math.toRadians(70)))
                .setReversed(true)
                .splineToLinearHeading(new Pose2d(dropX,dropY-1,dropA),dropET)
                .addTemporalMarker(robot::done)
                .build();
        TrajectorySequence initialtrajectory = robot.roadrun.trajectorySequenceBuilder(new Pose2d(42.85, 63.25, Math.toRadians(90)))
                .setReversed(true)
//                .splineTo(new Vector2d(34,50),toRadians(270))
//                .lineTo(new Vector2d(37.5,56))
//                .splineToSplineHeading(new Pose2d(32, 30.5, toRadians(45)), toRadians(270))
                .splineToSplineHeading(new Pose2d(38, 51, toRadians(70)), toRadians(250))
                .addDisplacementMarker(10,()->{robot.roadrun.followTrajectorySequenceAsync(initialTrajectoryPart2);})
                .splineToSplineHeading(new Pose2d(36, 17, toRadians(90)), toRadians(270))
                .splineToSplineHeading(new Pose2d(29,7, toRadians(45)), toRadians(215))
                .build();
        TrajectorySequence pickupTrajectoryPart2 = robot.roadrun.trajectorySequenceBuilder(new Pose2d(pickupX2-15,16,0))
                .setReversed(false)
//                                        .splineToLinearHeading(new Pose2d(36,11.5, toRadians(0)),toRadians(240))
                .splineToSplineHeading(new Pose2d(pickupX2, pickupY2,toRadians(0)), toRadians(0))
                .addTemporalMarker(robot::done)
                .build();
        TrajectorySequence pickupTrajectory = robot.roadrun.trajectorySequenceBuilder(new Pose2d(dropX,dropY, dropA))
                .setReversed(false)
//                                        .splineToLinearHeading(new Pose2d(36,11.5, toRadians(0)),toRadians(240))
                .splineToSplineHeading(new Pose2d(pickupX2-20, 17,toRadians(0)), toRadians(5))
                .addTemporalMarker(()->{robot.roadrun.followTrajectorySequenceAsync(pickupTrajectoryPart2);})
                .splineToConstantHeading(new Vector2d(pickupX2, pickupY2), 0)
                .build();
        TrajectorySequence dropTrajectory = robot.roadrun.trajectorySequenceBuilder(new Pose2d(pickupX2, pickupY2, 0))
                .setReversed(true)
                .splineToSplineHeading(new Pose2d(dropX,dropY,dropET+toRadians(175)), dropET)
                .addTemporalMarker(robot::done)
                .build();
        TrajectorySequence drop1Trajectory = robot.roadrun.trajectorySequenceBuilder(new Pose2d(pickupX2, pickupY2, 0))
                .setReversed(true)
                .splineToSplineHeading(new Pose2d(dropX,dropY, dropA), dropET)
                .build();
        TrajectorySequence pickupTrajectory2Part2 = robot.roadrun.trajectorySequenceBuilder(new Pose2d(pickupX2-15,15,0))
                .setReversed(false)
//                                        .splineToLinearHeading(new Pose2d(36,11.5, toRadians(0)),toRadians(240))
                .splineToSplineHeading(new Pose2d(pickupX2, pickupY2,toRadians(0)), toRadians(0))
                .addTemporalMarker(robot::done)
                .build();
        TrajectorySequence pickupTrajectory2 = robot.roadrun.trajectorySequenceBuilder(new Pose2d(dropX,dropY, dropA))
                .setReversed(false)
//                                        .splineToLinearHeading(new Pose2d(36,11.5, toRadians(0)),toRadians(240))
                .splineToSplineHeading(new Pose2d(pickupX2-22, 19.9,toRadians(0)), toRadians(5))
                .addTemporalMarker(()->{robot.roadrun.followTrajectorySequenceAsync(pickupTrajectory2Part2);})
                .splineToConstantHeading(new Vector2d(pickupX2, pickupY2), 0)
                .build();
//        TrajectorySequence parkTrajectory = robot.roadrun.trajectorySequenceBuilder(new Pose2d(dropX,dropY, dropA))
//                .splineToSplineHeading(new Pose2d(-36, 33, toRadians(90)), toRadians(90))
//                .build();
        TrajectorySequence park1trajectory = robot.roadrun.trajectorySequenceBuilder(new Pose2d(dropX,dropY, dropA))
                .setReversed(false)
                .splineToLinearHeading(new Pose2d(60,15, toRadians(0)),toRadians(270))

                .build();

        TrajectorySequence park2trajectory = robot.roadrun.trajectorySequenceBuilder(new Pose2d(dropX,dropY, dropA))
                .lineToLinearHeading(new Pose2d(38, 16,toRadians(270)))
                .build();
        TrajectorySequence park3trajectorypart2 = robot.roadrun.trajectorySequenceBuilder(new Pose2d(pickupX2-15,14,0))
                .setReversed(false)
//                                        .splineToLinearHeading(new Pose2d(36,11.5, toRadians(0)),toRadians(240))
                .splineToSplineHeading(new Pose2d(pickupX2, pickupY2,toRadians(0)), toRadians(0))
                .addTemporalMarker(robot::done)
                .build();
        TrajectorySequence park3trajectory = robot.roadrun.trajectorySequenceBuilder(new Pose2d(dropX,dropY, dropA))
                .setReversed(false)
                .splineToLinearHeading(new Pose2d(dropX+4,dropY-7, toRadians(-5)),toRadians(dropA))
                .setReversed(true)
                .splineToLinearHeading(new Pose2d(15,13,toRadians(0)), toRadians(0))
                .build();
        while(!isStarted()){
            telemetry.addData("pos",robot.cv.getPosition());
            telemetry.addData("CLAW_CLOSED:", CLAW_CLOSED.getStatus());
            telemetry.update();
            robot.updateClawStates();
            robot.updateLiftArmStates();
            if(getRuntime()>3){
                int color = robot.cv.getPosition();

                if (color == 1) {
                    robot.heartbeatRed();
                }
                else if (color == 2) {
                    robot.darkGreen();
                }
                else {
                    robot.violet();
                }
            }
        }
        resetRuntime();
        dummyP = robot.cv.getPosition();

        if (isStopRequested()) return;


        while (opModeIsActive() && !isStopRequested()) {
            logger.loopcounter++;
            robot.followTrajectorySequenceAsync(initialtrajectory);
            robot.raiseLiftArmToOuttake(true);
            robot.delay(0.3);
            robot.liftToPosition(LIFT_MED_JUNCTION);
            robot.delay(0.15);
            robot.openClaw(false);
            robot.delay(0.4);
            robot.cycleLiftArmToCycle(true);
            robot.delay(0.5);
            robot.wideClaw();
            robot.delay(0.5);
            robot.liftToPosition((int) stackPos[0],true);
            robot.followTrajectorySequenceAsync(pickupTrajectory2);
            robot.closeClaw(false);
            robot.followTrajectorySequenceAsync(dropTrajectory);
            robot.delay(0.61);
            robot.raiseLiftArmToOuttake(true);
            robot.delay(0.21);
            robot.liftToPosition(LIFT_MED_JUNCTION);
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
                robot.followTrajectorySequenceAsync(dropTrajectory);
                robot.delay(0.3+0.005*(3-i));
                robot.raiseLiftArmToOuttake(true);
                robot.delay(0.0+0.005*(3-i));
                robot.liftToPosition(LIFT_MED_JUNCTION);
                robot.delay(0.15);
                robot.openClaw(false);
            }

            robot.delay(0.8);
            robot.lowerLiftArmToIntake(true);
            robot.delay(1.5);
            robot.wideClaw();
            robot.delay(1.5);
            robot.liftToPosition(0);

            if (dummyP == 1) {
//                robot.delay(0.5);
                robot.followTrajectorySequenceAsync(park1trajectory);
            } else if (dummyP == 3) {
                robot.followTrajectorySequenceAsync(park3trajectory);
            } else {
//                robot.delay(0.5);
                robot.followTrajectorySequenceAsync(park2trajectory);
            }
            robot.queuedStop();
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