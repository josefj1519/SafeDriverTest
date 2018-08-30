import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.ArrayList;


public class Objects extends Rectangle{
	int width, height, Xs, Ys, centerX, centerY, pwidth, pheight, px, py; 
	float time;
	int cwidth, cheight,  cenx, ceny, xc, yc;
	boolean hit, inter, start, delete, momentum;
	Rectangle center, hitbox;
	
	public Objects(int width, int height, int Xs, int Ys){
		super(Xs, Ys, width, height);
		this.width = width; pwidth = width;
		this.height = height; pheight = height;
		this.Xs = Xs; px = Xs;
		this.Ys = Ys; py = Ys;
		hit = false;
		centerX = (int) (this.getX()+this.getWidth()/2);
		centerY = (int) (this.getY()+this.getHeight()/2);
		time = 0;
		inter = true;
		
		//hitboxes
		hitbox = new Rectangle(250, 30, 275, 300);
		
		//center rectangle
		center = new Rectangle(300,100, 200,200);
		cenx =(int)(center.getX()+center.getWidth()/2); xc = (int) center.getX();
		ceny = (int)(center.getY()+center.getHeight()/2); yc = (int) center.getY();
		cwidth = (int) center.getWidth(); cheight = (int)center.getHeight();
		
		
		
	}
	
	public void moveAndDraw(Graphics2D win, int theta, int vel, boolean steering) {
		
		time += .5;
		
		//cenbox
		center.setBounds(xc, yc, cwidth, cheight);
		cwidth = (int)this.findWidth(vel, center);
		cheight = (int)this.findHeight(vel, center);
		xc = this.findX(cwidth, cenx);
		yc = this.findY(cheight, ceny);
		
		//object
		this.setBounds(Xs, Ys, width, height);
		width = (int)this.findWidth(vel, this);
		height = (int)this.findHeight(vel, this);
				
		//object in center and turning dont change x
		if(steering) {
			this.getTurning(theta);
		}
			
		if(inter && !steering) {
			Xs = this.findX(width, centerX);	
		}
			
		Ys = this.findY(height, centerY);

		if(!steering) {
			this.getZoom(vel);
		}
		
		this.getPosition(theta);	
		start = false;
	}
	
	
	
	protected void getPosition(int theta) {
		
		if(Ys<hitbox.getY() && ((hitbox.getX()<Xs+width && 
		Xs+width<hitbox.getX()+hitbox.getWidth())||(hitbox.getX()+hitbox.getWidth()>Xs && hitbox.getX()<Xs))) {
			hit = true;
		}
		if(this.contains(hitbox)) {
			hit = true;
		}
		
		if(Ys<=0||Ys<=0 ) {
			delete = true;
		}
		
		
		//going right
		if(Xs+width>sdtComp.MAX && theta<0) {
			Xs = sdtComp.MIN-width;
			
		}
		
		//going left
		if(Xs<sdtComp.MIN && theta>0) {
			Xs = sdtComp.MAX;
		}
				
	}
	
	protected void getTurning(int theta) {
		
		if(Xs<=xc && theta<0) {
			Xs -= theta*.5;
		}
		if(Xs<=xc && theta>0) {
			Xs -= theta*2;
		}
		if(Xs>=xc+cwidth && theta<0) {
			Xs -= theta*2;
		}
		if(Xs>=xc+cwidth && theta>0) {
			Xs -= theta*.5;
		}
		if(xc<Xs+width && xc+cwidth>Xs) {
			Xs -= theta;
			inter = false;
		}
		
	}

	public void getZoom(double vel) {
	
		if(!center.contains(this) && !start) {
			if(xc>Xs){
				inter = false;
				Xs -= (vel*2*(time*.1));
				
			}
			if(xc+cwidth<Xs) {
				inter = false;
				Xs += (vel*2*(time*.1));
			}
		}
	}

	//for car moving (object hitboxes)
	public double findHeight(double vel, Rectangle r) {
		return (((vel*time*.1)+r.getHeight()));
	}
	public double findWidth(double vel,Rectangle r) {
		return (((vel*time*.1)+r.getWidth()));
	}
	public int findX(int w, int cx) {
		return (int) (cx - w/2);
	}
	public int findY(int h, int cy) {
		return (int) (cy - h/2);
	}
	
	
	

}
