package main;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.StringTokenizer;

/**
 * 
 * @author mxz301
 * 
 *         A Handler class that implements Runnable Interface and processes the
 *         server requests
 * 
 */
final public class ServerRequestHandler implements Runnable {

	final static String CRLF = "\r\n";
	Socket socket;
	private String fileName = null;

	// Constructor
	public ServerRequestHandler(Socket socket) {
		this.socket = socket;

	}

	// Implements the run method from the Runnable() interface
	@Override
	public void run() {

		try {

			ProcessRequest();
		}

		catch (Exception e) {
			System.out.println(e);
		}

	}

	void ProcessRequest() {
		System.out.println("Process the request");
		// get a reference to the socket's input and output streams
		BufferedReader is = null;
		try {
			is = new BufferedReader(new InputStreamReader(
					socket.getInputStream()));
		} catch (IOException e1) {

			System.out.println("Server: BufferReader input stream error");
			e1.printStackTrace();
		}

		DataOutputStream dos = null;
		try {
			dos = new DataOutputStream(socket.getOutputStream());
		} catch (IOException e1) {
			System.out.println("Server: DataOutputStream error");

			e1.printStackTrace();
		}

		// get the request line from the HTTP request message
		String requestLine = null;
		try {
			requestLine = is.readLine();
		} catch (IOException e1) {

			System.out.println("Server: Request Line read error");
			e1.printStackTrace();
		}

		// extract the filename from the request line
		StringTokenizer tokens = new StringTokenizer(requestLine);

		// skip over the method which should be "GET"
		tokens.nextToken();
		// get the filename

		// depending on the user input it either gets the file from the token or
		// from the user input

		fileName = tokens.nextToken();

		// append . so that the file is in the local directory because the
		// browser precedes with a /
		fileName = "." + fileName;
		// catch if the file is not found
		FileInputStream fis = null;
		boolean isfound = true;

		try {
			fis = new FileInputStream(fileName);
		} catch (FileNotFoundException e) {
			isfound = false;
		}

		// form the response message
		String statusLine = null;
		String contentTypeLine = null;
		String entityBody = null;
		if (isfound) {
			statusLine = "HTTP/1.1 200 OK" + CRLF;
			contentTypeLine = "Content Type: " + contentType(fileName) + CRLF;
		} else {
			statusLine = "HTTP/1.1 404 Not Found" + CRLF;
			contentTypeLine = "Content Type: " + "txt/html" + CRLF;
			entityBody = "<HTML>" + "<HEAD>" + "<TITLE>" + "Not Found"
					+ "</TITLE>" + "</HEAD>" + "<BODY>" + "Not Found"
					+ "</BODY>" + "</HTML>" + CRLF;

		}

		// send the status line
		System.out.println("File Found Status:" + isfound);
		System.out.println(statusLine);
		System.out.println(contentTypeLine);
		System.out.println();
		try {
			dos.writeBytes(statusLine);

		} catch (IOException e) {
			System.out.println("Server :DataOutputStream write bytes error");
		}
		// send the content type line
		try {
			dos.writeBytes("Filename:" + fileName + CRLF);
			dos.writeBytes(contentTypeLine + CRLF);

		} catch (IOException e) {
			System.out.println("Server :DataOutputStream write error");

		}

		// send the entity body
		if (isfound) {
			try {
				sendBytes(fis, dos);
			} catch (Exception e) {
				System.out.println("Server :Send Bytes error");

			}
			try {
				fis.close();
			} catch (IOException e) {
				System.out.println("Server :Close fis error");

			}
		} else {
			try {
				dos.writeBytes(entityBody);
			} catch (IOException e) {
				System.out
						.println("Server :DataOutputStream write entity file error");

			}
		}

		// display the request line

		System.out.println("The server got the following request");
		System.out.println(requestLine);
		// get and display the header lines

		// close the data output stream, buffer reader and socket
		try {
			dos.flush();

			dos.close();
			is.close();
			socket.close();

		} catch (IOException e) {
			System.out.println("Server :socket close error");
		}
	}

