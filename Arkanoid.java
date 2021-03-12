//RANA AL-KHULAIDI
//ICS4U
//JANUARY 24 2020
//this program mimics the game Arkanoid and includes different features such as increased speeds and extra lives
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.awt.image.*;
import java.io.*;
import javax.imageio.*;
import java.util.ArrayList;
import java.awt.Rectangle.*;
import javax.sound.midi.*;
//this class sets up panels, music, start screen
public class Arkanoid extends JFrame implements ActionListener{
	private JLayeredPane layeredPane=new JLayeredPane(); //allows for start screen
	private int round; //counter
	Timer myTimer;
	GamePanel game;
	private static Sequencer midiPlayer; //music player

	//this function allows for the playing of music
	public static void startMidi(String midFilename) {
      try {
         File midiFile = new File(midFilename);
         Sequence song = MidiSystem.getSequence(midiFile);
         midiPlayer = MidiSystem.getSequencer();
         midiPlayer.open();
         midiPlayer.setSequence(song);
         midiPlayer.setLoopCount(10); // repeat 10 times
         midiPlayer.start();
      } catch (MidiUnavailableException e) {
         e.printStackTrace();
      } catch (InvalidMidiDataException e) {
         e.printStackTrace();
      } catch (IOException e) {
         e.printStackTrace();
      }
   	}

    public Arkanoid() {
		super("ARKANOID :)");

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(600,750);

		startMidi("Mushrooms.mid");

		myTimer = new Timer(20, this);
		round=1; //init rounds counter

		game = new GamePanel(this);
		setLayout(new BorderLayout());

		//image for start screen
		ImageIcon backPic = new ImageIcon("startscreen.jpg");
		JLabel back = new JLabel(backPic);
		back.setBounds(0, 0,600,750);
		layeredPane.add(back,1); //adds it to a layer rather than the panel itself
		//setContentPane(layeredPane);

		add(game);
		game.blockMaker();
		setResizable(false);
		setVisible(true);

    }
	public void start(){
		myTimer.start();
	}

