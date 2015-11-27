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

import java.util.Enumeration;
import java.util.Stack;

public class RevPolishNotation
{    
    public static final int FALSE   = -2;
    public static final int TRUE    = -3;
    public static final int NOTDEF  = -4;
    public static final int AND     = -5;
    public static final int OR      = -6;
    public static final int NOT     = -7;
    public static final int P_OPEN  = -8;
    public static final int P_CLOSE = -9;

    ///////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////
    public static Stack<RPNElem> infixToPostfix(RPNElem expr[])
    {
       Stack<RPNElem> tmpStack = new Stack<RPNElem>();
       Stack<RPNElem> retStack = new Stack<RPNElem>();

       for ( int i = 0; i < expr.length; i++ )
       {
          Integer s = expr[i].getValue();
          if( s == NOT )
          {
             tmpStack.push( new RPNElem(NOT, false) );
          }
          else if( s == OR )
          {
             tmpStack.push( new RPNElem(OR, false) );
          }
          else if( s == AND )
          {
             tmpStack.push( new RPNElem(AND, false) );
          }
          else if( s == P_CLOSE )
          {
             retStack.push(tmpStack.pop());
          }
          else if( s == P_OPEN )
          {
          }
          else
          {
             retStack.push( new RPNElem(s, true, expr[i].getActivityType()) );
          }
       }

       // Enumeration<RPNElem> postfixEnum = retStack.elements();
       // System.out.println("DEBUG: Rule in reverse polish notation: ");
       // while( postfixEnum.hasMoreElements() )
       // {
       //    RPNElem thisElem = postfixEnum.nextElement();
       //    System.out.println( "isReactionIndex=" + thisElem.isReactionIndex() + " activityType=" + thisElem.getActivityType() + " value=" + thisElem.getValue());
       // }
       // System.out.println();

       return(retStack);
    }
    ///////////////////////////////////////////////////////////////////////////


    ///////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////
    public static Integer execute( Integer intStack[] )
    {
       Integer ret;
       Stack<Integer> vals = new Stack<Integer>();
       Integer tmpInt1;
       Integer tmpInt2;

       for( int i = 0; i < intStack.length; i++ )
       {
          int ti = intStack[i];

          if( ti == NOT )
          {
             tmpInt1 = vals.pop();
             if( tmpInt1 == NOTDEF )
             {
                // NOT(NOTDEF) = NOTDEF
                vals.push( NOTDEF );
             }
             else if( tmpInt1 == FALSE )
             {
                // NOT(FALSE) = TRUE
                vals.push( TRUE );
             }
             else if( tmpInt1 == TRUE )
             {
                // NOT(TRUE) = FALSE
                vals.push( FALSE );
             }
             else
             {
                System.out.println("DEBUG: ReversePolishNotation.execute(): invalid operand in NOT operation: " + tmpInt1);
                System.out.println("       execution aborted.");
                System.exit(-1);
             }
          }
          else if( ti == AND )
          {
             tmpInt1 = vals.pop();
             tmpInt2 = vals.pop();

             if( tmpInt1 == FALSE || tmpInt2 == FALSE )
             {
                vals.push( FALSE );
             }
             else if( tmpInt1 == NOTDEF || tmpInt2 == NOTDEF )
             {
                vals.push( NOTDEF );
             }
             else if( tmpInt1 == TRUE && tmpInt2 == TRUE )
             {
                vals.push( TRUE );
             }
             else
             {
                System.out.println("DEBUG: ReversePolishNotation.execute(): invalid operand in AND operation: ");
                System.out.println("       tmpInt1: " + tmpInt1);
                System.out.println("       tmpInt2: " + tmpInt2);
                System.out.println("       execution aborted.");
                System.exit(-1);
             }
          }
          else if( ti == OR )
          {
             tmpInt1 = vals.pop();
             tmpInt2 = vals.pop();

             if( tmpInt1 == TRUE || tmpInt2 == TRUE )
             {
                vals.push( TRUE );
             }
             else if( tmpInt1 == NOTDEF || tmpInt2 == NOTDEF )
             {
                vals.push( NOTDEF );
             }
             else if( tmpInt1 == FALSE && tmpInt2 == FALSE )
             {
                vals.push( FALSE );
             }
             else
             {
                System.out.println("DEBUG: ReversePolishNotation.execute(): invalid operand in OR operation: ");
                System.out.println("       tmpInt1: " + tmpInt1);
                System.out.println("       tmpInt2: " + tmpInt2);
                System.out.println("       execution aborted.");
                System.exit(-1);
             }
          }
          else
          {
             if( ti == FALSE )
             {
                vals.push( FALSE );
             }
             else if( ti == TRUE )
             {
                vals.push( TRUE );
             }
             else if( ti == NOTDEF )
             {
                vals.push( NOTDEF );
             }
             else
             {
                System.out.println("DEBUG: ReversePolishNotation.execute(): invalid input: " + ti);
                System.out.println("       execution aborted.");
                System.exit(-1);
             }
          }
       }

       ret = vals.pop();

       return( ret );
    }
    ///////////////////////////////////////////////////////////////////////////
}
