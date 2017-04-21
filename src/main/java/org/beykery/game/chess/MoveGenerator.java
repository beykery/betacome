/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.beykery.game.chess;

/**
 *
 * @author beykery
 */
public final class MoveGenerator {

    int m_nMoveCount;
    Chessmove[][] m_MoveList = new Chessmove[10][80];

    public static boolean isBlack(int x) {
        return x >= Eveluation.B_BEGIN && x <= Eveluation.B_END;
    }

    public static boolean isRed(int x) {
        return x >= Eveluation.R_BEGIN && x <= Eveluation.R_END;
    }

    public static boolean isSameSide(int x, int y) {
        return (isBlack(x) && isBlack(y)) || (isRed(x) && isRed(y));
    }

    /**
     * 是否为合法走步
     *
     * @param position
     * @param nFromX
     * @param nFromY
     * @param nToX
     * @param nToY
     * @return
     */
    public static boolean isValidMove(byte[][] position, int nFromX, int nFromY, int nToX, int nToY) {
        int i = 0, j = 0;
        int nMoveChessID, nTargetID;

        if (nFromY == nToY && nFromX == nToX) {
            return false;//目的与源相同
        }
        nMoveChessID = position[nFromY][nFromX];
        nTargetID = position[nToY][nToX];

        if (isSameSide(nMoveChessID, nTargetID)) {
            return false;//不能吃自己的棋
        }
        switch (nMoveChessID) {
            case Eveluation.B_KING:
                if (nTargetID == Eveluation.R_KING)//老将见面?
                {
                    if (nFromX != nToX) {
                        return false;
                    }
                    for (i = nFromY + 1; i < nToY; i++) {
                        if (position[i][nFromX] != Eveluation.NOCHESS) {
                            return false;
                        }
                    }
                } else {
                    if (nToY > 2 || nToX > 5 || nToX < 3) {
                        return false;//目标点在九宫之外
                    }
                    if (Math.abs(nFromY - nToY) + Math.abs(nToX - nFromX) > 1) {
                        return false;//将帅只走一步直线:
                    }
                }
                break;
            case Eveluation.R_BISHOP:

                if (nToY < 7 || nToX > 5 || nToX < 3) {
                    return false;//士出九宫	
                }
                if (Math.abs(nFromY - nToY) != 1 || Math.abs(nToX - nFromX) != 1) {
                    return false;	//士走斜线
                }
                break;

            case Eveluation.B_BISHOP:   //黑士

                if (nToY > 2 || nToX > 5 || nToX < 3) {
                    return false;//士出九宫	
                }
                if (Math.abs(nFromY - nToY) != 1 || Math.abs(nToX - nFromX) != 1) {
                    return false;	//士走斜线
                }
                break;

            case Eveluation.R_ELEPHANT://红象

                if (nToY < 5) {
                    return false;//相不能过河
                }
                if (Math.abs(nFromX - nToX) != 2 || Math.abs(nFromY - nToY) != 2) {
                    return false;//相走田字
                }
                if (position[(nFromY + nToY) / 2][(nFromX + nToX) / 2] != Eveluation.NOCHESS) {
                    return false;//相眼被塞住了
                }
                break;

            case Eveluation.B_ELEPHANT://黑象 

                if (nToY > 4) {
                    return false;//相不能过河
                }
                if (Math.abs(nFromX - nToX) != 2 || Math.abs(nFromY - nToY) != 2) {
                    return false;//相走田字
                }
                if (position[(nFromY + nToY) / 2][(nFromX + nToX) / 2] != Eveluation.NOCHESS) {
                    return false;//相眼被塞住了
                }
                break;

            case Eveluation.B_PAWN:     //黑兵

                if (nToY < nFromY) {
                    return false;//兵不回头
                }
                if (nFromY < 5 && nFromY == nToY) {
                    return false;//兵过河前只能直走
                }
                if (nToY - nFromY + Math.abs(nToX - nFromX) > 1) {
                    return false;//兵只走一步直线:
                }
                break;

            case Eveluation.R_PAWN:    //红兵

                if (nToY > nFromY) {
                    return false;//兵不回头
                }
                if (nFromY > 4 && nFromY == nToY) {
                    return false;//兵过河前只能直走
                }
                if (nFromY - nToY + Math.abs(nToX - nFromX) > 1) {
                    return false;//兵只走一步直线:
                }
                break;

            case Eveluation.R_KING:
                if (nTargetID == Eveluation.B_KING)//老将见面?
                {
                    if (nFromX != nToX) {
                        return false;//两个将不在同一列
                    }
                    for (i = nFromY - 1; i > nToY; i--) {
                        if (position[i][nFromX] != Eveluation.NOCHESS) {
                            return false;//中间有别的子
                        }
                    }
                } else {
                    if (nToY < 7 || nToX > 5 || nToX < 3) {
                        return false;//目标点在九宫之外
                    }
                    if (Math.abs(nFromY - nToY) + Math.abs(nToX - nFromX) > 1) {
                        return false;//将帅只走一步直线:
                    }
                }
                break;

            case Eveluation.B_CAR:
            case Eveluation.R_CAR:

                if (nFromY != nToY && nFromX != nToX) {
                    return false;	//车走直线:
                }
                if (nFromY == nToY) {
                    if (nFromX < nToX) {
                        for (i = nFromX + 1; i < nToX; i++) {
                            if (position[nFromY][i] != Eveluation.NOCHESS) {
                                return false;
                            }
                        }
                    } else {
                        for (i = nToX + 1; i < nFromX; i++) {
                            if (position[nFromY][i] != Eveluation.NOCHESS) {
                                return false;
                            }
                        }
                    }
                } else {
                    if (nFromY < nToY) {
                        for (j = nFromY + 1; j < nToY; j++) {
                            if (position[j][nFromX] != Eveluation.NOCHESS) {
                                return false;
                            }
                        }
                    } else {
                        for (j = nToY + 1; j < nFromY; j++) {
                            if (position[j][nFromX] != Eveluation.NOCHESS) {
                                return false;
                            }
                        }
                    }
                }

                break;

            case Eveluation.B_HORSE:
            case Eveluation.R_HORSE:

                if (!((Math.abs(nToX - nFromX) == 1 && Math.abs(nToY - nFromY) == 2)
                        || (Math.abs(nToX - nFromX) == 2 && Math.abs(nToY - nFromY) == 1))) {
                    return false;//马走日字
                }
                if (nToX - nFromX == 2) {
                    i = nFromX + 1;
                    j = nFromY;
                } else if (nFromX - nToX == 2) {
                    i = nFromX - 1;
                    j = nFromY;
                } else if (nToY - nFromY == 2) {
                    i = nFromX;
                    j = nFromY + 1;
                } else if (nFromY - nToY == 2) {
                    i = nFromX;
                    j = nFromY - 1;
                }

                if (position[j][i] != Eveluation.NOCHESS) {
                    return false;//绊马腿
                }
                break;

            case Eveluation.B_CANON:
            case Eveluation.R_CANON:

                if (nFromY != nToY && nFromX != nToX) {
                    return false;	//炮走直线:
                }
                //炮不吃子时经过的路线中不能有棋子

                if (position[nToY][nToX] == Eveluation.NOCHESS) {
                    if (nFromY == nToY) {
                        if (nFromX < nToX) {
                            for (i = nFromX + 1; i < nToX; i++) {
                                if (position[nFromY][i] != Eveluation.NOCHESS) {
                                    return false;
                                }
                            }
                        } else {
                            for (i = nToX + 1; i < nFromX; i++) {
                                if (position[nFromY][i] != Eveluation.NOCHESS) {
                                    return false;
                                }
                            }
                        }
                    } else {
                        if (nFromY < nToY) {
                            for (j = nFromY + 1; j < nToY; j++) {
                                if (position[j][nFromX] != Eveluation.NOCHESS) {
                                    return false;
                                }
                            }
                        } else {
                            for (j = nToY + 1; j < nFromY; j++) {
                                if (position[j][nFromX] != Eveluation.NOCHESS) {
                                    return false;
                                }
                            }
                        }
                    }
                } //炮吃子时
                else {
                    int count = 0;
                    if (nFromY == nToY) {
                        if (nFromX < nToX) {
                            for (i = nFromX + 1; i < nToX; i++) {
                                if (position[nFromY][i] != Eveluation.NOCHESS) {
                                    count++;
                                }
                            }
                            if (count != 1) {
                                return false;
                            }
                        } else {
                            for (i = nToX + 1; i < nFromX; i++) {
                                if (position[nFromY][i] != Eveluation.NOCHESS) {
                                    count++;
                                }
                            }
                            if (count != 1) {
                                return false;
                            }
                        }
                    } else {
                        if (nFromY < nToY) {
                            for (j = nFromY + 1; j < nToY; j++) {
                                if (position[j][nFromX] != Eveluation.NOCHESS) {
                                    count++;
                                }
                            }
                            if (count != 1) {
                                return false;
                            }
                        } else {
                            for (j = nToY + 1; j < nFromY; j++) {
                                if (position[j][nFromX] != Eveluation.NOCHESS) {
                                    count++;
                                }
                            }
                            if (count != 1) {
                                return false;
                            }
                        }
                    }
                }
                break;
            default:
                return false;
        }

        return true;
    }

