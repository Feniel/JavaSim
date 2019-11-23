package org.javasim.examples.operations;

import org.javasim.*;


public class Reporter extends SimulationProcess
{
public Reporter(long interval)
{
	check=interval;	
}
public void run ()
{
	for (;;)
	{
		try
		{
		hold(check);
		}
		catch (SimulationException e)
		{
		}
		catch (RestartException e)
		{
		}
		
		checkcount++;
		entryqueue += Clinic.EntQ.size();
		idlewaitqueue += Clinic.IWQ.size();
		if (!Clinic.M.IsOperational())
			{
			blockfrq++;
			}
		else if (Clinic.M.Processing())
			operfrq++;
		
		if (Clinic.IRQ.isEmpty())
			recovery_busy_count++;
	}
}
public void report()
{
//	System.out.println("Checks "+checkcount);
//	System.out.println("Entry queue "+ entryqueue );
//	System.out.println(("Idlequeue "+ idlewaitqueue ));
//	System.out.println("Blockings "+ blockfrq );
	System.out. println(entryqueue+" "+idlewaitqueue+" "+blockfrq+" "+recovery_busy_count+" "+operfrq);
}

public void reset()
{
	checkcount=0;
	entryqueue=0;
	idlewaitqueue=0;
	blockfrq=0;
	operfrq=0;
	recovery_busy_count=0;
}
private long checkcount=0;
private long check;
private long entryqueue=0;
private long idlewaitqueue=0;
private long blockfrq=0;
private long operfrq=0;
private long recovery_busy_count=0;

}
