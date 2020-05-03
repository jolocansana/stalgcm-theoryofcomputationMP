import java.util.ArrayList;
import java.io.*;

public class Token
{
	private String content;
	private int type;

	public Token(String c, int t)
	{
		content = c;
		type = t;
	}

	public void print()
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
		else if(this.type == 5)
		{
			System.out.println(this.content + " - value");
		}
		else
		{
			System.out.println(this.content + " - invalid");
		}
	}

	public static void main(String[] args) throws Exception
	{

		int state=0;
		int i; // index
		char curr; // currently processing character
		String input3 = "<amu let size=\"medium\"> <gem>sapphire</gem> </amulet>";
		String input2 = "<><</<></>>amulet<car>";
		//String input = "<?XMLFile><Colors><Color1>White</Color1><Color2>Blue</Color2><Color3>Black</Color3><Color4 Special=\"Light\tOpaque\">Green</Color4><Color5>Red</Color5></Colors><Fruits><Fruits1>Apple</Fruits1><Fruits2>Pineapple</Fruits2><Fruits3>Grapes</Fruits3><Fruits4>Melon</Fruits4></Fruits></XMLFile ?>";
		String block = ""; // a block that will collect characters which builds up to a tag or attribute
		ArrayList<Token> list = new ArrayList<>();
		Token t;
		//System.out.println(input);

		File file = new File("C:\\Users\\louis\\Desktop\\test.txt");

		BufferedReader br = new BufferedReader(new FileReader(file));

		String input;
		while ((input = br.readLine()) != null)
		//System.out.println(input);


		for(i = 0; i<input.length(); i++)
		{
			curr = input.charAt(i);

			switch(state)
			{
				case 0: // initial state
					if(curr == 60 || curr == 47 || curr == 63) // < /
					{
						state = 1;
						list.add(new Token(String.valueOf(curr), 0)); // < - symbol
					}
					else if(curr == 62) // >
					{
						state = 7; // state after a >
						list.add(new Token(String.valueOf(curr), 0)); // > - symbol
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
						list.add(new Token(String.valueOf(curr), 0));
					}
					else if(curr == 62) // >
					{
						state = 7;
						list.add(new Token(String.valueOf(curr), 0));
					}
					else if(curr == 32) // Space
					{
						state = 1;
					}
					else
					{
						state = 2; // tag name build
						block = block.concat(String.valueOf(curr));
					}
					break;

				case 2: // building tag name
					if(curr == 60 || curr == 47 || curr == 63) // <
					{
						state = 1;
						list.add(new Token(String.valueOf(curr), 0));
					}
					else if(curr == 62) // /
					{
						state = 7;
						list.add(new Token(block, 1)); // tokenize tag name
						list.add(new Token(String.valueOf(curr), 0)); // backslash tokenized as symbol
						block = ""; // reset block
					}
					else if(curr == 32) // Space
					{
						state = 3; // move on to accepting attribute name
						block = block.concat(String.valueOf(curr));
						list.add(new Token(block, 1)); // tokenize tag name
						block = ""; // reset block
					}
					else
					{
						block = block.concat(String.valueOf(curr)); // continue building tag name
					}
					break;

				case 3: // building attribute name
					if(curr == 32) // space
					{
						state = 3;
						list.add(new Token(block, 2)); // tokenize attribute name
						block = "";
					}
					else if(curr == 60 || curr == 62 || curr == 47 || curr == 63 ) // < > / ?
					{
						if( curr == 60 || curr == 47 || curr == 63)
							state = 1;
						else
							state = 7;

						list.add(new Token(String.valueOf(curr), 0)); // tokenize symbole
					}
					else if(curr == 61) // =
					{
						state = 4; // move on to accepting attribute value
						list.add(new Token(block, 2)); // tokenize attribute name
						list.add(new Token(String.valueOf(curr), 4)); // tokenize symbol =
						block = "";
					}
					else
					{
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

						list.add(new Token(String.valueOf(curr), 0)); // tokenize symbole
					}
					else if(curr == 61) // =
					{
						state = 4; // move on to accepting attribute value
						list.add(new Token(block, 2)); // tokenize attribute name
						list.add(new Token(String.valueOf(curr), 4)); // tokenize symbol =
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
						list.add(new Token(block, 3)); // tokenize attribute value
						block="";
					}
					/*
					else if(curr == 60 || curr == 62 || curr == 47 || curr == 63 ) // < > / ?
					{
						if( curr == 60 || curr == 47 || curr == 63)
							state = 1;
						else
							state = 7;

						list.add(new Token(String.valueOf(curr), 0)); // tokenize symbole
					}
					else if(curr == 61) // =
					{
						state = 4; // move on to accepting attribute value
						list.add(new Token(block, 2)); // tokenize attribute name
						list.add(new Token(String.valueOf(curr), 4)); // tokenize symbol =
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

						list.add(new Token(String.valueOf(curr), 0)); // tokenize symbol
					}
					else if(curr == 61) // = symbol
					{
						state = 4;
						list.add(new Token(String.valueOf(curr), 4));
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

						list.add(new Token(String.valueOf(curr), 0)); // tokenize symbole
					}
					else if(curr == 61) // =
					{
						state = 4;
						list.add(new Token(String.valueOf(curr), 4));
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

						list.add(new Token(block, 5)); // tokenize inner xml value
						list.add(new Token(String.valueOf(curr), 0)); // tokenize symbol
						block = "";
					}
					/*
					else if(curr == 61) // =
					{
						state = 4;
						list.add(new Token(String.valueOf(curr), 4));
					}
					*/
					else
					{
						state = 8;
						block = block.concat(String.valueOf(curr));
					}
					break;
			}
		}

		list.add(new Token(block, 5));

		//print all tokens
		for(int x=0; x<list.size(); x++)
		{
			list.get(x).print();
		}
	}
}

