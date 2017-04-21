/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.beykery.game.chess;

/**
 *
 * @author beykery
 */
public final class NegamaxEngine extends SearchEngine {

    @Override
    public void searchAGoodMove(byte[][] position) {
        m_nMaxDepth = m_nSearchDepth;
        for (int i = 0; i < position.length; i++) {
            System.arraycopy(position[i], 0, curPosition[i], 0, position[i].length);
        }
        negaMax(m_nMaxDepth);
        makeMove(m_cmBestMove);
        for (int i = 0; i < curPosition.length; i++) {
            System.arraycopy(curPosition[i], 0, position[i], 0, curPosition[i].length);
        }
    }

    private int negaMax(int depth) {
        int current = -20000;
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
            score = -negaMax(depth - 1);
            unMakeMove(m_pMG.m_MoveList[depth][i], type);
            if (score > current) {
                current = score;
                if (depth == m_nMaxDepth) {
                    m_cmBestMove = m_pMG.m_MoveList[depth][i];
                }
            }
        }
        return current;
    }
}
