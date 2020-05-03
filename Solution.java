import java.util.*;

public class Solution
{
	// Properties of Token
    private String content;
    private int type;	/*	Type (classification of tokens):
						0 - symbol
						1 - tag
						2 - attribute
						3 - attribute value
						4 - operator
						5 - value
						*/
    
	// Instatiating Token
    public Solution(String c, int t)
    {
        content = c;
        type = t;
	}
	
	// Helper Functions
	public static boolean isLetter(char c)
	{
		boolean lower, upper;

		return (c >= 65 && c <=90) || (c >= 97 && c <=122);
	}

	public static boolean isNumeric(char c)
	{
		return c >= 48 && c <= 57;
	}

	public static boolean isUnderscore(char c)
	{
		return c == 95;
	}

	public static boolean isAlphanumeric(char c)
	{
		return isLetter(c) || isNumeric(c) || isUnderscore(c);
	}


	// Print Tokens with Type
    public void printTokens()
    {
        if(this.type == 0)
        {
            System.out.println(this.content + " - symbol");
        }
        else if(this.type == 1)
        {
            System.out.println(this.content + " - tag");
        }
        else if(this.type == 2)
        {
            System.out.println(this.content + " - attribute");
        }
        else if(this.type == 3)
        {
            System.out.println(this.content + " - attribute value");
        }
        else if(this.type == 4)
        {
            System.out.println(this.content + " - operator");
        }
        else if(this.type == 6)
        {
            System.out.println(this.content + " - invalid");
        }
        else
        {
            System.out.println(this.content + " - value");
        }
    }

