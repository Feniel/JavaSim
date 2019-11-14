package org.javasim.examples.operations;

import org.javasim.RestartException;
import org.javasim.SimulationException;
import org.javasim.SimulationProcess;
import org.javasim.streams.ExponentialStream;

import java.io.IOException;

public class OperationRoom extends SimulationProcess {
    public OperationRoom(double mean)
    {
        STime = new ExponentialStream(mean);
        threadJobsDone = 0;
        operational = true;
        working = false;
        J = null;
    }

    public void run ()
    {
        double ActiveStart, ActiveEnd;

        while (!terminated())
        {
            working = true;

            while (!Coordinator.Queue2.isEmpty())
            {
                ActiveStart = currentTime();
                Coordinator.CheckFreq++;

                Coordinator.JobsInQueue += Coordinator.Queue2.queueSize();
                J = Coordinator.Queue2.dequeue();

                try
                {
                    hold(J.OperationTime);
                }
                catch (SimulationException e)
                {
                }
                catch (RestartException e)
                {
                }

                ActiveEnd = currentTime();
                Coordinator.MachineActiveTime += ActiveEnd - ActiveStart;
                //increment of processedJobs
                Coordinator.ProcessedJobs++;
                threadJobsDone++;
                System.out.println("OperationRoom-" + Thread.currentThread().getName() + ": Processed 1 job (total: "+ threadJobsDone +")");

                /*
                 * Introduce this new method because we usually rely upon the
                 * destructor of the object to do the work in C++.
                 */

                Coordinator.Queue3.enqueue(J);
            }

            working = false;

            try
            {
                cancel();
            }
            catch (RestartException e)
            {
            }
        }
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
    
    private int threadJobsDone;

    private Job J;
}
