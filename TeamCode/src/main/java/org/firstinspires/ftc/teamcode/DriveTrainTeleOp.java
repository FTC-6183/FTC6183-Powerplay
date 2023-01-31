package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
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
    public DriveTrainTeleOp(HardwareMap hardwareMap) {
        backLeft = hardwareMap.get(DcMotorEx.class, "BL");
        backRight = hardwareMap.get(DcMotorEx.class, "BR");
        frontLeft = hardwareMap.get(DcMotorEx.class, "FL");
        frontRight = hardwareMap.get(DcMotorEx.class, "FR");
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
    public void DriveTrain(double lStickX, double lStickY, double rStickX, boolean rBump, Telemetry telemetry){
        double y = 0.8*(Math.pow(-lStickY,2))*Math.signum(-lStickY); //y value is inverted
        double x = 0.8*(Math.pow(lStickX,2))*Math.signum(lStickX);
        double rx = rStickX;
        if (rBump){
            speedDivide = 3;
        } else {
            speedDivide = 1;
        }
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