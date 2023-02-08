package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;


public class DriveTrainTeleOp {
    private DcMotorEx backLeft;
    private DcMotorEx backRight;
    private DcMotorEx frontLeft;
    private DcMotorEx frontRight;
    private double speedDivide;
    private CRServo Intake;
    private int var, var2;
    public DriveTrainTeleOp(HardwareMap hardwareMap) {
        backLeft = hardwareMap.get(DcMotorEx.class, "BL");
        backRight = hardwareMap.get(DcMotorEx.class, "BR");
        frontLeft = hardwareMap.get(DcMotorEx.class, "FL");
        frontRight = hardwareMap.get(DcMotorEx.class, "FR");
        Intake = hardwareMap.get(CRServo.class, "Intake");
        backLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        backRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        frontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        frontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        backLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        backRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        frontLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        frontRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

    }
    public void DriveTrain(double lStickX, double lStickY, double rStickX, double rTrig, Telemetry telemetry, boolean g1x, double lTrig){
        double y = 0.8*(Math.pow(-lStickY,2))*Math.signum(-lStickY); //y value is inverted
        double x = 0.8*(Math.pow(lStickX,2))*Math.signum(lStickX);
        double rx = rStickX;
        if (rTrig>0.2){
            var = 1;
            var2+=1;
        } else if (lTrig>0.2){
            var = 0;
        }
        speedDivide = 1+var*2;
        double frontLeftSpd=-(y+x+rx)/speedDivide;
        double frontRightSpd=(y-x-rx)/speedDivide;
        double backLeftSpd=-(y-x+rx)/speedDivide;
        double backRightSpd=(y+x-rx)/speedDivide;
        double denominator = Math.max(Math.abs(y)+Math.abs(x)+Math.abs(rx),1);
        frontRightSpd /= denominator;
        frontLeftSpd /= denominator;
        backRightSpd /= denominator;
        backLeftSpd /= denominator;

        frontLeft.setPower(frontLeftSpd);
        frontRight.setPower(frontRightSpd);
        backLeft.setPower(backLeftSpd);
        backRight.setPower(backRightSpd);
        /*
        telemetry.addData("FL", frontLeft.getCurrentPosition());
        telemetry.addData("FR",frontRight.getCurrentPosition());
        telemetry.addData("BL",backLeft.getCurrentPosition());
        telemetry.addData("BR",backRight.getCurrentPosition());
        telemetry.update();*/
    }
}