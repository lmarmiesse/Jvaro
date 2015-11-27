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

import java.util.Scanner;
import java.util.ArrayList;
import java.util.Stack;
import java.util.BitSet;
import java.util.Iterator;

import ch.javasoft.util.ints.IntList;
import ch.javasoft.util.ints.IntIterator;
import ch.javasoft.metabolic.efm.column.Column;
import ch.javasoft.metabolic.efm.model.NetworkEfmModel;
import ch.javasoft.metabolic.efm.util.ReactionMapping;
import ch.javasoft.metabolic.efm.util.ReactionMapping.Layer;
import ch.javasoft.metabolic.efm.config.Config;

public class GeneticRule
{
   private String strRule;
   private String strTgtReac;
   private Integer intTgtReac;
   private String strBoolFnc;
   private NetworkEfmModel efmModel;
   private Config config;
   private String ruleAsStringArray[];
   private RPNElem ruleAsArrayIdx[];
   private Stack<RPNElem> ruleAsStackIdx;

   private IntList intLstTgtReacSortedIdx;
   private Integer intMaxTgtReacSortedIdx;
   private Integer intMaxFncReacSortedIdx;
   private Integer intMaxTotReacSortedIdx;

   private Boolean boolTgtReacIsReversible;
   private ArrayList<String>  arrLstReacNames;
   private ArrayList<Integer> arrLstReacOrigIdx;
   private ArrayList<String>  arrLstRevReacNames;
   private ArrayList<Integer> arrLstRevReacOrigIdx;
   private ArrayList<String>  arrLstIrrevReacNames;
   private ArrayList<Integer> arrLstIrrevReacOrigIdx;

   private Boolean boolIsIterationQualified;

   private ArrayList<GeneBitSet> arrLstBitSetRule;

   ////////////////////////////////////////////////////////////////////////////
   ////////////////////////////////////////////////////////////////////////////
   public GeneticRule(String ruleAsString, NetworkEfmModel model, Config conf) throws FoundCommentLineException
   {
      // System.out.println("DEBUG: entered constructor GeneticRule.GeneticRule(): Rule: " + ruleAsString);
      strRule    = ruleAsString;
      efmModel   = model;
      config     = conf;
      boolTgtReacIsReversible  = false;
      boolIsIterationQualified = false;
      arrLstReacNames        = new ArrayList<String>();
      arrLstReacOrigIdx      = new ArrayList<Integer>();
      arrLstRevReacNames     = new ArrayList<String>();
      arrLstRevReacOrigIdx   = new ArrayList<Integer>();
      arrLstIrrevReacNames   = new ArrayList<String>();
      arrLstIrrevReacOrigIdx = new ArrayList<Integer>();
      arrLstBitSetRule       = new ArrayList<GeneBitSet>();
      // ruleAsStackIdx         = new Stack<Integer>();
      ruleAsStackIdx         = new Stack<RPNElem>();

      // extract target reaction and boolean function
      if( !this.processRuleString() )
      {
         throw new FoundCommentLineException("Found comment line: '" + ruleAsString + "'");
      }

      // tokenize boolean function
      this.tokenize();

      // convert array of token to reverse polish notation
      ruleAsStackIdx = RevPolishNotation.infixToPostfix(ruleAsArrayIdx);

      boolIsIterationQualified = this.determineIterationQualification();

      if( boolIsIterationQualified )
      {
         this.generateBitSets();
      }
   }
   ////////////////////////////////////////////////////////////////////////////


