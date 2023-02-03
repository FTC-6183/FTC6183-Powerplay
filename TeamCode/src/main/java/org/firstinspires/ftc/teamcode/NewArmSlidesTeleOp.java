package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class NewArmSlidesTeleOp {
    public enum LiftState{
        START,
        INTAKE,
        LIFT,
        DEPOSIT,
        RETURN,
        secondRETURN
    }
    private double previousError1 = 0, error1 = 0, integralSum1, derivative1, VBMotorPower;
    private double Kp = 0.0045, Kd = 0, Ki = 0; //Don't use Ki
    private ElapsedTime lowerTimer = new ElapsedTime(ElapsedTime.Resolution.SECONDS);
    public LiftState liftState = LiftState.START;
    private int targetPos = 0,armTarget = 0, x, level, ab,bc;
    private boolean adjusted;
    private ElapsedTime eTime = new ElapsedTime(ElapsedTime.Resolution.MILLISECONDS);
    private DcMotorEx RSlides, LSlides, VBMotor;
    private CRServo Intake;
    public NewArmSlidesTeleOp(HardwareMap hardwareMap){
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
        VBMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        LSlides.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        RSlides.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        LiftState liftState = LiftState.START;
    }
    public void Lifter(double rTrig, double lTrig, boolean a, boolean b, boolean y, boolean lBump, double lSticky, double rSticky, Telemetry telemetry){//add telemetry
        switch(liftState){
            case START: //back to start
                x=ab=bc=0;
                adjusted = false;
                Intake.setPower(0);
                if (rTrig>0.2){
                    liftState = LiftState.INTAKE;
                } else if (lBump){ //reset to state in which you can intake again
                    LSlides.setPower(0.3);RSlides.setPower(-0.3);
                    targetPos = 600;
                }
                if (a) {
                    armTarget = 525;
                    level = 1;
                    eTime.reset();
                    liftState = LiftState.LIFT;
                } else if (b) {
                    armTarget = 525;
                    level = 2;
                    eTime.reset();
                    liftState = LiftState.LIFT;
                } else if (y) {
                    armTarget = 525;
                    level = 3;
                    eTime.reset();
                    liftState = LiftState.LIFT;
                }
                break;
            case INTAKE: //move the slides down and intake the cone with servo
                LSlides.setPower(-0.3);RSlides.setPower(0.3);
                targetPos = 0;
                Intake.setPower(0.8);
                liftState = LiftState.START;
                break;
            case LIFT: //first move arm halfway, then move slides full up
                if (rTrig>0.2){Intake.setPower(0.8);}
                else{Intake.setPower(0);}
                LSlides.setPower(0.4);RSlides.setPower(-0.4);
                if (a) { //level 1
                    level = 1;
                    armTarget = 525;
                    adjusted = true;
                    eTime.reset();
                } else if (b) {
                    level = 2;
                    armTarget = 525;
                    adjusted = true;
                    eTime.reset();
                } else if (y) {
                    level = 3;
                    armTarget = 525;
                    adjusted = true;
                    eTime.reset();
                }
                if ((eTime.time()>1200)){
                    if (adjusted||bc==0) {
                        if (level == 1) {
                            targetPos = 100;
                        } else if (level == 2) {
                            targetPos = 1900;
                        } else if (level == 3) {
                            targetPos = 3090;
                        }
                        bc+=1;
                    }
                }
                if ((VBMotor.getCurrentPosition()<525)||(adjusted)){adjusted = false;}
                else if (eTime.time()>1800&&((Math.abs(LSlides.getCurrentPosition()-LSlides.getTargetPosition()))<20)&&(Math.abs(VBMotor.getCurrentPosition()-VBMotor.getTargetPosition())<20)) {
                    if (level == 3) {armTarget = 745;}
                    else {armTarget = 700;}
                    if ((Math.abs(VBMotor.getCurrentPosition() - VBMotor.getTargetPosition()) < 20)) {
                        eTime.reset();
                        liftState = LiftState.DEPOSIT;
                    }
                }
                break;
            case DEPOSIT:
                LSlides.setPower(0.7);RSlides.setPower(0.7);
                if ((lTrig>0.2)||(x>1)){ //deposit after some time
                    Intake.setPower(-0.8); //deposit
                    if (x==0){eTime.reset();}
                    x+=1;
                    if (eTime.time()>700) {
                        Intake.setPower(0);
                        LSlides.setPower(-0.5);
                        RSlides.setPower(0.5);
                        armTarget = 525;
                        if ((targetPos!=600)&&(ab==0)){
                            targetPos = 600;
                            ab+=1;
                        }

                        if ((lTrig>0.2)&&(Math.abs(VBMotor.getCurrentPosition()-armTarget)<30)&&(Math.abs(LSlides.getCurrentPosition()-600)<30)){
                            armTarget = 0;
                            liftState = LiftState.RETURN;
                        }
                    }
                }
                break;
            case RETURN:
                if (Math.abs(VBMotor.getCurrentPosition()-armTarget)<35){
                    LSlides.setPower(0);
                    RSlides.setPower(0);
                    liftState = LiftState.START;
                }
                break;
        }
        VBMotorPower = PIDControl1(armTarget, VBMotor.getCurrentPosition());
        VBMotor.setPower(VBMotorPower);
        targetPos -= lSticky;
        if (targetPos<0){targetPos=0;}
        if ((level==3)&&(targetPos>2500)){
            LSlides.setTargetPosition(targetPos);
            RSlides.setTargetPosition(-targetPos-150);
        } else {
            LSlides.setTargetPosition(targetPos);
            RSlides.setTargetPosition(-targetPos);
        }
        telemetry.addData("state",liftState);
        telemetry.addData("v4b pos", VBMotor.getCurrentPosition());
        telemetry.addData("armTarget",armTarget);
        telemetry.addData("v4b power", VBMotor.getPower());
        telemetry.addData("eTime",eTime.time());
        telemetry.addData("Lslides pos",LSlides.getCurrentPosition());
        telemetry.addData("lSlides target",LSlides.getTargetPosition());
        telemetry.addData("rslide pos", RSlides.getCurrentPosition());
        telemetry.addData("rslide target",RSlides.getTargetPosition());
        telemetry.addData("busyL",LSlides.isBusy());
        telemetry.update();
    }
    public double PIDControl1(double target, double state) {
        previousError1 = error1;
        error1 = target - state;
        integralSum1 += error1 * lowerTimer.seconds();
        derivative1 = (error1 - previousError1) / lowerTimer.seconds();

        lowerTimer.reset();
        double output = (error1 * Kp) + (derivative1 * Kd) + (integralSum1 * Ki);
        //inc Kp if it is too low power
        //inc Kd = less oscillation
        //Ki is yucky

        return output;
    }
}
//credit to steven gu of the gu family, in the gu variety