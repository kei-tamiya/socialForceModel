package app;
import java.awt.*;
import java.util.Random;

/**
 * Created by keitamiya on 2016/12/20.
 */
//public class Main {
//    public static void main(String[] args) {
//        Random rnd = new Random();
//        for (int i=0; i < 20; i++) {
//            System.out.println("[" + rnd.nextDouble()*130 + ", " + rnd.nextDouble()*130 + "],");
//        }
//    }
//}
public class Main extends Canvas
        implements Runnable {

    private final int MAX_IMAGE = 7;
    private final int WIDTH = 500;
    private final int HIGHT = 400;
    private Image[] image = new Image[1];
    private MediaTracker tracker;

    //ダブルバッファリング（オフスクリーン・バッファ用）変数
    private Graphics offg;
    private Image offImage;
    private int imageNum = 0;          //利用する画像
    private int imageCount = 1;         //画像の枚数カウント制御
    private int moveCount = 20;        //移動距離カウント制御
    private int tickMax = 1000;
    private int tick = 0;

    Agent agent1 = new Agent(10.0, 3.0);
    Agent agent2 = new Agent(20.0, 9.0);
    Agent[] agents = new Agent[2];

    /**コンストラクタ*/
    public Main() {
        setSize(WIDTH, HIGHT);
        setBackground(Color.black);

        Toolkit tk = Toolkit.getDefaultToolkit();

        tracker = new MediaTracker(this);

        //キャラクターイメージの作成
        image[0] = tk.getImage("/Users/keitamiya/.go/src/github.com/kei-tamiya/socialForceModel/public/images/agent.png");

        agents[0]= agent1;
        agents[1]= agent2;

        //全イメージのダウンロードが終わるまで待つ
        try {
            tracker.waitForAll();
        }
        catch (InterruptedException e) {
            System.err.println("tracker error");
        }
    }

    /**メインの処理（スレッド処理）*/
    public void run() {
        while (true) {	//永久に繰り返し
            try {
                Thread.sleep(1000);		//100ミリ秒(0.1秒)スリープ
            }
            catch (InterruptedException ex) {
                System.err.println(ex);
            }

            repaint();
            tick++;
            if (tick > tickMax) {
                System.out.println("finish");
                return;
            }
        }
    }

    public static void exportLog(Agent[] agents) {
        System.out.println("Tick: ");
        for (int i = 0; i < agents.length; i++) {
            System.out.println("agents" + i + " => posX : " + agents[i].posX + ", posY : " + agents[i].posY);
        }
    }

    /**画像描写が必要な場合の処理*/
    public void update (Graphics g) {

        //キャラクター、障害物の消去（全画面消去）
        //オフスクリーン・バッファへの描き込み。実際にはまだ見えない
        offg.setColor(Color.black);
        offg.fillRect(0, 0, WIDTH, HIGHT);

        Random rnd = new Random();
        for (int i = 0; i < agents.length; i++) {
            int posX = rnd.nextInt(500);
            int posY = rnd.nextInt(400);

            offg.drawImage(image[0], posX, posY, this);
            g.drawImage(offImage, posX, posY, this);
        }
    }

    /**キャンバスで描写の必要のあるときに呼ばれるメソッド*/
    public void paint(Graphics g) {

        //オフスクリーン・バッファの領域がない場合は作成
        if (offg == null) {

            //キャンバスと同じ大きさの仮想画面を生成
            offImage = createImage(getSize().width, getSize().height);

            //offimageから仮想画面描写用のグラフィックスコンテキストを取得
            offg = offImage.getGraphics();
        }
        Random rnd = new Random();

        g.drawImage(image[0], rnd.nextInt(500), rnd.nextInt(400), this);
    }

    /**main()*/
    public static void main( String[] args) {

        Main canvas = new Main();
        Frame frame = new Frame("Animation1");
        frame.add(canvas);
        frame.pack();
        frame.setVisible(true);

        //スレッドを生成して、ゲーム開始
        new Thread(canvas).start();
    }
}

