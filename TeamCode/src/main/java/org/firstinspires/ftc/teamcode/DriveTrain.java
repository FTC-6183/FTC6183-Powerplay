package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;

@TeleOp
public class DriveTrain extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException{
        DcMotorEx backLeft = hardwareMap.get(DcMotorEx.class, "BL");
        DcMotorEx backRight = hardwareMap.get(DcMotorEx.class, "BR");
        DcMotorEx frontLeft = hardwareMap.get(DcMotorEx.class, "FL");
        DcMotorEx frontRight = hardwareMap.get(DcMotorEx.class, "FR");
        double speedDivide = 1;
        waitForStart(); //waits for start

        while (opModeIsActive()){
            double y = (Math.pow(-gamepad1.left_stick_y,2))*Math.signum(-gamepad1.left_stick_y); //y value is inverted
            double x = (Math.pow(gamepad1.left_stick_x,2))*Math.signum(gamepad1.left_stick_x);
            double rx = gamepad1.right_stick_x;
            
            if (gamepad1.left_bumper){
                speedDivide = 4;
            }
            double frontLeftSpd=(y+x+rx)/speedDivide; //y+x+rx
            double frontRightSpd=(y-x-rx)/speedDivide;
            double backLeftSpd=(y-x+rx)/speedDivide; //y-x+rx
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
            backLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            frontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            backRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            frontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        }
    }
}
