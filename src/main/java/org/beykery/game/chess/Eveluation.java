/**
 * 估值，王小春先生的算法
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
// Eveluation.h: interface for the CEveluation class.
//
//////////////////////////////////////////////////////////////////////
public final class Eveluation
{

  public static final int BASEVALUE_PAWN = 100;
  public static final int BASEVALUE_BISHOP = 250;
  public static final int BASEVALUE_ELEPHANT = 250;
  public static final int BASEVALUE_CAR = 500;
  public static final int BASEVALUE_HORSE = 350;
  public static final int BASEVALUE_CANON = 350;
  public static final int BASEVALUE_KING = 10000;
  public static final int FLEXIBILITY_PAWN = 15;
  public static final int FLEXIBILITY_BISHOP = 1;
  public static final int FLEXIBILITY_ELEPHANT = 1;
  public static final int FLEXIBILITY_CAR = 6;
  public static final int FLEXIBILITY_HORSE = 12;
  public static final int FLEXIBILITY_CANON = 6;
  public static final int FLEXIBILITY_KING = 0;
  public static final int NOCHESS = 0;    //没有棋子
  public static final int B_KING = 1; //黑帅
  public static final int B_CAR = 2; //黑车
  public static final int B_HORSE = 3; //黑马
  public static final int B_CANON = 4; //黑炮
  public static final int B_BISHOP = 5; //黑士
  public static final int B_ELEPHANT = 6; //黑象
  public static final int B_PAWN = 7;  //黑卒
  public static final int B_BEGIN = B_KING;
  public static final int B_END = B_PAWN;
  public static final int R_KING = 8;  //红帅
  public static final int R_CAR = 9; //红车
  public static final int R_HORSE = 10;  //红马
  public static final int R_CANON = 11; //红炮
  public static final int R_BISHOP = 12; //红士
  public static final int R_ELEPHANT = 13; //红象
  public static final int R_PAWN = 14; //红兵
  public static final int R_BEGIN = R_KING;
  public static final int R_END = R_PAWN;
  public final int[][] BA0 =
  {
    {
      0, 0, 0, 0, 0, 0, 0, 0, 0
    },
    {
      90, 90, 110, 120, 120, 120, 110, 90, 90
    },
    {
      90, 90, 110, 120, 120, 120, 110, 90, 90
    },
    {
      70, 90, 110, 110, 110, 110, 110, 90, 70
    },
    {
      70, 70, 70, 70, 70, 70, 70, 70, 70
    },
    {
      0, 0, 0, 0, 0, 0, 0, 0, 0
    },
    {
      0, 0, 0, 0, 0, 0, 0, 0, 0
    },
    {
      0, 0, 0, 0, 0, 0, 0, 0, 0
    },
    {
      0, 0, 0, 0, 0, 0, 0, 0, 0
    },
    {
      0, 0, 0, 0, 0, 0, 0, 0, 0
    },
  };
  public final int[][] BA1 =
  {
    {
      0, 0, 0, 0, 0, 0, 0, 0, 0
    },
    {
      0, 0, 0, 0, 0, 0, 0, 0, 0
    },
    {
      0, 0, 0, 0, 0, 0, 0, 0, 0
    },
    {
      0, 0, 0, 0, 0, 0, 0, 0, 0
    },
    {
      0, 0, 0, 0, 0, 0, 0, 0, 0
    },
    {
      70, 70, 70, 70, 70, 70, 70, 70, 70
    },
    {
      70, 90, 110, 110, 110, 110, 110, 90, 70
    },
    {
      90, 90, 110, 120, 120, 120, 110, 90, 90
    },
    {
      90, 90, 110, 120, 120, 120, 110, 90, 90
    },
    {
      0, 0, 0, 0, 0, 0, 0, 0, 0
    },
  };
  int[] m_BaseValue = new int[15];
  int[] m_FlexValue = new int[15];
  short[][] m_AttackPos = new short[10][9];
  byte[][] m_GuardPos = new byte[10][9];
  byte[][] m_FlexibilityPos = new byte[10][9];
  int[][] m_chessValue = new int[10][9];
  int nPosCount;
  Point[] RelatePos = new Point[20];
  public static int count = 0;

  public Eveluation()
  {
    m_BaseValue[B_KING] = BASEVALUE_KING;
    m_BaseValue[B_CAR] = BASEVALUE_CAR;
    m_BaseValue[B_HORSE] = BASEVALUE_HORSE;
    m_BaseValue[B_BISHOP] = BASEVALUE_BISHOP;
    m_BaseValue[B_ELEPHANT] = BASEVALUE_ELEPHANT;
    m_BaseValue[B_CANON] = BASEVALUE_CANON;
    m_BaseValue[B_PAWN] = BASEVALUE_PAWN;

    m_BaseValue[R_KING] = BASEVALUE_KING;
    m_BaseValue[R_CAR] = BASEVALUE_CAR;
    m_BaseValue[R_HORSE] = BASEVALUE_HORSE;
    m_BaseValue[R_BISHOP] = BASEVALUE_BISHOP;
    m_BaseValue[R_ELEPHANT] = BASEVALUE_ELEPHANT;
    m_BaseValue[R_CANON] = BASEVALUE_CANON;
    m_BaseValue[R_PAWN] = BASEVALUE_PAWN;

    m_FlexValue[B_KING] = FLEXIBILITY_KING;
    m_FlexValue[B_CAR] = FLEXIBILITY_CAR;
    m_FlexValue[B_HORSE] = FLEXIBILITY_HORSE;
    m_FlexValue[B_BISHOP] = FLEXIBILITY_BISHOP;
    m_FlexValue[B_ELEPHANT] = FLEXIBILITY_ELEPHANT;
    m_FlexValue[B_CANON] = FLEXIBILITY_CANON;
    m_FlexValue[B_PAWN] = FLEXIBILITY_PAWN;

    m_FlexValue[R_KING] = FLEXIBILITY_KING;
    m_FlexValue[R_CAR] = FLEXIBILITY_CAR;
    m_FlexValue[R_HORSE] = FLEXIBILITY_HORSE;
    m_FlexValue[R_BISHOP] = FLEXIBILITY_BISHOP;
    m_FlexValue[R_ELEPHANT] = FLEXIBILITY_ELEPHANT;
    m_FlexValue[R_CANON] = FLEXIBILITY_CANON;
    m_FlexValue[R_PAWN] = FLEXIBILITY_PAWN;
  }

  int getBingValue(int x, int y, byte[][] curSituation)
  {
    if (curSituation[y][x] == R_PAWN)
    {
      return BA0[y][x];
    }
    if (curSituation[y][x] == B_PAWN)
    {
      return BA1[y][x];
    }
    return 0;
  }

  public int eveluate(byte[][] position, boolean bIsRedTurn)
  {
    int i, j, k;
    int nChessType, nTargetType;
    count++;
    m_chessValue = new int[10][9];
    m_AttackPos = new short[10][9];
    m_GuardPos = new byte[10][9];
    m_FlexibilityPos = new byte[10][9];
    for (i = 0; i < 10; i++)
    {
      for (j = 0; j < 9; j++)
      {
        if (position[i][j] != NOCHESS)
        {
          nChessType = position[i][j];
          getRelatePiece(position, j, i);
          for (k = 0; k < nPosCount; k++)
          {
            nTargetType = position[RelatePos[k].y][RelatePos[k].x];
            if (nTargetType == NOCHESS)
            {
              m_FlexibilityPos[i][j]++;
            } else if (MoveGenerator.isSameSide(nChessType, nTargetType))
            {
              m_GuardPos[RelatePos[k].y][RelatePos[k].x]++;
            } else
            {
              m_AttackPos[RelatePos[k].y][RelatePos[k].x]++;
              m_FlexibilityPos[i][j]++;
              switch (nTargetType)
              {
                case R_KING:
                  if (!bIsRedTurn)
                  {
                    return 18888;
                  }
                  break;
                case B_KING:
                  if (bIsRedTurn)
                  {
                    return 18888;
                  }
                  break;
                default:
                  m_AttackPos[RelatePos[k].y][RelatePos[k].x] += (30 + (m_BaseValue[nTargetType] - m_BaseValue[nChessType]) / 10) / 10;
                  break;
              }
            }
          }
        }
      }
    }

    for (i = 0; i < 10; i++)
    {
      for (j = 0; j < 9; j++)
      {
        if (position[i][j] != NOCHESS)
        {
          nChessType = position[i][j];
          m_chessValue[i][j]++;
          m_chessValue[i][j] += m_FlexValue[nChessType] * m_FlexibilityPos[i][j];
          m_chessValue[i][j] += getBingValue(j, i, position);
        }
      }
    }
    int nHalfvalue;
    for (i = 0; i < 10; i++)
    {
      for (j = 0; j < 9; j++)
      {
        if (position[i][j] != NOCHESS)
        {
          nChessType = position[i][j];
          nHalfvalue = m_BaseValue[nChessType] / 16;
          m_chessValue[i][j] += m_BaseValue[nChessType];
          if (MoveGenerator.isRed(nChessType))
          {
            if (m_AttackPos[i][j] != 0)
            {
              if (bIsRedTurn)
              {
                if (nChessType == R_KING)
                {
                  m_chessValue[i][j] -= 20;
                } else
                {
                  m_chessValue[i][j] -= nHalfvalue * 2;
                  if (m_GuardPos[i][j] != 0)
                  {
                    m_chessValue[i][j] += nHalfvalue;
                  }
                }
              } else
              {
                if (nChessType == R_KING)
                {
                  return 18888;
                }
                m_chessValue[i][j] -= nHalfvalue * 10;
                if (m_GuardPos[i][j] != 0)
                {
                  m_chessValue[i][j] += nHalfvalue * 9;
                }
              }
              m_chessValue[i][j] -= m_AttackPos[i][j];
            } else if (m_GuardPos[i][j] != 0)
            {
              m_chessValue[i][j] += 5;
            }
          } else if (m_AttackPos[i][j] != 0)
          {
            if (!bIsRedTurn)
            {
              if (nChessType == B_KING)
              {
                m_chessValue[i][j] -= 20;
              } else
              {
                m_chessValue[i][j] -= nHalfvalue * 2;
                if (m_GuardPos[i][j] != 0)
                {
                  m_chessValue[i][j] += nHalfvalue;
                }
              }
            } else
            {
              if (nChessType == B_KING)
              {
                return 18888;
              }
              m_chessValue[i][j] -= nHalfvalue * 10;
              if (m_GuardPos[i][j] != 0)
              {
                m_chessValue[i][j] += nHalfvalue * 9;
              }
            }
            m_chessValue[i][j] -= m_AttackPos[i][j];
          } else if (m_GuardPos[i][j] != 0)
          {
            m_chessValue[i][j] += 5;
          }
        }
      }
    }
    int nRedValue = 0;
    int nBlackValue = 0;
    for (i = 0; i < 10; i++)
    {
      for (j = 0; j < 9; j++)
      {
        nChessType = position[i][j];
//			if (nChessType == R_KING || nChessType == B_KING)
//				m_chessValue[i][j] = 10000;	
        if (nChessType != NOCHESS)
        {
          if (MoveGenerator.isRed(nChessType))
          {
            nRedValue += m_chessValue[i][j];
          } else
          {
            nBlackValue += m_chessValue[i][j];
          }
        }
      }
    }
    if (bIsRedTurn)
    {
      return nRedValue - nBlackValue;
    } else
    {
      return nBlackValue - nRedValue;
    }
  }

  int getRelatePiece(byte[][] position, int j, int i)
  {
    nPosCount = 0;
    byte nChessID;
    boolean flag;
    int x, y;
    nChessID = position[i][j];
    switch (nChessID)
    {
      case R_KING:
      case B_KING:
        for (y = 0; y < 3; y++)
        {
          for (x = 3; x < 6; x++)
          {
            if (canTouch(position, j, i, x, y))
            {
              addPoint(x, y);
            }
          }
        }
        for (y = 7; y < 10; y++)
        {
          for (x = 3; x < 6; x++)
          {
            if (canTouch(position, j, i, x, y))
            {
              addPoint(x, y);
            }
          }
        }
        break;
      case R_BISHOP:

        for (y = 7; y < 10; y++)
        {
          for (x = 3; x < 6; x++)
          {
            if (canTouch(position, j, i, x, y))
            {
              addPoint(x, y);
            }
          }
        }
        break;

      case B_BISHOP:

        for (y = 0; y < 3; y++)
        {
          for (x = 3; x < 6; x++)
          {
            if (canTouch(position, j, i, x, y))
            {
              addPoint(x, y);
            }
          }
        }
        break;

      case R_ELEPHANT:
      case B_ELEPHANT:

        x = j + 2;
        y = i + 2;
        if (x < 9 && y < 10 && canTouch(position, j, i, x, y))
        {
          addPoint(x, y);
        }

        x = j + 2;
        y = i - 2;
        if (x < 9 && y >= 0 && canTouch(position, j, i, x, y))
        {
          addPoint(x, y);
        }

        x = j - 2;
        y = i + 2;
        if (x >= 0 && y < 10 && canTouch(position, j, i, x, y))
        {
          addPoint(x, y);
        }

        x = j - 2;
        y = i - 2;
        if (x >= 0 && y >= 0 && canTouch(position, j, i, x, y))
        {
          addPoint(x, y);
        }
        break;

      case R_HORSE:
      case B_HORSE:
        x = j + 2;
        y = i + 1;
        if ((x < 9 && y < 10) && canTouch(position, j, i, x, y))
        {
          addPoint(x, y);
        }

        x = j + 2;
        y = i - 1;
        if ((x < 9 && y >= 0) && canTouch(position, j, i, x, y))
        {
          addPoint(x, y);
        }

        x = j - 2;
        y = i + 1;
        if ((x >= 0 && y < 10) && canTouch(position, j, i, x, y))
        {
          addPoint(x, y);
        }

        x = j - 2;
        y = i - 1;
        if ((x >= 0 && y >= 0) && canTouch(position, j, i, x, y))
        {
          addPoint(x, y);
        }

        x = j + 1;
        y = i + 2;
        if ((x < 9 && y < 10) && canTouch(position, j, i, x, y))
        {
          addPoint(x, y);
        }
        x = j - 1;
        y = i + 2;
        if ((x >= 0 && y < 10) && canTouch(position, j, i, x, y))
        {
          addPoint(x, y);
        }
        x = j + 1;
        y = i - 2;
        if ((x < 9 && y >= 0) && canTouch(position, j, i, x, y))
        {
          addPoint(x, y);
        }
        x = j - 1;
        y = i - 2;
        if ((x >= 0 && y >= 0) && canTouch(position, j, i, x, y))
        {
          addPoint(x, y);
        }
        break;

      case R_CAR:
      case B_CAR:
        x = j + 1;
        y = i;
        while (x < 9)
        {
          if (NOCHESS == position[y][x])
          {
            addPoint(x, y);
          } else
          {
            addPoint(x, y);
            break;
          }
          x++;
        }

        x = j - 1;
        y = i;
        while (x >= 0)
        {
          if (NOCHESS == position[y][x])
          {
            addPoint(x, y);
          } else
          {
            addPoint(x, y);
            break;
          }
          x--;
        }

        x = j;
        y = i + 1;//
        while (y < 10)
        {
          if (NOCHESS == position[y][x])
          {
            addPoint(x, y);
          } else
          {
            addPoint(x, y);
            break;
          }
          y++;
        }

        x = j;
        y = i - 1;//よ
        while (y >= 0)
        {
          if (NOCHESS == position[y][x])
          {
            addPoint(x, y);
          } else
          {
            addPoint(x, y);
            break;
          }
          y--;
        }
        break;

      case R_PAWN:
        y = i - 1;
        x = j;

        if (y >= 0)
        {
          addPoint(x, y);
        }

        if (i < 5)
        {
          y = i;
          x = j + 1;
          if (x < 9)
          {
            addPoint(x, y);
          }
          x = j - 1;
          if (x >= 0)
          {
            addPoint(x, y);
          }
        }
        break;

      case B_PAWN:
        y = i + 1;
        x = j;

        if (y < 10)
        {
          addPoint(x, y);
        }

        if (i > 4)
        {
          y = i;
          x = j + 1;
          if (x < 9)
          {
            addPoint(x, y);
          }
          x = j - 1;
          if (x >= 0)
          {
            addPoint(x, y);
          }
        }
        break;

      case B_CANON:
      case R_CANON:

        x = j + 1;		//
        y = i;
        flag = false;
        while (x < 9)
        {
          if (NOCHESS == position[y][x])
          {
            if (!flag)
            {
              addPoint(x, y);
            }
          } else if (!flag)
          {
            flag = true;
          } else
          {
            addPoint(x, y);
            break;
          }
          x++;
        }

        x = j - 1;
        flag = false;
        while (x >= 0)
        {
          if (NOCHESS == position[y][x])
          {
            if (!flag)
            {
              addPoint(x, y);
            }
          } else if (!flag)
          {
            flag = true;
          } else
          {
            addPoint(x, y);
            break;
          }
          x--;
        }
        x = j;
        y = i + 1;
        flag = false;
        while (y < 10)
        {
          if (NOCHESS == position[y][x])
          {
            if (!flag)
            {
              addPoint(x, y);
            }
          } else if (!flag)
          {
            flag = true;
          } else
          {
            addPoint(x, y);
            break;
          }
          y++;
        }

        y = i - 1;	//
        flag = false;
        while (y >= 0)
        {
          if (NOCHESS == position[y][x])
          {
            if (!flag)
            {
              addPoint(x, y);
            }
          } else if (!flag)
          {
            flag = true;
          } else
          {
            addPoint(x, y);
            break;
          }
          y--;
        }
        break;

      default:
        break;

    }
    return nPosCount;
  }

  void addPoint(int x, int y)
  {
    Point p = new Point(x, y);
    RelatePos[nPosCount] = p;
    nPosCount++;
  }

  boolean canTouch(byte[][] position, int nFromX, int nFromY, int nToX, int nToY)
  {
    int i = 0, j = 0;
    int nMoveChessID, nTargetID;
    if (nFromY == nToY && nFromX == nToX)
    {
      return false;//目的与源相同
    }
    nMoveChessID = position[nFromY][nFromX];
    nTargetID = position[nToY][nToX];
    switch (nMoveChessID)
    {
      case B_KING:
        if (nTargetID == R_KING)//老将见面?
        {
          if (nFromX != nToX)
          {
            return false;
          }
          for (i = nFromY + 1; i < nToY; i++)
          {
            if (position[i][nFromX] != NOCHESS)
            {
              return false;
            }
          }
        } else
        {
          if (nToY > 2 || nToX > 5 || nToX < 3)
          {
            return false;//目标点在九宫之外
          }
          if (Math.abs(nFromY - nToY) + Math.abs(nToX - nFromX) > 1)
          {
            return false;//将帅只走一步直线:
          }
        }
        break;
      case R_BISHOP:

        if (nToY < 7 || nToX > 5 || nToX < 3)
        {
          return false;//士出九宫	
        }
        if (Math.abs(nFromY - nToY) != 1 || Math.abs(nToX - nFromX) != 1)
        {
          return false;	//士走斜线
        }
        break;

      case B_BISHOP:   //黑士

        if (nToY > 2 || nToX > 5 || nToX < 3)
        {
          return false;//士出九宫	
        }
        if (Math.abs(nFromY - nToY) != 1 || Math.abs(nToX - nFromX) != 1)
        {
          return false;	//士走斜线
        }
        break;

      case R_ELEPHANT://红象

        if (nToY < 5)
        {
          return false;//相不能过河
        }
        if (Math.abs(nFromX - nToX) != 2 || Math.abs(nFromY - nToY) != 2)
        {
          return false;//相走田字
        }
        if (position[(nFromY + nToY) / 2][(nFromX + nToX) / 2] != NOCHESS)
        {
          return false;//相眼被塞住了
        }
        break;

      case B_ELEPHANT://黑象 

        if (nToY > 4)
        {
          return false;//相不能过河
        }
        if (Math.abs(nFromX - nToX) != 2 || Math.abs(nFromY - nToY) != 2)
        {
          return false;//相走田字
        }
        if (position[(nFromY + nToY) / 2][(nFromX + nToX) / 2] != NOCHESS)
        {
          return false;//相眼被塞住了
        }
        break;

      case B_PAWN:     //黑兵

        if (nToY < nFromY)
        {
          return false;//兵不回头
        }
        if (nFromY < 5 && nFromY == nToY)
        {
          return false;//兵过河前只能直走
        }
        if (nToY - nFromY + Math.abs(nToX - nFromX) > 1)
        {
          return false;//兵只走一步直线:
        }
        break;

      case R_PAWN:    //红兵

        if (nToY > nFromY)
        {
          return false;//兵不回头
        }
        if (nFromY > 4 && nFromY == nToY)
        {
          return false;//兵过河前只能直走
        }
        if (nFromY - nToY + Math.abs(nToX - nFromX) > 1)
        {
          return false;//兵只走一步直线:
        }
        break;

      case R_KING:
        if (nTargetID == B_KING)//老将见面?
        {
          if (nFromX != nToX)
          {
            return false;//两个将不在同一列
          }
          for (i = nFromY - 1; i > nToY; i--)
          {
            if (position[i][nFromX] != NOCHESS)
            {
              return false;//中间有别的子
            }
          }
        } else
        {
          if (nToY < 7 || nToX > 5 || nToX < 3)
          {
            return false;//目标点在九宫之外
          }
          if (Math.abs(nFromY - nToY) + Math.abs(nToX - nFromX) > 1)
          {
            return false;//将帅只走一步直线:
          }
        }
        break;

      case B_CAR:
      case R_CAR:

        if (nFromY != nToY && nFromX != nToX)
        {
          return false;	//车走直线:
        }
        if (nFromY == nToY)
        {
          if (nFromX < nToX)
          {
            for (i = nFromX + 1; i < nToX; i++)
            {
              if (position[nFromY][i] != NOCHESS)
              {
                return false;
              }
            }
          } else
          {
            for (i = nToX + 1; i < nFromX; i++)
            {
              if (position[nFromY][i] != NOCHESS)
              {
                return false;
              }
            }
          }
        } else if (nFromY < nToY)
        {
          for (j = nFromY + 1; j < nToY; j++)
          {
            if (position[j][nFromX] != NOCHESS)
            {
              return false;
            }
          }
        } else
        {
          for (j = nToY + 1; j < nFromY; j++)
          {
            if (position[j][nFromX] != NOCHESS)
            {
              return false;
            }
          }
        }

        break;

      case B_HORSE:
      case R_HORSE:

        if (!((Math.abs(nToX - nFromX) == 1 && Math.abs(nToY - nFromY) == 2)
                || (Math.abs(nToX - nFromX) == 2 && Math.abs(nToY - nFromY) == 1)))
        {
          return false;//马走日字
        }
        if (nToX - nFromX == 2)
        {
          i = nFromX + 1;
          j = nFromY;
        } else if (nFromX - nToX == 2)
        {
          i = nFromX - 1;
          j = nFromY;
        } else if (nToY - nFromY == 2)
        {
          i = nFromX;
          j = nFromY + 1;
        } else if (nFromY - nToY == 2)
        {
          i = nFromX;
          j = nFromY - 1;
        }

        if (position[j][i] != (byte) NOCHESS)
        {
          return false;//绊马腿
        }
        break;
      case B_CANON:
      case R_CANON:

        if (nFromY != nToY && nFromX != nToX)
        {
          return false;	//炮走直线:
        }
        //炮不吃子时经过的路线中不能有棋子:------------------

        if (position[nToY][nToX] == NOCHESS)
        {
          if (nFromY == nToY)
          {
            if (nFromX < nToX)
            {
              for (i = nFromX + 1; i < nToX; i++)
              {
                if (position[nFromY][i] != NOCHESS)
                {
                  return false;
                }
              }
            } else
            {
              for (i = nToX + 1; i < nFromX; i++)
              {
                if (position[nFromY][i] != NOCHESS)
                {
                  return false;
                }
              }
            }
          } else if (nFromY < nToY)
          {
            for (j = nFromY + 1; j < nToY; j++)
            {
              if (position[j][nFromX] != NOCHESS)
              {
                return false;
              }
            }
          } else
          {
            for (j = nToY + 1; j < nFromY; j++)
            {
              if (position[j][nFromX] != NOCHESS)
              {
                return false;
              }
            }
          }
        } //以上是炮不吃子-------------------------------------
        //吃子时:=======================================
        else
        {
          int nCount = 0;
          if (nFromY == nToY)
          {
            if (nFromX < nToX)
            {
              for (i = nFromX + 1; i < nToX; i++)
              {
                if (position[nFromY][i] != NOCHESS)
                {
                  nCount++;
                }
              }
              if (nCount != 1)
              {
                return false;
              }
            } else
            {
              for (i = nToX + 1; i < nFromX; i++)
              {
                if (position[nFromY][i] != NOCHESS)
                {
                  nCount++;
                }
              }
              if (nCount != 1)
              {
                return false;
              }
            }
          } else if (nFromY < nToY)
          {
            for (j = nFromY + 1; j < nToY; j++)
            {
              if (position[j][nFromX] != NOCHESS)
              {
                nCount++;
              }
            }
            if (nCount != 1)
            {
              return false;
            }
          } else
          {
            for (j = nToY + 1; j < nFromY; j++)
            {
              if (position[j][nFromX] != NOCHESS)
              {
                nCount++;
              }
            }
            if (nCount != 1)
            {
              return false;
            }
          }
        }
        //以上是炮吃子时================================
        break;
      default:
        return false;
    }
    return true;
  }
}
