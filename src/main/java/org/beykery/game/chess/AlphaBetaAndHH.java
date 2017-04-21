/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.beykery.game.chess;

/**
 *
 * @author beykery
 */
public final class AlphaBetaAndHH extends SearchEngine {

    int[][] m_HistoryTable = new int[90][90];
    Chessmove[] m_TargetBuff = new Chessmove[100];

    public void resetHistoryTable() {
        for (int i = 0; i < m_HistoryTable.length; i++) {
            for (int j = 0; j < m_HistoryTable[i].length; j++) {
                m_HistoryTable[i][j] = 10;
            }
        }
    }

    public int getHistoryScore(Chessmove move) {
        int nFrom, nTo;
        nFrom = move.from.y * 9 + move.from.x;
        nTo = move.to.y * 9 + move.to.x;
        return m_HistoryTable[nFrom][nTo];
    }

    void enterHistoryScore(Chessmove move, int depth) {
        int nFrom, nTo;
        nFrom = move.from.y * 9 + move.from.x;
        nTo = move.to.y * 9 + move.to.x;
        m_HistoryTable[nFrom][nTo] += (2 << depth);
    }

    void merge(Chessmove[] source, Chessmove[] target, int l, int m, int r) {//从小到大排序
        int i = l;
        int j = m + 1;
        int k = l;

        while ((i <= m) && (j <= r)) {
            if (source[i].score <= source[j].score) {
                target[k++] = source[i++];
            } else {
                target[k++] = source[j++];
            }
        }

        if (i > m) {
            for (int q = j; q <= r; q++) {
                target[k++] = source[q];
            }
        } else {
            for (int q = i; q <= m; q++) {
                target[k++] = source[q];
            }
        }
    }

    void mergePass(Chessmove[] source, Chessmove[] target, final int s, final int n, final boolean direction) {
        int i = 0;
        while (i <= n - 2 * s) {
            //合并大小为s的相邻二段子数组
            if (direction) {
                merge(source, target, i, i + s - 1, i + 2 * s - 1);
            } else {
                merge_A(source, target, i, i + s - 1, i + 2 * s - 1);
            }
            i = i + 2 * s;
        }

        if (i + s < n) //剩余的元素个数小於2s
        {
            if (direction) {
                merge(source, target, i, i + s - 1, n - 1);
            } else {
                merge_A(source, target, i, i + s - 1, n - 1);
            }
        } else {
            for (int j = i; j <= n - 1; j++) {
                target[j] = source[j];
            }
        }
    }

    void merge_A(Chessmove[] source, Chessmove[] target, int l, int m, int r) {//从大到小排序
        int i = l;
        int j = m + 1;
        int k = l;

        while ((i <= m) && (j <= r)) {
            if (source[i].score >= source[j].score) {
                target[k++] = source[i++];
            } else {
                target[k++] = source[j++];
            }
        }

        if (i > m) {
            for (int q = j; q <= r; q++) {
                target[k++] = source[q];
            }
        } else {
            for (int q = i; q <= m; q++) {
                target[k++] = source[q];
            }
        }
    }

    public void mergeSort(Chessmove[] source, int n, boolean direction) {
        int s = 1;
        while (s < n) {
            mergePass(source, m_TargetBuff, s, n, direction);
            s += s;
            mergePass(m_TargetBuff, source, s, n, direction);
            s += s;
        }
    }

    @Override
    public void searchAGoodMove(byte[][] position) {
        for (int i = 0; i < position.length; i++) {
            System.arraycopy(position[i], 0, curPosition[i], 0, position[i].length);
        }
        m_nMaxDepth = m_nSearchDepth;
        resetHistoryTable();
        alphabeta(m_nMaxDepth, -20000, 20000);
        makeMove(m_cmBestMove);
        for (int i = 0; i < curPosition.length; i++) {
            System.arraycopy(curPosition[i], 0, position[i], 0, curPosition[i].length);
        }
    }

   private  int alphabeta(int depth, int alpha, int beta) {
        int score;
        int Count, i;
        byte type;
        i = isGameOver(curPosition, depth);
        if (i != 0) {
            return i;
        }
        if (depth <= 0) //叶子节点取估值
        {
            return m_pEval.eveluate(curPosition, ((m_nMaxDepth - depth) % 2) != 0);
        }
        Count = m_pMG.createPossibleMove(curPosition, depth, ((m_nMaxDepth - depth) % 2));
        for (i = 0; i < Count; i++) {
            m_pMG.m_MoveList[depth][i].score = getHistoryScore(m_pMG.m_MoveList[depth][i]);
        }
        mergeSort(m_pMG.m_MoveList[depth], Count, false);
        int bestmove = -1;
        for (i = 0; i < Count; i++) {
            type = makeMove(m_pMG.m_MoveList[depth][i]);
            score = -alphabeta(depth - 1, -beta, -alpha);
            unMakeMove(m_pMG.m_MoveList[depth][i], type);
            if (score > alpha) {
                alpha = score;
                if (depth == m_nMaxDepth) {
                    m_cmBestMove = m_pMG.m_MoveList[depth][i];
                }
                bestmove = i;
            }
            if (alpha >= beta) {
                bestmove = i;
                break;
            }
        }
        if (bestmove != -1) {
            enterHistoryScore(m_pMG.m_MoveList[depth][bestmove], depth);
        }
        return alpha;
    }
}
