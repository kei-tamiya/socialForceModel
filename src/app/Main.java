package app;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Logger;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Random;
import java.io.File;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.awt.geom.Point2D;

/**
 * Created by keitamiya on 2016/12/20.
 */
public class Main {
    public static void main(String[] args) throws IOException {
        int agentNum = 100; // Agent生成数
        int maxStep = 500;
        int step = 0;
        LinkedList<Agent> agents = new LinkedList<>();
        for (int i=0; i < agentNum; i++) {
            Double posY = Math.random()*10;
            if (i%2 == 0) {
                Point2D.Double destination = new Point2D.Double(6.0, posY);
                Agent a = new Agent(4.0, posY, 1, i+1, destination);
                agents.add(a);
            } else {
                Point2D.Double destination = new Point2D.Double(4.0, posY);
                Agent a = new Agent(6.0, posY, 1, i+1, destination);
                agents.add(a);
            }
        }
//        Space space = new Space(1000.0, 1000.0);

        while(step < maxStep) {
            for (Iterator<Agent> it = agents.iterator(); it.hasNext();) {
                Agent a = it.next();
                a.moveToDestination(agents);
            }
            step++;
        }

        try {
            File file = new File("/Users/keitamiya/Documents/兼田研究室/Untitled Folder/text.txt");
            BufferedWriter bw = new BufferedWriter(new FileWriter(file));

            for (Iterator<Agent> it = agents.iterator(); it.hasNext();) {
                Agent a = it.next();
                bw.write("" + a.positionLog);
                bw.newLine();
            }
            bw.close();
        } catch (IOException e) {
            System.out.println("error : " + e);
        }

    }
}