	// Tokenizer
	public static ArrayList<Solution> tokenizer(String input)
	{
		int state=0;
        int i; // index
		char curr; // currently processing character
		String block = ""; // a block that will collect characters which builds up to a tag or attribute
		boolean terminate = false;
        ArrayList<Solution> list = new ArrayList<>();
        Solution t;
		
		for(i = 0; i<input.length(); i++)
		{
			if(terminate)
				break;
			curr = input.charAt(i);

			switch(state)
			{
				case 0: // initial state
					if(curr == 60 || curr == 47 || curr == 63) // < /
					{
						state = 1;
						list.add(new Solution(String.valueOf(curr), 0)); // < - symbol
					}
					else if(curr == 62) // >
					{
						state = 7; // state after a >
						list.add(new Solution(String.valueOf(curr), 0)); // > - symbol
					}
					else
					{
						state = 2; // state after < + string
						block = block.concat(String.valueOf(curr));
					}
					break;

				case 1: // accepted <
					if(curr == 60 || curr == 47 || curr == 63) // < or backslash
					{
						state = 1;
						list.add(new Solution(String.valueOf(curr), 0));
					}
					else if(curr == 62) // >
					{
						state = 7;
						list.add(new Solution(String.valueOf(curr), 0));
					}
					else if(curr == 32) // Space
					{
						state = 1;
					}
					else if( isLetter(curr) )
					{
						state = 2; // tag name build
						block = block.concat(String.valueOf(curr));
					}
					else
					{
						state = 10;
						block = block.concat(String.valueOf(curr));
					}
					break;

				case 2: // building tag name
				/*
					if(curr == 60 || curr == 47 || curr == 63) // < ? /
					{
						state = 1;
						list.add(new Solution(String.valueOf(curr), 0));
					}
				*/
					if(curr == 62 || curr == 63) // >
					{
						state = 7;
						list.add(new Solution(block, 1)); // tokenize tag name
						list.add(new Solution(String.valueOf(curr), 0)); // backslash tokenized as symbol
						block = ""; // reset block
					}
					else if(curr == 32) // Space
					{
						state = 3; // move on to accepting attribute name
						block = block.concat(String.valueOf(curr));
						list.add(new Solution(block, 1)); // tokenize tag name
						block = ""; // reset block
					}
					else if( isAlphanumeric(curr) )
					{
						block = block.concat(String.valueOf(curr)); // continue building tag name
					}
					else
					{
						state = 10;
						block = block.concat(String.valueOf(curr));
					}
					break;

				case 3: // building attribute name
					if(curr == 32) // space
					{
						state = 3;
						list.add(new Solution(block, 2)); // tokenize attribute name
						block = "";
					}
					else if(curr == 60 || curr == 62 || curr == 47 || curr == 63 ) // < > / ?
					{
						if( curr == 60 || curr == 47 || curr == 63)
							state = 1;
						else
							state = 7;

						list.add(new Solution(String.valueOf(curr), 0)); // tokenize symbole
					}
					else if(curr == 61) // =
					{
						state = 4; // move on to accepting attribute value
						list.add(new Solution(block, 2)); // tokenize attribute name
						list.add(new Solution(String.valueOf(curr), 4)); // tokenize symbol =
						block = "";
					}
					else if( isAlphanumeric(curr) )
					{
						block = block.concat(String.valueOf(curr)); // continue building attr name
					}
					else
					{
						state = 10;
						block = block.concat(String.valueOf(curr));
					}
					break;

				case 4: // accepted = symbol

					if(curr == 34) // open quotation mark "
					{
						state = 5;
						block = block.concat(String.valueOf(curr));
					}
					else if(curr == 60 || curr == 62 || curr == 47 || curr == 63 ) // < > / ?
					{
						if( curr == 60 || curr == 47 || curr == 63)
							state = 1;
						else
							state = 7;

						list.add(new Solution(String.valueOf(curr), 0)); // tokenize symbole
					}
					else if(curr == 61) // =
					{
						state = 4; // move on to accepting attribute value
						list.add(new Solution(block, 2)); // tokenize attribute name
						list.add(new Solution(String.valueOf(curr), 4)); // tokenize symbol =
						block = "";
					}
					else
					{
						block = block.concat(String.valueOf(curr));
					}
					break;

				case 5: // building attribute value (accepted open quote)
					if(curr == 34) // close quote "
					{
						state = 6;
						block = block.concat(String.valueOf(curr));
						list.add(new Solution(block, 3)); // tokenize attribute value
						block="";
					}
					/*
					else if(curr == 60 || curr == 62 || curr == 47 || curr == 63 ) // < > / ?
					{
						if( curr == 60 || curr == 47 || curr == 63)
							state = 1;
						else
							state = 7;

						list.add(new Solution(String.valueOf(curr), 0)); // tokenize symbole
					}
					else if(curr == 61) // =
					{
						state = 4; // move on to accepting attribute value
						list.add(new Solution(block, 2)); // tokenize attribute name
						list.add(new Solution(String.valueOf(curr), 4)); // tokenize symbol =
						block = "";
					}
					*/
					else
					{
						block = block.concat(String.valueOf(curr));
					}
					break;

				case 6: // accepted close quote mark

					if(curr == 32) // space
					{
						state = 6;
					}
					else if(curr == 60 || curr == 62 || curr == 47 || curr == 63 ) // < > / ?
					{
						if( curr == 60 || curr == 47 || curr == 63)
							state = 1;
						else
							state = 7;

						list.add(new Solution(String.valueOf(curr), 0)); // tokenize symbol
					}
					else if(curr == 61) // = symbol
					{
						state = 4;
						list.add(new Solution(String.valueOf(curr), 4));
					}
					else
					{
						state = 3; // building another attribute name
						block = block.concat(String.valueOf(curr));
					}
					break;

				case 7: // tag is closed
					if(curr == 32) // space
					{
						state = 7;
					}
					else if(curr == 60 || curr == 62 || curr == 47 || curr == 63 ) // < > / ?
					{
						if( curr == 60 || curr == 47 || curr == 63)
							state = 1;
						else
							state = 7;

						list.add(new Solution(String.valueOf(curr), 0)); // tokenize symbole
					}
					else if(curr == 61) // =
					{
						state = 4;
						list.add(new Solution(String.valueOf(curr), 4));
					}
					else
					{
						state = 8; // inner xml content
						block = block.concat(String.valueOf(curr));
					}
					break;

				case 8: // building inner xml content

				/*
					if(curr == 32) // space
					{
						state = 8;
					}
				*/
					if(curr == 60 || curr == 63 ) // < > / ?
					{
						if(curr == 60)
							state = 1;
						else
							state = 7;

						list.add(new Solution(block, 5)); // tokenize inner xml value
						list.add(new Solution(String.valueOf(curr), 0)); // tokenize symbol
						block = "";
					}
					/*
					else if(curr == 61) // =
					{
						state = 4;
						list.add(new Solution(String.valueOf(curr), 4));
					}
					*/
					else
					{
						state = 8;
						block = block.concat(String.valueOf(curr));
					}
					break;

				case 10:
					list.add(new Solution(block, 6));
					terminate = true;
					block="";
			}
		}


		if(!block.equals(""))
			list.add(new Solution(block, 5));

		return list;
	}

