package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;


public class ArmSlidesControlTeleOp {
    public enum State{
        START,
        LIFT,
        DEPOSIT,
        RETURN
    }
    public State liftState = State.START;
    private int targetPos = 0;
    private int armTarget = 0;
    private double ka = 0;
    private double rad = 2*(Math.PI)/5281.1;
    private double offset;
    private boolean back = false;
    private boolean adjusted = false;
    private ElapsedTime eTime = new ElapsedTime(ElapsedTime.Resolution.MILLISECONDS);
    private DcMotorEx RSlides;
    private DcMotorEx LSlides;
    private DcMotorEx VBMotor;
    private CRServo Intake;
    private Telemetry telemetry;
    public ArmSlidesControlTeleOp(HardwareMap hardwareMap){
        RSlides = hardwareMap.get(DcMotorEx.class, "RSlides");
        LSlides = hardwareMap.get(DcMotorEx.class, "LSlides");
        VBMotor = hardwareMap.get(DcMotorEx.class, "VBMotor");
        Intake = hardwareMap.get(CRServo.class, "Intake");
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
        State liftState = State.START;
    }

    public void ArmSlides(double rTrig, double lTrig, boolean a, boolean b, boolean y) {

        switch(liftState) {
            case START:

                adjusted = false;
                Intake.setPower(0);
                VBMotor.setTargetPosition(0);//back to zero position
                LSlides.setTargetPosition(0);
                RSlides.setTargetPosition(0);
                if (rTrig > 0.2) { //intake intake intake
                    Intake.setPower(0.5);
                }
                if (a) { //level 1
                    targetPos = 0;
                    armTarget = 525;
                    back = true;
                    liftState = State.LIFT;
                } else if (b) {
                    targetPos = 1600; //level 2
                    armTarget = 525;
                    back = true;
                    liftState = State.LIFT;
                } else if (y) {
                    targetPos = 2700; //level 3
                    armTarget = 525;
                    back = false;
                    liftState = State.LIFT;
                }
                break;
            case LIFT:
                LSlides.setPower(0.15); //set power to go to position
                RSlides.setPower(-0.15);
                VBMotor.setPower(0.4);
                VBMotor.setTargetPosition(armTarget); //halfway up
                LSlides.setTargetPosition(targetPos); //full up
                RSlides.setTargetPosition(-targetPos);
                if (lTrig > 0.2) {
                    if (back) { //whether the arm goes fully on the back
                        armTarget = 800;
                    } else {
                        armTarget = 786;
                    }
                    VBMotor.setPower(0.05);
                    VBMotor.setTargetPosition(armTarget); //finish art movement
                    telemetry.addData("currentPos",VBMotor.getCurrentPosition());
                    telemetry.addData("TargetPos",VBMotor.getTargetPosition());
                    telemetry.update();
                    if ((VBMotor.getCurrentPosition()-VBMotor.getTargetPosition())<30) {
                        VBMotor.setPower(0);
                        LSlides.setPower(0);
                        RSlides.setPower(0);
                            /*VBMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER); //get ready for dpad finetuners
                            LSlides.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
                            RSlides.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);*/
                        eTime.reset();
                        liftState = State.DEPOSIT;
                    }
                }
                break;
            case DEPOSIT:
                    /*
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
                    }*/
                if ((eTime.time()>250)||(eTime.time()>250&&adjusted)){ //deposit after some time, unless it is adjusted
                    Intake.setPower(-0.8); //deposit
                    if (eTime.time()>1210) {
                            /*
                            VBMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION); //get ready for zero positioning
                            LSlides.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                            RSlides.setMode(DcMotor.RunMode.RUN_TO_POSITION);*/
                        Intake.setPower(0);

                        LSlides.setPower(-0.2);
                        RSlides.setPower(0.2);
                        //VBMotor.setPower(((offset)*ka)-0.2);
                        VBMotor.setPower(-0.2);
                        VBMotor.setTargetPosition(525);//back to zero position
                        LSlides.setTargetPosition(0);
                        RSlides.setTargetPosition(0);
                        if (((VBMotor.getCurrentPosition()-VBMotor.getTargetPosition())<30)&&(LSlides.getCurrentPosition()-LSlides.getTargetPosition()<30)){
                            VBMotor.setPower(0);
                            VBMotor.setTargetPosition(0);
                            liftState = State.RETURN;
                        }
                    }
                }
                break;
            case RETURN:
                offset = (Math.sin((rad*(525)-rad*(VBMotor.getCurrentPosition()))/2));
                VBMotor.setPower((offset/5)-0.2);
                telemetry.addData("power",VBMotor.getPower());
                telemetry.addData("currentPos",VBMotor.getCurrentPosition());
                telemetry.addData("TargetPos",VBMotor.getTargetPosition());
                telemetry.update();
                if (VBMotor.getCurrentPosition()-VBMotor.getTargetPosition()<30){
                    liftState = State.START;
                }
                break;
        }
    }
}
