package org.firstinspires.ftc.teamcode;


import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.util.ElapsedTime;

@TeleOp
public class ArmSlidesControlTeleOp extends LinearOpMode {
    enum State{
        START,
        LIFT,
        DEPOSIT
    }
    @Override
    public void runOpMode() throws InterruptedException {
        DcMotorEx RSlides = hardwareMap.get(DcMotorEx.class, "RSlides");
        DcMotorEx LSlides = hardwareMap.get(DcMotorEx.class, "LSlides");
        DcMotorEx VBMotor = hardwareMap.get(DcMotorEx.class, "VBMotor");
        CRServo Intake = hardwareMap.get(CRServo.class, "Intake");
        RSlides.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        LSlides.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        VBMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        VBMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        VBMotor.setTargetPosition(0);
        RSlides.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        RSlides.setTargetPosition(0);
        LSlides.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        LSlides.setTargetPosition(0);
        VBMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        LSlides.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        RSlides.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        int targetPos = 0;
        int armTarget = 0;
        boolean back = false;
        boolean adjusted = false;
        ElapsedTime eTime = new ElapsedTime(ElapsedTime.Resolution.MILLISECONDS);
        State liftState = State.START;
        waitForStart();
        while (opModeIsActive()){
            switch(liftState) {
                case START:
                    adjusted = false;
                    Intake.setPower(0);
                    VBMotor.setTargetPosition(0);//back to zero position
                    LSlides.setTargetPosition(0);
                    RSlides.setTargetPosition(0);
                    if (gamepad1.right_trigger > 0.2) { //intake intake intake
                        Intake.setPower(0.5);
                    }
                    if (gamepad1.a) { //level 1
                        targetPos = 0;
                        armTarget = 525;
                        back = true;
                        liftState = State.LIFT;
                    } else if (gamepad1.b) {
                        targetPos = 1600; //level 2
                        armTarget = 525;
                        back = true;
                        liftState = State.LIFT;
                    } else if (gamepad1.y) {
                        targetPos = 3050; //level 3
                        armTarget = 525;
                        back = false;
                        liftState = State.LIFT;
                    }
                    break;
                case LIFT:
                    LSlides.setPower(0.4); //set power to go to position
                    RSlides.setPower(-0.4);
                    VBMotor.setPower(0.4);
                    VBMotor.setTargetPosition(armTarget); //halfway up
                    LSlides.setTargetPosition(targetPos); //full up
                    RSlides.setTargetPosition(targetPos);
                    if (gamepad1.left_trigger > 0.2) {
                        if (back) { //whether the arm goes fully on the back
                            armTarget = 800;
                        } else {
                            armTarget = 786;
                        }
                        VBMotor.setPower(0.2);
                        VBMotor.setTargetPosition(armTarget); //finish art movement
                        if (VBMotor.getCurrentPosition()==VBMotor.getTargetPosition()) {
                            VBMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER); //get ready for dpad finetuners
                            LSlides.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
                            RSlides.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
                            eTime.reset();
                            liftState = State.DEPOSIT;
                        }
                    }
                    break;
                case DEPOSIT:
                    if (gamepad1.dpad_down){ //dpad finetuners
                        LSlides.setPower(-0.07);
                        RSlides.setPower(0.07);
                        eTime.reset();
                        adjusted = true;
                    } else if (gamepad1.dpad_up){
                        LSlides.setPower(0.07);
                        RSlides.setPower(-0.07);
                        eTime.reset();
                        adjusted = true;
                    } else {
                        LSlides.setPower(0);
                        RSlides.setPower(0);
                    }
                    if (gamepad1.dpad_left){
                        VBMotor.setPower(-0.08);
                        eTime.reset();
                        adjusted = true;
                    } else if (gamepad1.dpad_right) {
                        VBMotor.setPower(0.08);
                        eTime.reset();
                        adjusted = true;
                    } else {
                        VBMotor.setPower(0);
                    }
                    if ((eTime.time()>1200)||(eTime.time()>250&&adjusted)){ //deposit after some time, unless it is adjusted
                        Intake.setPower(-0.8); //deposit
                        if (eTime.time()>2500) {
                            VBMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION); //get ready for zero positioning
                            LSlides.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                            RSlides.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                            LSlides.setPower(-0.3);
                            RSlides.setPower(0.3);
                            VBMotor.setPower(-0.15);
                            liftState = State.START;
                        }
                    }
                    break;
            }
        }
    }
}