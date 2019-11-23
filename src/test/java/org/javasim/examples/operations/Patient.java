package org.javasim.examples.operations;

import org.javasim.*;


public class Patient
{

    public Patient (double pretime, double optime, double rectime)
    {


        ResponseTime = 0.0;
        ArrivalTime = Scheduler.currentTime();
        PreDuration = pretime;
        OpDuration = optime;
        RecDuration = rectime;
    }

    public double PreparationTime ()
    {
        return PreDuration;
    }

    public double OperationTime ()
    {
        return OpDuration;
    }

    public double RecoveryTime ()
    {
        return RecDuration;
    }

    public void finished ()
    {
        ResponseTime = Scheduler.currentTime() - ArrivalTime;
        Clinic.TotalResponseTime += ResponseTime;
    }

    private double ResponseTime;
    private double ArrivalTime;
    private double PreDuration;
    private double OpDuration;
    private double RecDuration;

};