    int addMove(int nFromX, int nFromY, int nToX, int nToY, int nPly) {
        Chessmove cm = new Chessmove(nFromX, nFromY, nToX, nToY);
        m_MoveList[nPly][m_nMoveCount] = cm;
        m_nMoveCount++;
        return m_nMoveCount;
    }

    public void gen_KingMove(byte[][] position, int i, int j, int nPly) {
        int x, y;
        for (y = 0; y < 3; y++) {
            for (x = 3; x < 6; x++) {
                if (isValidMove(position, j, i, x, y)) {
                    addMove(j, i, x, y, nPly);
                }
            }
        }

        for (y = 7; y < 10; y++) {
            for (x = 3; x < 6; x++) {
                if (isValidMove(position, j, i, x, y)) {
                    addMove(j, i, x, y, nPly);
                }
            }
        }
    }

    public void gen_RBishopMove(byte[][] position, int i, int j, int nPly) {
        int x, y;
        for (y = 7; y < 10; y++) {
            for (x = 3; x < 6; x++) {
                if (isValidMove(position, j, i, x, y)) {
                    addMove(j, i, x, y, nPly);
                }
            }
        }
    }

    public void gen_BBishopMove(byte[][] position, int i, int j, int nPly) {
        int x, y;
        for (y = 0; y < 3; y++) {
            for (x = 3; x < 6; x++) {
                if (isValidMove(position, j, i, x, y)) {
                    addMove(j, i, x, y, nPly);
                }
            }
        }
    }

