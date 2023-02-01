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
    private double previousError1 = 0, error1 = 0, integralSum1, derivative1;
    private double Kp = 0.003, Kd = 0, Ki = 0; //Don't use Ki
    private ElapsedTime lowerTimer = new ElapsedTime(ElapsedTime.Resolution.SECONDS);
    public State liftState = State.START;
    private int targetPos = 0;
    private int armTarget = 0;
    private int pos2;
    private double x = 0;
    private double rad = 2*(Math.PI)/1425.1;
    private double offset;
    private boolean back = false;
    private boolean boom = false;
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

    public void ArmSlides(double rTrig, double lTrig, boolean a, boolean b, boolean y, Telemetry telemetry, boolean dpadU, boolean dpadD, boolean lBump) {
        switch(liftState) {
            case START:
                x=0;
                adjusted = false;
                boom = false;
                Intake.setPower(0);
                if (rTrig > 0.2) { //intake intake intake
                    LSlides.setPower(-0.3);
                    RSlides.setPower(0.3);
                    LSlides.setTargetPosition(0);
                    RSlides.setTargetPosition(0);
                    Intake.setPower(0.8);

                } else if (lBump){
                    LSlides.setPower(0.3);
                    RSlides.setPower(-0.3);
                    LSlides.setTargetPosition(600);
                    RSlides.setTargetPosition(-600);

                }
                if (a) { //level 1
                    targetPos = 0;
                    armTarget = 525;
                    back = true;
                    eTime.reset();
                    liftState = State.LIFT;
                } else if (b) {
                    targetPos = 1800; //level 2
                    armTarget = 525;
                    back = true;
                    eTime.reset();
                    liftState = State.LIFT;
                } else if (y) {
                    targetPos = 2650; //level 3
                    armTarget = 525;
                    back = false;
                    eTime.reset();
                    liftState = State.LIFT;
                }
                if (dpadU){
                    VBMotor.setTargetPosition(pos2);
                    pos2+=3;
                } else if (dpadD){
                    VBMotor.setTargetPosition(pos2);
                    pos2-=3;
                }
                break;
            case LIFT:
                Intake.setPower(0);
                if (rTrig>0.2) {
                    Intake.setPower(0.8);
                }
                LSlides.setPower(0.4); //set power to go to position
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
                    targetPos = 2650; //level 3
                    armTarget = 525;
                    back = false;
                    adjusted = true;
                    eTime.reset();
                }
                //halfway up
                if (eTime.time()>1200){
                    if (back){
                        LSlides.setTargetPosition(targetPos); //full up
                        RSlides.setTargetPosition(-targetPos);
                    } else {
                        LSlides.setTargetPosition(3090);
                        RSlides.setTargetPosition(-3240);
                    }

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

                    if ((Math.abs(VBMotor.getCurrentPosition()-VBMotor.getTargetPosition())<20)&&(lTrig>0.2)) {
                        eTime.reset();
                        liftState = State.DEPOSIT;
                    }
                }
                break;
            case DEPOSIT:
                VBMotor.setPower(-0.2);
                LSlides.setPower(0.7);
                RSlides.setPower(-0.7);
                VBMotor.setTargetPosition(armTarget);//back to zero position
                if (back||(targetPos==600)) {
                    LSlides.setTargetPosition(targetPos); //full up
                    RSlides.setTargetPosition(-targetPos);
                } else if (!back) {
                    LSlides.setTargetPosition(3090);
                    RSlides.setTargetPosition(-3240);
                }
                if ((lTrig>0.2)||(x>1)){ //deposit after some time
                    Intake.setPower(-0.8); //deposit
                    if (x==0){
                        eTime.reset();
                    }
                    x+=1;
                    if (eTime.time()>700) {
                            /*
                            VBMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION); //get ready for zero positioning
                            LSlides.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                            RSlides.setMode(DcMotor.RunMode.RUN_TO_POSITION);*/
                        Intake.setPower(0);

                        LSlides.setPower(-0.5);
                        RSlides.setPower(0.5);
                        //VBMotor.setPower(((offset)*x)-0.2);
                        VBMotor.setPower(-0.1);
                        armTarget = 525;
                        targetPos = 600;

                        if ((lTrig>0.2)&&(Math.abs(VBMotor.getCurrentPosition()-VBMotor.getTargetPosition())<30)&&(Math.abs(LSlides.getCurrentPosition()-600)<30)){
                            VBMotor.setPower(0);
                            VBMotor.setTargetPosition(0);
                            liftState = State.RETURN;
                        }
                    }
                }
                break;
            case RETURN:
                offset = (Math.sin((rad*(525)-rad*(VBMotor.getCurrentPosition()))/2));
                VBMotor.setPower((offset/5)-0.4);
                if (Math.abs(VBMotor.getCurrentPosition()-VBMotor.getTargetPosition())<35){
                    LSlides.setPower(0);
                    RSlides.setPower(0);
                    liftState = State.START;
                }
                break;
        }

        telemetry.addData("state",liftState);
        telemetry.addData("v4b pos", VBMotor.getCurrentPosition());
        telemetry.addData("v4b target", VBMotor.getTargetPosition());
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
