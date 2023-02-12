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
        NewArmSlidesTeleOp newArmSlidesTeleOp = new NewArmSlidesTeleOp(hardwareMap);
        DriveTrainTeleOp driveTrainTeleOp  = new DriveTrainTeleOp(hardwareMap);
        waitForStart();
        while (opModeIsActive()){
            newArmSlidesTeleOp.Lifter(gamepad1.right_trigger,gamepad1.left_trigger,gamepad1.a,gamepad1.b,gamepad1.y,gamepad1.left_bumper,gamepad1.left_stick_y,gamepad1.right_stick_y,telemetry, gamepad1.x, gamepad2.x,gamepad1.dpad_up );
            driveTrainTeleOp.DriveTrain(gamepad2.left_stick_x,gamepad2.left_stick_y,gamepad2.right_stick_x,gamepad2.right_trigger, telemetry, gamepad2.dpad_down, gamepad2.left_trigger);
        }

    }
}
