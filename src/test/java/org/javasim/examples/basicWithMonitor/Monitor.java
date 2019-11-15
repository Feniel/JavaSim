package org.javasim.examples.basicWithMonitor;

import org.javasim.RestartException;
import org.javasim.SimulationProcess;
import org.javasim.streams.ExponentialStream;

import java.io.IOException;

public class Monitor extends SimulationProcess
{
    public Monitor(Queue JobQ) {
        this.averageQueueLengths = 0;
        this.averageUtilization = 0;
        this.queue = JobQ;
        operational = true;
        working = false;

        totalJobs = 0;
        totalResponseTime = 0;
        jobsInQueue = 0;
        checkFreq = 0;
        machineActiveTime = 0;
        machineFailedTime = 0;
    }

    public void run ()
    {
        double ActiveStart, ActiveEnd;

        while (!terminated())
        {
            working = true;
            averageQueueLengths += queue.queueSize();
            averageQueueLengthCounter++;

            averageUtilization += MachineShop.MachineActiveTime;

            try
            {
                cancel();
            }
            catch (RestartException e)
            {
            }
        }
        working = false;
    }

    public void printData(){
        System.out.println("Current time "+currentTime());
        System.out.println("Total number of jobs present " + totalJobs);
        System.out.println("Total number of jobs processed "
                + MachineShop.ProcessedJobs);
        System.out.println("Total response time of " + totalResponseTime);
        System.out.println("Average response time = "
                + (totalResponseTime / MachineShop.ProcessedJobs));
        System.out
                .println("Probability that machine is working = "
                        + ((machineActiveTime - MachineShop.MachineFailedTime) / currentTime()));
        System.out.println("Probability that machine has failed = "
                + (MachineShop.MachineFailedTime / machineActiveTime));
        System.out.println("Average number of jobs present = "
                + (MachineShop.JobsInQueue / MachineShop.CheckFreq));
        System.out.println("-------------------");
        System.out.println("averageQueueLengths: "+ averageQueueLengths / averageQueueLengthCounter);
        System.out.println("averageUtilization: "+ averageUtilization / MachineShop.ProcessedJobs);
    }

    public void broken ()
    {
        operational = false;
    }

    public void fixed ()
    {
        operational = true;
    }

    public boolean isOperational ()
    {
        return operational;
    }

    public boolean processing ()
    {
        return working;
    }

    public double serviceTime ()
    {
        try
        {
            return STime.getNumber();
        }
        catch (IOException e)
        {
            return 0.0;
        }
    }

    private ExponentialStream STime;

    private boolean operational;

    private boolean working;

    private Queue queue;

    private double averageQueueLengths;

    private int averageQueueLengthCounter;

    private double averageUtilization;

    public int totalJobs;

    public int totalResponseTime;

    public long jobsInQueue = 0;

    public long checkFreq = 0;

    public double machineActiveTime = 0.0;

    public double machineFailedTime = 0.0;
}