	/**
	 * Send Bytes Method writes the file to the server byte by byte
	 * 
	 * @param fis
	 *            a file input stream
	 * @param dos
	 *            a data output stream
	 */
	private void sendBytes(FileInputStream fis, DataOutputStream dos) {
		// create a 10mb buffer
		byte buffer[] = new byte[10240];
		int bytes = 0;

		// copy the buffer file into the socket output stream
		try {
			while ((bytes = fis.read(buffer)) != -1) {
				dos.write(buffer, 0, bytes);
			}
		} catch (IOException e) {
			System.out
					.println("Server : SendBytes: DataOutputStream write error");

		}

	}

	/**
	 * ContentType method returns the content type of a particular file ending
	 * 
	 * @param fileName
	 *            the name of the file
	 * @return a string of the content type
	 */
	private String contentType(String fileName) {

		// check if the file is text
		if (fileName.endsWith(".htm") || fileName.endsWith(".html"))
			return "text/html";
		if (fileName.endsWith("323"))
			return "text/h323";
		if (fileName.endsWith(".gif") || fileName.endsWith(".png")) 
			return "image/png";
		if (fileName.endsWith("323"))
			return "text/h323";
		if (fileName.endsWith("*"))
			return "application/octet-stream";
		if (fileName.endsWith("acx"))
			return "application/internet-property-stream";
		if (fileName.endsWith("ai"))
			return "application/postscript";
		if (fileName.endsWith("aif"))
			return "audio/x-aiff";
		if (fileName.endsWith("aifc"))
			return "audio/x-aiff";
		if (fileName.endsWith("aiff"))
			return "audio/x-aiff";
		if (fileName.endsWith("asf"))
			return "video/x-ms-asf";
		if (fileName.endsWith("asr"))
			return "video/x-ms-asf";
		if (fileName.endsWith("asx"))
			return "video/x-ms-asf";
		if (fileName.endsWith("au"))
			return "audio/basic";
		if (fileName.endsWith("avi"))
			return "video/x-msvideo";
		if (fileName.endsWith("axs"))
			return "application/olescript";
		if (fileName.endsWith("bas"))
			return "text/plain";
		if (fileName.endsWith("bcpio"))
			return "application/x-bcpio";
		if (fileName.endsWith("bin"))
			return "application/octet-stream";
		if (fileName.endsWith("bmp"))
			return "image/bmp";
		if (fileName.endsWith("c"))
			return "text/plain";
		if (fileName.endsWith("cat"))
			return "application/vnd.ms-pkiseccat";
		if (fileName.endsWith("cdf"))
			return "application/x-cdf";
		if (fileName.endsWith("cdf"))
			return "application/x-netcdf";
		if (fileName.endsWith("cer"))
			return "application/x-x509-ca-cert";
		if (fileName.endsWith("class"))
			return "application/octet-stream";
		if (fileName.endsWith("clp"))
			return "application/x-msclip";
		if (fileName.endsWith("cmx"))
			return "image/x-cmx";
		if (fileName.endsWith("cod"))
			return "image/cis-cod";
		if (fileName.endsWith("cpio"))
			return "application/x-cpio";
		if (fileName.endsWith("crd"))
			return "application/x-mscardfile";
		if (fileName.endsWith("crl"))
			return "application/pkix-crl";
		if (fileName.endsWith("crt"))
			return "application/x-x509-ca-cert";
		if (fileName.endsWith("csh"))
			return "application/x-csh";
		if (fileName.endsWith("css"))
			return "text/css";
		if (fileName.endsWith("dcr"))
			return "application/x-director";
		if (fileName.endsWith("der"))
			return "application/x-x509-ca-cert";
		if (fileName.endsWith("dir"))
			return "application/x-director";
		if (fileName.endsWith("dll"))
			return "application/x-msdownload";
		if (fileName.endsWith("dms"))
			return "application/octet-stream";
		if (fileName.endsWith("doc"))
			return "application/msword";
		if (fileName.endsWith("dot"))
			return "application/msword";
		if (fileName.endsWith("dvi"))
			return "application/x-dvi";
		if (fileName.endsWith("dxr"))
			return "application/x-director";
		if (fileName.endsWith("eps"))
			return "application/postscript";
		if (fileName.endsWith("etx"))
			return "text/x-setext";
		if (fileName.endsWith("evy"))
			return "application/envoy";
		if (fileName.endsWith("exe"))
			return "application/octet-stream";
		if (fileName.endsWith("fif"))
			return "application/fractals";
		if (fileName.endsWith("flr"))
			return "x-world/x-vrml";
		if (fileName.endsWith("gif"))
			return "image/gif";
		if (fileName.endsWith("gtar"))
			return "application/x-gtar";
		if (fileName.endsWith("gz"))
			return "application/x-gzip";
		if (fileName.endsWith("h"))
			return "text/plain";
		if (fileName.endsWith("hdf"))
			return "application/x-hdf";
		if (fileName.endsWith("hlp"))
			return "application/winhlp";
		if (fileName.endsWith("hqx"))
			return "application/mac-binhex40";
		if (fileName.endsWith("hta"))
			return "application/hta";
		if (fileName.endsWith("htc"))
			return "text/x-component";
		if (fileName.endsWith("htm"))
			return "text/html";
		if (fileName.endsWith("html"))
			return "text/html";
		if (fileName.endsWith("htt"))
			return "text/webviewhtml";
		if (fileName.endsWith("ico"))
			return "image/x-icon";
		if (fileName.endsWith("ief"))
			return "image/ief";
		if (fileName.endsWith("iii"))
			return "application/x-iphone";
		if (fileName.endsWith("ins"))
			return "application/x-internet-signup";
		if (fileName.endsWith("isp"))
			return "application/x-internet-signup";
		if (fileName.endsWith("jfif"))
			return "image/pipeg";
		if (fileName.endsWith("jpe"))
			return "image/jpeg";
		if (fileName.endsWith("jpeg"))
			return "image/jpeg";
		if (fileName.endsWith("jpg"))
			return "image/jpeg";
		if (fileName.endsWith("js"))
			return "application/x-javascript";
		if (fileName.endsWith("latex"))
			return "application/x-latex";
		if (fileName.endsWith("lha"))
			return "application/octet-stream";
		if (fileName.endsWith("lsf"))
			return "video/x-la-asf";
		if (fileName.endsWith("lsx"))
			return "video/x-la-asf";
		if (fileName.endsWith("lzh"))
			return "application/octet-stream";
		if (fileName.endsWith("m13"))
			return "application/x-msmediaview";
		if (fileName.endsWith("m14"))
			return "application/x-msmediaview";
		if (fileName.endsWith("m3u"))
			return "audio/x-mpegurl";
		if (fileName.endsWith("man"))
			return "application/x-troff-man";
		if (fileName.endsWith("mdb"))
			return "application/x-msaccess";
		if (fileName.endsWith("me"))
			return "application/x-troff-me";
		if (fileName.endsWith("mht"))
			return "message/rfc822";
		if (fileName.endsWith("mhtml"))
			return "message/rfc822";
		if (fileName.endsWith("mid"))
			return "audio/mid";
		if (fileName.endsWith("mny"))
			return "application/x-msmoney";
		if (fileName.endsWith("mov"))
			return "video/quicktime";
		if (fileName.endsWith("movie"))
			return "video/x-sgi-movie";
		if (fileName.endsWith("mp2"))
			return "video/mpeg";
		if (fileName.endsWith("mp3"))
			return "audio/mpeg";
		if (fileName.endsWith("mpa"))
			return "video/mpeg";
		if (fileName.endsWith("mpe"))
			return "video/mpeg";
		if (fileName.endsWith("mpeg"))
			return "video/mpeg";
		if (fileName.endsWith("mpg"))
			return "video/mpeg";
		if (fileName.endsWith("mpp"))
			return "application/vnd.ms-project";
		if (fileName.endsWith("mpv2"))
			return "video/mpeg";
		if (fileName.endsWith("ms"))
			return "application/x-troff-ms";
		if (fileName.endsWith("msg"))
			return "application/vnd.ms-outlook";
		if (fileName.endsWith("mvb"))
			return "application/x-msmediaview";
		if (fileName.endsWith("nc"))
			return "application/x-netcdf";
		if (fileName.endsWith("nws"))
			return "message/rfc822";
		if (fileName.endsWith("oda"))
			return "application/oda";
		if (fileName.endsWith("p10"))
			return "application/pkcs10";
		if (fileName.endsWith("p12"))
			return "application/x-pkcs12";
		if (fileName.endsWith("p7b"))
			return "application/x-pkcs7-certificates";
		if (fileName.endsWith("p7c"))
			return "application/x-pkcs7-mime";
		if (fileName.endsWith("p7m"))
			return "application/x-pkcs7-mime";
		if (fileName.endsWith("p7r"))
			return "application/x-pkcs7-certreqresp";
		if (fileName.endsWith("p7s"))
			return "application/x-pkcs7-signature";
		if (fileName.endsWith("pbm"))
			return "image/x-portable-bitmap";
		if (fileName.endsWith("pdf"))
			return "application/pdf";
		if (fileName.endsWith("pfx"))
			return "application/x-pkcs12";
		if (fileName.endsWith("pgm"))
			return "image/x-portable-graymap";
		if (fileName.endsWith("pko"))
			return "application/ynd.ms-pkipko";
		if (fileName.endsWith("pma"))
			return "application/x-perfmon";
		if (fileName.endsWith("pmc"))
			return "application/x-perfmon";
		if (fileName.endsWith("pml"))
			return "application/x-perfmon";
		if (fileName.endsWith("pmr"))
			return "application/x-perfmon";
		if (fileName.endsWith("pmw"))
			return "application/x-perfmon";
		if (fileName.endsWith("pnm"))
			return "image/x-portable-anymap";
		if (fileName.endsWith("pot"))
			return "application/vnd.ms-powerpoint";
		if (fileName.endsWith("ppm"))
			return "image/x-portable-pixmap";
		if (fileName.endsWith("pps"))
			return "application/vnd.ms-powerpoint";
		if (fileName.endsWith("ppt"))
			return "application/vnd.ms-powerpoint";
		if (fileName.endsWith("prf"))
			return "application/pics-rules";
		if (fileName.endsWith("ps"))
			return "application/postscript";
		if (fileName.endsWith("pub"))
			return "application/x-mspublisher";
		if (fileName.endsWith("qt"))
			return "video/quicktime";
		if (fileName.endsWith("ra"))
			return "audio/x-pn-realaudio";
		if (fileName.endsWith("ram"))
			return "audio/x-pn-realaudio";
		if (fileName.endsWith("ras"))
			return "image/x-cmu-raster";
		if (fileName.endsWith("rgb"))
			return "image/x-rgb";
		if (fileName.endsWith("rmi"))
			return "audio/mid";
		if (fileName.endsWith("roff"))
			return "application/x-troff";
		if (fileName.endsWith("rtf"))
			return "application/rtf";
		if (fileName.endsWith("rtx"))
			return "text/richtext";
		if (fileName.endsWith("scd"))
			return "application/x-msschedule";
		if (fileName.endsWith("sct"))
			return "text/scriptlet";
		if (fileName.endsWith("setpay"))
			return "application/set-payment-initiation";
		if (fileName.endsWith("setreg"))
			return "application/set-registration-initiation";
		if (fileName.endsWith("sh"))
			return "application/x-sh";
		if (fileName.endsWith("shar"))
			return "application/x-shar";
		if (fileName.endsWith("sit"))
			return "application/x-stuffit";
		if (fileName.endsWith("snd"))
			return "audio/basic";
		if (fileName.endsWith("spc"))
			return "application/x-pkcs7-certificates";
		if (fileName.endsWith("spl"))
			return "application/futuresplash";
		if (fileName.endsWith("src"))
			return "application/x-wais-source";
		if (fileName.endsWith("sst"))
			return "application/vnd.ms-pkicertstore";
		if (fileName.endsWith("stl"))
			return "application/vnd.ms-pkistl";
		if (fileName.endsWith("stm"))
			return "text/html";
		if (fileName.endsWith("sv4cpio"))
			return "application/x-sv4cpio";
		if (fileName.endsWith("sv4crc"))
			return "application/x-sv4crc";
		if (fileName.endsWith("svg"))
			return "image/svg+xml";
		if (fileName.endsWith("swf"))
			return "application/x-shockwave-flash";
		if (fileName.endsWith("t"))
			return "application/x-troff";
		if (fileName.endsWith("tar"))
			return "application/x-tar";
		if (fileName.endsWith("tcl"))
			return "application/x-tcl";
		if (fileName.endsWith("tex"))
			return "application/x-tex";
		if (fileName.endsWith("texi"))
			return "application/x-texinfo";
		if (fileName.endsWith("texinfo"))
			return "application/x-texinfo";
		if (fileName.endsWith("tgz"))
			return "application/x-compressed";
		if (fileName.endsWith("tif"))
			return "image/tiff";
		if (fileName.endsWith("tiff"))
			return "image/tiff";
		if (fileName.endsWith("tr"))
			return "application/x-troff";
		if (fileName.endsWith("trm"))
			return "application/x-msterminal";
		if (fileName.endsWith("tsv"))
			return "text/tab-separated-values";
		if (fileName.endsWith("txt"))
			return "text/plain";
		if (fileName.endsWith("uls"))
			return "text/iuls";
		if (fileName.endsWith("ustar"))
			return "application/x-ustar";
		if (fileName.endsWith("vcf"))
			return "text/x-vcard";
		if (fileName.endsWith("vrml"))
			return "x-world/x-vrml";
		if (fileName.endsWith("wav"))
			return "audio/x-wav";
		if (fileName.endsWith("wcm"))
			return "application/vnd.ms-works";
		if (fileName.endsWith("wdb"))
			return "application/vnd.ms-works";
		if (fileName.endsWith("wks"))
			return "application/vnd.ms-works";
		if (fileName.endsWith("wmf"))
			return "application/x-msmetafile";
		if (fileName.endsWith("wps"))
			return "application/vnd.ms-works";
		if (fileName.endsWith("wri"))
			return "application/x-mswrite";
		if (fileName.endsWith("wrl"))
			return "x-world/x-vrml";
		if (fileName.endsWith("wrz"))
			return "x-world/x-vrml";
		if (fileName.endsWith("xaf"))
			return "x-world/x-vrml";
		if (fileName.endsWith("xbm"))
			return "image/x-xbitmap";
		if (fileName.endsWith("xla"))
			return "application/vnd.ms-excel";
		if (fileName.endsWith("xlc"))
			return "application/vnd.ms-excel";
		if (fileName.endsWith("xlm"))
			return "application/vnd.ms-excel";
		if (fileName.endsWith("xls"))
			return "application/vnd.ms-excel";
		if (fileName.endsWith("xlt"))
			return "application/vnd.ms-excel";
		if (fileName.endsWith("xlw"))
			return "application/vnd.ms-excel";
		if (fileName.endsWith("xof"))
			return "x-world/x-vrml";
		if (fileName.endsWith("xpm"))
			return "image/x-xpixmap";
		if (fileName.endsWith("xwd"))
			return "image/x-xwindowdump";
		if (fileName.endsWith("z"))
			return "application/x-compress";
		if (fileName.endsWith("zip"))
			return "application/zip";

		return "application/octet-stream";
	}

}
