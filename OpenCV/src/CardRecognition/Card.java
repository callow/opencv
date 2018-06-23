package CardRecognition;

import java.awt.image.BufferedImage;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Rect;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import component.ImageViewer;
import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
/**
 * https://blog.csdn.net/u012706811/article/details/52779271
 * http://www.voidcn.com/article/p-debrmlbo-bnz.html
 * @author 宋磊
 *
 */

public class Card {
	
	static {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

	public static void main(String[] args) 
	{
		Mat srcImage = loadImage("img/card.jpg");
		
		Mat grey = grey(srcImage);
		//new ImageViewer().show(grey, "show grey image");
		
		Mat binary = blackWhite(grey);
		//new ImageViewer().show(binary, "show binary image");
		
		Mat erode = imageErode(binary);
		//new ImageViewer().show(erode, "show eroded image");
		
		Mat adjust = filterAndcut(erode);
		//new ImageViewer().show(adjust, "show scope image");
		
		printResult(matToBufferImage(adjust));
		
		
	}
	
	/**
	 * 加载图片到这里
	 * @param path
	 * @return
	 */
	
	public static  Mat loadImage(String path) {
		Mat newImage = Imgcodecs.imread(path);
		return newImage;
	}
	
	
	/**
	 * 把Mat转成BufferImage
	 * @param grayMat
	 * @return bufferImage
	 */
	public static BufferedImage matToBufferImage(Mat grayMat) {
		if (grayMat == null) {
			return null;
		}
	    byte[] data1 = new byte[grayMat.rows() * grayMat.cols() * (int)(grayMat.elemSize())];
	    grayMat.get(0, 0, data1); // 获取所有的像素点
	    BufferedImage image1 = new BufferedImage(grayMat.cols(), grayMat.rows(),BufferedImage.TYPE_BYTE_GRAY);
	                           
	    image1.getRaster().setDataElements(0, 0, grayMat.cols(), grayMat.rows(), data1);
	    return image1;
	}
	
	/**
	 * 把BufferImage转成Mat
	 * @param src
	 * @return mat
	 */
	public static Mat bufferImageToMat(BufferedImage src) {
		if (src == null) {
			return null;
		}
	    Mat srcMat = new Mat(src.getHeight(), src.getWidth(), CvType.CV_8UC3);
	    return srcMat;
	}
	
	/**
	 * 该函数把原srcMat转换为灰度图像放入grayMat中,自己再转换为BufferedImage显示即可.
	 * @param srcMat
	 * @param destMat
	 */
	public static Mat grey(Mat srcMat) {
		Mat dest = new Mat (); 
		Imgproc.cvtColor(srcMat, dest, Imgproc.COLOR_RGB2GRAY);
		return dest;
	}
	/**
	 * 也就是只留两个值,黑白, Binary
	 * @param grayMat
	 * @return grayMat
	 */
	public static Mat blackWhite(Mat grayMat) {
		 Mat binaryMat = new Mat(grayMat.height(),grayMat.width(),CvType.CV_8UC1);
	     Imgproc.threshold(grayMat, binaryMat, 30, 200, Imgproc.THRESH_BINARY);
	     return binaryMat;
	}
	/**
	 * 腐蚀后变得更加宽,粗.便于识别
	 * @param srcMat
	 * @return
	 */
	public static Mat imageErode(Mat srcMat) {
		Mat destMat = new Mat();
		Mat element = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(3, 3));
        Imgproc.erode(srcMat,destMat,element);
        return destMat;
	}
	
	/**
	 * 获取截图的范围--从第一行开始遍历,统计每一行的像素点值符合阈值的个数,再根据个数判断该点是否为边界
                     判断该行的黑色像素点是否大于一定值（此处为150）,大于则留下,找到上边界,下边界后立即停止
	 * @param imgSrc
	 */
	
	public static Mat  filterAndcut(Mat destMat) {
		 int a =0, b=0, state = 0;
	        for (int y = 0; y < destMat.height(); y++)//行
	        {
	            int count = 0;
	            for (int x = 0; x < destMat.width(); x++) //列
	            {
	                //得到该行像素点的值
	                byte[] data = new byte[1];
	                destMat.get(y, x, data);
	                if (data[0] == 0)
	                    count = count + 1;
	            }
	            if (state == 0)//还未到有效行
	            {
	                if (count >= 150)//找到了有效行
	                {//有效行允许十个像素点的噪声
	                    a = y;
	                    state = 1;
	                }
	            }
	            else if (state == 1)
	            {
	                if (count <= 150)//找到了有效行
	                {//有效行允许十个像素点的噪声
	                    b = y;
	                    state = 2;
	                }
	            }
	        }
	        System.out.println("过滤下界"+Integer.toString(a));
	        System.out.println("过滤上界"+Integer.toString(b));


	        //参数,坐标X,坐标Y,截图宽度,截图长度
	        Rect rect = new Rect(0,a,destMat.width(),b - a);
	        Mat resMat = new Mat(destMat,rect);
	        return resMat;
	}
	
	public static void printResult(BufferedImage src) {
		ITesseract instance = new Tesseract();
		instance.setDatapath("F:/Tess4J-3.4.8-src/Tess4J/tessdata");
		//instance.setLanguage("chi_sim"); // 测试中文测试集
		long startTime = System.currentTimeMillis();
		String ocrResult;
		try {
			 ocrResult = instance.doOCR(src);
			 System.out.println("OCR Result: \n" + ocrResult + "\n 耗时：" + (System.currentTimeMillis() - startTime) + "ms");
		} catch (TesseractException e) {
			e.printStackTrace();
		}	
	}
	
	
//	/**
//	 * 二值图片,当成一个二维矩阵就可以了,双重循环,使用get方法获取像素点,使用put方法修改像素点
//	 * @param imgSrc
//	 */
//	public static Mat loopToChangePixelToGetFrame(Mat imgSrc) {
//		for (int y = 0; y < imgSrc.height(); y++) {
//           for (int x = 0; x < imgSrc.width(); x++) {
//           	//得到该行像素点的值
//               double[] data = imgSrc.get(y,x);
//               for (int i1 = 0; i1 < data.length; i1++) {
//                   data[i1] = 255;//像素点都改为白色
//               }
//               imgSrc.put(i,j,data);
//           }
//       }
//		
//		return imgSrc;
//	}
	
	
	

}
