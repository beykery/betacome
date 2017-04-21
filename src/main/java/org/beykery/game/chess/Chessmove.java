/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.beykery.game.chess;

/**
 *
 * @author beykery
 */
public final class Chessmove {

    public short chessID;	//
    public ChessmanPosition from;
    public ChessmanPosition to;			//
    public int score;		// 

    public Chessmove() {
    }

    Chessmove(int nFromX, int nFromY, int nToX, int nToY) {
        from = new ChessmanPosition(nFromX, nFromY);
        to = new ChessmanPosition(nToX, nToY);
    }
}
