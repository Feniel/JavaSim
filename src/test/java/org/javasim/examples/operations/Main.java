package org.javasim.examples.operations;

import java.util.ArrayList;
import java.util.List;

public class Main
{

public static void main (String[] args)
    {
    
	for (int i = 0; i < args.length; i++)
	{
	    if (args[i].equalsIgnoreCase("-help"))
	    {
		System.out.println("Usage: Main [-breaks] [-help]");
		System.exit(0);
	    }
	}

	Clinic m = new Clinic();

	m.Await();

	System.exit(0);
    }
    
}
