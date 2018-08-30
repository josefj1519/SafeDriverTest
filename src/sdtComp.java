import java.awt.Color;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class sdtComp extends GameDriverV3 implements MouseListener, KeyListener{
	private int gamestate, pos, time, delay0, delay1, finalobj, stime, mtime;
	private  ArrayList<Integer> topscore = new ArrayList<Integer>();
	private String user, scores;
	private final int SPEED = 1;
	//-800 --> 0 <SCREEN> 800 ----> 1600
	public static final int MIN = -800;
	public static final int MAX = 1600;
	static Color[] color;
	private boolean draw;
	private Rectangle background, grass;
	private GradientPaint bgColor; 
	private BufferedImage controls1, controls2, dash, house, tcanvas, sun, mountain, crashtxt, instructions, txt, radio, title; 
	private BufferedImage[] sprites1, sprites2, houseD, treeD; 
	private BufferedImage[][] img;
	ArrayList<Objects> obj;
	ArrayList<Rectangle> ground = new ArrayList<Rectangle>();
	Wheel wcontrol;
	Gear gcontrol;
	RadioUI radioU;
	Random r1;
	SoundDriver s1;
	FileDriverV2 f1;
	Font font;
	
	public sdtComp() {
		
		//string
		int count = 0;
		user = System.getProperty("user.home");
		for(int i =0;i<user.length()-1;i++) {
			if(user.substring(i, i+1).equals("/") || user.substring(i, i+1).equals("\\")) {
				count += 1;
			}
			if(count==2) {
				user = user.substring(i+1);
			}
		}
	
		
		
		//colors
		color = new Color[8];
		color[0] = new Color(70, 0, 250); // bluish violet
		color[1] = new Color(0, 200, 150); //light bluish
		color[2] = new Color(34,34,255); //dark bluish
		color[3] = new Color(101, 244, 66); //lightgreen
		color[4] = new Color(0, 171, 2); // green
		color[5] = new Color(199, 1, 9); //red
		color[6] = new Color(255,160,122); //salmon
		color[7] = new Color(0, 153, 0); //dark dark green
		bgColor = new GradientPaint(0,25,color[1],0,600,color[2]); //sky
		font = new Font("TimesRoman", Font.BOLD, 30);
		//shapes
		background = new Rectangle(0,0,800,600);
		grass = new Rectangle(0, 200, 800, 400);
		for(int i = 0;i<5;i++) {
			ground.add(new Rectangle(0, 200 + 80*i, 800, 40));
		}
		
		//images
		title = this.addImage("title.png"); //title
		crashtxt = this.addImage("crash.png"); //crash txt 
		instructions = this.addImage("instructions.png"); //instructions pg
		txt = this.addImage("text.png"); //standard text
		radio = this.addImage("radio.png"); //radio
		mountain = this.addImage("mountain.png"); //mountain
		sun = this.addImage("sun.png"); //sun...
		dash = this.addImage("ui.png"); //dashboard
		controls1 = this.addImage("wheel.png");  //wheel & buttons
		controls2 = this.addImage("gear.png"); //gears
		house = this.addImage("house.png"); // house
		tcanvas = this.addImage("trees.png"); //tree
		
		
		//house
		houseD = new BufferedImage[2];
		for(int i = 0;i<houseD.length;i++) {
			houseD[i] = house.getSubimage(house.getWidth()/2*i, 0, 800, 600); //front and side of house
		}
		
		//wheel and buttons
		sprites1 = new BufferedImage[1]; //wheel and buttons
		sprites1[0] = controls1.getSubimage(50, 40, 300, 300); //wheel
		//buttons are not used 
		
		
		//gears
		sprites2 = new BufferedImage[4];
		sprites2[0] = controls2.getSubimage(0, 0, controls2.getWidth()/4+15,controls2.getHeight());
		for(int i = 1; i<sprites2.length;i++) {
			sprites2[i] = controls2.getSubimage(120 + 40*(i-1) , 0, 45,controls2.getHeight()); //gears
		}
		
		//trees
		treeD = new BufferedImage[3];
		for(int i = 0; i< treeD.length;i++) {
			treeD[i] = tcanvas.getSubimage(i*tcanvas.getWidth()/3, 0, tcanvas.getWidth()/3, tcanvas.getHeight());
		}
		
		//radio and more buttons
		img = new BufferedImage[2][5];
		img[0][0] = radio.getSubimage(700, 80, 700, 230);
		img[0][1] = radio.getSubimage(100, 430, 150, 120);
		img[0][2] = radio.getSubimage(280, 430, 150, 120);
		img[0][3] = radio.getSubimage(100, 640, 150, 120);
		img[0][4] = radio.getSubimage(280, 640, 150, 120);
		
		//text
		for(int i=0; i<4;i++) {
			img[1][i] = txt.getSubimage(30, 60+(60*i), 380, 40);
		}
		
		//nums
		gamestate = 0;
		time = 0;
		delay0 = 120;
		delay1 = 30;
		
		
		//sound
		String[] str = new String[2];
		str[0] = "carengine.wav";
		str[1] = "menunoise.wav";
		s1 = new SoundDriver(str);
		
		//file driver
		f1 = new FileDriverV2();
		f1.createFolder("sdtscores", null);
		
		
		
		//classes
		r1 = new Random();
		
		//obj
		obj = new ArrayList<Objects>();
		obj.add(new permObjects(100, 100, 100, 50, sun));
		obj.add(new permObjects(800, 300, 1200, 120, mountain));
		obj.add(new ImageV(35, 30, 200,200, houseD));
		obj.add(new ImageV(35, 30, 600,200, houseD));
		
		//make a parent class for these below (update: over it)
		wcontrol = new Wheel(65, 250, 275, 275);
		gcontrol = new Gear(350, 400, 89, 150);
		
		//radioUI
		radioU = new RadioUI(img);
		
		//Key & mouse listeners
		this.addKeyListener(this);
		this.addMouseListener(this);
		this.addMouseListener(wcontrol);
		this.addMouseMotionListener(wcontrol);
		this.addMouseListener(gcontrol);
		this.addMouseMotionListener(gcontrol);
		this.addMouseListener(radioU);
		
		this.getOrder();
		
	}

	@Override
	public void draw(Graphics2D win) {
		
		time++;
		
		//backgroundu
		win.setPaint(bgColor);
		win.fill(background);
		win.setColor(color[3]);
		win.fill(grass);
		
		//engine noise
		if(!s1.isPlaying(0) && gamestate != 2 && gamestate != 3) {
			s1.play(0);
		}
		if(gamestate == 2 || gamestate == 3) {
			s1.stop(0);
		}
		
		//green ground
		this.movingGround(win);
		
		
		//preventing movement in death
		if(gamestate == -1)
			wcontrol.wheel_timer = 0;
		
		//drawing the initial objs in menu
		obj.get(0).moveAndDraw(win, (int) wcontrol.wheel_timer, SPEED, wcontrol.steering);
		obj.get(1).moveAndDraw(win, (int) wcontrol.wheel_timer, SPEED, wcontrol.steering);
		
		//crash
		if(gamestate == -1) {
			obj.get(finalobj).moveAndDraw(win, 0, 0, wcontrol.steering);
			radioU.getScore(""+(mtime*60+stime)/2);
		}	

		//menu
		if(gamestate == 0) {
			this.objectHandle(win);
			win.drawImage(title, -90, -100, null);
		}
		
		//in game
		if(gamestate == 1) {
			this.timer();
			this.objectHandle(win);
			radioU.getScore(mtime +":"+stime);
		}
	
		
		//highscores
		if(gamestate == 3) {
			win.setFont(font);
			win.setColor(Color.BLACK);
			win.drawString("Highscores on " +user+"'s computer:", 120, 100);
			
			for(int i = 0; i<topscore.size();i++) {
				win.drawString(i+1+". "+topscore.get(i), 120 , 130+30*i);
				if(i==4) {
					i = topscore.size()+1;
				}
			}
			
		}
		
		
		//dashboard
		win.drawImage(dash, 0,0,null);
		
				
		//insrtuctions
		if(gamestate == 2) {
			win.drawImage(instructions, -50, -30, null);
		}
		
		//crashed
		if(gamestate == -1) {
			win.drawImage(crashtxt, -80, 0, null);
		}
		
		
		//dashboard items
		radioU.moveAndDraw(win, gamestate, gcontrol.position);
		wcontrol.moveAndDraw(win, sprites1[0], radioU.NUM);
		gcontrol.moveAndDraw(win, sprites2);
		
		
		
		//dev mode
		if(draw) {
			gcontrol.gearHitbox(win);
			win.setColor(Color.BLACK);
			win.draw(wcontrol);	
			win.draw(radioU.left);
			win.draw(radioU.right);
		}
		
		
	}
	
	
	//getting list from save file and reordering them
	private void getOrder() {
		if(!f1.readText("score", "sdtscores",false).equals("")){
			scores = f1.readText("score", "sdtscores", false);
			String[] nums = scores.split("-");
			for(int i = 0; i<nums.length-1;i++) {
				topscore.add(Math.max(Integer.valueOf(nums[i]),Integer.valueOf(nums[i+1])));
			}
		}
		else {
			topscore.add(0);
		}
		
		Collections.sort(topscore);
		Collections.reverse(topscore);
	}
	
	
	
	//keeps time and keeps track of score
	private void timer() {
		if(time % 60 == 0) {
			stime += 1;
		}
		if(time % 3600 == 0) {
			mtime += 1;
		}
		
	}
	
	
	//translates dark green ground
	private void movingGround(Graphics2D win) {
		for(int i=0; i<ground.size();i++) {
			win.setColor(color[7]);
			win.fill(ground.get(i));
			if(gamestate == 0 || gamestate == 1) {
				ground.get(i).translate(0,3);
				ground.get(i).grow(0, SPEED);
			}
			if(ground.get(i).getY()>600) {
				ground.remove(i);
				ground.add(new Rectangle(0, 200, 800, 0));
			}
		}
		
		
	}
	
	//HANDLES OBJECTS DELTES, HITS AND MOVEMENT
	private void objectHandle(Graphics2D win) {
		
		if(gamestate == 1) {
			for(int i=obj.size()-1;i>1;i--) {
				if(obj.get(i).hit && !draw) {
					finalobj = i;
					if(f1.readText("score", "sdtscores", false) != null)
						f1.saveText(f1.readText("score", "sdtscores", false)+(mtime*60+stime)/2 + "-", "score", "sdtscores");
					else {
						f1.saveText(""+(mtime*60+stime)/2 + "-", "score", "sdtscores");
					}
					topscore.add((mtime*60+stime)/2);
					Collections.sort(topscore);
					Collections.reverse(topscore);
					gamestate = -1;
				}
		
			obj.get(i).moveAndDraw(win, (int) wcontrol.wheel_timer, SPEED, wcontrol.steering);
			
			if(draw) {
				
				win.setColor(Color.BLUE);
				win.draw(obj.get(i).center);
				win.setColor(Color.BLACK);
				win.draw(obj.get(i).hitbox);
				win.setColor(Color.RED);
				win.draw(obj.get(i));
			}
			if(obj.get(i).delete) {
				obj.remove(i);
					i--;
				}
			}
		}
		if(gamestate == 0) {
			for(int i=obj.size()-1;i>1;i--) {
				obj.get(i).moveAndDraw(win, (int) wcontrol.wheel_timer, SPEED, wcontrol.steering);
				if(obj.get(i).delete) {
					obj.remove(i);
					i--;
				}
			}
		}
		
		this.spawnObj();
		
	}
	
	//SPAWNS OBJS
	private void spawnObj() {
		if(gamestate == 0) {
			if(time % delay0 == 0) {
				obj.add(new ImageV(35, 30,600 ,200, treeD));
				obj.add(new ImageV(35, 30,200 ,200, treeD));
				
			}
			
		}
		if(gamestate == 1) {
			if(time % delay1 == 0) {
				pos = r1.nextInt(1600);
				if(r1.nextInt(10)<5)
					obj.add(new ImageV(35, 30, pos,200, treeD));
				else {
					obj.add(new ImageV(35, 30, pos,200, houseD));
				}
			}
		}
	}
	
	
	//resets the game everytime exit is pressed
	private void reset() {
		obj.clear();
		obj.add(new permObjects(100, 100, 100, 50, sun));
		obj.add(new permObjects(800, 300, 1200, 120, mountain));
		obj.add(new ImageV(35, 30, 200,200, houseD));
		obj.add(new ImageV(35, 30, 600,200, houseD));
		mtime = 0;
		stime = 0;
		gamestate = 0;
		ground.clear();
		for(int i = 0;i<5;i++) {
			ground.add(new Rectangle(0, 200 + 80*i, 800, 40));
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
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
		// TODO Auto-generated method stub
		if(gamestate == 0 && radioU.button[0].contains(e.getPoint()) && e.getButton()==MouseEvent.BUTTON1) {
			radioU.enterPress = true;
			s1.play(1);
			if(gcontrol.position[2]) {
				//highscores on computer
				gamestate = 3;
			}
			if(gcontrol.position[1]) {
				//instructions
				gamestate = 2;
			}
			if(gcontrol.position[0]) {
				//game
				gamestate = 1;
			}
		}
		if(radioU.button[1].contains(e.getPoint()) && e.getButton()==MouseEvent.BUTTON1) {
			radioU.exitPress = true;
			s1.play(1);
			this.reset();
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		
		if(e.getButton()==MouseEvent.BUTTON1) {
			radioU.exitPress = false;
			radioU.enterPress = false;
		}
		
	}

	@Override
	public void keyPressed(KeyEvent e) {

		//dev mode
		if(e.getKeyCode()==KeyEvent.VK_COMMA) {
			if(draw) {
				draw = false;
			}
			else {
				draw = true;
			}
		}
		
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	
	

}