   ////////////////////////////////////////////////////////////////////////////
   // returns 'false' if we found a comment line
   ////////////////////////////////////////////////////////////////////////////
   private Boolean processRuleString()
   {
      strTgtReac = new String();
      strBoolFnc = new String();
      String strLine = new String(strRule);


      /////////////////////////////////////////////////////////////////////////
      // remove all whitespace characters from line
      /////////////////////////////////////////////////////////////////////////
      strLine = strLine.replaceAll("\\s+","");
      // System.out.println("INFO: line without whitespaces: " + strLine);
      /////////////////////////////////////////////////////////////////////////


      /////////////////////////////////////////////////////////////////////////
      // return if line starts with '#', which means it is a comment line
      /////////////////////////////////////////////////////////////////////////
      if( strLine.indexOf('#') == 0 )
      {
         // System.out.println("DEBUG: found comment line");
         return(false);
      }
      /////////////////////////////////////////////////////////////////////////

      /////////////////////////////////////////////////////////////////////////
      // check if line contains any invalid character
      // also some other things are checked, however, this is by far
      // no real parser, but just a simple plausibility check!!
      /////////////////////////////////////////////////////////////////////////
      if( strLine.matches(".*[^0-9a-zA-Z_=!()|&:].*") )
      {
         System.out.println("FATAL ERROR: found invalid character in line '" + strLine + "'");
         System.out.println("             valid characters are: a-z,A-Z,0-9,_,!,&,|");
         System.out.println("             execution aborted.");
         System.exit(-1);
      }

      // check if two operators follow each other
      if( strLine.matches("||") || strLine.matches("|&") || strLine.matches("|!") ||
          strLine.matches("&|") || strLine.matches("&&") || strLine.matches("&!") ||
          strLine.matches("!|") || strLine.matches("!&") || strLine.matches("!!") )
      {
         System.out.println("FATAL ERROR: found invalid character combination (&&,||) in line '" + strLine + "'");
         System.out.println("             note: an AND is as signle '&', an OR is a single '|'");
         System.out.println("             execution aborted.");
         System.exit(-1);
      }

      // check the number of opening and closing parenthesis
      // and if the number of operators is equal to the number
      // of pairs of parenthesis
      int num_oparent  = getNumOccOfChar(strLine,'(');
      int num_cparent  = getNumOccOfChar(strLine,')');
      int num_operator = getNumOccOfChar(strLine,'!') + getNumOccOfChar(strLine,'|') + getNumOccOfChar(strLine,'&');

      if( num_oparent != num_cparent )
      {
         System.out.println("FATAL ERROR: number of opening parentheses (" + num_oparent + ")" +
                            " is not equal to number of closing parenthesis (" + num_cparent + ") in line '" + strLine + "'");
         System.out.println("             execution aborted.");
         System.exit(-1);
      }

      if( num_operator != num_oparent )
      {
         System.out.println("FATAL ERROR: number of operators (!,|,&) (" + num_operator + ")" +
                            " is not equal to number of pairs of parenthesis (" + num_cparent + ") in line '" + strLine + "'");
         System.out.println("             execution aborted.");
         System.exit(-1);
      }

      // check number of equal signs
      int num_equal = getNumOccOfChar(strLine,'=');
      if( num_equal != 1 )
      {
         System.out.println("FATAL ERROR: number of equal signs (=) is not equal to 1, but " + num_equal + " in line '" + strLine + "'");
         System.out.println("             execution aborted.");
         System.exit(-1);
      }

      // check if last character is a closing parenthesis
      if( strLine.charAt( strLine.length() - 1 ) != ')' )
      {
         System.out.println("FATAL ERROR: found invalid character at last position '" + strLine.charAt( strLine.length() - 1 ) + "' in line '" + strLine + "'");
         System.out.println("             character must be a closing parenthesis ')'");
         System.out.println("             execution aborted.");
         System.exit(-1);
      }

      // check if character right after equal sign is an opening parenthesis
      int intEqual = strLine.indexOf('=');
      String strFunc = strLine.substring( intEqual + 1);

      if( strFunc.charAt(0) != '(' )
      {
         System.out.println("FATAL ERROR: found invalid character at position 0 '" + strFunc.charAt(0) + "' in line '" + strLine + "'");
         System.out.println("             character must be an opening parenthesis '('");
         System.out.println("             execution aborted.");
         System.exit(-1);
      }
      /////////////////////////////////////////////////////////////////////////


      /////////////////////////////////////////////////////////////////////////
      // check if there is a '=' character
      /////////////////////////////////////////////////////////////////////////
      if( strLine.indexOf('=') == -1 )
      {
         System.out.println("FATAL ERROR: invalid line '" + strLine + "': character '=' missing");
         System.out.println("             execution aborted.");
         System.exit(-1);
      }
      /////////////////////////////////////////////////////////////////////////


      /////////////////////////////////////////////////////////////////////////
      // split line by '=' in order to get target gene and function
      /////////////////////////////////////////////////////////////////////////
      Scanner scnTgtFnc = new Scanner(strLine);
      scnTgtFnc.useDelimiter("=");
      if( scnTgtFnc.hasNext() )
      {
         strTgtReac = scnTgtFnc.next();
         strBoolFnc = scnTgtFnc.next();
      }
      /////////////////////////////////////////////////////////////////////////

      // System.out.println("DEBUG: found target reaction:  " + strTgtReac);
      // System.out.println("DEBUG: found boolean rule: " + strBoolFnc);


      /////////////////////////////////////////////////////////////////////////
      // check if target reaction is known
      /////////////////////////////////////////////////////////////////////////
      final ReactionMapping rmap = new ReactionMapping(config, efmModel.getMetabolicNetwork(), efmModel.getReactionSorting());
      // int reacIdx = efmModel.getMetabolicNetwork().getReactionIndex(strTgtReac);
      int reacIdx = rmap.getOriginalReactionIndexByName(strTgtReac);
      if( reacIdx == -1 )
      {
         System.out.println("FATAL ERROR: target gene/reaction '" + strTgtReac + "' not known.");
         System.out.println("             known genes/reactions are: " + efmModel.getMetabolicNetwork().toStringVerbose());
         System.out.println("             execution aborted.");
         System.exit(-1);
      }
      intTgtReac = reacIdx;
      /////////////////////////////////////////////////////////////////////////


      /////////////////////////////////////////////////////////////////////////
      // get reaction index of sorted matrix
      /////////////////////////////////////////////////////////////////////////
      // note: target reaction could be a reversible reaction
      // which would mean that there exist two indices in the sorted matrix
      intLstTgtReacSortedIdx = rmap.get(Layer.Original, reacIdx,  Layer.Sorted);

      if( intLstTgtReacSortedIdx.size() > 1 )
      {
         boolTgtReacIsReversible = true;
      }
      
      intMaxTgtReacSortedIdx = getMaxOfIntList(intLstTgtReacSortedIdx);
      /////////////////////////////////////////////////////////////////////////


      /////////////////////////////////////////////////////////////////////////
      /////////////////////////////////////////////////////////////////////////
      // System.out.println("DEBUG: original index of reaction '" + strTgtReac + "': " + reacIdx);
      // System.out.println("DEBUG: sorted index of reaction '"   + strTgtReac + "': " + intLstTgtReacSortedIdx);
      /////////////////////////////////////////////////////////////////////////

      return(true);
   }
   ////////////////////////////////////////////////////////////////////////////