	// Syntax Parser
    public static String syntaxAnalyzer(ArrayList<Solution> list, boolean debugger)
    {
        Stack<String> stack_checker = new Stack<>();
        String curr_tagname = "";
        int pos = 0;
        Solution curr;
        int state = 0;

        while(true)
        {
			curr = list.get(pos);
			if(debugger)
			{
				String topOfStack = "";
				if(stack_checker.isEmpty()) topOfStack = "EMPTY"; 
				else topOfStack = stack_checker.peek();

				System.out.println("---------------");
				System.out.println("CURRENT POS OF TOTAL: " + pos + "/" + (list.size()-1));
				System.out.println("TOP OF STACK: " + topOfStack);
				System.out.println("CURRENT POS: " + curr.content);
				System.out.println("CURRENT STATE: " + state);
			}

            switch(state)
            {
                // XML Header Parsers
                case 0:
                    if(!curr.content.equals("<")) return "NO";
                    state = 1;
                    break;
                case 1:
                    if(!curr.content.equals("?")) return "NO";
                    state = 2;
                    break;
				case 2:
					if(curr.content.charAt(curr.content.length()-1) == ' ') curr.content = curr.content.substring(0, curr.content.length()-1);
                    if(!curr.content.equals("xml")) return "NO";
					state = 3;
                    break;
				case 3:
                    if(curr.type == 2) state = 4;
                    else if(curr.content.equals("?")) state = 7;
                    else return "NO";
                    break;
                case 4:
                    if(curr.type != 4) return "NO";
                    state = 5;
                    break;
                case 5:
                    if(curr.type != 3) return "NO";
                    state = 6;
                    break;
                case 6:
                    if(curr.type == 2) state = 4;
                    else if(curr.content.equals("?")) state = 7;
                    else return "NO";
                    break;
                case 7:
                    if(!curr.content.equals(">")) return "NO";
                    state = 8;
                    break;
                
                // XML Parser
                case 8:
                    if(curr.content.equals("<"))
                    {
                        if(list.get(pos+1).content.equals("/")) state = 18; // THIS IS CASE 17
                        else state = 9;
                    }
                    else return "NO";
                    break;
				case 9:
                    if(curr.type != 1) return "NO";
					state = 10;
					if(curr.content.charAt(curr.content.length()-1) == ' ') curr.content = curr.content.substring(0, curr.content.length()-1);
                    stack_checker.push(curr.content);
					curr_tagname = curr.content;
                    break;
                case 10:
                    if(curr.type == 2) state = 11;
                    else if(curr.content.equals("/")) state = 14;
                    else if(curr.content.equals(">")) state = 16;
                    else return "NO";
                    break;
                case 11:
                    if(curr.type != 4) return "NO";
                    state = 12;
                    break;
                case 12:
                    if(curr.type != 3) return "NO";
                    state = 13;
                    break;
                case 13:
                    if(curr.type == 2) state = 11;
                    else if(curr.content.equals("/")) state = 14;
                    else if(curr.content.equals(">")) state = 16;
                    else return "NO";
                    break;
				case 14:
					stack_checker.pop();
					pos--;
                    state = 15;
                    break;
                case 15:
                    if(!curr.content.equals(">")) return "NO";
                    state = 16;
                    break;
				case 16:
                    if(curr.type == 5) state = 16;
                    else if(curr.content.equals("<"))
                    {
						if(list.get(pos+1).content.equals("/")) 
						{
							state = 18; // THIS IS CASE 17
							pos++;
						}
						else state = 9;
					}
					break;
				case 18:
                    if(stack_checker.peek().equals(curr.content))
                    {
                        stack_checker.pop();
                        state = 19;
                    }
					else return "NO"; // Error due to improper nesting;
					break;
                case 19:
					if(!curr.content.equals(">")) return "NO";
					state = 16;
                    break;
            }

			if(!stack_checker.isEmpty() && pos == list.size()-1) return "NO";
            if(pos == list.size()-1) return "YES";
            pos++;
        }
    }

    public static void main(String[] args)
    {
		/***************DEBUGGER********************/
		boolean debugger = true;
		boolean scannerOn = false;
		if(debugger)
		{
			System.out.println(">> DEBUGGER ENABLED");
		}
		if(!scannerOn)
        {
            System.out.println(">> NOT SCANNING INPUT");
        }
		/*******************************************/

        Scanner keyin = new Scanner(System.in);
        String input = "";
        String newline = "";
		
		// Scan STDIN
		if(scannerOn)
		{
			while(keyin.hasNextLine())
			{
				newline = keyin.nextLine();
				input = input.concat(newline);
			}
			
			input = input.replace("\t", "");
			keyin.close();
		}
		else
		{
			input = "<?xml?><a>";
			//input = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?><sample_tag>4989</sample_tag>";
		}

		ArrayList<Solution> list = Solution.tokenizer(input);

		if(debugger)
		{
			for(int x=0; x<list.size(); x++)
			{
				list.get(x).printTokens();
			}
		}
		String output = Solution.syntaxAnalyzer(list, debugger);

        System.out.println(output);
    }
}