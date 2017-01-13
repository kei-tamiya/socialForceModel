package app;

import com.sun.tools.jdi.DoubleValueImpl;
import javafx.scene.shape.Circle;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * Created by keitamiya on 2016/12/20.
 */
public class Agent {
    public Point2D.Double prePosition;
    public Point2D.Double position;
    public Point2D.Double destination;
    public Double m; // 体重
    public Double v0; // 速度
    public Double v; // 速度
    public Double[] vectorV = new Double[2]; // 速度ベクトル
    public Double pre_v; // 前ステップ時の速度
    public Integer step;
    public Double distanceByStep;
    public Double impactRadius; // 影響範囲半径
    public Circle impactRange; // 影響範囲
    public Double remainingDistance;
    Double[] impactVector = new Double[2];
    Double[] attractiveVector = new Double[2];
    public String name;
    LinkedList<String> positionLog = new LinkedList<>();

    public Agent(Double cx, Double cy, Integer step, int idx, Point2D.Double destination) {
        this.name = "Agent" + idx;
        this.position = new Point2D.Double(cx, cy);
        this.prePosition = this.position;
        this.m = 60.0; // kg
        this.v0 = 10.5 * step; // 希望歩行速度(m*step/s)
        this.v = 0.0;
        this.vectorV[0] = 0.0;
        this.vectorV[1] = 0.0;
        this.step = step;
        this.distanceByStep = this.v0 * step;
        this.destination = destination;
        this.remainingDistance = calcDistance(this.position, this.destination);
        this.impactRadius = Math.random() + 5.0; // とりあえずパーソナルスペースは5m〜6m TODO パーソナルスペース概念の実装
        this.impactRange = setImpactRange(cx, cy, this.impactRadius);
    }

    public void log() {
        positionLog.add("[" + position.getX() + ", " + position.getY() + "]");
    }
    public void moveToDestination(LinkedList<Agent> agents) {
        Double[] f = calcDestinationVector();
        Double fx = f[0];
        Double fy = f[1];
//        for (Iterator<Agent> it = agents.iterator(); it.hasNext();) {
//            Agent a = it.next();
//            if (!isCollision(a))
//                continue;
//            Double[] iv = calcImpactVector(a, f);
//            fx += iv[0];
//            fy += iv[1];
//        }
        if (remainingDistance != 0) {
            move(fx, fy);
        }
        log();
    }

    public void updateWhileMoving() {
        calcVectorV();
        remainingDistance = calcDistance(position, destination);
    }

    public void move(Double fx, Double fy) {
        Double ax = fx / m;
        Double ay = fy / m;
        Point2D.Double nextPosition = new Point2D.Double(position.getX()+vectorV[0]*step+ax*step*step/2, position.getY()+vectorV[1]*step+ay*step*step/2);
        prePosition = position;
//        Double test = vectorV[0]*step + ax*step*step/2;
        if (remainingDistance <= calcDistance(position, nextPosition)) {
            position = destination;
        } else {
            position = nextPosition;
        }
        updateWhileMoving();
    }

    public Circle setImpactRange(Double centerX, Double centerY, Double radius) {
        Circle impactRange = new Circle();
        impactRange.setCenterX(centerX);
        impactRange.setCenterY(centerY);
        impactRange.setRadius(radius);
        return impactRange;
    }

    public boolean isCollision(Agent agent) {
        Double diffX = position.getX() - agent.position.getX();
        Double diffY = position.getY() - agent.position.getY();
        return (Math.pow(diffX, 2) + Math.pow(diffY, 2)) <= impactRadius + agent.impactRadius;
    }

    public Double calcDistance(Point2D.Double p1, Point2D.Double p2) {
        Double diffX = p1.getX() - p2.getX();
        Double diffY = p1.getY() - p2.getY();
        return Math.sqrt(Math.pow(diffX, 2) + Math.pow(diffY, 2));
    }

//    public void update() {
//        Double movedDistance = calcDistance(position, prePosition);
//        Double currentVX = 0.0;
//        Double currentVY = 0.0;
//        if (movedDistance != 0) {
//            currentVX = v * movedDiffX / movedDistance;
//            currentVY = v * movedDiffY / movedDistance;
//        }
//    }

