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
        backLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        waitForStart(); //waits for start
        //double yOld = 0;
        //double xOld = 0;
        while (opModeIsActive()){
            double y = 0.8*(Math.pow(-gamepad1.left_stick_y,2))*Math.signum(-gamepad1.left_stick_y); //y value is inverted
            double x = 0.8*(Math.pow(gamepad1.left_stick_x,2))*Math.signum(gamepad1.left_stick_x);
            //double yNew = Math.min(yOld,y);
            //double xNew = Math.min(xOld,x);
            //yOld += 0.1*Math.signum(-gamepad1.left_stick_y)/50;
            //xOld += 0.1*Math.signum(gamepad1.right_stick_x)/50;

            double rx = gamepad1.right_stick_x;
            double speedDivide = gamepad1.right_trigger*3+1;

            /*
            if (Math.abs(x)<0.15){
                x=0;
            }
            */

            double frontLeftSpd=-(y+x+rx)/speedDivide; //y+x+rx
            double frontRightSpd=(y-x-rx)/speedDivide;
            double backLeftSpd=-(y-x+rx)/speedDivide; //y-x+rx
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
            telemetry.addData("leftStickY", y);
            telemetry.addData("leftStickX",x);
            telemetry.addData("yOld", yOld);
            telemetry.addData("xOld",xOld);
            telemetry.addData("yNew", yNew);
            telemetry.addData("xNew",xNew);
            telemetry.update();
            */

        }
    }
}
