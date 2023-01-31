package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.ArmSlidesControlTeleOp;
import org.firstinspires.ftc.teamcode.DriveTrainTeleOp;
@TeleOp
public class TwoPlayer extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException{
        ManualArmSlides manualArmSlides = new ManualArmSlides(hardwareMap);
        DriveTrainTeleOp driveTrainTeleOp  = new DriveTrainTeleOp(hardwareMap);
        waitForStart();
        while (opModeIsActive()){
            manualArmSlides.ArmSlides(gamepad1.left_stick_y,gamepad1.right_stick_y,gamepad1.x,gamepad1.y,gamepad1.a,gamepad1.b,gamepad1.right_trigger,gamepad1.left_trigger,gamepad1.left_bumper,telemetry);
            driveTrainTeleOp.DriveTrain(gamepad2.left_stick_x,gamepad2.left_stick_y,gamepad2.right_stick_x,gamepad2.right_bumper, telemetry);
        }

    }
}
