/**
 * 王小春先生的算法
 */
package org.beykery.game.chess;

// COPYRIGHT NOTES
// ---------------
// This source code is a part of chess which is an example of <Game Programing guide>.
// You may use, compile or redistribute it as part of your application 
// for free. 
// You cannot redistribute sources without the official agreement of the author. 
// If distribution of you application which contents code below was occured, place 
// e-mail <hidebug@hotmail.com> on it is to be appreciated.
// This code can be used WITHOUT ANY WARRANTIES on your own risk.
// 
// Spring Wang <hidebug@hotmail.com>
// ---------------
// 版权声明
// ---------------
// 本文件所含之代码是《人机博弈程序设计指南》的范例程序中国象棋的一部分
// 您可以免费的使用, 编译 或者作为您应用程序的一部分。 
// 但，您不能在未经作者书面许可的情况下分发此源代码。 
// 如果您的应用程序使用了这些代码，在您的应用程序界面上 
// 放入 e-mail <hidebug@hotmail.com> 是令人欣赏的做法。
// 此代码并不含有任何保证，使用者当自承风险。
// 
// 王小春 <hidebug@hotmail.com>
// SearchEngine.h: interface for the CSearchEngine class.
//
//////////////////////////////////////////////////////////////////////
public abstract class SearchEngine
{

  int m_nSearchDepth;
  int m_nMaxDepth;
  MoveGenerator m_pMG;
  Eveluation m_pEval;
  Chessmove m_cmBestMove;
  byte[][] curPosition;

  public SearchEngine()
  {
    curPosition = new byte[10][9];
  }

  public abstract void searchAGoodMove(byte[][] position);

  public void setSearchDepth(int nDepth)
  {
    m_nSearchDepth = nDepth;
  }

  public void setEveluator(Eveluation pEval)
  {
    m_pEval = pEval;
  }

  public void setMoveGenerator(MoveGenerator pMG)
  {
    m_pMG = pMG;
  }

  public byte makeMove(Chessmove move)
  {
    byte nChessID;
    nChessID = curPosition[move.to.y][move.to.x];
    curPosition[move.to.y][move.to.x] = curPosition[move.from.y][move.from.x];
    curPosition[move.from.y][move.from.x] = Eveluation.NOCHESS;
    return nChessID;
  }

  public void unMakeMove(Chessmove move, byte nChessID)
  {
    curPosition[move.from.y][move.from.x] = curPosition[move.to.y][move.to.x];
    curPosition[move.to.y][move.to.x] = nChessID;
  }

  public int isGameOver(byte[][] position, int nDepth)
  {
    int i, j;
    boolean RedLive = false, BlackLive = false;
    for (i = 7; i < 10; i++)
    {
      for (j = 3; j < 6; j++)
      {
        if (position[i][j] == Eveluation.B_KING)
        {
          BlackLive = true;
        }
        if (position[i][j] == Eveluation.R_KING)
        {
          RedLive = true;
        }
      }
    }

    for (i = 0; i < 3; i++)
    {
      for (j = 3; j < 6; j++)
      {
        if (position[i][j] == Eveluation.B_KING)
        {
          BlackLive = true;
        }
        if (position[i][j] == Eveluation.R_KING)
        {
          RedLive = true;
        }
      }
    }
    i = (m_nMaxDepth - nDepth + 1) % 2;
    if (!RedLive)
    {
      if (i != 0)
      {
        return 19990 + nDepth;
      } else
      {
        return -19990 - nDepth;
      }
    }
    if (!BlackLive)
    {
      if (i != 0)
      {
        return -19990 - nDepth;
      } else
      {
        return 19990 + nDepth;
      }
    }
    return 0;
  }
}