    public void calcVectorV() {
        Double movedDiffX = position.getX() - prePosition.getX();
        Double movedDiffY = position.getY() - prePosition.getY();
        Double movedDistance = calcDistance(position, prePosition);
        if (movedDistance != 0) {
            vectorV[0] = v * movedDiffX / movedDistance;
            vectorV[1] = v * movedDiffY / movedDistance;
        }
    }

    public Double[] calcDestinationVector() {
        Double dx = destination.getX() - position.getX();
        Double dy = destination.getY() - position.getY();
        Double sin = dy / remainingDistance; // 絶対値？
        Double cos = dx / remainingDistance;

        Double[] f = new Double[2];
        f[0] = (v0 * cos - vectorV[0]) / step;
        f[1] = (v0 * sin - vectorV[1]) / step;
        return f;
//        Double ax = f[0] / m;
//        Double ay = f[1] / m;
//
//        Point2D.Double nextPosition = new Point2D.Double(posX+currentVX*step+ax*step*step/2, posY+currentVY*step+ay*step*step/2);
//        if (remainingDistance <= calcDistance(position, nextPosition)) {
//            Double adjustV = remainingDistance / step;
//            vectorV[0] = adjustV * cos;
//            vectorV[1] = adjustV * sin;
//            return vectorV;
//        }
//        vectorV[0] = currentVX + ax*step;
//        vectorV[1] = currentVY + ay*step;
//        return vectorV;
    }

    public Double[] calcImpactVector(Agent agent, Double[] destinationVector) {
        Double posX = position.getX();
        Double posY = position.getY();
        Double otherPosX = agent.position.getX();
        Double otherPosY = agent.position.getY();
        Double dx = posX - otherPosX;
        Double dy = posY - otherPosY;
        Double agentDistance = Math.sqrt(Math.pow(dx, 2) + Math.pow(dy, 2));
        Double impactRadiusSum = impactRadius + agent.impactRadius;
        Double sin = Math.abs(dy) / agentDistance;
        Double cos = Math.abs(dx) / agentDistance;

        Double vx = destinationVector[0];
        Double vy = destinationVector[1];

        Double cosPhi = -(cos*vx + sin*vy) / (Math.sqrt(Math.pow(cos, 2) + Math.pow(sin, 2)) * Math.sqrt(Math.pow(vx, 2) + Math.pow(vy, 2)));
        // TODO パラメータ設定する
        Double paramA = - 0.8;
        Double paramB = 1.3;
        Double paramC = 1.0;

        Double f = paramA * Math.exp((impactRadiusSum - agentDistance) / paramB) * (1+cosPhi) / 2;
        impactVector[0] = f * cos;
        impactVector[1] = f * sin;

        attractiveVector[0] = -paramC * cos;
        attractiveVector[1] = -paramC * sin;

        return impactVector;
    }
//
//    public Double[] calcAttractiveVector() {
//        Double posX = position[0];
//        Double posY = position[1];
//        Double destX = destination[0];
//        Double destY = destination[1];
//        Double dx = destX - posX;
//        Double dy = destY - posY;
//        Double remainingDistance = Math.sqrt(Math.pow(dx, 2) + Math.pow(dy, 2));
//        Double sin = (destY - posY) / remainingDistance;
//        Double cos = (destX - posX) / remainingDistance;
//        Double sigX = Math.signum(dx);
//        Double sigY = Math.signum(dy);
//        Double nextVX;
//        Double nextVY;
//
//        if (remainingDistance >= distanceByStep) {
//            nextVX = distanceByStep * cos;
//            nextVY = distanceByStep * sin;
//        } else {
//            // TODO 中継ポイントの判定半径内にいるかどうかチェック増やす
//            nextVX = remainingDistance * cos;
//            nextVY = remainingDistance * sin;
//        }
//
//        // ベクトルに変換
//        nextVX = nextVX * sigX;
//        nextVY = nextVY * sigY;
//
//        Double preVX = posX - prePosition[0];
//        Double preVY = posY - prePosition[1];
//
//        destinationVector[0] = nextVX - preVX;
//        destinationVector[1] = nextVY - preVY;
//
//        return destinationVector;
//    }
//
//    public Agent[] searchSurroundAgents() {
//
//    }

    public void calcPositionByDestinationVector() {

    }

//    public Double[] calcPositionBySocialForce(Agent agent) {
//        prePosition = position;
//        pre_v = v;
//        Double[] dv = calcDestinationVector();
//        Double[] iv = calcImpactVector(agent, dv);
////        position = ;
//
//    }
}
