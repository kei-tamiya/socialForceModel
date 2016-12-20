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
    public Double step;
    public Double prev; // 前ステップ時の速度
    public Double distanceByStep;
    Double[] destinationVector = new Double[2];

    public Agent(Double cx, Double cy, Double step) {
        this.position = point(cx, cy);
        this.m = 60.0; // kg
        this.v = 1.5; // m/s
        this.step = step;
        this.distanceByStep = v * step;
        this.destination = point(500.003, 500.2332);
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
        Double sin = (destY - posY) / remainingDistance;
        Double cos = (destX - posX) / remainingDistance;
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

//    public Double[] calcAgentImpactVector() {
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
//    public Double[] calcObstacleImpactVector() {
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
