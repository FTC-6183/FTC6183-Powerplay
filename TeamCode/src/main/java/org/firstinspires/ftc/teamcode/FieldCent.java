package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class FieldCent {
    private DcMotorEx motorBackLeft, motorBackRight, motorFrontLeft, motorFrontRight;
    private double speedDivide;
    private boolean calib = false, correct = false;
    ElapsedTime eTime = new ElapsedTime(ElapsedTime.Resolution.MILLISECONDS);
    private double y, x, rx, botHeading, storedHead = 0, denominator, frontLeftPower, backLeftPower, frontRightPower, backRightPower, rotX, rotY;
    public FieldCent(HardwareMap hardwareMap){
        motorBackLeft = hardwareMap.get(DcMotorEx.class, "BL");
        motorBackRight = hardwareMap.get(DcMotorEx.class, "BR");
        motorFrontLeft = hardwareMap.get(DcMotorEx.class, "FL");
        motorFrontRight = hardwareMap.get(DcMotorEx.class, "FR");
        motorBackLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        motorBackRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        motorFrontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        motorFrontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        motorFrontLeft.setDirection(DcMotorEx.Direction.REVERSE);
        motorBackLeft.setDirection(DcMotorEx.Direction.REVERSE);


    }



    public void Drive(double lStickX, double lStickY, double rStickX, double rTrig, Telemetry telemetry, BNO055IMU imu, boolean dPadUp, boolean dPadDown, BNO055IMU.Parameters parameters){
        y = -lStickY; // Remember, this is reversed!
        x = lStickX * 1.1; // Counteract imperfect strafing
        rx = rStickX;
        speedDivide = 1+rTrig*2;
        if (dPadUp){
            calib = true;
        } else if (dPadDown){
            calib = false;
        }
        if (!calib){botHeading = -storedHead-imu.getAngularOrientation().firstAngle;
        }
        else if (calib){
            botHeading = 0.0;
            storedHead = -imu.getAngularOrientation().firstAngle;
        }

        rotX = x * Math.cos(botHeading) - y * Math.sin(botHeading);
        rotY = x * Math.sin(botHeading) + y * Math.cos(botHeading);
        denominator = Math.max(Math.abs(y) + Math.abs(x) + Math.abs(rx), 1);
        frontLeftPower = (rotY + rotX + rx) / denominator;
        backLeftPower = (rotY - rotX + rx) / denominator;
        frontRightPower = (rotY - rotX - rx) / denominator;
        backRightPower = (rotY + rotX - rx) / denominator;

        motorFrontLeft.setPower(frontLeftPower/speedDivide);
        motorBackLeft.setPower(backLeftPower/speedDivide);
        motorFrontRight.setPower(frontRightPower/speedDivide);
        motorBackRight.setPower(backRightPower/speedDivide);
        //telemetry.addData("botHead", botHeading);
        //telemetry.addData("angle",-imu.getAngularOrientation().firstAngle);
        //telemetry.update();
    }
}
