package app;

/**
 * Created by keitamiya on 2016/12/20.
 */
public class Agent {
    public Double[] prePosition;
    public Double[] position;
    public Double[] destination;
    public Double m; // 体重
    public Double v; // 速度
    public Double pre_v; // 前ステップ時の速度
    public Double step;
    public Double distanceByStep;
    public Double impactRange; // 影響範囲
    Double[] destinationVector = new Double[2];
    Double[] impactVector = new Double[2];
    Double[] attractiveVector = new Double[2];

    public Agent(Double cx, Double cy, Double step) {
        this.position = point(cx, cy);
        this.m = 60.0; // kg
        this.v = 1.5; // m/s
        this.step = step;
        this.distanceByStep = v * step;
        this.destination = point(500.003, 500.2332);
        this.impactRange = Math.random() + 5.0; // とりあえずパーソナルスペースは5m〜6m TODO パーソナルスペース概念の実装
    }

    public Double[] point(Double x, Double y) {
        Double[] d = new Double[2];
        d[0] = x;
        d[1] = y;
        return d;
    }

    public Double[] calcDestinationVector() {
        Double posX = position[0];
        Double posY = position[1];
        Double destX = destination[0];
        Double destY = destination[1];
        Double dx = destX - posX;
        Double dy = destY - posY;
        Double remainingDistance = Math.sqrt(Math.pow(dx, 2) + Math.pow(dy, 2));
        Double sin = Math.abs(destY - posY) / remainingDistance;
        Double cos = Math.abs(destX - posX) / remainingDistance;
        Double sigX = Math.signum(dx);
        Double sigY = Math.signum(dy);
        Double nextVX;
        Double nextVY;

        if (remainingDistance >= distanceByStep) {
            nextVX = distanceByStep * cos;
            nextVY = distanceByStep * sin;
        } else {
            // TODO 中継ポイントの判定半径内にいるかどうかチェック増やす
            nextVX = remainingDistance * cos;
            nextVY = remainingDistance * sin;
        }

        // ベクトルに変換
        nextVX = nextVX * sigX;
        nextVY = nextVY * sigY;

        Double preVX = posX - prePosition[0];
        Double preVY = posY - prePosition[1];

        destinationVector[0] = nextVX - preVX;
        destinationVector[1] = nextVY - preVY;

        return destinationVector;
    }

    public Double[] calcImpactVector(Agent agent, Double[] destinationVector) {
        Double posX = position[0];
        Double posY = position[1];
        Double otherPosX = agent.position[0];
        Double otherPosY = agent.position[1];
        Double dx = posX - otherPosX;
        Double dy = posY - otherPosY;
        Double agentDistance = Math.sqrt(Math.pow(dx, 2) + Math.pow(dy, 2));
        Double impactRangeSum = impactRange + agent.impactRange;
        Double sin = Math.abs(dy) / agentDistance;
        Double cos = Math.abs(dx) / agentDistance;

        Double vx = destinationVector[0];
        Double vy = destinationVector[1];

        Double cosPhi = -(cos*vx + sin*vy) / (Math.sqrt(Math.pow(cos, 2) + Math.pow(sin, 2)) * Math.sqrt(Math.pow(vx, 2) + Math.pow(vy, 2)));
        // TODO パラメータ設定する
        Double paramA = - 0.8;
        Double paramB = 1.3;
        Double paramC = 1.0;

        Double f = paramA * Math.exp((impactRangeSum - agentDistance) / paramB) * (1+cosPhi) / 2;
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

//    public Double[] calcPositionBySocialForce() {
//
//    }
}
