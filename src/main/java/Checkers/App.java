package Checkers;

//import org.reflections.Reflections;
//import org.reflections.scanners.Scanners;
import processing.core.PApplet;
import processing.event.MouseEvent;

import java.util.*;

public class App extends PApplet {

    public static final int CELLSIZE = 48;
    public static final int SIDEBAR = 0;
    public static final int BOARD_WIDTH = 8;

    public static final int[] BLACK_RGB = {181, 136, 99};
    public static final int[] WHITE_RGB = {240, 217, 181};
    public static final float[][][] coloursRGB = new float[][][] {
        //default - white & black
        {
                {240, 217, 181},
                {181, 136, 99}
        },
        //green
        {
                {105, 138, 76}, //when on white cell
                {105, 138, 76} //when on black cell
        },
        //blue
        {
                {196,224,232},
                {170,210,221}
        }
	};

    public static int WIDTH = CELLSIZE*BOARD_WIDTH+SIDEBAR;
    public static int HEIGHT = BOARD_WIDTH*CELLSIZE;

    public static final int FPS = 60;

    private static char currentPlayer = 'w';

    private static char[][] board = new char[8][8];
    private static int ex = 9;
    private static int ey = 9;
    private static List<int[]> availableMoves = new ArrayList<>();
    private static int enx = 9;
    private static int eny = 9;
    private static char won = 'n';
    private static int tick = 0;
    private static boolean transition = false;
    private static float k = 0;
    private static float l = 0;
    private static int ci = 9;
    private static int cj = 9;
    private static char piece;


    public App() {
        
    }

    /**
     * Initialise the setting of the window size.
    */
	@Override
    public void settings() {
        size(WIDTH, HEIGHT);
    }

	@Override
    public void setup() {
        frameRate(FPS);

        for (int i = 0; i < 8; i++){
            for (int j =0; j < 8; j++){
                if ((i+j)%2!=0 && i < 3){
                    board[i][j] = 'w';}
                else if ((i+j)%2!=0 && i > 4){
                    board[i][j] = 'b';}
                else {board[i][j] = ' ';}
            }
        }
    }
		//Set up the data structures used for storing data in the game


    /**
     * Receive key pressed signal from the keyboard.
    */
	@Override
    public void keyPressed(){

    }

    /**
     * Receive key released signal from the keyboard.
    */
	@Override
    public void keyReleased(){

    }

