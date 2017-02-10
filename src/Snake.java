import java.awt.Color;
import java.awt.Graphics;
import java.io.FileNotFoundException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Random;

import javax.swing.JPanel;

/**
 * 蛇模型
 */
class Snake extends JPanel {
	public final static int LEFT = 1;  //四个方向
	public final static int UP = 2;
	public final static int RIGHT = 3;
	public final static int DOWN = 4;

	private static int NODE_WIDTH = 10;  //结点宽度
	private static int NODE_HEIGHT= 10;  //结点高度
	private int width = 50; //宽度
	private int height = 30; //高度

	private int level = 1; //默认为第一关
	private Node food; //食物
	public boolean[][] matrix;  //某坐标点是否存在障碍，true为不可通行(食物位置也为true)，false为可通行
	private LinkedList<Node> snakeModel = new LinkedList<Node>();  //蛇身体，链表头部为头
	private LinkedList<Node> barrier = new LinkedList<Node>();  //障碍物

	private boolean running = false; //游戏是否结束
	private int direction = UP;  //当前运动方向,默认为向上
	private int score = 0; //分数
	private int timeInterval = 350;// 间隔时间（速度）
	private double speedChangeRate = 0.75;// 速度改变程度
	private boolean paused = true;// 游戏状态


	public void setLevel(int level) {
		this.level = level;
	}
	public int getLevel() {
		return level;
	}
	public void setRunning(boolean state) {
		running = state;
	}

	public boolean getRunning() {
		return running;
	}

	public void setTimeInterval(int timeInterval) {
		this.timeInterval = timeInterval;
	}

	public int getTimeInterval() {
		return timeInterval;
	}

	public void setPaused(boolean state) {
		this.paused = state;
	}

	public boolean getPaused() {
		return paused;
	}

	//改变方向
	public void setDirection(int direction) {
		if (this.direction % 2 != direction % 2) {// 避免冲突
			this.direction = direction;
		}
	}

	//设置障碍
	public void setBarrier(boolean matrix[][]) {
		for (int i = 0; i < 30; i++)
			for (int j = 0; j < 50; j++) {
				if (true == matrix[i][j]) {
					barrier.add(new Node(j, i));
				}
			}
	}

	//游戏初始化函数
	public void initial() throws FileNotFoundException {
		//初始化矩阵
		String fileName;
		switch(level) {
		case 1:
			fileName = "matrix1.txt";
			break;
		case 2:
			fileName = "matrix2.txt";
			break;
		case 3:
			fileName = "matrix3.txt";
			break;
		case 4:
			fileName = "matrix4.txt";
			break;
		case 5:
			fileName = "matrix5.txt";
			break;
		case 6:
			fileName = "matrix6.txt";
			break;
		case 7:
			fileName = "matrix7.txt";
			break;
		case 8:
			fileName = "matrix8.txt";
			break;
		case 9:
			fileName = "matrix9.txt";
			break;
		case 10:
			fileName = "matrix10.txt";
			break;
		default: fileName = "matrix1.txt";
		} 
        //初始化矩阵
		matrix = GreedSnake.readMatrix(fileName);
		//清除障碍中原来数据
		barrier.clear();
		setBarrier(matrix); //设置障碍链表，只能在未修改过matrix的情况下初始化
		direction = 2;
		score = 0;
		timeInterval = 350;
		paused = true;
		//清除蛇模型中原来数据
		snakeModel.clear();
		int x = 0, y = 0;
		for (int i = 0; i < 8; i++) { //初始化蛇,初始长度为8,位于屏幕中央
			x = width / 2 + i;
			y = height / 2;
			Node node = new Node(x, y);
			matrix[y][x] = true; //蛇所在位置为障碍
			snakeModel.addLast(node);
		}
		//产生食物
		food = createFood();
		repaint();
	}

	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

		//亮灰色背景颜色
		g.setColor(Color.LIGHT_GRAY);
		g.fillRect(0, 0, 500, 300);
 
		//白色计分板
		g.setColor(Color.WHITE);
		g.drawString("第" + String.valueOf(level) + "关", 220, 20);
		g.drawString(String.valueOf(score) + " 分", 250, 20);

		//黑灰色绘制蛇的头部
		g.setColor(Color.DARK_GRAY);
		drawNode(g, snakeModel.getFirst());

		//黑色绘制身体
		g.setColor(Color.BLACK); 
		Iterator<Node> snakeModelIt = snakeModel.iterator();
		snakeModelIt.next();
		while (snakeModelIt.hasNext()) {
			drawNode(g, snakeModelIt.next());
		}
		//红色绘制食物
		g.setColor(Color.RED);
		drawNode(g, food);

		//绘制障碍
		g.setColor(Color.MAGENTA);
		Iterator<Node> barrierIt = barrier.iterator();
		while (barrierIt.hasNext()) {
			drawNode(g, barrierIt.next());
		}
	}

	//    构造函数      
	public Snake(boolean[][] matrix) {
		this.matrix = matrix; //障碍矩阵
		setBarrier(matrix); //设置障碍链表，只能在未修改过matrix的情况下初始化
		int x = 0, y = 0;
		for (int i = 0; i < 8; i++) { //初始化蛇,初始长度为8,位于屏幕中央
			x = width / 2 + i;
			y = height / 2;
			Node node = new Node(x, y);
			matrix[y][x] = true; //蛇所在位置为障碍
			snakeModel.addLast(node);
		}
		//产生食物
		food = createFood();
	}

	//绘制单个结点,游戏中的坐标为实际绘图坐标的1/10，即游戏中移动一步为10个像素点
	private void drawNode(Graphics g, Node n) {
		g.fillRect(n.x * NODE_WIDTH, n.y * NODE_HEIGHT, NODE_WIDTH - 1, NODE_HEIGHT - 1);
	}

	//蛇移动函数
	public boolean move() {
		int x = snakeModel.getFirst().x;
		int y = snakeModel.getFirst().y;
		switch(direction) {
		case LEFT :
			x--;
			break;
		case UP :
			y--;
			break;
		case RIGHT :
			x++;
			break;
		case DOWN :
			y++;
			break;
		}

		if ((x >= 0 && x < width) && (y >= 0 && y < height)) {
			if (matrix[y][x]) {// 吃到食物或者撞到身体 
				//吃到苹果,增加头部长度
				if (food.x == x && food.y == y) {
					Node node = new Node(food.x, food.y);
					snakeModel.addFirst(node);
					food = createFood();
					score++; //获得一分
					return true;
				} else { //碰到障碍
					return false;
				}
			} else { //普通的移动
				Node node = new Node(x, y);
				snakeModel.addFirst(node); //插入新的结点
				matrix[y][x] = true; //蛇经过的地方为障碍

				x = snakeModel.getLast().x;
				y = snakeModel.getLast().y;
				snakeModel.removeLast(); //删除最后一个结点
				matrix[y][x] = false;  //蛇离开的地方为非障碍
				return true;
			}
		}
		//游戏失败
		return false;
	}

	//随机产生食物
	private Node createFood() {
		Random random = new Random();
		int x, y;
		do {
			x = random.nextInt(width);
			y = random.nextInt(height);
		} while(matrix[y][x]);
		matrix[y][x] = true; 
		return new Node(x, y);
	}

	//加速
	public void speedUp() {
		if (timeInterval >= 50) //最大速度为50ms
			timeInterval *= speedChangeRate;
	}

	//减速
	public void speedDown() {
		if (timeInterval <= 1000) //最小速度为1000ms
			timeInterval /= speedChangeRate;
	}

	//暂停或开始
	public void changePauseState() {
		paused = !paused;
	}
}