/*
 * =============================================================================
 * Simplified BSD License, see http://www.opensource.org/licenses/
 * -----------------------------------------------------------------------------
 * Copyright (c) 2012, Christian Jungreuthmayer, Vienna, Austria
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without 
 * modification, are permitted provided that the following conditions are met:
 * 
 *     * Redistributions of source code must retain the above copyright notice, 
 *       this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright 
 *       notice, this list of conditions and the following disclaimer in the 
 *       documentation and/or other materials provided with the distribution.
 *     * Neither the name of the Swiss Federal Institute of Technology Zurich 
 *       nor the names of its contributors may be used to endorse or promote 
 *       products derived from this software without specific prior written 
 *       permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" 
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE 
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE 
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE 
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR 
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF 
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS 
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN 
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) 
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE 
 * POSSIBILITY OF SUCH DAMAGE.
 * =============================================================================
 */
package at.acib.filter_thread;

import ch.javasoft.metabolic.efm.column.Column;
import ch.javasoft.metabolic.efm.memory.AppendableMemory;
import ch.javasoft.metabolic.efm.memory.IterableMemory;
import java.util.Iterator;

import at.acib.generegulation.GeneticRuleSet;

public class PostFilterThread implements Runnable
{
   Thread t;
   int t_id;
   int num_threads;
   GeneticRuleSet ruleSet;
   AppendableMemory<Column> filtered;
   IterableMemory<Column> memory;
   RemovedModesCounter remModesCnt;

   public PostFilterThread(int thread_id, int num, GeneticRuleSet myRuleSet, AppendableMemory<Column> myFiltered,
                           IterableMemory<Column> myMemory, RemovedModesCounter myRemCounter)
   {
      t_id        = thread_id;
      num_threads = num;
      ruleSet     = myRuleSet;
      filtered    = myFiltered;
      memory      = myMemory;
      remModesCnt = myRemCounter;

      t = new Thread(this, "PostFilterThread");
      t.start();
   }

   public void run()
   {
      Iterator<Column> myIt = memory.iterator();
      int col_cnt = 0;
      int col_inv = 0;
      while( myIt.hasNext() )
      {
         Column myCol = myIt.next();
         if( col_cnt%num_threads == t_id )
         {
            col_inv++;
            int hitRule = 0;
            if( (hitRule = ruleSet.doesEFMObeyRules( myCol )) == -1 )
            {
               synchronized(filtered)
               {
                  try
                  {
                     filtered.appendColumn(myCol);
                  }
                  catch(Exception e)
                  {
                     System.out.println("FATAL ERROR: child thread #" + t_id +" interrupted: " + e);
                     System.out.println("             execution aborted.");
                     System.exit(-1);
                  }
               }
            }
            else
            {
               synchronized(remModesCnt)
               {
                  remModesCnt.incNumRemovedModes();
                  remModesCnt.incRuleRemCnt(hitRule);
               }
            }
         }
         col_cnt++;
      }

      System.out.println("INFO: Exiting child thread #" + t_id + ". investigated modes: (" + col_inv + "/" + col_cnt + ")");
   }

   public Thread getThreadObj()
   {
      return(t);
   }
}

