package org.javasim.examples.operations;

import org.javasim.*;


public class OperationRoom extends SimulationProcess
{


    public OperationRoom ()
    {
        operational = true;
        working = false;
        J = null;
    }

    public void run ()
    {
        double ActiveStart, ActiveEnd;

        for (;;)
        {
            working = true;
            while (!Clinic.WaitQ.isEmpty()) {
                ActiveStart = currentTime();
                Clinic.CheckFreq++;
                Clinic.JobsInQueue += Clinic.WaitQ.size();
                waitroom =(PreparationRoom)Clinic.WaitQ.remove();
//	    	J = Clinic.JobQ.remove();
                J = waitroom.MyPatient();
                try {
                    if(Clinic.EntQ.isEmpty())
                        Clinic.IWQ.add(waitroom);
                    else
                        waitroom.activate();
                    hold(J.OperationTime());
                    ActiveEnd = currentTime();
                    Clinic.MachineActiveTime += ActiveEnd - ActiveStart;
                    Clinic.ProcessedJobs++;
                    Clinic.RecQ.add(J);
                    if (!Clinic.IRQ.isEmpty()) {
                        RecoveryRoom RR = (RecoveryRoom)Clinic.IRQ.remove();
                        RR.activate();
                    }
                    else {
                        Block(); //blocked
                        passivate();
                    }
                }
                catch (SimulationException e){}
                catch (RestartException e){}
            }
            working = false;
            try {
                cancel();
            }
            catch (RestartException e){}
        }
    }

    public void Block ()
    {
        operational = false;
    }

    public void Release ()
    {
        operational = true;
    }

    public boolean IsOperational ()
    {
        return operational;
    }

    public boolean Processing ()
    {
        return working;
    }


    private boolean operational;
    private boolean working;
    private Patient J;
    private PreparationRoom waitroom;

};