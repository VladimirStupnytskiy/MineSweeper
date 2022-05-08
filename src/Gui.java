import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.time.Duration;
import java.util.ArrayList;

/**
 * Created by Vladimir on 04.02.2016.
 */
public class Gui {
    private JFrame jFrame = new JFrame();
    private JPanel jPanel = new JPanel();
    private JButton startGame;
    private JLabel numberOfBombs;
    private JLabel width;
    private JLabel height;
    private JTextField numberOfBombsValue;
    private JTextField widthValue;
    private JTextField heightValue;
    private JPanel gamePanel;
    private JPanel optionPanel;
    private JTextField bombNumber;
    private JLabel record;



    private int resBomb = 0;
    private int bombNumb = 0;
    final String x = "x";
    int h;
    int w;
    int i;
    int j;
    int k;
    int m;
    long minutes;
    long seconds;
    long millis;
    ImageIcon bomb = new ImageIcon("img\\bomb.PNG");
    Icon boom = new ImageIcon("img\\boom.PNG");
    Icon flag = new ImageIcon("img\\flag.PNG");
    Icon clock = new ImageIcon("img\\clock.PNG");
    Icon smile = new ImageIcon("img\\smile2.PNG");
    BattleField battleField;
    private Cell[][] cells;
    private ArrayList<Cell> bombs;
    MouseListener mouseListener;
    private long lastTickTime;
    private Timer timer;

