package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class ManualArmSlides {
    public enum State{
        START,
        LIFT,
        DEPOSIT,
        RETURN
    }
    public ArmSlidesControlTeleOp.State liftState = ArmSlidesControlTeleOp.State.START;
    private int targetPos = 0;
    private int armTarget = 0;
    private int pos2;
    private double x = 0;
    private double rad = 2*(Math.PI)/1425.1;
    private double offset;
    private boolean back = false;
    private boolean boom = false;
    private boolean adjusted = false;
    private boolean returning = false;
    private ElapsedTime eTime = new ElapsedTime(ElapsedTime.Resolution.MILLISECONDS);
    private DcMotorEx RSlides;
    private DcMotorEx LSlides;
    private DcMotorEx VBMotor;
    private CRServo Intake;
    private Telemetry telemetry;
    public ManualArmSlides(HardwareMap hardwareMap){
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
        ArmSlidesControlTeleOp.State liftState = ArmSlidesControlTeleOp.State.START;
    }
    public void ArmSlides(double lStickY, double rStickY, boolean x, boolean y, boolean a, boolean b, double rtrig, double ltrig, boolean lbump, Telemetry telemetry){
        LSlides.setTargetPosition(targetPos);
        if (targetPos==3090){
            RSlides.setTargetPosition(-3240);
        } else {
            RSlides.setTargetPosition(-targetPos);
        }
        VBMotor.setTargetPosition(armTarget);
        targetPos+=lStickY;
        armTarget+=rStickY;
        LSlides.setPower(0.5);
        RSlides.setPower(0.5);
        VBMotor.setPower(0.3);
        if (((VBMotor.getTargetPosition()-525)<20)&&!returning){
            if (back){
                armTarget = 750;
            } else {
                armTarget = 725;
            }
            returning = true;
        }
        if (rtrig>0.2){
            Intake.setPower(0.4);
            targetPos=0;
            armTarget=0;
        } else if (ltrig>0.2){
            Intake.setPower(-0.4);
        } else {
            Intake.setPower(0);
        }
        if (lbump){
            targetPos = 600;
            armTarget = 0;
        }
        if (a){
            targetPos = 0;
            armTarget = 525;
            back = false;
            returning = false;
        } else if (b){
            targetPos = 1800;
            armTarget = 525;
            back = false;
            returning = false;
        } else if (y){
            targetPos = 3090;
            armTarget = 525;
            back = true;
            returning = false;
        }
    }
}
