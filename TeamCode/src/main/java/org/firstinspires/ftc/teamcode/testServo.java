package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class testServo extends LinearOpMode {

    Servo Intake = hardwareMap.get(Servo.class, "Intake");
    Servo R4B = hardwareMap.get(Servo.class, "R4B");
    Servo L4B = hardwareMap.get(Servo.class, "L4B");
    @Override
    public void runOpMode() throws InterruptedException {
        if (gamepad1.a) {
            Intake.setPosition(0);
        }
        else if (gamepad1.b) {Intake.setPosition(1);}
    }
}
