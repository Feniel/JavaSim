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

package org.javasim.examples.basic;

import org.javasim.RestartException;
import org.javasim.Simulation;
import org.javasim.SimulationException;
import org.javasim.SimulationProcess;

import java.util.ArrayList;

public class Coordinator extends SimulationProcess {
    public Coordinator(boolean isBreaks) {
        useBreaks = isBreaks;

        TotalResponseTime = 0.0;
        TotalJobs = 0;
        ProcessedJobs = 0;
        JobsInQueue = 0;
        CheckFreq = 0;
        MachineActiveTime = 0.0;
        MachineFailedTime = 0.0;
    }

    public void run() {
        try {
            Breaks B = null;
            Arrivals A = new Arrivals(8);
            Coordinator.O = new OperationRoom(8);
            System.out.println("Coordinator-" + Thread.currentThread().getName() + ": ACTIVATING the OperationRoom");
            Coordinator.O.activate();

            // activate Arrival
            System.out.println("Coordinator-" + Thread.currentThread().getName() + ": ACTIVATING an Arrivals");
            A.activate();

            if (useBreaks) {
                B = new Breaks();
                B.activate();
            }

            System.out.println("Starting Simulation");
            Simulation.start();

            // attempts to process 1000 jobs
            System.out.println("Looping until ProcessedJobs < 10");
            System.out.println("==================================================");

            while (Coordinator.ProcessedJobs < 10)
                hold(1000);

            System.out.println("Current time " + currentTime());
            System.out.println("Total number of jobs created " + TotalJobs);
            System.out.println("Total number of jobs processed " + ProcessedJobs);
            System.out.println("Total response time of " + TotalResponseTime);
            System.out.println("Average response time = " + (TotalResponseTime / ProcessedJobs));
            System.out.println("Probability that machine is working = " + ((MachineActiveTime - MachineFailedTime) / currentTime()));
            System.out.println("Probability that machine has failed = " + (MachineFailedTime / MachineActiveTime));
            System.out.println("Average number of jobs present = " + (JobsInQueue / CheckFreq));

            Simulation.stop();

            A.terminate();
            Coordinator.O.terminate();

            if (useBreaks)
                B.terminate();

            SimulationProcess.mainResume();
        } catch (SimulationException | RestartException e) {
        }
    }

    public void await() {
        this.resumeProcess();
        SimulationProcess.mainSuspend();
    }

    public static OperationRoom O = null;

    public static ArrayList<PreperationRoom> listP = null;

    public static ArrayList<RecoveryRoom> listR = null;

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
