import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;

public class RadioUI implements MouseListener{
	BufferedImage[][] img;
	boolean isLeft, isRight, enterPress, exitPress, start;
	boolean[] position;
	double NUM;
	int state, translate;
	Rectangle left, right;
	RoundRectangle2D[] button;
	private String finaltime;
	SoundDriver s1;
	
	public RadioUI(BufferedImage[][] img) {
		this.img = img;
		left = new Rectangle(470, 500, 50, 50);
		right = new Rectangle(680, 500, 50, 50);
		button = new RoundRectangle2D.Double[2];
		button[0] = new RoundRectangle2D.Double(530, 480, 70, 70, 50, 50) ;
		button[1] = new RoundRectangle2D.Double(600, 480, 70, 70, 50, 50) ;
		NUM = .01;
		start = true;
		String[] str = new String[1];
		str[0] = "menunoise.wav";
		s1 = new SoundDriver(str);
	}
	
	public void moveAndDraw(Graphics2D win, int gamestate, boolean position[]) {
		
		this.position = position;
		this.state = gamestate;
		this.getButtons(win);
		
		win.setColor(Color.GREEN);
		if(start) {
			win.drawString("Shift gears to change the mode", 520, 450);
			if(!position[1] || state != 0)
				start = false;
		}
		if(state == -1) {
			win.drawString("Final score: " + finaltime , 530, 450);
		}
		if(state == 0 && !start) {
			if(position[0]) {
				win.drawString("Press Enter to Play", 530, 450);
			}
			if(position[1]) {
				win.drawString("Press Enter for Instructions", 530, 450);
			}
			if(position[2]) {
				win.drawString("Press Enter for HighScores", 530, 450);
			}
		}
		if(state == 1) {
			win.drawString("Time: " + finaltime , 530, 450);
		}
		
		if(state == 2) {
			if(NUM < -.03) 
				NUM = -.03;
			if(NUM>.06)
				NUM = .06;
			win.drawString("Wheel Sensitivity:   "+ (int)((NUM*100)+4), 530, 450);
						
			
		}
	}
	
	//GETS FINAL SCORE TO DISPLAY
	public void getScore(String finaltime) {
		this.finaltime = finaltime;
	}
	
	//DRAWING ALL BUTTONS
	public void getButtons(Graphics2D win) {
		win.drawImage(img[0][0], 450, 380, 300, 200, null);
		if(!isLeft)
			win.drawImage(img[0][1], 470, 500, 50, 50, null);
		if(isLeft)
			win.drawImage(img[0][3], 470, 500, 50, 50, null);
		if(!isRight)
			win.drawImage(img[0][2], 680, 500, 50, 50, null);
		if(isRight)
			win.drawImage(img[0][4], 680, 500, 50, 50, null);
		

		win.setColor(Color.BLACK);
		win.fillRoundRect(530, 480, 140, 70, 20 , 20);
		
		for(int i=0;i<button.length;i++) {
			win.setColor(sdtComp.color[i+4]);
			win.fill(button[i]);
		}
		if(enterPress) {
			win.setColor(sdtComp.color[3]);
			win.fill(button[0]);
		}
		if(exitPress) {
			win.setColor(sdtComp.color[6]);
			win.fill(button[1]);
		}
		win.drawImage(img[1][2], 530, 480, 260, 100, null);
		win.drawImage(img[1][3], 610, 490, 260, 100, null);
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	//CHANGES NUM
	public void mousePressed(MouseEvent e) {
		if(e.getButton() == MouseEvent.BUTTON1) {
			if(left.contains(e.getPoint())) {
				s1.play(0);
				isLeft = true;
				if(state == 2) {
					NUM -= .01;
				}
			}
			if(right.contains(e.getPoint())) {
				s1.play(0);
				isRight = true;
				if(state == 2) {
					NUM += .01;
				}
			}
			
			
		}
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		if(e.getButton() == MouseEvent.BUTTON1) {
			if(left.contains(e.getPoint())) {
				isLeft = false;
			}
			if(right.contains(e.getPoint())) {
				isRight = false;
			}
		}
		
	}
}
