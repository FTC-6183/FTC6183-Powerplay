package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.util.ElapsedTime;

@TeleOp
public class Increments extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
        DcMotorEx RSlides = hardwareMap.get(DcMotorEx.class, "RSlides");
        DcMotorEx LSlides = hardwareMap.get(DcMotorEx.class, "LSlides");
        DcMotorEx VBMotor = hardwareMap.get(DcMotorEx.class, "VBMotor");
        RSlides.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        LSlides.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        VBMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        ElapsedTime eTime = new ElapsedTime(ElapsedTime.Resolution.MILLISECONDS);
        waitForStart();

        while (opModeIsActive()){
            if (gamepad1.a){
                LSlides.setPower(0.5);
                RSlides.setPower(-0.5);

            } else {
                LSlides.setPower(0);
                RSlides.setPower(0);
            }
            if (gamepad1.b){
                VBMotor.setPower(1);
            } else {
                VBMotor.setPower(0);
            }
            telemetry.addData("A", gamepad1.a);
            telemetry.addData("B", gamepad1.b);
            telemetry.addData("positionL", LSlides.getCurrentPosition());
            telemetry.addData("positionR", RSlides.getCurrentPosition());
            telemetry.addData("positionV", VBMotor.getCurrentPosition());
            telemetry.update();
        }
    }
}
