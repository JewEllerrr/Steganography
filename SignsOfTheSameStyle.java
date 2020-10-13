import java.io.*;

public class SignsOfTheSameStyle {

	private static final int SUBSTITUTION_TABLE_ROW = 19;
	private String messageBitset;
	private String containerText;
	private char[][] replacementTable = { { 'а', 'a' }, { 'е', 'e' }, { 'и', 'u' }, { 'к', 'k' }, { 'о', 'o' },
			{ 'р', 'p' }, { 'с', 'c' }, { 'у', 'y' }, { 'А', 'A' }, { 'В', 'B' }, { 'Е', 'E' }, { 'К', 'K' },
			{ 'М', 'M' }, { 'Н', 'H' }, { 'О', 'O' }, { 'Р', 'P' }, { 'С', 'C' }, { 'У', 'Y' }, { 'Х', 'X' }, };

	public void encode(String filename_container, String filename_stego, String filename_result) throws IOException {
		messageBitset = readUnicodeFileConvertToBinaryString(filename_stego);
		messageBitset = addMetaData(messageBitset);
		containerText = readUnicodeFile(filename_container);
		
		int counterOfBitset = 0;
		String resultTxt = new String();

		for (int i = 0; i < containerText.length(); i++) {
			char symbol = containerText.charAt(i);
			// if replacement is possible
			if (containsKey(symbol) && counterOfBitset < messageBitset.length()) 
			{
				if (messageBitset.charAt(counterOfBitset) == '1')
					resultTxt = resultTxt + returnLatinSymbol(symbol);
				else
					resultTxt = resultTxt + Character.toString(symbol);

				counterOfBitset++;
			} else
				resultTxt = resultTxt + Character.toString(symbol);
		}
		
		if (counterOfBitset < messageBitset.length())
			System.out.println("Message is not fully encoded");

		Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filename_result), "UTF-8"));
		out.write(resultTxt);
		out.close();

	}

	public void decode(String fileName, String file_result) throws IOException {

		String containerTextDecode = readUnicodeFile(fileName);

		int counterOfBitset = 0;
		int SizeOfMessage = Integer.MAX_VALUE;
		String result = new String();

		for (int i = 0; i < containerTextDecode.length() && counterOfBitset < SizeOfMessage; i++) {
			char symbol = containerTextDecode.charAt(i);
			if (containsKey(symbol)) {
				if (counterOfBitset == 8)
					SizeOfMessage = Integer.parseInt(result, 2) + 8;

				if (isLatinSymbol(symbol))
					result = result + "1";
				else
					result = result + "0";

				counterOfBitset++;
			}
		}

		String str = "";
		result = result.substring(8, result.length());

		for (int i = 0; i < result.length() / 8; i++) {
			int a = Integer.parseInt(result.substring(8 * i, (i + 1) * 8), 2);
			str += (char) (a);
		}

		System.out.println(str);

		try {
			FileWriter res_file = new FileWriter(file_result, false);
			res_file.write(str);
			res_file.close();

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private boolean containsKey(char symbol) {
		for (int i = 0; i < SUBSTITUTION_TABLE_ROW; i++)
			if (replacementTable[i][0] == symbol || replacementTable[i][1] == symbol)
				return true;
		return false;
	}

	private boolean isLatinSymbol(char symbol) {
		for (int i = 0; i < replacementTable.length; i++) {
			if (replacementTable[i][1] == symbol)
				return true;
		}

		return false;
	}

	private String returnLatinSymbol(char symbol) {
		for (int i = 0; i < SUBSTITUTION_TABLE_ROW; i++)
			if (replacementTable[i][0] == symbol)
				return Character.toString(replacementTable[i][1]);
		return Character.toString(symbol);
	}

	private String addMetaData(String messageBitset) {
		int sizeOfBits = messageBitset.length();
		String tmp = Integer.toBinaryString(sizeOfBits);
		while (tmp.length() != 8)
			tmp = "0" + tmp;

		return tmp + messageBitset;
	}

	private String readUnicodeFile(String fileName) throws IOException {

		BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(fileName), "UTF-8"));
		StringBuffer buffer = new StringBuffer();
		int ch = 0;
		while ((ch = br.read()) != -1)
			buffer.append((char) ch);

		return buffer.toString();

	}

	private String readUnicodeFileConvertToBinaryString(String filename_stego) {
		String bitset = new String();
		try {
			FileReader stego = new FileReader(filename_stego);
			BufferedReader reader = new BufferedReader(stego);

			String line = reader.readLine();
			while (line != null) {
				char[] messChar = new char[line.length()];
				messChar = line.toCharArray();
				for (int i = 0; i < messChar.length; i++) {
					String tmp = Integer.toBinaryString(messChar[i]);
					while (tmp.length() != 8)
						tmp = "0" + tmp;
					bitset += tmp;
				}

				line = reader.readLine();
			}
			return bitset;

		} catch (IOException ex) {
			System.out.println(ex.getMessage());
		}
		return bitset;

	}

}
