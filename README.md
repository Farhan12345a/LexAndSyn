# Lexical and Syntax Analyzer
A Lexical and Syntax Analyzer in Java

C.S. 310: Principles of Programming Languages:
Simple program that implements a lexical analyzer (scanner) and a syntax analyzer (recursive descent parser) for Ada. The following BNF is used: 
*******   GRAMMAR    for  the parser  **********************
<program> -> procedure name begin <stmt_list> end ;
<stmt_list> ->  <stmt> ;{ <stmt_list}*
<stmt> -> <assign> | <if>
<if> ->  if ( <bool>  )  then <stmt_list> [ else  <stmt_list>]   endif;
   
<assign> -> <var>: = <expr>;		// assignment is := in Ada
<expr> ->  <term> { ( + | - | * | / ) <term> } 
<term> -> <var> | <int>
<bool> ->  <var>(= | !=) <int>   

<letter> ->  a  |  b  |  c  |  d  |  e  | … |  z	 |  A  |  … |  Z
<digit>  ->  0 | 1 | 2 | 3 | 4 | 5 | 6 | 7 | 8 | 9
<letterdigit>    <letter> | <digit>
<var> ->  <letter><letterdigit>*
<int>  ->  <digit><digit>*

