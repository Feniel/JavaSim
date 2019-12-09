package org.javasim.examples.operations;

import org.javasim.*;

public class Reporter extends SimulationProcess {
	public Reporter(long interval) {
		check = interval;
	}

	public void run() {
		for (;;) {
			try {
				hold(check);
			} catch (SimulationException e) {
			} catch (RestartException e) {
			}

			checkcount++;
			entryqueue += Clinic.EntQ.size();
			idlewaitqueue += Clinic.IWQ.size();
			if (!Clinic.M.IsOperational()) {
				blockfrq++;
			} else if (Clinic.M.Processing())
				operfrq++;

			if (Clinic.IRQ.isEmpty())
				recovery_busy_count++;
		}
	}

	public void report() {
//	System.out.println("Checks "+checkcount);
//	System.out.println("Entry queue "+ entryqueue );
//	System.out.println(("Idlequeue "+ idlewaitqueue ));
//	System.out.println("Blockings "+ blockfrq );
		double x = 0; //zeroes
		double u = 0; //average
		for(int i = 0; i < Clinic.listPrep.size(); i++) {
			u += (Integer) Clinic.listPrep.get(i);
			if((Integer)Clinic.listPrep.get(i) == 0) {
				x++;
			}
		}

		double meanOfListPre = u/ Clinic.listPrep.size();
		double variance = 0;
		for(int i = 0; i< Clinic.listPrep.size();i++) {

			variance += (((Integer)Clinic.listPrep.get(i) - meanOfListPre) * ((Integer)Clinic.listPrep.get(i) - meanOfListPre)) / Clinic.listPrep.size();
		}
		double std_prep = Math.sqrt(variance);
//		System.out.println("-----------------------Probability of operation being in waiting state  " + Math.round((1-(x/ Clinic.listPrep.size()))*100.0)/100.0);
//		System.out.println("Patients without wait time: "+x);
//		System.out.println(" ");

		// Idle Que length
		double z = 0; //sum
		double a = 0; //zeroes
		for(int i = 0; i < Clinic.listIdleForPrep.size(); i++) {
			z += (Integer) Clinic.listIdleForPrep.get(i);
			if((Integer)Clinic.listIdleForPrep.get(i) == 0) {
				a++;
			}
		}

		double lower = meanOfListPre - 1.96 * std_prep;
		double higher = meanOfListPre + 1.96 * std_prep;


//		System.out.println("Average for idle Que "+  (Double)(z/Clinic.listIdleForPrep.size()) * 100);
//		System.out.println("Patients directly to preparation = "+a +"\n");
//
//		System.out.println("Average for Preparation Que = "+ meanOfListPre );
//		System.out.println("Variance of list preparation = " + variance );
//		System.out.println("Standard deviation = " + Math.sqrt(variance));
//		System.out.println("Lower 95% confidence limit = " + lower);
//		System.out.println("Higher 95% confidence limit = " + higher +"\n");

		//System.out
		//		.println(entryqueue + "\t" + idlewaitqueue + "\t" + blockfrq + "\t" + recovery_busy_count + "\t" + operfrq +"\t"+
		//				Math.round((1-(x/ Clinic.listPrep.size()))*100.0)/100.0 + "\t"+ meanOfListPre + "\t"+ variance + "\t"+Math.sqrt(variance)
		//				+ "\t" +lower + "\t"+higher );
	}

	public void reset() {
		checkcount = 0;
		entryqueue = 0;
		idlewaitqueue = 0;
		blockfrq = 0;
		operfrq = 0;
		recovery_busy_count = 0;
	}

	private long checkcount = 0;
	private long check;
	private long entryqueue = 0;
	private long idlewaitqueue = 0;
	private long blockfrq = 0;
	private long operfrq = 0;
	private long recovery_busy_count = 0;

}
