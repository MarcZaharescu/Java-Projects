import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

/**
 * Multi - Threading or downlading images
 * 
 * @author mxz301
 * 
 */
public class SomeRunnable implements Runnable {
	public String fileName;
	public String url;
	public String filePath;
	public int index;

	/**
	 * Initialises the fileName, website, folder path and index variables
	 * 
	 * @param fileName
	 * @param url
	 * @param filePath
	 * @param index
	 */
	public SomeRunnable(String fileName, String url, String filePath, int index) {

		this.fileName = fileName;
		this.url = url;
		this.filePath = filePath;
		this.index = index;

	}

	/**
	 * RUns the THread and downloads the images in the selected folder
	 */
	public void run() {
		System.out.println("...................Running.....................");
		try {
			System.out.println("Counter - " + index + " / Thread Name : "
					+ Thread.currentThread().getName());

			// updates the progress model values and repaints the progress bar
			Interface.progressModel
					.setValue(Interface.progressModel.getValue() + 1);
			Interface.progressBar_1.repaint();

			// creates a new url and downloads the images
			URL url1 = new URL(url);
			InputStream in = url1.openStream();
			OutputStream out = new BufferedOutputStream(new FileOutputStream(
					filePath + fileName));
			for (int b; (b = in.read()) != -1;) {
				out.write(b);
			}
			out.close();
			in.close();
		}

		catch (IOException e) {
			e.printStackTrace();
		}

	}

}
