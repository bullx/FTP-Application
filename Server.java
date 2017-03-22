import java.io.*;
import java.net.*;

public class Server {

	static InputStream in;
	static Socket soc = new Socket();
	static PrintWriter out;

	static BufferedOutputStream out1;
	static String dir = System.getProperty("user.dir");;
	static ObjectOutputStream os;
	static FileOutputStream fos;

	/**
	 * 
	 * @param args
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public static void main(String[] args) throws IOException,
			ClassNotFoundException {
		ServerSocket ss = new ServerSocket(9999);
		while (true) {
			soc = ss.accept();
			System.out.println("Connection done");
			cmd();
		}

	}

	/**
	 * 
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public static void cmd() throws IOException, ClassNotFoundException {

		String choice;
		BufferedReader br = new BufferedReader(new InputStreamReader(
				soc.getInputStream()));
		try {
			while ((choice = br.readLine()) != null) {
				switch (choice) {
				case "1":
					changedirectory();
					break;
				case "2":
					break;
				case "3":
					ls();
					break;
				case "4":
					out = new PrintWriter(new BufferedWriter(
							new OutputStreamWriter(soc.getOutputStream())),
							true);
					String current = dir;
					out.println(current);
					break;
				case "5":
					put();
					break;
				case "6":
					get();

					break;
				case "7":
					System.exit(0);
					break;

				}
			}
		} catch (IOException e) {

			// e.printStackTrace();
		}

	}

	/**
	 * 
	 * @throws IOException
	 */
	private static void get() throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(
				soc.getInputStream()));

		String sent_file = br.readLine();

		FileInputStream fis = null;
		BufferedInputStream bis = null;
		OutputStream os = null;

		try {

			File file1 = new File(sent_file);
			byte a[] = new byte[(int) file1.length()];
			fis = new FileInputStream(file1);
			bis = new BufferedInputStream(fis);
			try {
				bis.read(a, 0, a.length);
			} catch (IOException e) {

				e.printStackTrace();
			}
			try {
				os = soc.getOutputStream();
			} catch (IOException e) {

				e.printStackTrace();
			}

			try {
				os.write(a, 0, a.length);
			} catch (IOException e) {

				e.printStackTrace();
			}
			try {
				os.flush();
			} catch (IOException e) {

				e.printStackTrace();
			}

		} catch (Exception r) {
			r.printStackTrace();
		}
		bis.close();
		os.close();
		soc.close();

	}

	/**
	 * @param puts
	 *            file on server
	 */
	private static void put() {
		BufferedReader br = null;
		System.out.println(dir);
		try {
			br = new BufferedReader(new InputStreamReader(soc.getInputStream()));
		} catch (IOException e1) {

			e1.printStackTrace();
		}
		String temp2 = null;
		try {
			temp2 = br.readLine();

		} catch (IOException e1) {
			e1.printStackTrace();
		}
		System.out.println(temp2);
		String downloaded = dir + "/" + temp2;

		int read;
		int temp = 0;
		FileOutputStream fos = null;
		BufferedOutputStream bos = null;

		try {

			byte a[] = new byte[2000000];
			InputStream is = soc.getInputStream();
			fos = new FileOutputStream(downloaded);
			bos = new BufferedOutputStream(fos);
			read = is.read(a, 0, a.length);
			System.out.println(a.length);
			temp = read;

			while (read > -1) {
				read = is.read(a, temp, (a.length - temp));
				System.out.println("sss  "+(a.length-temp));
				System.out.println("read"+read);
				temp += read;
				System.out.println("temp"+temp);

			}

			bos.write(a, 0, temp);
			bos.flush();

		} catch (IOException e) {

			e.printStackTrace();
		}
		try {
			fos.close();
			bos.close();
			soc.close();

		} catch (IOException e) {

			e.printStackTrace();
		}
	}

	/**
	 * @return file list
	 * @throws IOException
	 */
	private static void ls() throws IOException {

		System.out.println(dir);
		File file1 = new File(dir);
		String[] listDir = file1.list();
		os = new ObjectOutputStream(soc.getOutputStream());
		os.writeObject(listDir);

	}

	/**
	 * @return changed directory
	 */
	private static void changedirectory() {

		BufferedReader br1 = null;
		try {
			br1 = new BufferedReader(
					new InputStreamReader(soc.getInputStream()));
			String change = br1.readLine();
			if (change.equals("..")) {

				System.out.println(change);
				dir = dir.substring(0, dir.lastIndexOf("/"));
			} else {
				String directory = System.getProperty("user.dir");
				// System.out.println(change);
				dir = change;
				System.out.println(dir);
			}

			out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(
					soc.getOutputStream())), true);
			out.flush();
			out.println(dir);

		} catch (IOException e) {

			e.printStackTrace();
		}

	}

}
