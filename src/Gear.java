import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;

public class Gear extends Rectangle implements MouseListener, MouseMotionListener {
	
	Rectangle[] gears;
	boolean[] position;
	boolean shift;
	
	public Gear(int x, int y, int width, int height) {
		super(x, y, width, height);
		gears = new Rectangle[3];
		position = new boolean[3];
		
		for(int i = 0;i<gears.length;i++) {
			gears[i] = new Rectangle((int) this.getX(), (int) (this.getY()+this.getHeight()/3*i), (int) this.getWidth(), (int) this.getHeight()/3);
			position[i] = false;
			
		}
		position[1] = true;
		
	}
	
	public void moveAndDraw(Graphics2D win, BufferedImage[] img) {
		
		win.drawImage(img[0], (int)this.getX()-5, (int)this.getY(), (int)this.getWidth(),(int)this.getHeight(), null);
		
		
		
		for(int i = 0; i<gears.length;i++) {
			if(position[i]) {
				if(i==1) {
					win.drawImage(img[i+1],(int) gears[i].getX()+20, (int) gears[i].getY()-20, null);
				}
				if(i==0) {
					win.drawImage(img[i+1],(int) gears[i].getX()+33, (int) gears[i].getY()-20, null);
				}
				if(i==2) {
					win.drawImage(img[i+1],(int) gears[i].getX()+15, (int) gears[i].getY()-20, null);
				}
			}
		}
		
		
		
	}
	
	public void gearHitbox(Graphics2D win) {
		for(int i = 0; i<gears.length;i++) {
			if(position[i]) {
				win.setColor(Color.RED);
				win.draw(gears[i]);
			}
			else {
				win.setColor(Color.BLACK);
				win.draw(gears[i]);
			}
		}
	}
	
	

	@Override
	public void mouseDragged(MouseEvent e) {
		
		if(shift) {
			
			if(gears[0].getMaxY()>e.getY()) {
				position[0] = true;
				position[1] = false;
				position[2] = false;
			}
			else if(gears[1].getMaxY()>e.getY()){
				position[1] = true;
				position[0] = false;
				position[2] = false;
			}
			else if(gears[2].getMaxY()>e.getY()){
				position[2] = true;
				position[1] = false;
				position[0] = false;
			}
			
		}
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		if(!shift && this.contains(e.getPoint()) && e.getButton()==MouseEvent.BUTTON1) {
			shift = true;
		}
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		if(e.getButton()==MouseEvent.BUTTON1) {
			shift = false;
		}
	}

}
