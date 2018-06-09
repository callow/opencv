package Util;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

public class ImageUtil {

	
	
	
	public void setupSlider(JFrame frame) {
		JLabel sliderLabel = new JLabel("Blur level", JLabel.CENTER);
		sliderLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		int minimum = 0;
		int maximum = 10;
		int initial =0;
		JSlider levelSlider = new JSlider(JSlider.HORIZONTAL,
		minimum, maximum, initial);
		levelSlider.setMajorTickSpacing(2);
		levelSlider.setMinorTickSpacing(1);
		levelSlider.setPaintTicks(true);
		levelSlider.setPaintLabels(true);
		levelSlider.addChangeListener(new ChangeListener() {
		public void stateChanged(ChangeEvent e) {
		JSlider source = (JSlider)e.getSource();
		int level = (int)source.getValue();
		Mat output = ImageViewer.blur(image, level);
		updateView(output);
		}
		});
		frame.add(sliderLabel);
		frame.add(levelSlider);
		}
	
	
	private void setupImage(JFrame frame) {
		JLabel mouseWarning = new JLabel("Try clicking on the image!",
		JLabel.CENTER);
		mouseWarning .setAlignmentX(Component.CENTER_ALIGNMENT);
		mouseWarning.setFont(new Font("Serif", Font.PLAIN, 18));
		frame.add(mouseWarning);
		imageView = new JLabel();
		final JScrollPane imageScrollPane = new JScrollPane(imageView);
		imageScrollPane.setPreferredSize(new Dimension(640, 480));
		imageView.addMouseListener(new MouseAdapter()
		{
		public void mousePressed(MouseEvent e)
		{
		Imgproc.circle(image,new Point(e.getX(),e.getY()),20, new
		Scalar(0,0,255), 4);
		updateView(image);
		}
		});
		frame.add(imageScrollPane);
		}
	
	
	
	private void setupButton(JFrame frame) {
		JButton clearButton = new JButton("Clear");
		clearButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				image = originalImage.clone();
				updateView(originalImage);
			}
		});
			clearButton.setAlignmentX(Component.CENTER_ALIGNMENT);
			frame.add(clearButton);
		}
		}
	
	// open specific image
		public Mat openFile(String fileName) throws Exception {
			Mat newImage = Imgcodecs.imread(fileName);
			if(newImage.dataAddr() ==0) {
				throw new Exception ("Couldn't open file "+fileName);
			}
			return newImage;
		}
}
