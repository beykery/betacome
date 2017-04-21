/**
 *
 */
package org.beykery.game.chess;

/**
 *
 * @author beykery
 */
public final class NegaScout_TT_HH extends SearchEngine
{

  private TranspositionTable tt;
  private AlphaBetaAndHH hh;

  public NegaScout_TT_HH()
  {
    tt = new TranspositionTable();
    hh = new AlphaBetaAndHH();
  }

  @Override
  public void searchAGoodMove(byte[][] position)
  {
    for (int i = 0; i < position.length; i++)
    {
      System.arraycopy(position[i], 0, curPosition[i], 0, position[i].length);
    }
    m_nMaxDepth = m_nSearchDepth;
    tt.calculateInitHashKey(curPosition);
    hh.resetHistoryTable();
//	m_nMaxDepth = 1;
//	NegaScout(m_nMaxDepth, -20000, 20000);
//	m_nMaxDepth = m_nSearchDepth;
//	for (m_nMaxDepth = 1; m_nMaxDepth <= m_nSearchDepth; m_nMaxDepth++)
    negaScout(m_nMaxDepth, -20000, 20000);
    makeMove(m_cmBestMove);
    for (int i = 0; i < curPosition.length; i++)
    {
      System.arraycopy(curPosition[i], 0, position[i], 0, curPosition[i].length);
    }
  }

  private int negaScout(int depth, int alpha, int beta)
  {
    int Count, i;
    byte type;
    int a, b, t;
    int side;
    int score;
    i = isGameOver(curPosition, depth);
    if (i != 0)
    {
      return i;
    }
    side = (m_nMaxDepth - depth) % 2;
    score = tt.lookUpHashTable(alpha, beta, depth, side);
    if (score != 66666)
    {
      return score;
    }
    if (depth <= 0) //叶子节点取估值
    {
      score = m_pEval.eveluate(curPosition, side != 0);
      tt.enterHashTable(TranspositionTable.ENTRY_TYPE.exact, (short) score, (short) depth, side);
      return score;
    }
    Count = m_pMG.createPossibleMove(curPosition, depth, side);
    for (i = 0; i < Count; i++)
    {
      m_pMG.m_MoveList[depth][i].score
              = hh.getHistoryScore(m_pMG.m_MoveList[depth][i]);
    }
    hh.mergeSort(m_pMG.m_MoveList[depth], Count, false);
    int bestmove = -1;
    a = alpha;
    b = beta;
    int eval_is_exact = 0;
    for (i = 0; i < Count; i++)
    {
      tt.hash_makeMove(m_pMG.m_MoveList[depth][i], curPosition);
      type = makeMove(m_pMG.m_MoveList[depth][i]);
      t = -negaScout(depth - 1, -b, -a);
      if (t > a && t < beta && i > 0)
      {
        a = -negaScout(depth - 1, -beta, -t);
        /*
                 * re-search
         */
        eval_is_exact = 1;
        if (depth == m_nMaxDepth)
        {
          m_cmBestMove = m_pMG.m_MoveList[depth][i];
        }
        bestmove = i;
      }
      tt.hash_unMakeMove(m_pMG.m_MoveList[depth][i], type, curPosition);
      unMakeMove(m_pMG.m_MoveList[depth][i], type);
      if (a < t)
      {
        eval_is_exact = 1;
        a = t;
        if (depth == m_nMaxDepth)
        {
          m_cmBestMove = m_pMG.m_MoveList[depth][i];
        }
      }
      if (a >= beta)
      {
        tt.enterHashTable(TranspositionTable.ENTRY_TYPE.lower_bound, (short) a, (short) depth, side);
        hh.enterHistoryScore(m_pMG.m_MoveList[depth][i], depth);
        return a;
      }
      b = a + 1;
      /*
             * set new null window
       */
    }
    if (bestmove != -1)
    {
      hh.enterHistoryScore(m_pMG.m_MoveList[depth][bestmove], depth);
    }
    if (eval_is_exact != 0)
    {
      tt.enterHashTable(TranspositionTable.ENTRY_TYPE.exact, (short) a, (short) depth, side);
    } else
    {
      tt.enterHashTable(TranspositionTable.ENTRY_TYPE.upper_bound, (short) a, (short) depth, side);
    }
    return a;
  }
}
