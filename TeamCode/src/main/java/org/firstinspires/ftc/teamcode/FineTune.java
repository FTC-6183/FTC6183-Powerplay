package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;

@TeleOp
public class FineTune extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException{
        DcMotorEx RSlides = hardwareMap.get(DcMotorEx.class, "RSlides");
        DcMotorEx LSlides = hardwareMap.get(DcMotorEx.class, "LSlides");
        DcMotorEx VBMotor = hardwareMap.get(DcMotorEx.class, "VBMotor");
        RSlides.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        LSlides.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        VBMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        VBMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        RSlides.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        LSlides.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        VBMotor.setTargetPosition(0);
        VBMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        int k = 100;
        waitForStart();
        while (opModeIsActive()){
            if (gamepad1.dpad_left){
                VBMotor.setPower(-0.2);
            } else if (gamepad1.dpad_right) {
                VBMotor.setPower(0.2);
            } else {
                VBMotor.setPower(0);
            }
            telemetry.addData("VBMotor", VBMotor.getCurrentPosition());
            telemetry.addData("LSlides",LSlides.getCurrentPosition());
            telemetry.addData("RSlides", RSlides.getCurrentPosition());
            telemetry.update();
        }
    }
}