   ////////////////////////////////////////////////////////////////////////////
   ////////////////////////////////////////////////////////////////////////////
   private void tokenize()
   {
      ArrayList<String>  arrlstTokens    = new ArrayList<String>();
      // ArrayList<Integer> arrlstTokensIdx = new ArrayList<Integer>();
      ArrayList<RPNElem> arrlstTokensIdx = new ArrayList<RPNElem>();

      String strFnc = new String(strBoolFnc);
      String tmpToken = new String();
      intMaxFncReacSortedIdx = -1;

      final ReactionMapping rmap = new ReactionMapping(config, efmModel.getMetabolicNetwork(), efmModel.getReactionSorting());

      int tokenFinished = 1;
      int reacIdx;
      while( strFnc.length() > 0 )
      {
         if( strFnc.charAt(0) == '(' || strFnc.charAt(0) == ')' ||
             strFnc.charAt(0) == '&' || strFnc.charAt(0) == '|' ||
             strFnc.charAt(0) == '!' )
         {
            // System.out.println("strFnc.charAt(0): " + strFnc.charAt(0) );
            if( tokenFinished == 0 )
            {
               // we are dealing with a reaction name

               // each reaction name must have a prefix that defines
               // if it is 0-active, 1-active, or full-active
               // prefix=0: 0-active
               // prefix=1: 1-active
               // prefix=f: full-active
               char prefix = tmpToken.charAt(0);
               int activityType = -1;

               if( prefix == '0' )
               {
                  activityType = RPNElem.ZERO_ACTIVE;
               }
               else if( prefix == '1' )
               {
                  activityType = RPNElem.ONE_ACTIVE;
               }
               else if( prefix == 'f' )
               {
                  activityType = RPNElem.FULL_ACTIVE;
               }
               else
               {
                  // invalid token name
                  System.out.println("FATAL ERROR: reaction '" + tmpToken + "' does not have a valid prefix.");
                  System.out.println("             valid prefixes are:");
                  System.out.println("             0 .... 0-activity");
                  System.out.println("             1 .... 1-activity");
                  System.out.println("             f .... f-activity");
                  System.out.println("             execution aborted.");
                  System.exit(-1);
               }

               // remove prefix from token name
               tmpToken = tmpToken.substring(1);

               // if( (reacIdx = efmModel.getMetabolicNetwork().getReactionIndex(tmpToken)) == -1 )
               if( (reacIdx = rmap.getOriginalReactionIndexByName(tmpToken)) == -1 )
               {
                  System.out.println("FATAL ERROR: reaction '" + tmpToken + "' not known.");
                  System.out.println("             known reactions are: " + efmModel.getMetabolicNetwork().toStringVerbose());
                  System.out.println("             execution aborted.");
                  System.exit(-1);
               }
               // System.out.println("original index of reaction '" + tmpToken + "' is: " + reacIdx);
               arrlstTokens.add( tmpToken );
               arrlstTokensIdx.add( new RPNElem(reacIdx, true, activityType) );


               IntList intLstReac = rmap.get(Layer.Original, reacIdx,  Layer.Sorted);
               Integer maxIdx     = getMaxOfIntList(intLstReac);
               // check if we have a new maximum reaction index
               if( maxIdx > intMaxFncReacSortedIdx )
               {
                  intMaxFncReacSortedIdx = maxIdx;
               }

               arrLstReacNames.add( tmpToken );
               arrLstReacOrigIdx.add( reacIdx );
               if( intLstReac.size() > 1 )
               {
                  arrLstRevReacNames.add( tmpToken );
                  arrLstRevReacOrigIdx.add( reacIdx );
               }
               else
               {
                  arrLstIrrevReacNames.add( tmpToken );
                  arrLstIrrevReacOrigIdx.add( reacIdx );
               }

               // System.out.println("DEBUG: sorted index of reaction '" + tmpToken + "': "  + intLstReac);

               // reset temporary token used for storing reaction name
               tmpToken = "";
            }

            if( strFnc.charAt(0) == '(' )
            {
               // arrlstTokensIdx.add( RevPolishNotation.P_OPEN );
               arrlstTokensIdx.add( new RPNElem( RevPolishNotation.P_OPEN, false) );
            }
            else if( strFnc.charAt(0) == ')' )
            {
               // arrlstTokensIdx.add( RevPolishNotation.P_CLOSE );
               arrlstTokensIdx.add( new RPNElem( RevPolishNotation.P_CLOSE, false) );
            }
            else if( strFnc.charAt(0) == '&' )
            {
               // arrlstTokensIdx.add( RevPolishNotation.AND );
               arrlstTokensIdx.add( new RPNElem( RevPolishNotation.AND, false) );
            }
            else if( strFnc.charAt(0) == '|' )
            {
               // arrlstTokensIdx.add( RevPolishNotation.OR );
               arrlstTokensIdx.add( new RPNElem( RevPolishNotation.OR, false) );
            }
            else if( strFnc.charAt(0) == '!' )
            {
               // arrlstTokensIdx.add( RevPolishNotation.NOT );
               arrlstTokensIdx.add( new RPNElem( RevPolishNotation.NOT, false) );
            }

            arrlstTokens.add( strFnc.substring(0,1) );
            tokenFinished = 1;
         }
         else
         {
            tokenFinished = 0;
            tmpToken = tmpToken.concat( strFnc.substring(0,1) );
         }
         strFnc = strFnc.substring(1);
      }

      ruleAsStringArray = new String[arrlstTokens.size()];
      ruleAsStringArray = arrlstTokens.toArray(ruleAsStringArray);

      // ruleAsArrayIdx = new Integer[arrlstTokensIdx.size()];
      ruleAsArrayIdx = new RPNElem[arrlstTokensIdx.size()];
      ruleAsArrayIdx = arrlstTokensIdx.toArray(ruleAsArrayIdx);

      intMaxTotReacSortedIdx = intMaxFncReacSortedIdx;
      if( intMaxTgtReacSortedIdx > intMaxTotReacSortedIdx )
      {
         intMaxTotReacSortedIdx = intMaxTgtReacSortedIdx;
      }
      // System.out.println("INFO: maximum total sorted index of all involved reactions: " + intMaxTotReacSortedIdx);
   }
   ////////////////////////////////////////////////////////////////////////////


