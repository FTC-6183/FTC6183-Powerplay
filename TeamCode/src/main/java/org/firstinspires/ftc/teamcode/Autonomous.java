package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
// auto goals: linear slide, april code parking, road runner
@com.qualcomm.robotcore.eventloop.opmode.Autonomous (name = "Auto")
public class Autonomous extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
        DcMotorEx backLeft = hardwareMap.get(DcMotorEx.class, "BL");
        DcMotorEx backRight = hardwareMap.get(DcMotorEx.class, "BR");
        DcMotorEx frontLeft = hardwareMap.get(DcMotorEx.class, "FL");
        DcMotorEx frontRight = hardwareMap.get(DcMotorEx.class, "FR");

        waitForStart(); //waits for start
        if (opModeIsActive()) {

        }

    }
}
