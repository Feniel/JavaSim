package org.javasim.examples.operations;

import org.javasim.RestartException;
import org.javasim.SimulationException;
import org.javasim.SimulationProcess;
import org.javasim.streams.ExponentialStream;

import java.io.IOException;

public class RecoveryRoom extends SimulationProcess {
    public RecoveryRoom(double mean)
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

            while (!Coordinator.Queue3.isEmpty())
            {
                ActiveStart = currentTime();
                Coordinator.CheckFreq++;

                Coordinator.JobsInQueue += Coordinator.Queue3.queueSize();
                J = Coordinator.Queue3.dequeue();

                try
                {
                    hold(serviceTime());
                }
                catch (SimulationException e)
                {
                }
                catch (RestartException e)
                {
                }

                ActiveEnd = currentTime();
                Coordinator.MachineActiveTime += ActiveEnd - ActiveStart;
                Coordinator.ProcessedJobs++;
                threadJobsDone++;
                System.out.println("RecoveryRoom-" + Thread.currentThread().getName() + ": Processed 1 job (total: "+ threadJobsDone +")");

                /*
                 * Introduce this new method because we usually rely upon the
                 * destructor of the object to do the work in C++.
                 */

                J.finished();
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