   ////////////////////////////////////////////////////////////////////////////
   ////////////////////////////////////////////////////////////////////////////
   private Integer getMaxOfIntList( IntList intLstIn )
   {
      Integer max = new Integer(-1);

      IntIterator intIt = intLstIn.iterator();

      while( intIt.hasNext() )
      {
         Integer i = intIt.nextInt();
         if( i > max)
         {
            max = i;
         }
      }

      return(max);
   }
   ////////////////////////////////////////////////////////////////////////////


   ////////////////////////////////////////////////////////////////////////////
   ////////////////////////////////////////////////////////////////////////////
   private Boolean determineIterationQualification()
   {
      Integer intIsQualified;
      Boolean isQualified = true;
      RPNElem elemAllTrueRule[] = new RPNElem[ ruleAsStackIdx.size() ];
      elemAllTrueRule = ruleAsStackIdx.toArray( elemAllTrueRule );
      Integer intAllTrueRule[] = new Integer[ ruleAsStackIdx.size() ];

      // file a stack where all reactions are set to 'true;
      // 'true' means the reaction carries a flux
      for( int i =0; i < elemAllTrueRule.length; i++ )
      {
         if( elemAllTrueRule[i].isReactionIndex() )
         {
            // we found an index value of an reaction

            // check if reaction is zero-active
            // if so then rule cannot be used during iteration
            // as we don't know anything about the required 1-state
            if( elemAllTrueRule[i].getActivityType() == RPNElem.ZERO_ACTIVE )
            {
               // System.out.println("DEBUG: GeneticRule.execute(): rule cannot be used during iteration as reaction (" + elemAllTrueRule[i].getValue() + ") is ZERO_ACTIVE");
               return( false );
            }

            // set the value of this reaction to 'true'
            intAllTrueRule[i] = RevPolishNotation.TRUE;
         }
         else
         {
            // we are dealing with an operator (such as AND, OR, or NOT)
            intAllTrueRule[i] = elemAllTrueRule[i].getValue();
         }
      }

      intIsQualified = RevPolishNotation.execute( intAllTrueRule );
      
      if( intIsQualified == RevPolishNotation.FALSE )
      {
         isQualified = true;
      }
      else if( intIsQualified == RevPolishNotation.TRUE )
      {
         isQualified = false;
      }
      else if( intIsQualified == RevPolishNotation.NOTDEF )
      {
         isQualified = false;
      }
      else
      {
         System.out.println("DEBUG: GeneticRule.determineIterationQualification(): invalid returned by RevPolishNotation.execute(): " + intIsQualified);
         System.out.println("       execution aborted.");
         System.exit(-1);
      }

      return( isQualified );
   }
   ////////////////////////////////////////////////////////////////////////////