    public void gen_ElephantMove(byte[][] position, int i, int j, int nPly) {
        int x, y;

        x = j + 2;
        y = i + 2;
        if (x < 9 && y < 10 && isValidMove(position, j, i, x, y)) {
            addMove(j, i, x, y, nPly);
        }

        x = j + 2;
        y = i - 2;
        if (x < 9 && y >= 0 && isValidMove(position, j, i, x, y)) {
            addMove(j, i, x, y, nPly);
        }

        x = j - 2;
        y = i + 2;
        if (x >= 0 && y < 10 && isValidMove(position, j, i, x, y)) {
            addMove(j, i, x, y, nPly);
        }

        x = j - 2;
        y = i - 2;
        if (x >= 0 && y >= 0 && isValidMove(position, j, i, x, y)) {
            addMove(j, i, x, y, nPly);
        }

    }

    public void gen_HorseMove(byte[][] position, int i, int j, int nPly) {
        int x, y;

        x = j + 2;
        y = i + 1;
        if ((x < 9 && y < 10) && isValidMove(position, j, i, x, y)) {
            addMove(j, i, x, y, nPly);
        }

        x = j + 2;
        y = i - 1;
        if ((x < 9 && y >= 0) && isValidMove(position, j, i, x, y)) {
            addMove(j, i, x, y, nPly);
        }

        x = j - 2;
        y = i + 1;
        if ((x >= 0 && y < 10) && isValidMove(position, j, i, x, y)) {
            addMove(j, i, x, y, nPly);
        }

        x = j - 2;
        y = i - 1;
        if ((x >= 0 && y >= 0) && isValidMove(position, j, i, x, y)) {
            addMove(j, i, x, y, nPly);
        }

        x = j + 1;
        y = i + 2;
        if ((x < 9 && y < 10) && isValidMove(position, j, i, x, y)) {
            addMove(j, i, x, y, nPly);
        }
        x = j - 1;
        y = i + 2;
        if ((x >= 0 && y < 10) && isValidMove(position, j, i, x, y)) {
            addMove(j, i, x, y, nPly);
        }
        x = j + 1;
        y = i - 2;
        if ((x < 9 && y >= 0) && isValidMove(position, j, i, x, y)) {
            addMove(j, i, x, y, nPly);
        }
        x = j - 1;
        y = i - 2;
        if ((x >= 0 && y >= 0) && isValidMove(position, j, i, x, y)) {
            addMove(j, i, x, y, nPly);
        }

    }

