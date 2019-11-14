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

package org.javasim.tests.internal;

import org.javasim.SimulationProcess;
import org.javasim.internal.SimulationProcessIterator;
import org.javasim.internal.SimulationProcessList;
import org.javasim.streams.ExponentialStream;
import org.junit.Test;

import static org.junit.Assert.*;

class DummyProcess extends SimulationProcess
{
    public DummyProcess (double mean)
    {
        InterArrivalTime = new ExponentialStream(mean);
    }

    public void run ()
    {
        try
        {
            hold(InterArrivalTime.getNumber());
        }
        catch (final Exception ex)
        {
        }
    }

    private ExponentialStream InterArrivalTime;
}

public class SimulationProcessIteratorUnitTest
{
    @Test
    public void test () throws Exception
    {
        SimulationProcessList list = new SimulationProcessList();
        DummyProcess d1 = new DummyProcess(0.0);
        DummyProcess d2 = new DummyProcess(1.0);
        
        list.insert(d1);
        list.insert(d2, true);
        
        SimulationProcessIterator iter = new SimulationProcessIterator(list);
        
        assertEquals(iter.get(), d2);
        assertEquals(iter.get(), d1);
        assertEquals(iter.get(), null);
    }
}
