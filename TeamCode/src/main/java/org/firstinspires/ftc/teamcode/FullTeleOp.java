package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.ArmSlidesControlTeleOp;
import org.firstinspires.ftc.teamcode.DriveTrainTeleOp;
@TeleOp
public class FullTeleOp extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException{
        ArmSlidesControlTeleOp armSlidesControlTeleOp = new ArmSlidesControlTeleOp(hardwareMap);
        DriveTrainTeleOp driveTrainTeleOp  = new DriveTrainTeleOp(hardwareMap);
        waitForStart();
        while (opModeIsActive()){
            armSlidesControlTeleOp.ArmSlides(gamepad1.right_trigger,gamepad1.left_trigger, gamepad1.a,gamepad1.b,gamepad1.y,telemetry,gamepad1.dpad_up,gamepad1.dpad_down);
            driveTrainTeleOp.DriveTrain(gamepad1.left_stick_x,gamepad1.left_stick_y,gamepad1.right_stick_x,gamepad1.right_bumper);
        }

    }
}
