package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.util.ElapsedTime;

import java.util.concurrent.TimeUnit;

@TeleOp
public class Increments extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
        DcMotorEx RSlides = hardwareMap.get(DcMotorEx.class, "RSlides");
        DcMotorEx LSlides = hardwareMap.get(DcMotorEx.class, "LSlides");
        DcMotorEx VBMotor = hardwareMap.get(DcMotorEx.class, "VBMotor");
        CRServo Intake = hardwareMap.get(CRServo.class, "Intake");
        RSlides.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        LSlides.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        VBMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        VBMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER); //set current pos to 0
        VBMotor.setTargetPosition(0);
        RSlides.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        RSlides.setTargetPosition(0);
        LSlides.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        LSlides.setTargetPosition(0);
        ElapsedTime eTime = new ElapsedTime(ElapsedTime.Resolution.MILLISECONDS);
        //ElapsedTime jTime = new ElapsedTime(ElapsedTime.Resolution.MILLISECONDS);
        waitForStart();

        while (opModeIsActive()){
            telemetry.addData("A", gamepad1.a);
            telemetry.addData("B", gamepad1.b);
            telemetry.addData("positionL", LSlides.getCurrentPosition());
            telemetry.addData("positionR", RSlides.getCurrentPosition());
            telemetry.addData("positionV", VBMotor.getCurrentPosition());
            telemetry.addData("lBusy", LSlides.isBusy());
            telemetry.addData("rBusy", RSlides.isBusy());
            telemetry.addData("vBusy", VBMotor.isBusy());
            telemetry.addData("lTargetPos", LSlides.isBusy());
            telemetry.addData("rTargetPos", RSlides.isBusy());
            telemetry.addData("targetPos", VBMotor.getTargetPosition());
            //telemetry.addData("time", P);
            telemetry.update(); //debug stuff
        }
    }
}
