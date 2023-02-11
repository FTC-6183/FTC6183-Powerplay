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
        LIFT,
        DEPOSIT,
        RETURN
    }
    private double previousError1 = 0, error1 = 0, integralSum1, derivative1, VBMotorPower, ab, offset, rad = 2*(Math.PI)/1425.1;
    private double Kp = 0.0052, Kd = 0.0001, Ki = 0; //Don't use Ki
    //0.0041, 0, 0
    //tune the power
    private ElapsedTime lowerTimer = new ElapsedTime(ElapsedTime.Resolution.SECONDS);
    public LiftState liftState = LiftState.START;
    private int targetPos = 0,armTarget = 0, x, level,bc, corr = 0, loop;
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
    public void Lifter(double rTrig, double lTrig, boolean a, boolean b, boolean y, boolean lBump, double lSticky, double rSticky, Telemetry telemetry, boolean g1x,boolean g2x){
        switch(liftState){
            case START: //back to start
                Kp = 0.0052;
                corr = 0;
                LSlides.setPower(-0.7);RSlides.setPower(0.7);
                x=bc=loop=0;
                ab=0;
                adjusted = false;
                Intake.setPower(0);
                if (rTrig>0.2){
                    armTarget = 0;
                    targetPos = 0;
                    Intake.setPower(0.8);
                } else if (lBump){ //reset to state in which you can intake again
                    targetPos = 600;
                }
                if (a) {
                    armTarget = 525;
                    level = 1;
                    eTime.reset();
                    loop = 0;
                    liftState = LiftState.LIFT;
                } else if (b) {
                    armTarget = 525;
                    level = 2;
                    eTime.reset();
                    loop = 0;
                    liftState = LiftState.LIFT;
                } else if (y) {
                    armTarget = 525;
                    level = 3;
                    eTime.reset();
                    loop = 0;
                    liftState = LiftState.LIFT;
                }
                break;
            case LIFT: //first move arm halfway, then move slides full up
                if (rTrig>0.2){Intake.setPower(0.8);}
                else{Intake.setPower(0);}
                LSlides.setPower(0.8);RSlides.setPower(-0.8);
                if (a) {
                    targetPos = 0;
                    level = 1;
                    armTarget = 525;
                    corr = 0;
                    adjusted = true;
                    loop = 0;
                    eTime.reset();
                } else if (b) {
                    level = 2;
                    targetPos = 1490;
                    armTarget = 525;
                    corr = 0;
                    adjusted = true;
                    loop = 0;
                    eTime.reset();
                } else if (y) {
                    targetPos = 3090;
                    level = 3;
                    armTarget = 525;
                    corr = 0;
                    adjusted = true;
                    loop = 0;
                    eTime.reset();
                }
                if ((eTime.time()>700)){

                    if (adjusted||bc==0) {
                        LSlides.setPower(0.8);
                        RSlides.setPower(0.8);
                        if (level == 1) {
                            targetPos = 0;
                        } else if (level == 2) {
                            targetPos = 1490;
                        } else if (level == 3) {
                            targetPos = 3090;
                        }
                        bc+=1;
                    }
                }
                if (VBMotor.getCurrentPosition()<500||adjusted){
                    if (loop == 0) {
                        armTarget = 525;
                    }
                    adjusted = false;}
                else if (eTime.time()>1300&&((Math.abs(LSlides.getCurrentPosition()-LSlides.getTargetPosition()))<35)&&(Math.abs(VBMotor.getCurrentPosition()-(armTarget+corr))<50)) {
                    if (level == 3) {

                        if (loop == 0) {
                            corr = 70;
                            armTarget = (738 - corr);
                        }
                    }//finish arm movement
                    else if (level ==1){

                        if (loop == 0) {
                            corr = 50;
                            armTarget = (730 - corr);
                        }
                    }
                    else if (level ==2){

                        if (loop == 0) {
                            corr = 60;
                            armTarget = (700 - corr);
                        }
                    }
                    if ((Math.abs(VBMotor.getCurrentPosition() - (corr+armTarget)) < (65+corr))&&(lTrig>0.2)) {
                        loop = 0;
                        liftState = LiftState.DEPOSIT;
                    }
                    telemetry.addData("test",bc);
                } else if (eTime.time()>800&&((Math.abs(LSlides.getCurrentPosition()-LSlides.getTargetPosition()))<35)&&(Math.abs(VBMotor.getCurrentPosition()-(armTarget+corr))<35)&&level ==1){
                    corr = 50;
                    if (loop == 0) {
                        armTarget = 700 - corr;
                    }
                    if ((Math.abs(VBMotor.getCurrentPosition() - (corr+armTarget)) < (65+corr))&&(lTrig>0.2)) {
                        eTime.reset();
                        loop = 0;
                        liftState = LiftState.DEPOSIT;
                    }
                }
                if (g1x){
                    loop = 0;
                    liftState = LiftState.DEPOSIT;
                }
                break;
            case DEPOSIT:
                LSlides.setPower(1);RSlides.setPower(1);
                if ((lTrig>0.2)||(x>1)){
                    Intake.setPower(-0.8); //out take
                    if (x==0){eTime.reset();}
                    x+=1;
                    if (eTime.time()>700) {
                        Kp = 0.0054;
                        Intake.setPower(0);
                        LSlides.setPower(1);
                        RSlides.setPower(1);
                        if (loop == 0){
                            armTarget = 525;
                        }
                         //go to halfway arm
                        if ((Math.abs(VBMotor.getCurrentPosition()-armTarget)<55)){
                            ab=1;
                            armTarget = 100;
                            liftState = LiftState.RETURN;
                        }
                    }
                }
                break;
            case RETURN:
                Kp = 0.0043;
                //fix the slam steven
                if (Math.abs(VBMotor.getCurrentPosition()-armTarget)<55){//fully get the arm down, before moving slides
                    targetPos = 600;//slides go down now
                    liftState = LiftState.START;

                }
                break;
        }
        if (Math.abs(rSticky)>0.2) {
            armTarget -= 15 * rSticky;
            loop = 1;
            corr = 0;
        }
        if (g2x){
            Intake.setPower(-0.8);
        }
        VBMotorPower = PIDControl1(armTarget, VBMotor.getCurrentPosition());
        if (ab == 1){
            offset = (Math.sin((rad*(525)-rad*(VBMotor.getCurrentPosition()))/2));
            VBMotor.setPower((offset/5)-0.2);
        }else{
            VBMotor.setPower(VBMotorPower);
        }
        targetPos -= 26*lSticky;
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
        telemetry.addData("adjusted",adjusted);
        telemetry.addData("ab",ab);
        telemetry.addData("Kp",Kp);
        telemetry.addData("corr",VBMotor.getCurrentPosition() - (corr+armTarget));
        telemetry.addData("rTrig",rSticky*15);
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
// I <3 5795
