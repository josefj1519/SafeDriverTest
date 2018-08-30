import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

//SUN AND MOUTAIN
public class permObjects extends Objects {
	BufferedImage img;
	
	public permObjects(int width, int height, int Xs, int Ys, BufferedImage img) {
		super(width, height, Xs, Ys);
		this.img = img;
	}
	
	public void moveAndDraw(Graphics2D win, int theta, int vel, boolean steering) {
		
		if(steering) {
			if(theta<0) {
				super.Xs += 2;
			}
			if(theta>0) {
				super.Xs -= 2;
			}
		}
		
		win.drawImage(img,super.Xs, super.Ys, super.width, super.height, null);
		this.getPosition(theta);
	}
	
}
