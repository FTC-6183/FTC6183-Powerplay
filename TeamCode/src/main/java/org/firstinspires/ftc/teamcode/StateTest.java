package org.firstinspires.ftc.teamcode;


import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
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
        int targetPos;
        ElapsedTime eTime = new ElapsedTime(ElapsedTime.Resolution.MILLISECONDS);
        State liftState = State.START;
        waitForStart();
        while (opModeIsActive()){
            switch(liftState){
                case START:
                    //height of lift and height of arm set here
                    if (gamepad1.right_trigger>0.2){
                        //go to pos
                        //rotate crservo
                    }
                    if (gamepad1.a){
                        targetPos = 1;
                        liftState=State.LIFT;
                    }
                    else if (gamepad1.b){
                        targetPos = 2;
                        liftState=State.LIFT;
                    }
                    else if (gamepad1.y){
                        targetPos = 3;
                        liftState=State.LIFT;
                    }
                    break;
                case LIFT:
                    //set lift pos to targetPos
                    //set arm pos to other pos
                    if (gamepad1.left_trigger>0.2){
                        liftState = State.DEPOSIT;
                        //servo power to outtake
                        eTime.reset();

                    }
                    break;
                case DEPOSIT:
                    if (eTime.time()>500){
                        liftState = State.START;

                    }
                    break;
            }
        }
    }
}