    void gen_RPawnMove(byte[][] position, int i, int j, int nPly) {
        int x, y;
        int nChessID;
        nChessID = position[i][j];
        y = i - 1;
        x = j;

        if (y > 0 && !isSameSide(nChessID, position[y][x])) {
            addMove(j, i, x, y, nPly);
        }

        if (i < 5) {
            y = i;
            x = j + 1;
            if (x < 9 && !isSameSide(nChessID, position[y][x])) {
                addMove(j, i, x, y, nPly);
            }
            x = j - 1;
            if (x >= 0 && !isSameSide(nChessID, position[y][x])) {
                addMove(j, i, x, y, nPly);
            }
        }
    }

    void gen_BPawnMove(byte[][] position, int i, int j, int nPly) {
        int x, y;
        int nChessID;

        nChessID = position[i][j];

        y = i + 1;
        x = j;

        if (y < 10 && !isSameSide(nChessID, position[y][x])) {
            addMove(j, i, x, y, nPly);
        }

        if (i > 4) {
            y = i;
            x = j + 1;
            if (x < 9 && !isSameSide(nChessID, position[y][x])) {
                addMove(j, i, x, y, nPly);
            }
            x = j - 1;
            if (x >= 0 && !isSameSide(nChessID, position[y][x])) {
                addMove(j, i, x, y, nPly);
            }
        }

    }

    void gen_CarMove(byte[][] position, int i, int j, int nPly) {
        int x, y;
        int nChessID;

        nChessID = position[i][j];

        x = j + 1;
        y = i;
        while (x < 9) {
            if (Eveluation.NOCHESS == position[y][x]) {
                addMove(j, i, x, y, nPly);
            } else {
                if (!isSameSide(nChessID, position[y][x])) {
                    addMove(j, i, x, y, nPly);
                }
                break;
            }
            x++;
        }

        x = j - 1;
        y = i;
        while (x >= 0) {
            if (Eveluation.NOCHESS == position[y][x]) {
                addMove(j, i, x, y, nPly);
            } else {
                if (!isSameSide(nChessID, position[y][x])) {
                    addMove(j, i, x, y, nPly);
                }
                break;
            }
            x--;
        }

        x = j;
        y = i + 1;//
        while (y < 10) {
            if (Eveluation.NOCHESS == position[y][x]) {
                addMove(j, i, x, y, nPly);
            } else {
                if (!isSameSide(nChessID, position[y][x])) {
                    addMove(j, i, x, y, nPly);
                }
                break;
            }
            y++;
        }

        x = j;
        y = i - 1;//
        while (y >= 0) {
            if (Eveluation.NOCHESS == position[y][x]) {
                addMove(j, i, x, y, nPly);
            } else {
                if (!isSameSide(nChessID, position[y][x])) {
                    addMove(j, i, x, y, nPly);
                }
                break;
            }
            y--;
        }
    }

