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
        double x = 0;
        double P = 0.3;
        double PS = 0.5;
        double NP = -0.11;
        int backPos = 800;
        ElapsedTime eTime = new ElapsedTime(ElapsedTime.Resolution.MILLISECONDS);
        //ElapsedTime jTime = new ElapsedTime(ElapsedTime.Resolution.MILLISECONDS);
        waitForStart();

        while (opModeIsActive()){
            if (eTime.time()<3000){ //suck up the cone
                Intake.setPower(0.3);
            } else if (eTime.time()>7000){
                eTime.reset();
            } else {
                Intake.setPower(0);
            }
            if (gamepad1.y) {
                //x = jTime.now(TimeUnit.MILLISECONDS);
                LSlides.setTargetPosition(1550);
                RSlides.setTargetPosition(-1550);
                LSlides.setPower(PS);
                RSlides.setPower(-PS);
                LSlides.setMode(DcMotor.RunMode.RUN_TO_POSITION); //go back to position 0
            } else if (gamepad1.a){
                LSlides.setTargetPosition(0);
                RSlides.setTargetPosition(0);
                LSlides.setPower(-PS);
                RSlides.setPower(PS);
                LSlides.setMode(DcMotor.RunMode.RUN_TO_POSITION); //go to pos
            } else {
                if (LSlides.getCurrentPosition()!=LSlides.getTargetPosition()&&LSlides.getTargetPosition()>LSlides.getCurrentPosition()){
                    LSlides.setPower(PS); //continue going forward to target pos
                    RSlides.setPower(-PS);
                } else if (LSlides.getCurrentPosition()!=LSlides.getTargetPosition()&&LSlides.getTargetPosition()<LSlides.getCurrentPosition()){
                    LSlides.setPower(-PS); //continue going backwards to target pos
                    RSlides.setPower(PS);
                } else if (LSlides.getCurrentPosition()==LSlides.getTargetPosition()){
                    LSlides.setPower(0); // stop going after reaching target pos
                    RSlides.setPower(0);
                }
            }
            /*if (gamepad1.a){
                LSlides.setPower(0.3);
                RSlides.setPower(-0.3);

            } else {
                LSlides.setPower(0);
                RSlides.setPower(0); //slides code in progress
            }*/
            if (gamepad1.b){
                VBMotor.setTargetPosition(0);
                VBMotor.setPower(NP);
                VBMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION); //go back to position 0
            } else if (gamepad1.x){
                //jTime.reset();
                VBMotor.setTargetPosition(backPos);
                VBMotor.setPower(P);
                VBMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION); // go to target pos
            } else {
                if (VBMotor.getCurrentPosition()!=VBMotor.getTargetPosition()&&VBMotor.getTargetPosition()>VBMotor.getCurrentPosition()){
                    if(VBMotor.getCurrentPosition()<400){
                        VBMotor.setPower(P); //continue going forward to target pos
                    } else {
                        if(LSlides.getCurrentPosition()!=LSlides.getTargetPosition()){
                            VBMotor.setPower(0);
                        } else {
                            VBMotor.setPower(P/(VBMotor.getCurrentPosition()-400)/40);
                        }

                    }
                } else if (VBMotor.getCurrentPosition()!=VBMotor.getTargetPosition()&&VBMotor.getTargetPosition()<VBMotor.getCurrentPosition()){
                    VBMotor.setPower(NP); //continue going backwards to target pos
                } else if (VBMotor.getCurrentPosition()==VBMotor.getTargetPosition()){
                    VBMotor.setPower(0); // stop going after reaching target pos
                }
            }
            telemetry.addData("A", gamepad1.a);
            telemetry.addData("B", gamepad1.b);
            telemetry.addData("positionL", LSlides.getCurrentPosition());
            telemetry.addData("positionR", RSlides.getCurrentPosition());
            telemetry.addData("positionV", VBMotor.getCurrentPosition());
            telemetry.addData("busy", VBMotor.isBusy());
            telemetry.addData("targetPos", VBMotor.getTargetPosition());
            //telemetry.addData("time", P);
            telemetry.update(); //debug stuff
        }
    }
}
