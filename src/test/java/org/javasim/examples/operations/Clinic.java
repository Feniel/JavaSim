package org.javasim.examples.operations;

import org.javasim.*;
import org.javasim.streams.*;
import java.util.*;


public class Clinic extends SimulationProcess
{
    
public Clinic ()
    {
	ExponentialStream inter = new ExponentialStream(25);
	ExponentialStream pre  = new ExponentialStream(40);
	ExponentialStream op = new ExponentialStream(40);
	ExponentialStream rec = new ExponentialStream(40);
	A = new Arrivals(inter, pre, op, rec);
    Clinic.M = new OperationRoom();
	for(int i=0;i<4;i++)
		{
			PRooms[i]= new PreparationRoom();
			IWQ.add(PRooms[i]);
		}
	
	for(int i=0;i<4;i++)
		{
			RRooms[i]= new RecoveryRoom();
			IRQ.add(RRooms[i]);
		}

    }
    
public void run ()
    {
	try
	{
	    
	    monitor.activate();
	    A.activate();
	    
	    Simulation.start();
	    hold(1000);
	    monitor.reset();
	    for(int i=0;i<20;i++)
	    {
	    hold(1000);
	    monitor.report();
	    hold(1000);
	    monitor.reset();
	    }
	    System.out.println("Total number of jobs present "+TotalJobs);
	    System.out.println("Total response time of "+TotalResponseTime);
	    System.out.println("Average response time = "+(TotalResponseTime / ProcessedJobs));
	    System.out.println("Probability that machine is working = "+(MachineActiveTime  / currentTime()));
	    System.out.println("Average number of jobs present = "+(JobsInQueue / CheckFreq));
	    System.out.println("Total number of jobs processed "+ProcessedJobs);
	    System.out.println(" ");
	    
	    
	    // Preparation Que Length
	    //System.out.println("List preparation   " + this.listPrep.size());	    
	    double x = 0; //zeroes
	    double u = 0; //average
	    for(int i = 0; i < this.listPrep.size(); i++) {
	    	u += (Integer) this.listPrep.get(i);
	    	if((Integer)this.listPrep.get(i) == 0) {
	    		x++;
	    	}
	    }	 
	    
	    double meanOfListPre = u/ listPrep.size();
	    double variance = 0;
	    for(int i = 0; i<this.listPrep.size();i++) {
	    	
	    	variance += (((Integer)this.listPrep.get(i) - meanOfListPre) * ((Integer)this.listPrep.get(i) - meanOfListPre)) / this.listPrep.size();
	    }
	    double std_prep = Math.sqrt(variance);
	    System.out.println("Probability of operation being in waiting state  " + Math.round((1-(x/ this.listPrep.size()))*100.0)/100.0);
	    System.out.println("Patients without wait time: "+x);
	    System.out.println(" ");
	    
	    // Idle Que length
	    double z = 0; //sum
	    double a = 0; //zeroes
	    for(int i = 0; i < this.listIdleForPrep.size(); i++) {
	    	z += (Integer) this.listIdleForPrep.get(i);
		    if((Integer)this.listIdleForPrep.get(i) == 0) {
	    		a++;
	    	}
	    }
	    
	    double lower = meanOfListPre - 1.96 * std_prep;
	    double higher = meanOfListPre + 1.96 * std_prep;


		System.out.println("Average for idle Que "+  (Double)(z/this.listIdleForPrep.size()));
		System.out.println("Patients directly to preparation = "+a +"\n");

		System.out.println("Average for Preparation Que = "+ meanOfListPre );
		System.out.println("Variance of list preparation = " + variance );
		System.out.println("Standard deviation = " + Math.sqrt(variance));
	    System.out.println("Lower 95% confidence limit = " + lower);
	    System.out.println("Higher 95% confidence limit = " + higher +"\n");
	    
	    Simulation.stop();

	    A.terminate();
	    Clinic.M.terminate();
	    monitor.terminate();
    
	    SimulationProcess.mainResume();
	}
	catch (SimulationException e)
	{
	}
	catch (RestartException e)
	{
	}
    }

public void Await ()
    {
	this.resumeProcess();
	SimulationProcess.mainSuspend();
    }

public static Arrivals A=null;
public static OperationRoom M = null;
public static PreparationRoom[] PRooms = new PreparationRoom [10];
public static RecoveryRoom[] RRooms = new RecoveryRoom [10];
public static Reporter monitor = new Reporter(10);
public static LinkedList<Patient> JobQ = new LinkedList<Patient>();
public static LinkedList<Patient> RecQ = new LinkedList<Patient>();
public static LinkedList<Patient> EntQ = new LinkedList<Patient>();
public static LinkedList<SimulationProcess> WaitQ = new LinkedList<SimulationProcess>(); //preparations waiting operation
public static LinkedList<SimulationProcess> IWQ = new LinkedList<SimulationProcess>(); // Idle preparations
public static LinkedList<SimulationProcess> IRQ = new LinkedList<SimulationProcess>(); //Idle Recoveries

public static double TotalResponseTime = 0.0;
public static long TotalJobs = 0;
public static long ProcessedJobs = 0;
public static long JobsInQueue = 0;
public static long CheckFreq = 0;
public static double MachineActiveTime = 0.0;

public static List listPrep = new ArrayList<>();
public static List listIdleForPrep = new ArrayList<>();	
   
    
};