    void gen_CanonMove(byte[][] position, int i, int j, int nPly) {
        int x, y;
        boolean flag;
        int nChessID;
        nChessID = position[i][j];
        x = j + 1;		//
        y = i;
        flag = false;
        while (x < 9) {
            if (Eveluation.NOCHESS == position[y][x]) {
                if (!flag) {
                    addMove(j, i, x, y, nPly);
                }
            } else {
                if (!flag) {
                    flag = true;
                } else {
                    if (!isSameSide(nChessID, position[y][x])) {
                        addMove(j, i, x, y, nPly);
                    }
                    break;
                }
            }
            x++;
        }

        x = j - 1;
        flag = false;
        while (x >= 0) {
            if (Eveluation.NOCHESS == position[y][x]) {
                if (!flag) {
                    addMove(j, i, x, y, nPly);
                }
            } else {
                if (!flag) {
                    flag = true;
                } else {
                    if (!isSameSide(nChessID, position[y][x])) {
                        addMove(j, i, x, y, nPly);
                    }
                    break;
                }
            }
            x--;
        }
        x = j;
        y = i + 1;
        flag = false;
        while (y < 10) {
            if (Eveluation.NOCHESS == position[y][x]) {
                if (!flag) {
                    addMove(j, i, x, y, nPly);
                }
            } else {
                if (!flag) {
                    flag = true;
                } else {
                    if (!isSameSide(nChessID, position[y][x])) {
                        addMove(j, i, x, y, nPly);
                    }
                    break;
                }
            }
            y++;
        }

        y = i - 1;	//
        flag = false;
        while (y >= 0) {
            if (Eveluation.NOCHESS == position[y][x]) {
                if (!flag) {
                    addMove(j, i, x, y, nPly);
                }
            } else {
                if (!flag) {
                    flag = true;
                } else {
                    if (!isSameSide(nChessID, position[y][x])) {
                        addMove(j, i, x, y, nPly);
                    }
                    break;
                }
            }
            y--;
        }

    }

    int createPossibleMove(byte[][] position, int nPly, int nSide) {
        int nChessID;
        int i, j;
        m_nMoveCount = 0;
        for (i = 0; i < 10; i++) {
            for (j = 0; j < 9; j++) {
                if (position[i][j] != Eveluation.NOCHESS) {
                    nChessID = position[i][j];
                    if (!(nSide != 0) && isRed(nChessID)) {
                        continue;
                    }
                    if ((nSide != 0) && isBlack(nChessID)) {
                        continue;
                    }
                    switch (nChessID) {
                        case Eveluation.R_KING:
                        case Eveluation.B_KING:
                            gen_KingMove(position, i, j, nPly);
                            break;

                        case Eveluation.R_BISHOP:
                            gen_RBishopMove(position, i, j, nPly);
                            break;

                        case Eveluation.B_BISHOP:
                            gen_BBishopMove(position, i, j, nPly);
                            break;

                        case Eveluation.R_ELEPHANT:
                        case Eveluation.B_ELEPHANT:
                            gen_ElephantMove(position, i, j, nPly);
                            break;

                        case Eveluation.R_HORSE:
                        case Eveluation.B_HORSE:
                            gen_HorseMove(position, i, j, nPly);
                            break;

                        case Eveluation.R_CAR:
                        case Eveluation.B_CAR:
                            gen_CarMove(position, i, j, nPly);
                            break;

                        case Eveluation.R_PAWN:
                            gen_RPawnMove(position, i, j, nPly);
                            break;

                        case Eveluation.B_PAWN:
                            gen_BPawnMove(position, i, j, nPly);
                            break;

                        case Eveluation.B_CANON:
                        case Eveluation.R_CANON:
                            gen_CanonMove(position, i, j, nPly);
                            break;

                        default:
                            break;

                    }
                }
            }
        }
        return m_nMoveCount;
    }
}
