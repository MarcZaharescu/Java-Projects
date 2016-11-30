import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 
 * @author mxz301 Downloads Images of selected type from selected url to a
 *         specific folder
 * 
 */
public class JSoupExampleImageGrabber {

	public ArrayList<String> files = new ArrayList<String>();
	public ArrayList<String> resolutions = new ArrayList<String>();
	public static ArrayList<String> links = new ArrayList<String>();

	public void writeFile(InputStream is, OutputStream os) throws IOException {
		byte[] buf = new byte[512]; 
		int num;
		while ((num = is.read(buf)) != -1) {
			os.write(buf, 0, num);
		}
	}
  
	/**Gets the images from the website and downloads them
	 * 
	 * @param website the selected website
	 * @param type the type of image
	 * @return returns an array list containing the images  details
	 * @throws IOException
	 */
	public ArrayList<String> ImageGrabber(String website, String type)
			throws IOException {
		System.out
				.println("...........................Getting the Images..............................");
		Document doc;
		try {

			String folderPath = "C:\\Users\\User\\Desktop\\Images\\";
			;
			// get all images
			doc = Jsoup.connect("http://" + website).get();
			// selector uses CSS selector with regular expression
			Elements images = doc.select("img[src~=(?i)\\.(" + type + ")]");
			for (Element image : images) {
				String urlstr = image.attr("src");

				//gets the file name , lins and resolution of the images
				String fileName = urlstr.substring(urlstr.lastIndexOf('/') + 1,
						urlstr.length());
				String link = urlstr.substring(0, urlstr.lastIndexOf('/') + 1);

				String height = image.attr("height");
				String width = image.attr("width");
				String Height = height.substring(urlstr.lastIndexOf('=') + 1,
						height.length());
				String Width = width.substring(urlstr.lastIndexOf('=') + 1,
						width.length());
				String resolution = "(" + Width + "," + Height + ")";
				link = link + fileName;
				//adds the links to the array lists
				links.add(link);

				
				resolutions.add(resolution);
			
				files.add(urlstr);

				// Open a URL Stream
				URL url = new URL(urlstr);
				InputStream in = url.openStream();
				OutputStream out = new BufferedOutputStream(
						new FileOutputStream(folderPath + fileName));
				for (int b; (b = in.read()) != -1;) {
					out.write(b);
				}
				out.close();
				in.close();

			}

		} catch (IOException e) {
			e.printStackTrace();
		}
		return files;
	}
	
	/**Puts the images in a queue for multi - thread downloading
	 * 
	 * @param number number of threads
	 * @param filePath1 the selected folder path
	 */

	public void QueImages(int number, String filePath1) {
		System.out
				.println("...................Queing Images........................");
		// creates and executor object of the selected number of threads
		ExecutorService pool = Executors.newFixedThreadPool(number);
		System.out.println(links.size());

		// for every image link creates a SomeRUnneble instance for the image name, website, folder path and index of image
		for (int i = 0; i < links.size(); i++) {

			System.out.println(i);
			String fileName = links.get(i).substring(
					links.get(i).lastIndexOf('/') + 1, links.get(i).length());
			String url = links.get(i);
			String filePath = filePath1 + "/";

			SomeRunnable obj1 = new SomeRunnable(fileName, url, filePath, i);
            //submits the SomeRunnble object to the pool
			pool.submit(obj1);

		}
       
		//closes the pool
		pool.shutdown();
	}

	/**
	 * 
	 * @return the array list of resolutions
	 */
	public ArrayList<String> getResolution() {
		return this.resolutions;
	}

	/**
	 * 
	 * @return the array list of links
	 */
	public ArrayList<String> getLinks() {
		return links;
	}

}
