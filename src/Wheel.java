import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;

public class Wheel extends Rectangle implements MouseListener, MouseMotionListener{
	
	Point wheel, start;
	boolean steering, turning, clockwise, lock;
	float mousespeed, prevspeed; 
	double wheel_timer, wheelangle;
	
	
	public Wheel(int x, int y, int width, int height) {
		super(x,y,width,height);
		wheel = new Point((int)(this.getX() + this.getWidth()/2), (int)(this.getY()+this.getHeight()/2));
	}
	/*
	 * calculate theta using mouse speed
	 * polar somewhat worked but whole image would rotate
	 */
	public void moveAndDraw(Graphics2D win, BufferedImage img, double NUM) {
		
		win.setColor(Color.BLACK);
		if(steering && mousespeed!= prevspeed) {	
			if(clockwise) {
				wheel_timer += mousespeed*NUM;
			}
			if(!clockwise) {
				wheel_timer -=  mousespeed*NUM;
			}
		}
		if(!steering) {
			if(wheel_timer<-.1) {
				wheel_timer += .05;
			}
			if(wheel_timer>.1) {
				wheel_timer -= .05;
			}
		}
		if((wheel_timer>7.5)||(wheel_timer<-7.5)) {
			if(wheel_timer>7.5)
				wheel_timer = 7.5;
			else
				wheel_timer = -7.5;
		}
		
		AffineTransform at = win.getTransform();
		win.rotate( wheel_timer,  wheel.x,  wheel.y);
		win.drawImage(img, 50, 250, null);
		win.setTransform(at);
		
			
		prevspeed =  mousespeed;
			
		if(wheel_timer>-.1 && wheel_timer<.1) {
			turning = false;
		}
		else {
			turning = true;
		}
		
		
	}
	
	
	//CLOCKWISE OR COUNTERCLOCKWISE
	public boolean direction(Point cen, Point prev, Point cur) {
		if((prev.x-cen.x)*(cur.y-cen.y) - (prev.y-cen.y)*(cur.x-cen.x)==0) {
			return clockwise;
		}
		return	((prev.x-cen.x)*(cur.y-cen.y) - (prev.y-cen.y)*(cur.x-cen.x)) > 0;
	}


	@Override
	public void mouseDragged(MouseEvent e) {

		if(steering) {
			mousespeed = (float) Math.sqrt(Math.pow(start.x-e.getX(),2)+ Math.pow(start.y-e.getY(), 2));
			if(this.direction(wheel, start, e.getPoint())) {
				clockwise = true;
			}
			else {
				clockwise = false;
				
			}
			start = new Point(e.getPoint());
			
			
			if(clockwise) {
				wheelangle = 1+mousespeed*.01;
			}
			else {
				wheelangle = -1-mousespeed*.01;
			}	
		}
		
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		
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
		//add additional conditional statement to determine if it is in box for drawing
		if(!steering &&  this.contains(e.getPoint()) && e.getButton()==MouseEvent.BUTTON1) {
			start = new Point(e.getPoint());
			steering = true;
		}
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		if(steering && e.getButton()==MouseEvent.BUTTON1) {
			start = new Point(e.getPoint());
			steering = false;
		}
		
	}

}
