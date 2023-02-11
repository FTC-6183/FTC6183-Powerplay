package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcore.internal.android.dx.cf.iface.Field;

@TeleOp
public class FieldPlayers extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException{
        NewArmSlidesTeleOp newArmSlidesTeleOp = new NewArmSlidesTeleOp(hardwareMap);
        FieldCent fieldCent = new FieldCent(hardwareMap);
        waitForStart();
        while (opModeIsActive()){
            newArmSlidesTeleOp.Lifter(gamepad1.right_trigger,gamepad1.left_trigger,gamepad1.a,gamepad1.b,gamepad1.y,gamepad1.left_bumper,gamepad1.left_stick_y,gamepad1.right_stick_y,telemetry, gamepad1.x, gamepad2.x);
            fieldCent.Drive(gamepad2.left_stick_x,gamepad2.left_stick_y,gamepad2.right_stick_x,gamepad2.right_trigger);
        }
    }
}
