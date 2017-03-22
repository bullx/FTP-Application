import java.io.*;
import java.net.*;
import java.util.*;

public class Client {
	static Socket s;
	static PrintWriter out;
	static OutputStream out1;
	static BufferedInputStream in1;
	static String dir = System.getProperty("user.dir");;

	/**
	 * 
	 * @param args
	 * @throws UnknownHostException
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public static void main(String[] args) throws UnknownHostException,
			IOException, ClassNotFoundException {

		Client c = new Client();
		c.user();
	}

	/**
	 * 
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public void user() throws IOException, ClassNotFoundException {

		System.out.println("Enter commands");
		System.out
				.println("cd - change current directory on the server side \n"
						+ "lcd - change current directory on the client side \n"
						+ "ls - list files in the current directory on the server side \n"
						+ "pwd - show the current directory on the server side \n"
						+ "put <file> - transfer <file> from client to server \n"
						+ "get <file> - transfer <file> from server to client \n"
						+ "quit - exit the session");
		Scanner s1 = new Scanner(System.in);
		String choice = s1.next();

		switch (choice) {

		case "cd":
			s = new Socket("localhost", 9999);

			out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(
					s.getOutputStream())), true);
			out.flush();
			out.println(1);

			changedirectory();
			s.close();
			break;

		case "lcd":

			lcd();
			break;
		case "ls":
			s = new Socket("localhost", 9999);

			out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(
					s.getOutputStream())), true);
			out.flush();
			out.println(3);
			ls();
			s.close();
			break;
		case "pwd":
			s = new Socket("localhost", 9999);

			out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(
					s.getOutputStream())), true);
			out.flush();
			out.println(4);
			path();
			s.close();
			break;
		case "put":
			s = new Socket("localhost", 9999);

			out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(
					s.getOutputStream())), true);
			out.flush();

			out.println(5);
			put();
			break;
		case "get":
			s = new Socket("localhost", 9999);
			out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(
					s.getOutputStream())), true);
			out.flush();
			out.println(6);

			get();

			break;
		case "quit":
			out.println("7");
			System.exit(0);
			break;
		}
		user();
	}

	/**
	 * 
	 * @throws FileNotFoundException
	 */
	private void put() throws FileNotFoundException {

		System.out.println("Enter the file to be sent");
		Scanner filesent = new Scanner(System.in);
		String name = filesent.next();
		try {
			out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(
					s.getOutputStream())), true);
		} catch (IOException e1) {

			e1.printStackTrace();
		}
		out.flush();
		out.println(name);
		FileInputStream fis = null;
		BufferedInputStream bis = null;
		OutputStream os = null;

		try {

			File file = new File(name);
			byte array[] = new byte[(int) file.length()];
			fis = new FileInputStream(file);
			bis = new BufferedInputStream(fis);
			try {
				bis.read(array, 0, array.length);
				os = s.getOutputStream();
				os.write(array, 0, array.length);
				os.flush();
			} catch (IOException e) {

				e.printStackTrace();
			}

		} catch (Exception f) {
			f.printStackTrace();
		}
		try {
			bis.close();
			os.close();
			s.close();

		} catch (IOException e) {

			e.printStackTrace();
		}

	}

	/**
	 * @return File downloaded
	 */
	private void get() {
		System.out.println("Enter the name of file to be downloaded");
		Scanner s4 = new Scanner(System.in);
		String file_download = s4.next();
		out.println(file_download);
		int read;
		int temp;
		FileOutputStream fos = null;
		BufferedOutputStream bos = null;

		String downloaded = dir + "/" + file_download;
		try {

			byte a[] = new byte[100000];
			InputStream is = s.getInputStream();
			fos = new FileOutputStream(downloaded);
			bos = new BufferedOutputStream(fos);
			read = is.read(a, 0, a.length);
			temp = read;

			while (read > -1) {
				read = is.read(a, temp, (a.length - temp));
				temp += read;

			}

			bos.write(a, 0, temp);
			bos.flush();

		} catch (IOException e) {

			e.printStackTrace();
		}

		try {
			fos.close();
			bos.close();
			s.close();

		} catch (IOException e) {

			e.printStackTrace();
		}

	}

	/**
	 * 
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	private void ls() throws IOException, ClassNotFoundException {
		ObjectInputStream inputStream = null;
		inputStream = new ObjectInputStream(s.getInputStream());
		Object temp = inputStream.readObject();
		String[] reader = (String[]) temp;

		for (int i = 0; i < reader.length; i++) {

			System.out.println(reader[i]);

		}

	}

	/**
	 * @return new directory
	 */
	private void lcd() {
		System.out.println("Enter the new directory name");
		Scanner userinput = new Scanner(System.in);
		String input = userinput.next();
		if (input.equals("..")) {

			System.out.println(input);
			dir = dir.substring(0, dir.lastIndexOf("/"));
			System.out.println(dir);
		} else {

			dir = input;
			System.out.println(dir);
		}

	}

	/**
	 * 
	 * @throws IOException
	 */
	private void path() throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(
				s.getInputStream()));
		String s = br.readLine();
		System.out.println(s);
	}

	/**
	 * @return changed directory
	 */
	public void changedirectory() {

		System.out.println("Enter directory");
		Scanner dd = new Scanner(System.in);
		String f = dd.next();
		out.println(f);
		BufferedReader br = null;
		String s8 = null;
		try {
			br = new BufferedReader(new InputStreamReader(s.getInputStream()));
			s8 = br.readLine();
		} catch (IOException e) {

			e.printStackTrace();
		}
		System.out.println(s8);

	}

}
