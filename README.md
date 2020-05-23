# STAGLCM - Theory of Computation
## Introduction
For this machine project, the objective is to create a Java implementation of lexical and syntax analyzer for a markup language known as eXtensible Markup Language (XML). Given a source code, the final product should be able to parse the source code and output “YES” if it is a valid XML source code, and “NO” if it is not. In this project, the programmers used state machines to define and determine the sets of rules. 
## Token Class
A token contains the following attributes:
* Content (string) - the actual string or symbol itself
* Type (integer) - classification of token
    * 0 - symbol
    * 1 - tag
    * 2 - attribute
    * 3 - attribute value
    * 4 - operator
    * 5 - value
## Lexical Analyzer
States are implemented as cases of a switch statement. Cases are labeled with an integer, and the current state is stored in a variable. Initially, the state is set to 0. The program will loop through each character of the string, which is the source code, starting from the first character. Each case contains a set of if-else statements. The condition of these statements are dependent on the value of the most recently looped-through character. When the condition of an if/else statement is met, it’s block will execute. Inside the block are instructions that will change the integer value for the state. Thus, a transition is done when the next loop iterates and possibly a different case is executed.
## Syntax Analyzer
The Syntax Analyzer of the system was programmed based on the PDA seen in Diagram 1 (found in Annex) where in a case in the switch statement represented a single state in the automata. The syntax analyzer ran using the output from the tokenizer and traversed the list of tokens linearly from 0 to its last item. As the list is being traversed, the switch statement aligns the current token to its case or state and from the case, it is processed. In processing, the general idea is to check if the current token is a legal token at this position, if it is, increment the current token position to the next and change the state to the corresponding based on the PDA. If the current token is illegal, the function returns a “NO” that is then printed to STDOUT, else, continue until everything is traversed, then output "YES".