   ////////////////////////////////////////////////////////////////////////////
   ////////////////////////////////////////////////////////////////////////////
   private void generateBitSets()
   {
      GeneBitSet initBitSet = new GeneBitSet( this.getMaxReacSortedIdx() );
      final ReactionMapping rmap = new ReactionMapping(config, efmModel.getMetabolicNetwork(), efmModel.getReactionSorting());

      /////////////////////////////////////////////////////////////////////////
      // add all irreversible reactions to bitmap
      /////////////////////////////////////////////////////////////////////////
      Iterator<Integer> intItIrrev =  arrLstIrrevReacOrigIdx.iterator();
      while( intItIrrev.hasNext() )
      {
         Integer reacIdx =  intItIrrev.next();
         // System.out.println("DEBUG: GeneticRule.generateBitSets(): irrev reactions: reacIdx = " + reacIdx);
         IntList intListReacSortedIdx = rmap.get(Layer.Original, reacIdx,  Layer.Sorted);

         Iterator<Integer> intItSort = intListReacSortedIdx.iterator();
         while( intItSort.hasNext() )
         {
            initBitSet.set(intItSort.next());
         }
      }
      // System.out.println("DEBUG: GeneticRule.generateBitSets(): initBitSet = " + initBitSet );

      // add initial BitSet to global ArrayList
      arrLstBitSetRule.add(initBitSet);
      /////////////////////////////////////////////////////////////////////////


      /////////////////////////////////////////////////////////////////////////
      // add all reversible reactions to bitmap
      /////////////////////////////////////////////////////////////////////////
      Iterator<Integer> intItRev =  arrLstRevReacOrigIdx.iterator();
      while( intItRev.hasNext() )
      {
         Integer reacIdx =  intItRev.next();
         // System.out.println("DEBUG: GeneticRule.generateBitSets(): rev reactions: reacIdx = " + reacIdx);
         IntList intListReacSortedIdx = rmap.get(Layer.Original, reacIdx,  Layer.Sorted);

         ArrayList<GeneBitSet> arrLstNewBitSets = new ArrayList<GeneBitSet>();

         Iterator<GeneBitSet> bitSetIt = arrLstBitSetRule.iterator();

         while( bitSetIt.hasNext() )
         {
            GeneBitSet tmpBitSet = bitSetIt.next();

            Iterator<Integer> intItSort = intListReacSortedIdx.iterator();
            while( intItSort.hasNext() )
            {
               GeneBitSet tmp2BitSet = new GeneBitSet();
               tmp2BitSet = (GeneBitSet) tmpBitSet.clone();
               tmp2BitSet.set( intItSort.next() );
               arrLstNewBitSets.add( tmp2BitSet );
            }
         }
         arrLstBitSetRule = arrLstNewBitSets;
      }
      /////////////////////////////////////////////////////////////////////////


      /////////////////////////////////////////////////////////////////////////
      // add target reaction
      /////////////////////////////////////////////////////////////////////////
      Iterator<GeneBitSet> bitSetItTgt = arrLstBitSetRule.iterator();
      ArrayList<GeneBitSet> arrLstNewBitSetsTgt = new ArrayList<GeneBitSet>();

      while( bitSetItTgt.hasNext() )
      {
         GeneBitSet tmpBitSetTgt = bitSetItTgt.next();
            
         Iterator<Integer> intItSortTgt = intLstTgtReacSortedIdx.iterator();
         while( intItSortTgt.hasNext() )
         {
            GeneBitSet tmp2BitSetTgt = new GeneBitSet();
            tmp2BitSetTgt = (GeneBitSet) tmpBitSetTgt.clone();
            tmp2BitSetTgt.set( intItSortTgt.next() );
            arrLstNewBitSetsTgt.add( tmp2BitSetTgt );
         }  
      }  
      arrLstBitSetRule = arrLstNewBitSetsTgt;
      /////////////////////////////////////////////////////////////////////////


      /////////////////////////////////////////////////////////////////////////
      /////////////////////////////////////////////////////////////////////////
      Iterator<GeneBitSet> finIt = arrLstBitSetRule.iterator();
      while( finIt.hasNext() )
      {
         GeneBitSet curBitSet = finIt.next();
         curBitSet.setMaxReacIdx( curBitSet.length() - 1 );
         // System.out.println("DEBUG: GeneticRule.generateBitSets(): final bitset: " + curBitSet + " length: " + curBitSet.length());
      }
      /////////////////////////////////////////////////////////////////////////
   }
   ////////////////////////////////////////////////////////////////////////////


