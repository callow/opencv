package Entrance;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.CvType;
import org.opencv.core.Scalar;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.videoio.VideoCapture;

import component.ImageViewer;
import component.VideoViewer;
class Entrance {
	
	static 
	{ 
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME); 
	}

	public static void main(String[] args) {
//		System.out.println("Welcome to OpenCV " + Core.VERSION);
//		
//		Mat m = new Mat(5, 10, CvType.CV_8UC1, new Scalar(0));
//		
//		System.out.println("OpenCV Mat: " + m);
//		
//		m.row(1).setTo(new Scalar(1));
//		m.col(5).setTo(new Scalar(5));
//		
//		System.out.println("OpenCV Mat data:\n" + m.dump());
//-----------------------------------------------------------------------------		
		
//		String filePath = "img/songlei.jpg";
//		Mat newImage = Imgcodecs.imread(filePath);
//		if(newImage.dataAddr()==0) {
//			System.out.println("Couldn't open file " + filePath);
//		} else {
//			new ImageViewer().show(newImage, "Loaded image");
//			
//		}
//----------------------------------------------------------------------------------		
		//´ò¿ªvideo camera£¬ page 47 	
		VideoViewer video = new VideoViewer();
		video.initGUI();
		video.runMainLoop(args);
//-----------------------------------------------------------------------------------
		
		
		

	}
}
