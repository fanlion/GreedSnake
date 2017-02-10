/**
 * 作者：李繁
 * 时间：2015年3月7日
 * 简介：贪吃蛇小游戏，没有实现关卡功能，不同关卡需要设置相应障碍。
 * 绘图在Jpane中绘制，利用Jpane双缓存机制解决闪屏问题。
 * 游戏中的坐标系为窗口坐标系的十分之一，即移动一个单位代表是个像素点。
 * 障碍分布信息用文本文件存储在当前目录下，共十个。
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
	static int WINDOW_WIDTH = 500;  //窗口宽度
	static int WINDOW_HEIGHT = 300; //窗口高度

	//读取文本文件中的障碍数据并返回二维数组
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
		frame.setTitle("贪吃蛇");

		JMenuBar menubar = new JMenuBar();
		JMenu level = new JMenu("关卡");
		JMenu help = new JMenu("帮助");
		JMenuItem author = new JMenuItem("作者");
		JMenuItem rule = new JMenuItem("规则");
		help.add(author);
		help.add(rule);

		//选关菜单监听器
		ActionListener levelListener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JMenuItem item = (JMenuItem) e.getSource();
				if ("第一关".equals(item.getText())) {
					frame.snake.setLevel(1);
				}
				if ("第二关".equals(item.getText())) {
					frame.snake.setLevel(2);
				}
				if ("第三关".equals(item.getText())) {
					frame.snake.setLevel(3);
				}
				if ("第四关".equals(item.getText())) {
					frame.snake.setLevel(4);
				}
				if ("第五关".equals(item.getText())) {
					frame.snake.setLevel(5);
				}
				if ("第六关".equals(item.getText())) {
					frame.snake.setLevel(6);
				}
				if ("第七关".equals(item.getText())) {
					frame.snake.setLevel(7);
				}
				if ("第八关".equals(item.getText())) {
					frame.snake.setLevel(8);
				}
				if ("第九关".equals(item.getText())) {
					frame.snake.setLevel(9);
				}
				if ("第十关".equals(item.getText())) {
					frame.snake.setLevel(10);
				}
				try {
					frame.snake.initial();
				} catch (FileNotFoundException e1) {
					e1.printStackTrace();
				}
			}
		};

		JMenuItem level1 = new JMenuItem("第一关");
		JMenuItem level2 = new JMenuItem("第二关");
		JMenuItem level3 = new JMenuItem("第三关");
		JMenuItem level4 = new JMenuItem("第四关");
		JMenuItem level5 = new JMenuItem("第五关");
		JMenuItem level6 = new JMenuItem("第六关");
		JMenuItem level7 = new JMenuItem("第七关");
		JMenuItem level8 = new JMenuItem("第八关");
		JMenuItem level9 = new JMenuItem("第九关");
		JMenuItem level10 = new JMenuItem("第十关");

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
				JOptionPane.showMessageDialog(null, "作者：李繁 \n" +
						"时间：2015年3月7日\n" +
						"简介：贪吃蛇小游戏,小游戏为新生练习。\n" +
						"联系方式：QQ310488571\n" ,
						"作者信息", JOptionPane.INFORMATION_MESSAGE);
			}
		});

		rule.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(null, "操作说明：箭头控制方向 \n" +
						"PageUp和+增加移动速度,PageDown和-减低移动速度\n" +
						"空格和P开始或暂停游戏,S和回车重新开始游戏\n" +
						"游戏说明：没有长度限制，不可穿越游戏窗口，得分为所吃苹果个数\n", 
						"帮助", JOptionPane.INFORMATION_MESSAGE);
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
				if (snake.move()) {// 未结束
					snake.repaint();
				} else {// 游戏结束
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
				snake.speedUp();// 加速
				break;
			case KeyEvent.VK_SUBTRACT:
			case KeyEvent.VK_PAGE_DOWN:
				snake.speedDown();// 减速
				break;
			case KeyEvent.VK_SPACE:
			case KeyEvent.VK_P:
				snake.changePauseState();// 暂停或继续
				break;
			default:
			}
		// 重新开始
		if (keyCode == KeyEvent.VK_S || keyCode == KeyEvent.VK_ENTER) {
			try {
				snake.initial();
			} catch (FileNotFoundException e1) {
				e1.printStackTrace();
			}
		}
	}
}