   ////////////////////////////////////////////////////////////////////////////
   ////////////////////////////////////////////////////////////////////////////
   public Integer getMaxReacSortedIdx()
   {
      return(intMaxTotReacSortedIdx);
   }
   ////////////////////////////////////////////////////////////////////////////


   ////////////////////////////////////////////////////////////////////////////
   ////////////////////////////////////////////////////////////////////////////
   public Boolean isTgtReactReversible()
   {
      return(boolTgtReacIsReversible);
   }
   ////////////////////////////////////////////////////////////////////////////


   ////////////////////////////////////////////////////////////////////////////
   ////////////////////////////////////////////////////////////////////////////
   public String[] getFncReacNames()
   {
      String retString[] = new String[arrLstReacNames.size()];
      retString = arrLstReacNames.toArray(retString);
      return( retString );
   }
   ////////////////////////////////////////////////////////////////////////////


   ////////////////////////////////////////////////////////////////////////////
   ////////////////////////////////////////////////////////////////////////////
   public Integer[] getFncReacOrigIdx()
   {
      Integer retInteger[]  = new Integer[arrLstReacOrigIdx.size()];
      retInteger = arrLstReacOrigIdx.toArray(retInteger);
      return(retInteger);
   }
   ////////////////////////////////////////////////////////////////////////////


