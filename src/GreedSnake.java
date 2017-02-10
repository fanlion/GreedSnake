/**
 * ���ߣ��
 * ʱ�䣺2015��3��7��
 * ��飺̰����С��Ϸ��û��ʵ�ֹؿ����ܣ���ͬ�ؿ���Ҫ������Ӧ�ϰ���
 * ��ͼ��Jpane�л��ƣ�����Jpane˫������ƽ���������⡣
 * ��Ϸ�е�����ϵΪ��������ϵ��ʮ��֮һ�����ƶ�һ����λ�����Ǹ����ص㡣
 * �ϰ��ֲ���Ϣ���ı��ļ��洢�ڵ�ǰĿ¼�£���ʮ����
 */
import java.awt.*;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

public class GreedSnake extends JFrame implements Runnable, KeyListener {
	static int WINDOW_WIDTH = 500;  //���ڿ��
	static int WINDOW_HEIGHT = 300; //���ڸ߶�

	//��ȡ�ı��ļ��е��ϰ����ݲ����ض�ά����
	public static boolean[][] readMatrix(String fileName) throws FileNotFoundException {
		boolean[][] matrix = new boolean[30][50];
		ArrayList<Integer> list = new ArrayList<Integer>();
		File file = new File(fileName);
		Scanner input = new Scanner(file);
		while(input.hasNext()) {
			list.add(input.nextInt());
		}
		
		for (int i = 0; i < 1500; i++) {
			int row = i / 50;
			int col = i % 50;
			int value = list.get(i);

			if (value == 0) 
				matrix[row][col] = false;
			else
				matrix[row][col] = true;
		}
		input.close();
		return matrix;
	}

	private Snake snake;

	public GreedSnake() throws FileNotFoundException {
		snake = new Snake(GreedSnake.readMatrix("matrix1.txt"));
		add(snake);
	}

