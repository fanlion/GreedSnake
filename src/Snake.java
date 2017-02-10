import java.awt.Color;
import java.awt.Graphics;
import java.io.FileNotFoundException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Random;

import javax.swing.JPanel;

/**
 * ��ģ��
 */
class Snake extends JPanel {
	public final static int LEFT = 1;  //�ĸ�����
	public final static int UP = 2;
	public final static int RIGHT = 3;
	public final static int DOWN = 4;

	private static int NODE_WIDTH = 10;  //�����
	private static int NODE_HEIGHT= 10;  //���߶�
	private int width = 50; //���
	private int height = 30; //�߶�

	private int level = 1; //Ĭ��Ϊ��һ��
	private Node food; //ʳ��
	public boolean[][] matrix;  //ĳ������Ƿ�����ϰ���trueΪ����ͨ��(ʳ��λ��ҲΪtrue)��falseΪ��ͨ��
	private LinkedList<Node> snakeModel = new LinkedList<Node>();  //�����壬����ͷ��Ϊͷ
	private LinkedList<Node> barrier = new LinkedList<Node>();  //�ϰ���

	private boolean running = false; //��Ϸ�Ƿ����
	private int direction = UP;  //��ǰ�˶�����,Ĭ��Ϊ����
	private int score = 0; //����
	private int timeInterval = 350;// ���ʱ�䣨�ٶȣ�
	private double speedChangeRate = 0.75;// �ٶȸı�̶�
	private boolean paused = true;// ��Ϸ״̬


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

	//�ı䷽��
	public void setDirection(int direction) {
		if (this.direction % 2 != direction % 2) {// �����ͻ
			this.direction = direction;
		}
	}

	//�����ϰ�
	public void setBarrier(boolean matrix[][]) {
		for (int i = 0; i < 30; i++)
			for (int j = 0; j < 50; j++) {
				if (true == matrix[i][j]) {
					barrier.add(new Node(j, i));
				}
			}
	}

	//��Ϸ��ʼ������
	public void initial() throws FileNotFoundException {
		//��ʼ������
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
        //��ʼ������
		matrix = GreedSnake.readMatrix(fileName);
		//����ϰ���ԭ������
		barrier.clear();
		setBarrier(matrix); //�����ϰ�����ֻ����δ�޸Ĺ�matrix������³�ʼ��
		direction = 2;
		score = 0;
		timeInterval = 350;
		paused = true;
		//�����ģ����ԭ������
		snakeModel.clear();
		int x = 0, y = 0;
		for (int i = 0; i < 8; i++) { //��ʼ����,��ʼ����Ϊ8,λ����Ļ����
			x = width / 2 + i;
			y = height / 2;
			Node node = new Node(x, y);
			matrix[y][x] = true; //������λ��Ϊ�ϰ�
			snakeModel.addLast(node);
		}
		//����ʳ��
		food = createFood();
		repaint();
	}

	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

		//����ɫ������ɫ
		g.setColor(Color.LIGHT_GRAY);
		g.fillRect(0, 0, 500, 300);
 
		//��ɫ�Ʒְ�
		g.setColor(Color.WHITE);
		g.drawString("��" + String.valueOf(level) + "��", 220, 20);
		g.drawString(String.valueOf(score) + " ��", 250, 20);

		//�ڻ�ɫ�����ߵ�ͷ��
		g.setColor(Color.DARK_GRAY);
		drawNode(g, snakeModel.getFirst());

		//��ɫ��������
		g.setColor(Color.BLACK); 
		Iterator<Node> snakeModelIt = snakeModel.iterator();
		snakeModelIt.next();
		while (snakeModelIt.hasNext()) {
			drawNode(g, snakeModelIt.next());
		}
		//��ɫ����ʳ��
		g.setColor(Color.RED);
		drawNode(g, food);

		//�����ϰ�
		g.setColor(Color.MAGENTA);
		Iterator<Node> barrierIt = barrier.iterator();
		while (barrierIt.hasNext()) {
			drawNode(g, barrierIt.next());
		}
	}

	//    ���캯��      
	public Snake(boolean[][] matrix) {
		this.matrix = matrix; //�ϰ�����
		setBarrier(matrix); //�����ϰ�����ֻ����δ�޸Ĺ�matrix������³�ʼ��
		int x = 0, y = 0;
		for (int i = 0; i < 8; i++) { //��ʼ����,��ʼ����Ϊ8,λ����Ļ����
			x = width / 2 + i;
			y = height / 2;
			Node node = new Node(x, y);
			matrix[y][x] = true; //������λ��Ϊ�ϰ�
			snakeModel.addLast(node);
		}
		//����ʳ��
		food = createFood();
	}

	//���Ƶ������,��Ϸ�е�����Ϊʵ�ʻ�ͼ�����1/10������Ϸ���ƶ�һ��Ϊ10�����ص�
	private void drawNode(Graphics g, Node n) {
		g.fillRect(n.x * NODE_WIDTH, n.y * NODE_HEIGHT, NODE_WIDTH - 1, NODE_HEIGHT - 1);
	}

	//���ƶ�����
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
			if (matrix[y][x]) {// �Ե�ʳ�����ײ������ 
				//�Ե�ƻ��,����ͷ������
				if (food.x == x && food.y == y) {
					Node node = new Node(food.x, food.y);
					snakeModel.addFirst(node);
					food = createFood();
					score++; //���һ��
					return true;
				} else { //�����ϰ�
					return false;
				}
			} else { //��ͨ���ƶ�
				Node node = new Node(x, y);
				snakeModel.addFirst(node); //�����µĽ��
				matrix[y][x] = true; //�߾����ĵط�Ϊ�ϰ�

				x = snakeModel.getLast().x;
				y = snakeModel.getLast().y;
				snakeModel.removeLast(); //ɾ�����һ�����
				matrix[y][x] = false;  //���뿪�ĵط�Ϊ���ϰ�
				return true;
			}
		}
		//��Ϸʧ��
		return false;
	}

	//�������ʳ��
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

	//����
	public void speedUp() {
		if (timeInterval >= 50) //����ٶ�Ϊ50ms
			timeInterval *= speedChangeRate;
	}

	//����
	public void speedDown() {
		if (timeInterval <= 1000) //��С�ٶ�Ϊ1000ms
			timeInterval /= speedChangeRate;
	}

	//��ͣ��ʼ
	public void changePauseState() {
		paused = !paused;
	}
}