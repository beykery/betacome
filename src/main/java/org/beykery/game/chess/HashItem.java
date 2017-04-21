/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.beykery.game.chess;

/**
 *
 * @author beykery
 */
public final class HashItem {

    long checksum;	// or long long might be even better
    TranspositionTable.ENTRY_TYPE entry_type;
    short depth;
    short eval;
}