	public static void main(String[] args) throws FileNotFoundException {
		final GreedSnake frame = new GreedSnake();
		Thread thread = new Thread(frame);
		frame.setResizable(false);
		frame.setVisible(true);
		frame.setPreferredSize(new Dimension(505, 352));
		frame.pack();
		frame.setTitle("̰����");

		JMenuBar menubar = new JMenuBar();
		JMenu level = new JMenu("�ؿ�");
		JMenu help = new JMenu("����");
		JMenuItem author = new JMenuItem("����");
		JMenuItem rule = new JMenuItem("����");
		help.add(author);
		help.add(rule);

		//ѡ�ز˵�������
		ActionListener levelListener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JMenuItem item = (JMenuItem) e.getSource();
				if ("��һ��".equals(item.getText())) {
					frame.snake.setLevel(1);
				}
				if ("�ڶ���".equals(item.getText())) {
					frame.snake.setLevel(2);
				}
				if ("������".equals(item.getText())) {
					frame.snake.setLevel(3);
				}
				if ("���Ĺ�".equals(item.getText())) {
					frame.snake.setLevel(4);
				}
				if ("�����".equals(item.getText())) {
					frame.snake.setLevel(5);
				}
				if ("������".equals(item.getText())) {
					frame.snake.setLevel(6);
				}
				if ("���߹�".equals(item.getText())) {
					frame.snake.setLevel(7);
				}
				if ("�ڰ˹�".equals(item.getText())) {
					frame.snake.setLevel(8);
				}
				if ("�ھŹ�".equals(item.getText())) {
					frame.snake.setLevel(9);
				}
				if ("��ʮ��".equals(item.getText())) {
					frame.snake.setLevel(10);
				}
				try {
					frame.snake.initial();
				} catch (FileNotFoundException e1) {
					e1.printStackTrace();
				}
			}
		};

		JMenuItem level1 = new JMenuItem("��һ��");
		JMenuItem level2 = new JMenuItem("�ڶ���");
		JMenuItem level3 = new JMenuItem("������");
		JMenuItem level4 = new JMenuItem("���Ĺ�");
		JMenuItem level5 = new JMenuItem("�����");
		JMenuItem level6 = new JMenuItem("������");
		JMenuItem level7 = new JMenuItem("���߹�");
		JMenuItem level8 = new JMenuItem("�ڰ˹�");
		JMenuItem level9 = new JMenuItem("�ھŹ�");
		JMenuItem level10 = new JMenuItem("��ʮ��");

		level1.addActionListener(levelListener);
		level2.addActionListener(levelListener);
		level3.addActionListener(levelListener);
		level4.addActionListener(levelListener);
		level5.addActionListener(levelListener);
		level6.addActionListener(levelListener);
		level7.addActionListener(levelListener);
		level8.addActionListener(levelListener);
		level9.addActionListener(levelListener);
		level10.addActionListener(levelListener);

		level.add(level1);
		level.add(level2);
		level.add(level3);
		level.add(level4);
		level.add(level5);
		level.add(level6);
		level.add(level7);
		level.add(level8);
		level.add(level9);
		level.add(level10);

		menubar.add(level);
		menubar.add(help);
		frame.add(menubar, BorderLayout.NORTH);

		author.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(null, "���ߣ�� \n" +
						"ʱ�䣺2015��3��7��\n" +
						"��飺̰����С��Ϸ,С��ϷΪ������ϰ��\n" +
						"��ϵ��ʽ��QQ310488571\n" ,
						"������Ϣ", JOptionPane.INFORMATION_MESSAGE);
			}
		});

		rule.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(null, "����˵������ͷ���Ʒ��� \n" +
						"PageUp��+�����ƶ��ٶ�,PageDown��-�����ƶ��ٶ�\n" +
						"�ո��P��ʼ����ͣ��Ϸ,S�ͻس����¿�ʼ��Ϸ\n" +
						"��Ϸ˵����û�г������ƣ����ɴ�Խ��Ϸ���ڣ��÷�Ϊ����ƻ������\n", 
						"����", JOptionPane.INFORMATION_MESSAGE);
			}
		});

		frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
		frame.addKeyListener(frame);
		thread.start();
	}

	@Override
	public void run() {
		snake.setRunning(true);
		while (snake.getRunning()) {
			try {
				Thread.sleep(snake.getTimeInterval());
			} catch (Exception e) {
				break;
			}
			if (!snake.getPaused()) {
				if (snake.move()) {// δ����
					snake.repaint();
				} else {// ��Ϸ����
					JOptionPane.showMessageDialog(null, "GAME  OVER", 
							"Game Over", JOptionPane.INFORMATION_MESSAGE);
					snake.setPaused(true);
				}
			}
		}
	}


	@Override
	public void keyTyped(KeyEvent e) {
	}
	@Override
	public void keyReleased(KeyEvent e) {
	}

	@Override
	public void keyPressed(KeyEvent e) {
		int keyCode = e.getKeyCode();
		if (snake.getRunning())
			switch (keyCode) {
			case KeyEvent.VK_UP:
				snake.setDirection(Snake.UP);
				break;
			case KeyEvent.VK_DOWN:
				snake.setDirection(Snake.DOWN);
				break;
			case KeyEvent.VK_LEFT:
				snake.setDirection(Snake.LEFT);
				break;
			case KeyEvent.VK_RIGHT:
				snake.setDirection(Snake.RIGHT);
				break;
			case KeyEvent.VK_ADD:
			case KeyEvent.VK_PAGE_UP:
				snake.speedUp();// ����
				break;
			case KeyEvent.VK_SUBTRACT:
			case KeyEvent.VK_PAGE_DOWN:
				snake.speedDown();// ����
				break;
			case KeyEvent.VK_SPACE:
			case KeyEvent.VK_P:
				snake.changePauseState();// ��ͣ�����
				break;
			default:
			}
		// ���¿�ʼ
		if (keyCode == KeyEvent.VK_S || keyCode == KeyEvent.VK_ENTER) {
			try {
				snake.initial();
			} catch (FileNotFoundException e1) {
				e1.printStackTrace();
			}
		}
	}
}
