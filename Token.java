import java.util.ArrayList;

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
		else if(this.type == 6)
		{
			System.out.println(this.content + " - invalid");
		}
		else
		{
			System.out.println(this.content + " - value");
		}
	}

	public static void main(String[] args)
	{
		int state=0;
		int i; // index
		char curr; // currently processing character
		String input3 = "<amu let size=\"medium\"> <gem>sapphire</gem> </amulet>";
		String input2 = "<><</<></>>amulet<car>";
		String input = "<XMLFile><Colors><Color1>White</Color1><Color2>Blue</Color2><Color3>Black</Color3><Color4 Special=\"Light\tOpaque\">Green</Color4><Color5>Red</Color5></Colors><Fruits><Fruits1>Apple</Fruits1><Fruits2>Pineapple</Fruits2><Fruits3>Grapes</Fruits3><Fruits4>Melon</Fruits4></Fruits></XMLFile>";
		String block = ""; // a block that will collect characters which builds up to a tag or attribute
		ArrayList<Token> list = new ArrayList<>();
		Token t;
		System.out.println(input);
		System.out.println();
		System.out.println();
		System.out.println();

		for(i = 0; i<input.length(); i++)
		{
			curr = input.charAt(i);

			switch(state)
			{
				case 0:
					if(curr == 60)
					{
						state = 1;
						list.add(new Token(String.valueOf(curr), 0));
					}
					else if(curr == 62)
					{
						state = 7;
						list.add(new Token(String.valueOf(curr), 0));
					}
					else
					{
						state = 2;
						block = block.concat(String.valueOf(curr));
					}
					break;

				case 1:
					if(curr == 60 || curr == 47)
					{
						state = 1;
						list.add(new Token(String.valueOf(curr), 0));
					}
					else if(curr == 62)
					{
						state = 7;
						list.add(new Token(String.valueOf(curr), 0));
					}
					else if(curr == 32)
					{
						state = 1;
					}
					else if(curr == 32)
					{
						state = 1;
					}
					else
					{
						state = 2;
						block = block.concat(String.valueOf(curr));
					}
					break;
				case 2:
					if(curr == 60)
					{
						state = 1;
						list.add(new Token(String.valueOf(curr), 0));
					}
					else if(curr == 62)
					{
						state = 7;
						list.add(new Token(block, 1));
						list.add(new Token(String.valueOf(curr), 0));
						block = "";
					}
					else if(curr == 32)
					{
						state = 3;
						block = block.concat(String.valueOf(curr));
						list.add(new Token(block, 1));
						block = "";
					}
					else
					{
						block = block.concat(String.valueOf(curr));
					}
					break;

				case 3:
					if(curr == 32)
					{
						state = 3;
						list.add(new Token(block, 2));
						block = "";
					}
					else if(curr == 60 || curr == 62)
					{
						state = 1;
						list.add(new Token(String.valueOf(curr), 0));
					}
					else if(curr == 61)
					{
						state = 4;
						list.add(new Token(block, 2));
						list.add(new Token(String.valueOf(curr), 4));
						block = "";
					}
					else
					{
						block = block.concat(String.valueOf(curr));
					}
					break;

				case 4:
					if(curr == 34)
					{
						state = 5;
						block = block.concat(String.valueOf(curr));
					}
					else if(curr == 60 || curr == 62 )
					{
						state = 1;
						list.add(new Token(String.valueOf(curr), 0));
					}
					else if(curr == 61 )
					{
						state = 4;
						list.add(new Token(String.valueOf(curr), 4));
					}
					else if(curr == 100000)
					{
						state = 4;
						list.add(new Token(block, 2));
						list.add(new Token(String.valueOf(curr), 4));
						block = "";
					}
					else
					{
						block = block.concat(String.valueOf(curr));
					}
					break;

				case 5:
					if(curr == 34)
					{
						state = 6;
						block = block.concat(String.valueOf(curr));
						list.add(new Token(block, 3));
						block="";
					}
					else
					{
						block = block.concat(String.valueOf(curr));
					}
					break;

				case 6:
					if(curr == 32)
					{
						state = 6;
					}
					else if(curr == 62)
					{
						state = 7;
						list.add(new Token(String.valueOf(curr), 0));
					}
					else if(curr == 60)
					{
						state = 1;
						list.add(new Token(String.valueOf(curr), 0));
					}
					else if(curr == 61)
					{
						state = 4;
						list.add(new Token(String.valueOf(curr), 4));
					}
					else
					{
						state = 3;
						block = block.concat(String.valueOf(curr));
					}
					break;

				case 7:
					if(curr == 32)
					{
						state = 7;
					}
					else if(curr == 60)
					{
						state = 1;
						list.add(new Token(String.valueOf(curr), 0));
					}
					else if(curr == 62)
					{
						state = 7;
						list.add(new Token(String.valueOf(curr), 0));
					}
					else if(curr == 61)
					{
						state = 4;
						list.add(new Token(String.valueOf(curr), 4));
					}
					else
					{
						state = 8;
						block = block.concat(String.valueOf(curr));
					}
					break;

				case 8:
					if(curr == 60)
					{
						state = 1;
						list.add(new Token(block, 5));
						list.add(new Token(String.valueOf(curr), 0));
						block="";
					}
					else if(curr == 62)
					{
						state = 7;
						list.add(new Token(String.valueOf(curr), 0));
					}
					else if(curr == 61)
					{
						state = 4;
						list.add(new Token(String.valueOf(curr), 4));
					}
					else
					{
						state = 8;
						block = block.concat(String.valueOf(curr));
					}
					break;

			}
		}

		//print all tokens
		for(int x=0; x<list.size(); x++)
		{
			list.get(x).print();
		}
	}
}

