/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.crimsonrpg.mafiacraft.gov;

import com.crimsonrpg.mafiacraft.player.MPlayer;

/**
 *
 * @author simplyianm
 */
public class Government implements LandOwner {
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean canBuild(MPlayer player) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