   ////////////////////////////////////////////////////////////////////////////
   ////////////////////////////////////////////////////////////////////////////
   public String[] getFncRevReacNames()
   {
      String retString[] = new String[arrLstRevReacNames.size()];
      retString = arrLstRevReacNames.toArray(retString);
      return( retString );
   }
   ////////////////////////////////////////////////////////////////////////////


   ////////////////////////////////////////////////////////////////////////////
   ////////////////////////////////////////////////////////////////////////////
   public Integer[] getFncRevReacOrigIdx()
   {
      Integer retInteger[]  = new Integer[arrLstRevReacOrigIdx.size()];
      retInteger = arrLstRevReacOrigIdx.toArray(retInteger);
      return(retInteger);
   }
   ////////////////////////////////////////////////////////////////////////////


   ////////////////////////////////////////////////////////////////////////////
   ////////////////////////////////////////////////////////////////////////////
   public String[] getFncIrrevReacNames()
   {
      String retString[] = new String[arrLstIrrevReacNames.size()];
      retString = arrLstIrrevReacNames.toArray(retString);
      return( retString );
   }
   ////////////////////////////////////////////////////////////////////////////


   ////////////////////////////////////////////////////////////////////////////
   ////////////////////////////////////////////////////////////////////////////
   public Integer[] getFncIrrevReacOrigIdx()
   {
      Integer retInteger[]  = new Integer[arrLstIrrevReacOrigIdx.size()];
      retInteger = arrLstIrrevReacOrigIdx.toArray(retInteger);
      return(retInteger);
   }
   ////////////////////////////////////////////////////////////////////////////


   ////////////////////////////////////////////////////////////////////////////
   ////////////////////////////////////////////////////////////////////////////
   public Boolean isIterationQualified()
   {
      return(boolIsIterationQualified);
   }
   ////////////////////////////////////////////////////////////////////////////


   ////////////////////////////////////////////////////////////////////////////
   ////////////////////////////////////////////////////////////////////////////
   public ArrayList<GeneBitSet> getBitSets()
   {
      return(arrLstBitSetRule);
   }
   ////////////////////////////////////////////////////////////////////////////


