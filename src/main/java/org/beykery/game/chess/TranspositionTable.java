/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.beykery.game.chess;

import java.util.Random;

/**
 *
 * @author beykery
 */
public final class TranspositionTable {

    enum ENTRY_TYPE {

        exact, lower_bound, upper_bound
    };
    int[][][] m_nHashKey32;
    long[][][] m_ulHashKey64;
    HashItem[][] m_pTT;
    int m_HashKey32;
    long m_HashKey64;

    /**
     * 初始化置换表
     */
    public TranspositionTable() {
        this.initializeHashKey();
    }

    private void initializeHashKey() {
        int i, j, k;
        Random r = new Random();
        this.m_pTT = new HashItem[2][];
        this.m_nHashKey32 = new int[15][10][9];
        this.m_ulHashKey64 = new long[15][10][9];
        for (i = 0; i < 15; i++) {
            for (j = 0; j < 10; j++) {
                for (k = 0; k < 9; k++) {
                    m_nHashKey32[i][j][k] = r.nextInt() & 0x7fffffff;
                    m_ulHashKey64[i][j][k] = r.nextLong() & 0x7fffffffffffffffl;
                }
            }
        }
        for (i = 0; i < m_pTT.length; i++) {
            m_pTT[i]=new HashItem[1024*1024];
            for (j = 0; j < m_pTT[i].length; j++) {
                m_pTT[i][j] = new HashItem();
            }
        }
    }

    public void calculateInitHashKey(byte[][] curPosition) {
        int j, k, nChessType;
        m_HashKey32 = 0;
        m_HashKey32 = 0;
        for (j = 0; j < 10; j++) {
            for (k = 0; k < 9; k++) {
                nChessType = curPosition[j][k];
                if (nChessType != Eveluation.NOCHESS) {
                    m_HashKey32 = m_HashKey32 ^ m_nHashKey32[nChessType][j][k];
                    m_HashKey64 = m_HashKey64 ^ m_ulHashKey64[nChessType][j][k];
                }
            }
        }
    }

    public void hash_makeMove(Chessmove move, byte[][] curPosition) {
        byte nToID, nFromID;
        nFromID = curPosition[move.from.y][move.from.x];
        nToID = curPosition[move.to.y][move.to.x];
        m_HashKey32 = m_HashKey32 ^ m_nHashKey32[nFromID][move.from.y][move.from.x];
        m_HashKey64 = m_HashKey64 ^ m_ulHashKey64[nFromID][move.from.y][move.from.x];
        if (nToID != Eveluation.NOCHESS) {
            m_HashKey32 = m_HashKey32 ^ m_nHashKey32[nToID][move.to.y][move.to.x];
            m_HashKey64 = m_HashKey64 ^ m_ulHashKey64[nToID][move.to.y][move.to.x];
        }
        m_HashKey32 = m_HashKey32 ^ m_nHashKey32[nFromID][move.to.y][move.to.x];
        m_HashKey64 = m_HashKey64 ^ m_ulHashKey64[nFromID][move.to.y][move.to.x];
    }

    public void hash_unMakeMove(Chessmove move, byte nChessID, byte[][] curPosition) {
        byte nToID;
        nToID = curPosition[move.to.y][move.to.x];
        m_HashKey32 = m_HashKey32 ^ m_nHashKey32[nToID][move.from.y][move.from.x];
        m_HashKey64 = m_HashKey64 ^ m_ulHashKey64[nToID][move.from.y][move.from.x];
        m_HashKey32 = m_HashKey32 ^ m_nHashKey32[nToID][move.to.y][move.to.x];
        m_HashKey64 = m_HashKey64 ^ m_ulHashKey64[nToID][move.to.y][move.to.x];
        if (nChessID != 0) {
            m_HashKey32 = m_HashKey32 ^ m_nHashKey32[nChessID][move.to.y][move.to.x];
            m_HashKey64 = m_HashKey64 ^ m_ulHashKey64[nChessID][move.to.y][move.to.x];
        }
    }

    public int lookUpHashTable(int alpha, int beta, int depth, int TableNo) {
        int x;
        HashItem pht;
        x = m_HashKey32 & 0xFFFFF;
        pht = m_pTT[TableNo][x];
        if (pht.depth >= depth && pht.checksum == m_HashKey64) {
            switch (pht.entry_type) {
                case exact:
                    return pht.eval;
                case lower_bound:
                    if (pht.eval >= beta) {
                        return (pht.eval);
                    } else {
                        break;
                    }
                case upper_bound:
                    if (pht.eval <= alpha) {
                        return (pht.eval);
                    } else {
                        break;
                    }
            }
        }
        return 66666;
    }

    /**
     * 加入哈希表
     *
     * @param entry_type
     * @param eval
     * @param depth
     * @param TableNo
     */
    public void enterHashTable(ENTRY_TYPE entry_type, short eval, short depth, int TableNo) {
        int x;
        HashItem pht;
        x = m_HashKey32 & 0xFFFFF;//二十位哈希地址
        pht = m_pTT[TableNo][x];
        pht.checksum = m_HashKey64;
        pht.entry_type = entry_type;
        pht.eval = eval;
        pht.depth = depth;
    }
}
