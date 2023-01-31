package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;

public class NewArmSlidesTeleOp {
    public enum LiftState{
        START,
        INTAKE,
        LIFT,
        DEPOSIT,
        RETURN,
        secondRETURN
    }
    public LiftState liftState = LiftState.START;
    private int targetPos = 0;
    private int armTarget = 0;
    private boolean back; //whether or not it is level 3
    private boolean adjusted;
    private ElapsedTime eTime = new ElapsedTime(ElapsedTime.Resolution.MILLISECONDS);
    private DcMotorEx RSlides;
    private DcMotorEx LSlides;
    private DcMotorEx VBMotor;
    private CRServo Intake;
    private double rad = 2*(Math.PI)/1425.1;
    private double offset;
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
        VBMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        LSlides.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        RSlides.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        LiftState liftState = LiftState.START;
    }
    public void Lifter(double rTrig, double lTrig, boolean a, boolean b, boolean y, boolean lBump){//add telemetry
        switch(liftState){
            case START: //back to start
                armTarget = 525;
                adjusted = false;
                Intake.setPower(0);
                if (rTrig>0.2){
                    liftState = LiftState.INTAKE;
                } else if (lBump){ //reset to state in which you can intake again
                    LSlides.setPower(0.3);
                    RSlides.setPower(-0.3);
                    LSlides.setTargetPosition(600);
                    RSlides.setTargetPosition(-600);
                }
                if (a) {
                    targetPos = 0; //level 1
                    back = true;
                    eTime.reset();
                    liftState = LiftState.LIFT;
                } else if (b) {
                    targetPos = 1800; //level 2
                    back = true;
                    eTime.reset();
                    liftState = LiftState.LIFT;
                } else if (y) {
                    targetPos = 3090; //level 3
                    back = false;
                    eTime.reset();
                    liftState = LiftState.LIFT;
                }
            case INTAKE: //move the slides down and intake the cone with servo
                LSlides.setPower(-0.3);
                RSlides.setPower(0.3);
                LSlides.setTargetPosition(0);
                RSlides.setTargetPosition(0);
                Intake.setPower(0.8);
                liftState = LiftState.START;
            case LIFT: //first move arm halfway, then move slides full up
                Intake.setPower(0);
                if (rTrig>0.2){
                    Intake.setPower(0);
                }
                LSlides.setPower(0.4); //set power to go to the position
                RSlides.setPower(-0.4);
                VBMotor.setPower(0.25);
                if (a) { //level 1
                    targetPos = 0;
                    armTarget = 525;
                    back = true;
                    adjusted = true;
                    eTime.reset();
                } else if (b) {
                    targetPos = 1800; //level 2
                    armTarget = 525;
                    back = true;
                    adjusted = true;
                    eTime.reset();
                } else if (y) {
                    targetPos = 3090; //level 3
                    armTarget = 525;
                    back = false;
                    adjusted = true;
                    eTime.reset();
                }
                if ((eTime.time()>1200)&&(back)){
                    LSlides.setTargetPosition(targetPos); //full up
                    RSlides.setTargetPosition(-targetPos);
                } else if ((eTime.time()>1200)&&(!back)){
                    LSlides.setTargetPosition(3090);
                    RSlides.setTargetPosition(-3240);
                }
                if ((VBMotor.getCurrentPosition()<525)||(adjusted)){
                    VBMotor.setTargetPosition(armTarget);
                    adjusted = false;
                }
                else if (eTime.time()>1800&&((Math.abs(LSlides.getCurrentPosition()-LSlides.getTargetPosition()))<20)&&(Math.abs(VBMotor.getCurrentPosition()-VBMotor.getTargetPosition())<20)) {
                    if (back) { //whether the arm goes fully on the back
                        armTarget = 750;
                    } else {
                        armTarget = 725;
                    }
                    //finish arm movement
                    VBMotor.setPower(0.12);
                    VBMotor.setTargetPosition(armTarget);

                    if ((Math.abs(VBMotor.getCurrentPosition() - VBMotor.getTargetPosition()) < 20) && (lTrig > 0.2)) {
                        eTime.reset();
                        liftState = LiftState.DEPOSIT;
                    }
                }
                break;
            case DEPOSIT:

                break;
        }
    }
}
