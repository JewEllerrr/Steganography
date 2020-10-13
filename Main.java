import java.io.IOException;

public class Main {

	public static void main(String[] args) {
		String path = "C:\\Users\\lizfr\\Desktop\\";
		SignsOfTheSameStyle op = new SignsOfTheSameStyle();
		try {
			op.encode(path + "container.html", path + "stego.txt", path + "result.html");
			op.decode(path + "result.html", path + "decoded.html");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
