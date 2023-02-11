package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.DcMotorEx;

public class FieldCent {
    private DcMotorEx motorBackLeft, motorBackRight, motorFrontLeft, motorFrontRight;
    private double speedDivide;
    private double y, x, rx, botHeading, denominator, frontLeftPower, backLeftPower, frontRightPower, backRightPower, rotX, rotY;
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
        BNO055IMU imu = hardwareMap.get(BNO055IMU.class, "imu");
        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
        parameters.angleUnit = BNO055IMU.AngleUnit.RADIANS;
        imu.initialize(parameters);
        botHeading = -imu.getAngularOrientation().firstAngle;
    }



    public void Drive(double lStickX, double lStickY, double rStickX, double rTrig){
        y = -lStickY; // Remember, this is reversed!
        x = lStickX * 1.1; // Counteract imperfect strafing
        rx = rStickX;
        speedDivide = 1+rTrig*2;
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
    }
}