	public void actionPerformed(ActionEvent evt){
		//allows the start screen to only be displayed once
		if(round==1){
			//game.delay(1000); //allows time for person to see start screen
		}
			if(game!= null && game.ready==true ){
					game.move();
					game.repaint();
					round++; //counter increases per round
			}
	}
    public static void main(String[] arguments) {
		Arkanoid frame = new Arkanoid();
    }
}
//this class displays all objects and does the logic
 class GamePanel extends JPanel {
	//objects
	private Bar bar;
	private Ball ball;
	//images
	private Image back;
	private Image title;
	private Image lifeBar;
	private Image gameOverPic;
	private Image gameOverSc;
	private Image backg,titlepic,lifeBars;
	//booleans
	private boolean []keys;
	private boolean gameScreenDone;
	private boolean score1000;
	private boolean score550;
	private boolean gameOver;
	public boolean ready=true;
	private Arkanoid mainFrame;
	private static ArrayList <Block> blocks=new ArrayList<Block>();
	//ints
	private int blocksHit;
	private int score;
	public int highscore;
	private int lives;
	//fonts
	Font fontLocal=null, fontSys=null,fontSys2=null,fontSys3=null;

	//this method creates blocks with their sizes and positions
	public void blockMaker(){
		for (int y=0;y<=5;y++){
			for (int x=0;x<=12;x++){
			blocks.add(new Block(x*45+4,y*27+130,44,25));
			}
		}
	}

	public GamePanel(Arkanoid m){
		//objects
		bar=new Bar(260,625,80,17);
		ball=new Ball();

		keys = new boolean[KeyEvent.KEY_LAST+1];
		mainFrame = m;
		blocksHit=0;
		highscore=0;
		lives=3;

		//booleans
		gameScreenDone=false;
		gameOver=false;
		score1000=false;
		score550=false;
		addKeyListener(new moveListener());
		fontSys = new Font("Comic Sans MS",Font.PLAIN,20);
		fontSys2 = new Font("Comic Sans MS",Font.PLAIN,35);
		fontSys3 = new Font("Comic Sans MS",Font.PLAIN,50);

		//loads images
		try {
    		back = ImageIO.read(new File("arkanoidbg.png"));
    		title = ImageIO.read(new File("titleimage.png"));
    		lifeBar = ImageIO.read(new File("arkbar.png"));
			gameOverPic=ImageIO.read(new File("gameover.png"));
		}
		catch (IOException e) {
		}
	 	 backg=back.getScaledInstance(600,725, Image.SCALE_DEFAULT);
	 	 titlepic=title.getScaledInstance(180,60, Image.SCALE_DEFAULT);
	 	 lifeBars=lifeBar.getScaledInstance(40,10, Image.SCALE_DEFAULT);
		 gameOverSc=gameOverPic.getScaledInstance(600,750, Image.SCALE_DEFAULT);
	}

	//this method creates a delay
	public static void delay (long len){
		try	{
		    Thread.sleep (len);
		}
		catch (InterruptedException ex)	{
		}
    }

	public void addNotify() {
        super.addNotify();
        requestFocus();
        mainFrame.start();
        ready = true;
    }

	//this method draws all the objects
	public void paintComponent(Graphics g){
		//sets background
	 	g.setColor(new Color(36,37,50));
        g.fillRect(0,0,getWidth(),getHeight());
        g.drawImage(backg,0,75,this);
        g.drawImage(titlepic,210,7,this);
        //draws objects
        bar.draw(g);
        ball.draw(g);
		//all the blocks being drawn here
        for(int x=0;x<lives-1;x++){
        	g.drawImage(lifeBars,50*x+20,680,this);
        }
        for(int x=0;x<13;x++){
        	g.setColor(new Color(216,216,216));
        	g.fillRect(blocks.get(x).getPosX(),blocks.get(x).getPosY(),44,25);
        }
        for(int x=13;x<26;x++){
        	g.setColor(new Color(247,16,16));
        	g.fillRect(blocks.get(x).getPosX(),blocks.get(x).getPosY(),44,25);
        }
        for(int x=26;x<39;x++){
        	g.setColor(new Color(243,243,14));
        	g.fillRect(blocks.get(x).getPosX(),blocks.get(x).getPosY(),44,25);
        }
        for(int x=39;x<52;x++){
        	g.setColor(new Color(0,124,255));
        	g.fillRect(blocks.get(x).getPosX(),blocks.get(x).getPosY(),44,25);
        }
        for(int x=52;x<65;x++){
        	g.setColor(new Color(209,0,255));
        	g.fillRect(blocks.get(x).getPosX(),blocks.get(x).getPosY(),44,25);
        }
        for(int x=65;x<78;x++){
        	g.setColor(new Color(89,255,0));
        	g.fillRect(blocks.get(x).getPosX(),blocks.get(x).getPosY(),44,25);
        }
        //prepares font
        g.setColor(new Color(227, 232, 255));
        g.setFont(fontSys);
        //draws scores on panel
		g.drawString("SCORE",70,35);
		g.drawString(String.valueOf(score),88,55);
		g.drawString("HIGHSCORE",425,35);
		g.drawString(String.valueOf(highscore),465,55);
		//displays game over picture and removes all other possible writing
		if(gameOver==true){
			score1000=false; //disables features
			score550=false;
			g.setColor(new Color(255, 209, 213));
			g.setFont(fontSys2);
			g.drawImage(gameOverSc,0,0,this);
			g.drawString("SCORE: "+String.valueOf(score),175,330); //final score
			gameScreenDone=true; //causes program to wait for the game over display before starting new game
		}
		//displays the fact that you got an additional life
		if(score1000==true){
			g.setFont(fontSys3);
			g.setColor(new Color(255, 209, 213));
			g.drawString(" +1 LIFE ",175,450);
		}
		//displays the fact that the speed is halved
		if(score550==true){
			g.setFont(fontSys3);
			g.setColor(new Color(255, 209, 213));
			g.drawString(" x0.5 SPEED ",130,520);
		}
	}
	//this method does all the logic for the moving objects
	public void move(){
		if(keys[KeyEvent.VK_RIGHT]){
			bar.move(8);
		}
		if(keys[KeyEvent.VK_LEFT]){
			bar.move(-8);
		}
		if(keys[KeyEvent.VK_SPACE]) {
		//displays option panel that pauses trhe game
	 	 	String options[] = {"Play","Exit"};
    		int answer = JOptionPane.showOptionDialog(null,"Continue playing?", "Play?", JOptionPane.DEFAULT_OPTION,
        	JOptionPane.QUESTION_MESSAGE, null, options, options[1]);
        	//shows that the key is no longer pressed
        	keys[KeyEvent.VK_SPACE] = false;
    		if (answer == JOptionPane.YES_OPTION){
    			return;
			}
    		else{
    			System.exit(0);
    		}
		}
		ball.move();
		//checks if ball hits bar
		if (bar.ballCollision(ball.getPosX(),ball.getPosY(),ball.getRadius())){
			ball.barCollision(); //creates bounce
		}
		//checks if any blocks were hit
		for(int x=0;x<blocks.size();x++){
			if(ball.blockCollision(blocks.get(x).getPosX(),blocks.get(x).getPosY(),blocks.get(x).getWidth(),blocks.get(x).getHeight())){
				blocks.set(x, new Block(800,800,0,0));
				ball.setDirY(-1); //causes ball to bounce in other direction
				ball.setDirX(-1);
				blocksHit+=1; //counts the blocks hit
				if(blocksHit==78){
					gameOver=true; //ends game if all blocks are hit
				}
				score+=50; //50 points per block
				if(score>highscore){ //counts the highscore
					highscore=score;
				}
				//adds life if score is a multiple of 1000
				if (score%1000==0 && score>0 ){
					scoreLifeAdd();
				}
				//stops displaying the "+1 life" string
				if (score%1200==0&& score>0){
					score1000=false;
				}
				//slows speed if score is a multiple of 550
				if (score%550==0 && score>0 ){
					ball.setSpeed(5);
					score550=true;
				}
				//returns speed when score is a multiple of 700
				if (score%700==0&& score>0){
					ball.setSpeed(10);
					//stops displaying "0.5 speed" string
					score550=false;
				}
			}
		}
		//checks if ball fell and restarts positions
		if (ball.minusLife()){
			lives-=1;
			ball=new Ball();
			bar=new Bar(260,625,80,17);
			if(lives==0){
				gameOver=true;
				//ends game if all lives are lost
			}
		}
		//restarts game
		if (gameOver==true && gameScreenDone==true){ //ensures game over screen was already displayed
	 	 	ready=false; //freezes everything
	 	 	String options[] = {"Play", "Exit"}; //options for pane
    		int answer = JOptionPane.showOptionDialog(null,"Do you want to play again?", "Play again?", JOptionPane.DEFAULT_OPTION,
        	JOptionPane.WARNING_MESSAGE, null, options, options[1]);
    		if (answer == JOptionPane.YES_OPTION){
    			//restarts all objects and variables
    			gameOver=false;
    			gameScreenDone=false;
    			blocks.clear();
    			blockMaker();
    			lives=3;
    			score=0;
				ready=true;
				move();
				repaint();
    		}
    		else{
    			System.exit(0);
    			//directly exits
    		}
	 	}
	}

    class moveListener implements KeyListener{
	    public void keyTyped(KeyEvent e) {}

	    public void keyPressed(KeyEvent e) {
	        keys[e.getKeyCode()] = true;
	    }
	    public void keyReleased(KeyEvent e) {
	        keys[e.getKeyCode()] = false;
	    }
    }

	//this method adds lives not exceeding 5
	public void scoreLifeAdd(){
		score1000=true;
		if(lives<5){
			lives+=1;
		}
	}
}
//this class holds all the bar's info
class Bar{
	private Image barBig;
	private int posX,posY,width,height;
	private Image barPic;
	//constructor takes all that is needed for a rectangle
	public Bar(int posX,int posY,int width,int height){
		this.posX=posX;
		this.posY=posY;
		this.width=width;
		this.height=height;
		try {
    		barBig = ImageIO.read(new File("arkbar.png"));
		}
		catch (IOException e) {
		}
	}
	//moves bar
	public void move(int dx){
		if(posX<495 && dx>0){
			posX += dx;
		}
		else if(posX>5 && dx<0){
			posX += dx;
		}
	}
	//creates a rect object and uses contain method to see if ball hits bar
	public boolean ballCollision(int pX,int pY,int radius){
		Rectangle barRect=new Rectangle(posX-radius,posY,width+radius,height);
		//includes the area around the circle
		if ((barRect.contains(pX-radius,pY-radius))||(barRect.contains(pX+radius,pY+radius))){
			return true;
		}
		else{
			return false;
		}
	}
	//this method draws the bar
	public void draw(Graphics g){
		barPic=barBig.getScaledInstance(80,17, Image.SCALE_DEFAULT);
        g.drawImage(barPic,posX,posY,null);
	}
	//setter/getter
	public int getWidth(){
		return width;
	}
	public int getHeight(){
		return height;
	}
	public int getPosX(){
		return posX;
	}
	public int getPosY(){
		return posY;
	}
}
//this class hold the blocks info
class Block{
	private int posX,posY,width,height;
	private boolean notHit;
	//constructors needed to create rect object
	public Block(int posX,int posY,int width,int height){
		this.posX=posX;
		this.posY=posY;
		this.width=width;
		this.height=height;
	}
	public int getPosX(){
		return posX;
	}
	public int getPosY(){
		return posY;
	}
	public int getWidth(){
		return width;
	}
	public int getHeight(){
		return height;
	}
}
//this method holds the ball's info and checks collisions
class Ball{
	private int posX,posY;
	private int dirX, dirY;
	private final int MIDX=293;
	private final int MIDY=607;
	private int speed=7;
	private int ballRadius=12;
	private final int SCWIDTH=600;
	private final int SCHEIGHT=750;
	public Ball(){
    	this.posX = MIDX;//starting pos
    	this.posY = MIDY;
   		this.dirX = 0; //no horizontal movement
    	this.dirY = 1; //up
  	}
  	public void draw(Graphics g) {
    	g.setColor(new Color(255,255,255));
    	g.fillOval(posX, posY,ballRadius,ballRadius);
  	}
	public void move() {
		//moves ball with direction
    	posX = speed * dirX+ posX;
    	posY = speed * dirY + posY;

		//wall collisions
		//right
		if (posX+ballRadius>= SCWIDTH) {
	      dirX*= -1;
	    }
	    //left
	    if (posX-ballRadius<= 0) {
	      dirX*= -1;
	    }
	    //top
	    if (posY-ballRadius<= 75) {
	      dirY*= -1;
	      dirX=randint(-1,1); //random direction
	    }
	}
	//this method causes ball to bounce off of bar
	public void barCollision(){
		dirY*= -1;
		dirX=randint(-1,1);
	}

	//this method produces random integers
	public static int randint(int low, int high){
		return (int)(Math.random()*(high-low+1)+low);
	}

	//this method checks if part of the ball is touching a block
	public boolean blockCollision(int pX,int pY,int width,int height){
		Rectangle brick=new Rectangle(pX,pY,width,height);
		if ((brick.contains(posX+ballRadius,posY+ballRadius)) || (brick.contains(posX-ballRadius,posY-ballRadius))){
			return true;
		}
		else{
			return false;
		}
	}

	//removes life if ball goes below bars
	public boolean minusLife(){
		if(posY>625){
			return true;
		}
		else{
			return false;
		}
	}

	public int getPosX(){
		return posX;
	}
	public int getPosY(){
		return posY;
	}
	public int getRadius(){
		return ballRadius;
	}
	public void setDirY(int dir){
		dirY*=dir;
	}
	public void setDirX(int dir){
		dirX=dir;
	}
	public void setSpeed(int num){
		speed=num;
	}
	public void setRadius(int num){
		ballRadius=num;
	}

}