    public Gui() {
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jFrame.getContentPane().add(jPanel);
        jFrame.setSize(220, 220);
        jFrame.setVisible(true);
        jFrame.setLocationRelativeTo(null);
        jFrame.setTitle("Minesweeper");
        jFrame.setIconImage(bomb.getImage());
        initilizationStartWindow();
    }
    public void start() {
        JPanel p1 = new JPanel();
        jPanel.setLayout(new FlowLayout());
        p1.add(numberOfBombs);
        p1.add(numberOfBombsValue);
        jPanel.add(p1);

        JPanel p2 = new JPanel();
        p2.add(width);
        p2.add(widthValue);
        jPanel.add(p2);

        JPanel p3 = new JPanel();
        p3.add(height);
        p3.add(heightValue);
        jPanel.add(p3);
        jPanel.add(startGame);
        jPanel.revalidate();

        startGame.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                h = Integer.parseInt(heightValue.getText());
                w = Integer.parseInt(widthValue.getText());
                battleField = new BattleField(h, w);
                resBomb = Integer.parseInt(numberOfBombsValue.getText());
                bombNumb = resBomb;
                battleField.setBombNumber(resBomb);
                battleField.setBombToCell();
                battleField.setR();
                bombs = battleField.getBombs();
                battleField.printToConsole();

                int sq = Integer.parseInt(widthValue.getText()) * Integer.parseInt(heightValue.getText());
                if (Integer.parseInt(numberOfBombsValue.getText()) > sq) {
                    JOptionPane.showConfirmDialog(null, "Number of bombs must be more then " + sq, "ERROR", JOptionPane.CLOSED_OPTION, JOptionPane.ERROR_MESSAGE);
                } else {
                    jFrame.remove(jPanel);
                    createGameWindow();
                    jFrame.revalidate();
                    if (!timer.isRunning()) {
                        lastTickTime = System.currentTimeMillis();
                        timer.start();
                    }
                }
            }
        });
    }

    private void createGameWindow() {
        h = Integer.parseInt(heightValue.getText());
        w = Integer.parseInt(widthValue.getText());
        JPanel bombCounter = new JPanel();
        bombCounter.setLayout(new BoxLayout(bombCounter,BoxLayout.X_AXIS));
        bombNumber = new JTextField(numberOfBombsValue.getText());
        bombNumber.setBorder(null);
        bombNumber.setEditable(false);
        optionPanel = new JPanel();
        optionPanel.setLayout(new BorderLayout());
        JButton newGame = new JButton(smile);
        newGame.setBackground(new Color(238,238,238));
        newGame.setBorder(null);
        newGame.setPreferredSize(new Dimension(20,20));



        JPanel ng = new JPanel();
        JPanel ti = new JPanel();
        JPanel imgs = new JPanel();
        ti.setLayout(new BoxLayout(ti,BoxLayout.X_AXIS));


       ng.add(newGame);

        newGame.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                jFrame.remove(gamePanel);
                createGameWindow();
                jFrame.revalidate();
                System.out.println(":)");
            }
        });

        bombCounter.add(new JLabel(bomb));
        bombCounter.add(bombNumber);
        optionPanel.add(ng, BorderLayout.CENTER);
        optionPanel.add(bombCounter, BorderLayout.EAST);
        record = new JLabel(String.format("%02d:%02d", 0, 0));
        ti.add(new JLabel(clock));
        ti.add(record);
        optionPanel.add(ti,BorderLayout.WEST);

        timer = new Timer(100, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                long runningTime = System.currentTimeMillis() - lastTickTime;
                Duration duration = Duration.ofMillis(runningTime);
                long hours = duration.toHours();
                duration = duration.minusHours(hours);
                 minutes = duration.toMinutes();
                duration = duration.minusMinutes(minutes);
                 millis = duration.toMillis();
                 seconds = millis / 1000;
                millis -= (seconds * 1000);
                record.setText(String.format(" "+"%02d:%02d",  minutes, seconds));
            }
        });


        gamePanel = new JPanel();
        gamePanel.setLayout(new GridLayout(h, w));

        cells = battleField.getCells();
        for (k = 0; k < cells.length; k++) {
            for (m = 0; m < cells[k].length; m++) {
                cells[k][m].setMargin(new Insets(0, 0, 0, 0));
                cells[k][m].setPreferredSize(new Dimension(20, 20));
                cells[k][m].addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        Cell cell = (Cell) e.getSource();
                        if (x.equals(cell.getSecret())) {
                            cell.setIcon(boom);
                            timer.stop();
                            JOptionPane.showConfirmDialog(null, "You lose :(", "Game over", JOptionPane.CLOSED_OPTION, JOptionPane.ERROR_MESSAGE);
                            return;
                        }

                        cell.setTextt(cell.getSecret());
                        zeroClaster(cell);
                        cellFormate(cell);
                    }
                });

                cells[k][m].addMouseListener(new MouseListener() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        Cell cell = (Cell) e.getSource();
                        int onMask = MouseEvent.BUTTON1_DOWN_MASK & MouseEvent.BUTTON3_DOWN_MASK;
                        int offMask = MouseEvent.BUTTON1_DOWN_MASK | MouseEvent.BUTTON3_DOWN_MASK;
                        if ((e.getModifiersEx() & (onMask | offMask)) == onMask) {
                            System.out.println("r+l");
                        }

                        if (SwingUtilities.isRightMouseButton(e) && e.getClickCount() == 1) {

                            if (cell.getFlag()) {
                                cell.setIcon(null);
                                cell.setFlag(false);
                                bombNumber.setText(Integer.toString(++bombNumb));
                            } else if (cell.isEditable()){
                                cell.setFlag(true);
                                cell.setIcon(flag);
                                bombNumber.setText(Integer.toString(--bombNumb));
                            }
                            if (gameOver()){
                                timer.stop();
                                JOptionPane.showConfirmDialog(null, "You win ^_^"+"\n"+"your time:"+ String.format(" "+"%02d:%02d.%03d",  minutes, seconds, millis), "Congratulation!", JOptionPane.CLOSED_OPTION, JOptionPane.INFORMATION_MESSAGE);
                        }}
                    }

                    @Override
                    public void mousePressed(MouseEvent e) {

                    }

                    @Override
                    public void mouseReleased(MouseEvent e) {

                    }

                    @Override
                    public void mouseEntered(MouseEvent e) {

                    }

                    @Override
                    public void mouseExited(MouseEvent e) {

                    }
                });
                gamePanel.add(cells[k][m]);

            }
        }
        jFrame.setLayout(new BorderLayout());
      jFrame.add(optionPanel, BorderLayout.NORTH);
       jFrame.add(gamePanel,  BorderLayout.CENTER);
      jFrame.pack();
        jFrame.setResizable(false);
        jFrame.revalidate();
        jFrame.setLocationRelativeTo(null);

    }

    public void checkRadius(Cell cell) {
        if (cell.getRadiusBomb() != 0) {
            cell.setIfChecked(true);
            cell.setTextt("");
            cell.setEnabled(false);
            cell.setEditable(false);
            try {
                zeroClaster(cells[cell.getnIndex() - 1][cell.getmIndex() - 1]); // \

            } catch (ArrayIndexOutOfBoundsException e) {
            }
            try {
                zeroClaster(cells[cell.getnIndex() - 1][cell.getmIndex()]); // |

            } catch (ArrayIndexOutOfBoundsException e) {
            }
            try {
                zeroClaster(cells[cell.getnIndex() - 1][cell.getmIndex() + 1]); // /

            } catch (ArrayIndexOutOfBoundsException e) {
            }
            try {
                zeroClaster(cells[cell.getnIndex()][cell.getmIndex() + 1]); // ->

            } catch (ArrayIndexOutOfBoundsException e) {
            }
            try {
                zeroClaster(cells[cell.getnIndex() + 1][cell.getmIndex() + 1]); // \

            } catch (ArrayIndexOutOfBoundsException e) {
            }
            try {
                zeroClaster(cells[cell.getnIndex() + 1][cell.getmIndex()]); // |

            } catch (ArrayIndexOutOfBoundsException e) {
            }
            try {
                zeroClaster(cells[cell.getnIndex() + 1][cell.getmIndex() - 1]); // /

            } catch (ArrayIndexOutOfBoundsException e) {
            }
            try {
                zeroClaster(cells[cell.getnIndex()][cell.getmIndex() - 1]); // <-

            } catch (ArrayIndexOutOfBoundsException e) {
            }
            return;

        } else {

            if (!x.equals(cell.getSecret())) cell.setTextt(cell.getSecret());
            cellFormate(cell);
            cell.setEditable(false);
            return;
        }
    }



    public void initilizationStartWindow() {
        startGame = new JButton();
        startGame.setText("click to start the game");
        numberOfBombs = new JLabel("Set number of bombs");
        width = new JLabel("Set width of war field");
        height = new JLabel("Set height of war field");
        heightValue = new JTextField("10", 2);
        widthValue = new JTextField("15", 2);
        numberOfBombsValue = new JTextField("15", 2);
    }

    public void zeroClaster(Cell cell) {
        if (cell.getRadiusBomb() == 0 && cell.getIfChecked() == false) {
            cell.setIfChecked(true);
            cell.setTextt("");
            cell.setEnabled(false);
            cell.setEditable(false);
            try {
                zeroClaster(cells[cell.getnIndex() - 1][cell.getmIndex() - 1]); // \

            } catch (ArrayIndexOutOfBoundsException e) {
            }
            try {
                zeroClaster(cells[cell.getnIndex() - 1][cell.getmIndex()]); // |

            } catch (ArrayIndexOutOfBoundsException e) {
            }
            try {
                zeroClaster(cells[cell.getnIndex() - 1][cell.getmIndex() + 1]); // /

            } catch (ArrayIndexOutOfBoundsException e) {
            }
            try {
                zeroClaster(cells[cell.getnIndex()][cell.getmIndex() + 1]); // ->

            } catch (ArrayIndexOutOfBoundsException e) {
            }
            try {
                zeroClaster(cells[cell.getnIndex() + 1][cell.getmIndex() + 1]); // \

            } catch (ArrayIndexOutOfBoundsException e) {
            }
            try {
                zeroClaster(cells[cell.getnIndex() + 1][cell.getmIndex()]); // |

            } catch (ArrayIndexOutOfBoundsException e) {
            }
            try {
                zeroClaster(cells[cell.getnIndex() + 1][cell.getmIndex() - 1]); // /

            } catch (ArrayIndexOutOfBoundsException e) {
            }
            try {
                zeroClaster(cells[cell.getnIndex()][cell.getmIndex() - 1]); // <-

            } catch (ArrayIndexOutOfBoundsException e) {
            }
            return;

        } else {

            if (!x.equals(cell.getSecret())) cell.setTextt(cell.getSecret());
            cellFormate(cell);
            cell.setEditable(false);
            return;
        }
    }

    public void cellFormate(Cell cell) {
        switch (cell.getRadiusBomb()) {
            case 1:
                cell.setForeground(Color.BLUE);
                break;
            case 2:
                cell.setForeground(Color.RED);
                break;
            case 3:
                cell.setForeground(Color.DARK_GRAY);
                break;
            case 4:
                cell.setForeground(Color.magenta);
                break;
            case 5:
                cell.setForeground(Color.magenta);
                break;
            case 6:
                cell.setForeground(Color.magenta);
                break;
            case 7:
                cell.setForeground(Color.magenta);
                break;
            case 8:
                cell.setForeground(Color.magenta);
                break;
            case 0: {
                cell.setTextt("");
                cell.setEnabled(false);
            }
            break;

            default:
                ;
                break;
        }
    }

    public boolean gameOver (){
        boolean b = false;
        for (int l = 0; l < bombs.size(); l++) {
            if (!bombs.get(l).getFlag()) return false;
            else b=true;
        }

        return b;
    }
}
