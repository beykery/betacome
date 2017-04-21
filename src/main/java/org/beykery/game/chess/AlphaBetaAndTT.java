/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.beykery.game.chess;

/**
 * alphabeta+置换表搜索
 *
 * @author beykery
 */
public final class AlphaBetaAndTT extends SearchEngine {

    private TranspositionTable tt;

    public AlphaBetaAndTT() {
        tt = new TranspositionTable();
    }

    @Override
    public void searchAGoodMove(byte[][] position) {
        for (int i = 0; i < position.length; i++) {
            System.arraycopy(position[i], 0, curPosition[i], 0, position[i].length);
        }
        tt.calculateInitHashKey(curPosition);
        m_nMaxDepth = m_nSearchDepth;
        alphabeta(m_nMaxDepth, -20000, 20000);
        makeMove(m_cmBestMove);
        for (int i = 0; i < curPosition.length; i++) {
            System.arraycopy(curPosition[i], 0, position[i], 0, curPosition[i].length);
        }
    }

    public int alphabeta(int depth, int alpha, int beta) {
        int score;
        int Count, i;
        byte type;
        int side;
        i = isGameOver(curPosition, depth);
        if (i != 0) {
            return i;
        }
        side = (m_nMaxDepth - depth) % 2;
        score = this.tt.lookUpHashTable(alpha, beta, depth, side);
        if (score != 66666) {
            return score;
        }
        if (depth <= 0) //叶子节点取估值
        {
            score = m_pEval.eveluate(curPosition, side != 0);
            tt.enterHashTable(TranspositionTable.ENTRY_TYPE.exact, (short) score, (short) depth, side);
            return score;
        }
        Count = m_pMG.createPossibleMove(curPosition, depth, side);
        int eval_is_exact = 0;
        for (i = 0; i < Count; i++) {
            tt.hash_makeMove(m_pMG.m_MoveList[depth][i], curPosition);
            type = makeMove(m_pMG.m_MoveList[depth][i]);
            score = -alphabeta(depth - 1, -beta, -alpha);
            tt.hash_unMakeMove(m_pMG.m_MoveList[depth][i], type, curPosition);
            unMakeMove(m_pMG.m_MoveList[depth][i], type);
            if (score >= beta) {
                tt.enterHashTable(TranspositionTable.ENTRY_TYPE.lower_bound, (short) score, (short) depth, side);
                return score;
            }
            if (score > alpha) {
                alpha = score;
                eval_is_exact = 1;
                if (depth == m_nMaxDepth) {
                    m_cmBestMove = m_pMG.m_MoveList[depth][i];
                }
            }
        }
        if (eval_is_exact != 0) {
            tt.enterHashTable(TranspositionTable.ENTRY_TYPE.exact, (short) alpha, (short) depth, side);
        } else {
            tt.enterHashTable(TranspositionTable.ENTRY_TYPE.upper_bound, (short) alpha, (short) depth, side);
        }
        return alpha;
    }
}
