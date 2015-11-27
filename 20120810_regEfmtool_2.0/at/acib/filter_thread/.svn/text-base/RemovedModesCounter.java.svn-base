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

public class RemovedModesCounter
{
   int rem_modes;
   Integer[] modes_rem_stat;
   int numberOfRules;

   public RemovedModesCounter(int numRules)
   {
      rem_modes = 0;
      numberOfRules = numRules;
      modes_rem_stat = new Integer[numRules + 1];

      for( int i = 1; i <= numRules; i++ )
      {
         modes_rem_stat[i] = 0;
      }
   }

   public int getNumRemovedModes()
   {
      return(rem_modes);
   }

   public void incNumRemovedModes()
   {
      rem_modes++;
      return;
   }

   public void incRuleRemCnt(int ruleId)
   {
      if( ruleId < 1 )
      {
         System.out.println("FATAL ERROR: ruleId (" + ruleId + ") must not be less than 1");
         System.out.println("             execution aborted.");
         System.exit(-1);
      }
      if( ruleId  > numberOfRules )
      {
         System.out.println("FATAL ERROR: ruleId (" + ruleId + ") must not be greater than number of rules" + numberOfRules);
         System.out.println("             execution aborted.");
         System.exit(-1);
      }

      modes_rem_stat[ruleId]++;
   }

   public Integer[] getRuleHitStatistics()
   {
      return(modes_rem_stat);
   }

   public void printRuleHitStatistics()
   {
      for( int i = 1; i <= numberOfRules; i++ )
      {
         System.out.println("Rule #" + i + ": " + modes_rem_stat[i]);
      }
   }
}