   ////////////////////////////////////////////////////////////////////////////
   ////////////////////////////////////////////////////////////////////////////
   public Boolean doesEFMObeyRule(Column col)
   {
      Boolean modeTgtVal;
      Integer intExpcTgtVal;
      RPNElem elemRule[] = new RPNElem[ ruleAsStackIdx.size() ];
      elemRule = ruleAsStackIdx.toArray( elemRule );
      Integer intRule[] = new Integer[ ruleAsStackIdx.size() ];

      final ReactionMapping rmap = new ReactionMapping(config, efmModel.getMetabolicNetwork(), efmModel.getReactionSorting());

      for( int i = 0; i < intRule.length; i++ )
      {
         if( elemRule[i].isReactionIndex() )
         {
            // we found an index value of an reaction
            // set the value of this reaction to 'true'
            intRule[i] = getValByIdx(rmap, col, elemRule[i].getValue() );

            if( (intRule[i] == RevPolishNotation.FALSE && elemRule[i].getActivityType() == RPNElem.ONE_ACTIVE ) ||
                (intRule[i] == RevPolishNotation.TRUE  && elemRule[i].getActivityType() == RPNElem.ZERO_ACTIVE) )
            {
               // we can't say anything about the real state of the reaction
               // hence, we must set its value to NOTDEF
               // return( true );
               intRule[i] = RevPolishNotation.NOTDEF;
            }
         }
         else
         {
            intRule[i] = elemRule[i].getValue();
         }
      }

      // expected value of target reaction
      intExpcTgtVal = RevPolishNotation.execute( intRule );

      if( intExpcTgtVal == RevPolishNotation.NOTDEF )
      {
         // if we received a NOT DEFINED from the execute-function
         // then rule cannot applied -> hence, we return 'obeyed'
         return( true );
      }

      // value of target reaction given by EFM
      modeTgtVal = true;
      if( getValByIdx(rmap, col, intTgtReac ) == RevPolishNotation.FALSE )
      {
         modeTgtVal = false;  
      }
      else if( getValByIdx(rmap, col, intTgtReac ) == RevPolishNotation.TRUE )
      {
         modeTgtVal = true;  
      }
      else
      {
         System.out.println("FATAL INTERNAL ERROR: unknown value returned by getValByIdx():" + getValByIdx(rmap, col, intTgtReac ));
         System.out.println("                      execution arborted.");
         System.exit(-1);
      }

      if( (intExpcTgtVal == RevPolishNotation.TRUE  && modeTgtVal == true) ||
          (intExpcTgtVal == RevPolishNotation.FALSE && modeTgtVal == false) )
      {
         // expected value and value from EFM are equal
         // -> rule is obeyed
         return( true );
      }
      else
      {
         // expected value and value from EFM are not equal
         // -> rule is NOT obeyed
         return( false );
      }
   }
   ////////////////////////////////////////////////////////////////////////////


   ////////////////////////////////////////////////////////////////////////////
   ////////////////////////////////////////////////////////////////////////////
   private Integer getValByIdx( ReactionMapping rmap, Column col, Integer idx)
   {
      IntList intListReacSortedIdx = rmap.get(Layer.Original, idx,  Layer.Sorted);

      Iterator<Integer> intItSort = intListReacSortedIdx.iterator();
      while( intItSort.hasNext() )
      {
         Integer sortedIdx = intItSort.next();

         if( col.get( sortedIdx ) == false )
         {
            // note: if the get-method of the Column-class
            // returns 'false', then this means that the reaction
            // carries a flux, whereas if the get-method returns
            // 'true' then it does not carry a flux
            return( RevPolishNotation.TRUE);
         }
      }

      return( RevPolishNotation.FALSE);
   }
   ////////////////////////////////////////////////////////////////////////////


   ////////////////////////////////////////////////////////////////////////////
   ////////////////////////////////////////////////////////////////////////////
   private int getNumOccOfChar(String str, char c)
   {
      int num = 0;

      for( int i = 0; i < str.length(); i++ )
      {
         if( str.charAt(i) == c )
         {
            num++;
         }
      }

      return(num);
   }
   ////////////////////////////////////////////////////////////////////////////


   ////////////////////////////////////////////////////////////////////////////
   ////////////////////////////////////////////////////////////////////////////
   public String getRuleString()
   {
      return(strRule);
   }
   ////////////////////////////////////////////////////////////////////////////
}