    @Override
    public void mousePressed(MouseEvent e) {
        //Check if the user clicked on a piece which is theirs - make sure only whoever's current turn it is, can click on pieces

        //TODO: Check if user clicked on an available move - move the selected piece there.
        //TODO: Remove captured pieces from the board
        //TODO: Check if piece should be promoted and promote it
        //TODO: Then it's the other player's turn.

        if (!transition) {
            int y = e.getX() / App.CELLSIZE;
            int x = e.getY() / App.CELLSIZE;
            if (x < 0 || x >= BOARD_WIDTH || y < 0 || y >= BOARD_WIDTH || ((x + y) % 2 == 0)) return;

            boolean flag = false;
            for (int[] p : availableMoves) {
                if (p[0] == x && p[1] == y) {
                    flag = true;
                    ci = p[2];
                    cj = p[3];
                }
            }

            if (ex == 9) {
                if (board[x][y] == currentPlayer || board[x][y] == Character.toUpperCase(currentPlayer)) {
                    ex = x;
                    ey = y;
                    findingMoves();
                }
            } else if (ex == x && ey == y) {
                ex = 9;
                ey = 9;
                findingMoves();
            } else if (board[x][y] == currentPlayer || board[x][y] == Character.toUpperCase(currentPlayer)) {
                ex = x;
                ey = y;
                findingMoves();
            } else if (flag) {
                enx = x;
                eny = y;
                transition = true;
                processMove();
            }
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        
    }

    /**
     * Draw all elements in the game by current frame. 
    */
	@Override
    public void draw() {
        this.noStroke();
        background(WHITE_RGB[0], WHITE_RGB[1], WHITE_RGB[2]);

		//draw the board

		//draw highlighted cells

		//check if the any player has no more pieces. The winner is the player who still has pieces remaining

        for (int i = 0; i < BOARD_WIDTH; i++) {
            for (int j = 0; j < BOARD_WIDTH; j++) {
                if ((i+j) % 2 == 1) {

                    int colour = 0;
                    if (currentPlayer != 'w'){
                        colour = 1;
                    }

                    boolean flag = false;
                    for (int[] p: availableMoves){
                        if (p[0]==i && p[1]==j) {
                            flag = true;
                        }
                    }

                    if (i == ex && j == ey && !transition){
                        this.setFill(1, colour);
                        this.rect(j*App.CELLSIZE, i*App.CELLSIZE, App.CELLSIZE, App.CELLSIZE);
                    }else if (flag){
                        this.setFill(2, colour);
                        this.rect(j * App.CELLSIZE, i * App.CELLSIZE, App.CELLSIZE, App.CELLSIZE);
                    }
                    else {
                        this.setFill(0, 1);
                        this.rect(j * App.CELLSIZE, i * App.CELLSIZE, App.CELLSIZE, App.CELLSIZE);
                    }

                    //int j = i2;
                    if (board[i][j] != ' '){
                        if (board[i][j] == 'w'){
                            this.fill(255);
                            this.stroke(0);
                            this.strokeWeight(5);
                            this.ellipse(j * App.CELLSIZE + App.CELLSIZE / 2, i * App.CELLSIZE + App.CELLSIZE / 2, App.CELLSIZE * 0.8f, App.CELLSIZE * 0.8f);
                            this.noStroke();
                        }else if (board[i][j] == 'b') {
                            this.fill(0);
                            this.stroke(255);
                            this.strokeWeight(5);
                            this.ellipse(j * App.CELLSIZE + App.CELLSIZE / 2, i * App.CELLSIZE + App.CELLSIZE / 2, App.CELLSIZE * 0.8f, App.CELLSIZE * 0.8f);
                            this.noStroke();
                        }else if (board[i][j] == 'W') {
                            this.fill(255);
                            this.stroke(0);
                            this.strokeWeight(4);
                            this.ellipse(j * App.CELLSIZE + App.CELLSIZE / 2, i * App.CELLSIZE + App.CELLSIZE / 2, App.CELLSIZE * 0.8f, App.CELLSIZE * 0.8f);
                            this.noStroke();

                            this.fill(255);
                            this.stroke(0);
                            this.strokeWeight(5);
                            this.ellipse(j * App.CELLSIZE + App.CELLSIZE / 2, i * App.CELLSIZE + App.CELLSIZE / 2, App.CELLSIZE * 0.45f, App.CELLSIZE * 0.45f);
                            this.noStroke();
                        }else if (board[i][j] == 'B') {
                            this.fill(0);
                            this.stroke(255);
                            this.strokeWeight(4);
                            this.ellipse(j * App.CELLSIZE + App.CELLSIZE / 2, i * App.CELLSIZE + App.CELLSIZE / 2, App.CELLSIZE * 0.8f, App.CELLSIZE * 0.8f);
                            this.noStroke();

                            this.fill(0);
                            this.stroke(255);
                            this.strokeWeight(5);
                            this.ellipse(j * App.CELLSIZE + App.CELLSIZE / 2, i * App.CELLSIZE + App.CELLSIZE / 2, App.CELLSIZE * 0.45f, App.CELLSIZE * 0.45f);
                            this.noStroke();
                        }

                    }
                }
            }
        }
        if (won == 'b'){
            fill(0);
            textSize(32);
            text("White wins!", 100, HEIGHT/2);
        } else if (won == 'w') {
            fill(0);
            textSize(32);
            text("Black wins!", 100, HEIGHT/2);
        }

        if (transition) {

            int i = ex;
            int j = ey;
            int n = 1;
            if (enx - i == 2 || i - enx == 2){n = 2;}

            if (enx > i) {
                k += n;
            } else{
                k -= n;
            }
            if (eny > j) {
                l += n;
            }else{
                l -= n;
            }
            tick++;

            if (piece == 'w'){
                this.fill(255);
                this.stroke(0);
                this.strokeWeight(5);
                this.ellipse(j * App.CELLSIZE + App.CELLSIZE / 2 + l, i * App.CELLSIZE + App.CELLSIZE / 2 + k, App.CELLSIZE * 0.8f, App.CELLSIZE * 0.8f);
                this.noStroke();
            }else if (piece == 'b') {
                this.fill(0);
                this.stroke(255);
                this.strokeWeight(5);
                this.ellipse(j * App.CELLSIZE + App.CELLSIZE / 2 + l, i * App.CELLSIZE + App.CELLSIZE / 2 + k, App.CELLSIZE * 0.8f, App.CELLSIZE * 0.8f);
                this.noStroke();
            }else if (piece == 'W') {
                this.fill(255);
                this.stroke(0);
                this.strokeWeight(4);
                this.ellipse(j * App.CELLSIZE + App.CELLSIZE / 2 + l, i * App.CELLSIZE + App.CELLSIZE / 2 + k, App.CELLSIZE * 0.8f, App.CELLSIZE * 0.8f);
                this.noStroke();

                this.fill(255);
                this.stroke(0);
                this.strokeWeight(5);
                this.ellipse(j * App.CELLSIZE + App.CELLSIZE / 2 + l, i * App.CELLSIZE + App.CELLSIZE / 2 + k, App.CELLSIZE * 0.45f, App.CELLSIZE * 0.45f);
                this.noStroke();
            }else if (piece == 'B') {
                this.fill(0);
                this.stroke(255);
                this.strokeWeight(4);
                this.ellipse(j * App.CELLSIZE + App.CELLSIZE / 2 + l, i * App.CELLSIZE + App.CELLSIZE / 2 + k, App.CELLSIZE * 0.8f, App.CELLSIZE * 0.8f);
                this.noStroke();

                this.fill(0);
                this.stroke(255);
                this.strokeWeight(5);
                this.ellipse(j * App.CELLSIZE + App.CELLSIZE / 2 + l, i * App.CELLSIZE + App.CELLSIZE / 2 + k, App.CELLSIZE * 0.45f, App.CELLSIZE * 0.45f);
                this.noStroke();
            }
            if (tick >= CELLSIZE) {
                k = l = tick = 0;
                transition = false;
                processMove();
            }
        }
    }
	
	/**
     * Set fill colour for cell background
     * @param colourCode The colour to set
     * @param blackOrWhite Depending on if 0 (white) or 1 (black) then the cell may have different shades
     */
	public void setFill(int colourCode, int blackOrWhite) {
		this.fill(coloursRGB[colourCode][blackOrWhite][0], coloursRGB[colourCode][blackOrWhite][1], coloursRGB[colourCode][blackOrWhite][2]);
	}


    public void findingMoves(){

        availableMoves =  new ArrayList<>();
        if (ex != 9){
            for (int i = 0; i < BOARD_WIDTH; i++) {
                for (int j = 0; j < BOARD_WIDTH; j++) {
                    if ((i+j) % 2 == 1 && board[i][j] == ' ') {

                        char z = currentPlayer;
                        int ck = 9;
                        int cl = 9;
                        boolean flag = true;

                        if (board[ex][ey] == 'w') {
                            if (ex > i) {
                                flag = false;
                            }
                        }
                        if (board[ex][ey] == 'b') {
                            if (ex < i) {
                                flag = false;
                            }
                        }

                        if ((ex + 1 == i || ex - 1 == i) && (ey + 1 == j || ey - 1 == j)) {

                        } else if ((ex + 2 == i || ex - 2 == i) && (ey + 2 == j || ey - 2 == j)) {

                            if (ex > i && ey > j && board[ex - 1][ey - 1] != ' ') {
                                if (board[ex -1][ey-1] != z && board[ex -1][ey-1] != Character.toUpperCase(z)){
                                    ck = ex -1;
                                    cl = ey-1;
                                }
                            } else if (ex > i && ey < j && board[ex - 1][ey + 1] != ' ') {
                                if (board[ex -1][ey+1] != z && board[ex -1][ey+1] != Character.toUpperCase(z)) {
                                    ck = ex - 1;
                                    cl = ey + 1;
                                }
                            } else if (ex < i && ey > j && board[ex + 1][ey - 1] != ' ') {
                                if (board[ex +1][ey-1] != z && board[ex +1][ey-1] != Character.toUpperCase(z)){
                                        ck = ex +1;
                                        cl = ey-1;
                                }
                            } else if (ex < i && ey < j && board[ex + 1][ey + 1] != ' ') {
                                if (board[ex +1][ey+1] != z && board[ex +1][ey+1] != Character.toUpperCase(z)){
                                        ck = ex +1;
                                        cl = ey+1;
                                }
                            }else {
                                flag = false;
                            }
                        } else {
                            flag = false;
                        }

                        if (flag){
                            availableMoves.add(new int[]{i,j,ck,cl});
                        }
                    }
                }
            }
        }
    }

    public void processMove(){

        if (transition){
            piece = board[ex][ey];
            board[ex][ey] = ' ';
            availableMoves =  new ArrayList<>();
            
        } else {
            ex = ey = 9;
            board[enx][eny] = piece;
            if (ci != 9) {
                board[ci][cj] = ' ';
            }

            if (enx == 7 && board[enx][eny] == 'w') {
                board[enx][eny] = 'W';
            } else if (enx == 0 && board[enx][eny] == 'b') {
                board[enx][eny] = 'B';
            }

            if (currentPlayer == 'w') {
                currentPlayer = 'b';
            } else {
                currentPlayer = 'w';
            }
            isGameOver();
        }
    }


    private static void isGameOver() {
        boolean flag = false;
        for (char[] cc: board){
            for (char j : cc){
                if (j == currentPlayer || j == Character.toUpperCase(currentPlayer)){flag = true;}
            }
        }
        if (!flag){
            won = currentPlayer;
        }
    }
    

    public static void main(String[] args) {
        PApplet.main("Checkers.App");
    }


}