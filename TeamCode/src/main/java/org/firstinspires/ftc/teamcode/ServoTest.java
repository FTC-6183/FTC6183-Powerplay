package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.ServoImpl;
import com.qualcomm.robotcore.hardware.ServoImplEx;

@TeleOp
public class ServoTest extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
        CRServo Intake = hardwareMap.get(CRServo.class, "Intake");
        double x;
        waitForStart();

        while (opModeIsActive()){
            if (gamepad1.right_trigger>0.3){
                Intake.setPower(0.8);
            } else if (gamepad1.left_trigger>0.3) {
                Intake.setPower(-0.8);
            } else {
                Intake.setPower(0);
            }
            x= Intake.getPower();
            telemetry.addData("power", x);
            telemetry.update();

        }

    }
}
