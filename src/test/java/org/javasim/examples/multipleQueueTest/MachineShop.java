/*
 * Copyright 1990-2008, Mark Little, University of Newcastle upon Tyne
 * and others contributors as indicated 
 * by the @authors tag. All rights reserved. 
 * See the copyright.txt in the distribution for a
 * full listing of individual contributors. 
 * This copyrighted material is made available to anyone wishing to use,
 * modify, copy, or redistribute it subject to the terms and conditions
 * of the GNU Lesser General Public License, v. 2.1.
 * This program is distributed in the hope that it will be useful, but WITHOUT A 
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
 * PARTICULAR PURPOSE.  See the GNU Lesser General Public License for more details.
 * You should have received a copy of the GNU Lesser General Public License,
 * v.2.1 along with this distribution; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, 
 * MA  02110-1301, USA.
 * 
 * (C) 1990-2008,
 */

package org.javasim.examples.multipleQueueTest;

import org.javasim.RestartException;
import org.javasim.Simulation;
import org.javasim.SimulationException;
import org.javasim.SimulationProcess;

public class MachineShop extends SimulationProcess
{
    public MachineShop(boolean isBreaks)
    {
        useBreaks = isBreaks;

        TotalResponseTime = 0.0;
        TotalJobs = 0;
        ProcessedJobs = 0;
        JobsInQueue = 0;
        CheckFreq = 0;
        MachineActiveTime = 0.0;
        MachineFailedTime = 0.0;
    }

    public void run ()
    {
        try
        {
            Arrivals A = new Arrivals(8);
            MachineShop.M1 = new Machine(8);
            MachineShop.M2 = new Machine(8);
            Job J = new Job();

            A.activate();

            Simulation.start();

            while (MachineShop.ProcessedJobs < 1000)
                hold(1000);

            System.out.println("Current time "+currentTime());
            System.out.println("Total number of jobs present " + TotalJobs);
            System.out.println("Total number of jobs processed "
                    + ProcessedJobs);
            System.out.println("Total response time of " + TotalResponseTime);
            System.out.println("Average response time = "
                    + (TotalResponseTime / ProcessedJobs));
            System.out
                    .println("Probability that machine is working = "
                            + ((MachineActiveTime - MachineFailedTime) / currentTime()));
            System.out.println("Probability that machine has failed = "
                    + (MachineFailedTime / MachineActiveTime));
            System.out.println("Average number of jobs present = "
                    + (JobsInQueue / CheckFreq));

            Simulation.stop();

            A.terminate();
            MachineShop.M1.terminate();
            MachineShop.M2.terminate();

            SimulationProcess.mainResume();
        }
        catch (SimulationException e)
        {
        }
        catch (RestartException e)
        {
        }
    }

    public void await ()
    {
        this.resumeProcess();
        SimulationProcess.mainSuspend();
    }

    public static Machine M1 = null;

    public static Machine M2 = null;

    public static Queue JobQ = new Queue();

    public static double TotalResponseTime = 0.0;

    public static long TotalJobs = 0;

    public static long ProcessedJobs = 0;

    public static long JobsInQueue = 0;

    public static long CheckFreq = 0;

    public static double MachineActiveTime = 0.0;

    public static double MachineFailedTime = 0.0;

    private boolean useBreaks;
}
