/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.beykery.game.chess;

/**
 *
 * @author beykery
 */
public final class AlphaBetaEngine extends SearchEngine {

    @Override
    public void searchAGoodMove(byte[][] position) {
        for (int i = 0; i < position.length; i++) {
            System.arraycopy(position[i], 0, curPosition[i], 0, position[i].length);
        }
        m_nMaxDepth = m_nSearchDepth;
        alphabeta(m_nMaxDepth, -20000, 20000);
        makeMove(m_cmBestMove);
        for (int i = 0; i < curPosition.length; i++) {
            System.arraycopy(curPosition[i], 0, position[i], 0, curPosition[i].length);
        }
    }

    private int alphabeta(int depth, int alpha, int beta) {
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
        Count = m_pMG.createPossibleMove(curPosition, depth, (m_nMaxDepth - depth) % 2);
        for (i = 0; i < Count; i++) {
            type = makeMove(m_pMG.m_MoveList[depth][i]);
            score = -alphabeta(depth - 1, -beta, -alpha);
            unMakeMove(m_pMG.m_MoveList[depth][i], type);
            if (score > alpha) {
                alpha = score;
                if (depth == m_nMaxDepth) {
                    m_cmBestMove = m_pMG.m_MoveList[depth][i];
                }
            }
            if (alpha >= beta) {
                break;
            }
        }
        return alpha;
    }
}
