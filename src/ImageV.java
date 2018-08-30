import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.Random;

public class ImageV extends Objects{
	BufferedImage[] img;
	Random r1;
	int pos;

	
	public ImageV(int width, int height, int Xs, int Ys, BufferedImage[] img) {
		super(width, height, Xs, Ys);
		this.img = img;
		r1 = new Random();
		pos = -1;
	}
	
	public void moveAndDraw(Graphics2D win, int theta, int vel, boolean steering) {
		super.moveAndDraw(win, theta, vel, steering);
		
		if(pos==-1) {
			pos = r1.nextInt(img.length);
		}
		
		win.drawImage(img[pos], (int)super.getX(), (int) super.getY(), (int)super.getWidth(), (int)super.getHeight(), null);
	
		
		
	}

}
