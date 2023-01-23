package org.firstinspires.ftc.teamcode;


import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.util.ElapsedTime;

@TeleOp
public class StateTest extends LinearOpMode {
    enum State{
        START,
        LIFT,
        DEPOSIT
    }
    @Override
    public void runOpMode() throws InterruptedException {
        DcMotorEx RSlides = hardwareMap.get(DcMotorEx.class, "RSlides");
        DcMotorEx LSlides = hardwareMap.get(DcMotorEx.class, "LSlides");
        DcMotorEx VBMotor = hardwareMap.get(DcMotorEx.class, "VBMotor");
        CRServo Intake = hardwareMap.get(CRServo.class, "Intake");
        RSlides.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        LSlides.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        VBMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        VBMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        VBMotor.setTargetPosition(0);
        RSlides.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        RSlides.setTargetPosition(0);
        LSlides.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        LSlides.setTargetPosition(0);
        VBMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        LSlides.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        RSlides.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        int targetPos = 0;
        int armTarget = 0;
        boolean back = false;
        ElapsedTime eTime = new ElapsedTime(ElapsedTime.Resolution.MILLISECONDS);
        State liftState = State.START;
        waitForStart();
        while (opModeIsActive()){
            switch(liftState) {
                case START:
                    Intake.setPower(0);
                    VBMotor.setTargetPosition(0);
                    LSlides.setTargetPosition(0);
                    RSlides.setTargetPosition(0);
                    if (gamepad1.right_trigger > 0.2) {
                        Intake.setPower(0.5);
                    }
                    if (gamepad1.a) {
                        targetPos = 0;
                        armTarget = 525;
                        back = true;
                        liftState = State.LIFT;
                    } else if (gamepad1.b) {
                        targetPos = 1600;
                        armTarget = 525;
                        back = true;
                        liftState = State.LIFT;
                    } else if (gamepad1.y) {
                        targetPos = 3050;
                        armTarget = 525;
                        back = false;
                        liftState = State.LIFT;
                    }
                    break;
                case LIFT:
                    LSlides.setPower(0.25);
                    RSlides.setPower(-0.25);
                    VBMotor.setPower(0.4);
                    VBMotor.setTargetPosition(armTarget);
                    LSlides.setTargetPosition(targetPos);
                    RSlides.setTargetPosition(targetPos);
                    if (gamepad1.left_trigger > 0.2) {
                        if (back) {
                            armTarget = 800;
                        } else {
                            armTarget = 786;
                        }
                        VBMotor.setTargetPosition(armTarget);
                        liftState = State.DEPOSIT;
                        eTime.reset();
                    }
                    break;
                case DEPOSIT:
                    if (eTime.time()>1500){
                        Intake.setPower(-0.8);
                    } else if (eTime.time()>3-00){
                        LSlides.setPower(-0.25);
                        RSlides.setPower(0.25);
                        VBMotor.setPower(-0.2);
                        liftState = State.START;
                    }
                    break;
            }
        }
    }
}
