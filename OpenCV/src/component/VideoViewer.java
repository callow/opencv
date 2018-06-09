package component;

import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import org.opencv.core.Mat;
import org.opencv.videoio.VideoCapture;
import org.opencv.videoio.Videoio;

public class VideoViewer {
	private JFrame frame;
	private JLabel imageLabel;
	
	
	public void initGUI() {
		frame = new JFrame("Camera Input Example");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(400,400);
		imageLabel = new JLabel();
		frame.add(imageLabel);
		frame.setVisible(true);
	}
	
	public void runMainLoop(String[] args) {
		ImageViewer imageProcessor = new ImageViewer();
		Mat webcamMatImage = new Mat();
		Image tempImage;
		VideoCapture capture = new VideoCapture(0);  /*new VideoCapture("img/songlei.avi");*/
		capture.set(Videoio.CAP_PROP_FRAME_WIDTH,320);
		capture.set(Videoio.CAP_PROP_FRAME_HEIGHT,240);
		if( capture.isOpened()) {
			while (true) {
				capture.read(webcamMatImage);
				if(!webcamMatImage.empty()) {
					tempImage= imageProcessor.toBufferedImage(webcamMatImage);
					ImageIcon imageIcon = new ImageIcon(tempImage, "Captured video");
					imageLabel.setIcon(imageIcon);
					frame.pack(); //this will resize the window to fit the image
				} else {
					System.out.println(" -- Frame not captured -- Break!");
					break;
				}
			}
		} else {
			System.out.println("Couldn't open capture.");
		}
	}
}
