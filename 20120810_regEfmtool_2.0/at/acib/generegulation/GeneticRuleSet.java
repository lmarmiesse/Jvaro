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
package at.acib.generegulation;

import java.io.FileInputStream;
import java.io.DataInputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.Iterator;

import ch.javasoft.metabolic.efm.column.Column;
import ch.javasoft.metabolic.efm.model.NetworkEfmModel;
import ch.javasoft.metabolic.efm.model.EfmModel;
import ch.javasoft.metabolic.efm.config.Config;

public class GeneticRuleSet
{
   private String strFileName;
   private NetworkEfmModel efmModel;
   private Config config;
   private ArrayList<GeneticRule> arrLstGeneticRules;
   private ArrayList<GeneBitSet> arrLstAllBitSets;
   private GeneBitSet arrAllBitSets[];
   private int numRules;

   ////////////////////////////////////////////////////////////////////////////
   ////////////////////////////////////////////////////////////////////////////
   public GeneticRuleSet(String nameOfRuleFile, EfmModel model, Config conf)
   {
      // System.out.println("DEBUG: entered constructor GeneticRuleSet.GeneticRuleSet(): name of gene regulation file: " + nameOfRuleFile);
      strFileName = nameOfRuleFile;
      efmModel    = (NetworkEfmModel) model;
      config      = conf;
      arrLstAllBitSets = new ArrayList<GeneBitSet>();
      arrLstGeneticRules = new ArrayList<GeneticRule>();
      this.readGeneFile();
   }
   ////////////////////////////////////////////////////////////////////////////


   ////////////////////////////////////////////////////////////////////////////
   ////////////////////////////////////////////////////////////////////////////
   private void readGeneFile()
   {
      try
      {
         FileInputStream fstream = new FileInputStream(strFileName);
         DataInputStream inStr   = new DataInputStream(fstream);
         BufferedReader  bufRd   = new BufferedReader(new InputStreamReader(inStr));

         String strLine;

         while((strLine = bufRd.readLine()) != null)
         {
            // System.out.println("INFO: read line: " + strLine);
            try
            {
               GeneticRule myGeneticRule = new GeneticRule(strLine, efmModel, config);
               arrLstGeneticRules.add(myGeneticRule);
               numRules++;
               // System.out.println("DEBUG: maximum reaction index: " +  myGeneticRule.getMaxReacSortedIdx() );
               // System.out.println("DEBUG: target reaction is reversible: " +  myGeneticRule.isTgtReactReversible() );
               // System.out.println("DEBUG: target reaction names: " +  Arrays.toString(myGeneticRule.getFncReacNames()) );
               // System.out.println("DEBUG: original target reaction index: " +  Arrays.toString(myGeneticRule.getFncReacOrigIdx()) );
               // System.out.println("DEBUG: function reversible reaction names: " +  Arrays.toString(myGeneticRule.getFncRevReacNames()) );
               // System.out.println("DEBUG: original function reversible reaction indices: " +  Arrays.toString(myGeneticRule.getFncRevReacOrigIdx()) );
               // System.out.println("DEBUG: rule qualifies for iteration application: " +  myGeneticRule.isIterationQualified() );

               ArrayList<GeneBitSet> arrLstGeneBitSet = myGeneticRule.getBitSets();
               Iterator<GeneBitSet> bitSetIt = arrLstGeneBitSet.iterator();
               while( bitSetIt.hasNext() )
               {
                  GeneBitSet curBitSet = bitSetIt.next();
                  arrLstAllBitSets.add(curBitSet);
                  // System.out.println("                          :" +  curBitSet );
               }
            }
            catch(Exception FoundCommentLineException )
            {
               // System.out.println("DEBUG: Caught FoundCommentLineException(): " + strLine);
            }
         }
         inStr.close();
      }
      catch(Exception e)
      {
         System.err.println("Error: " + e.getMessage());
         System.exit(-1);
      }

      arrAllBitSets = new GeneBitSet[ arrLstAllBitSets.size() ];
      arrAllBitSets = arrLstAllBitSets.toArray( arrAllBitSets );
      
   }
   ////////////////////////////////////////////////////////////////////////////


   ////////////////////////////////////////////////////////////////////////////
   ////////////////////////////////////////////////////////////////////////////
   public ArrayList<GeneticRule> getGeneticRules()
   {
      return(arrLstGeneticRules);
   }
   ////////////////////////////////////////////////////////////////////////////


   ////////////////////////////////////////////////////////////////////////////
   ////////////////////////////////////////////////////////////////////////////
   public GeneBitSet[] getAllBitSets()
   {
      return(arrAllBitSets);
   }
   ////////////////////////////////////////////////////////////////////////////


   ////////////////////////////////////////////////////////////////////////////
   ////////////////////////////////////////////////////////////////////////////
   public int doesEFMObeyRules(Column col)
   {
      Integer ruleCnt = 0;
      // System.out.println("DEBUG: GeneticRuleSet.doesEFMObeyRules(): col = " + col + " booleanSize(): " + col.booleanSize());
      Iterator<GeneticRule> itRule = arrLstGeneticRules.iterator();

      while( itRule.hasNext() )
      {
         ruleCnt++;
         GeneticRule rule = itRule.next();
         // System.out.println("DEBUG: checking rule '" + rule.getRuleString() + "'");
         if( rule.doesEFMObeyRule(col) == false )
         {
            return(ruleCnt);
         }
      }

      return( -1 );
   }
   ////////////////////////////////////////////////////////////////////////////


   ////////////////////////////////////////////////////////////////////////////
   ////////////////////////////////////////////////////////////////////////////
   public int getNumberOfRules()
   {
      return(numRules);
   }
   ////////////////////////////////////////////////////////////////////////////

}
