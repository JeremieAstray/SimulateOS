/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ram;

import java.util.ArrayList;

/**
 *
 * @author Administrator
 */
public class OFTLEManager {

    public OFTLEManager() {

    }

    public boolean isOFTLEExist(ArrayList<OFTLE> oftleList, OFTLE oftle) {
        return oftleList.indexOf(oftle) != -1;
    }

    public int returnOFTLEIndex(ArrayList<OFTLE> oftleList, String absouletRoute) {
        for (int i = 0; i < oftleList.size(); i++) {
            if (oftleList.get(i).getAbsoultRoute().equals(absouletRoute)) {
                return i;
            }
        }
        return -1;
    }
    
    
    public void deleteOFTLE(ArrayList<OFTLE> oftleList, OFTLE o){
        oftleList.remove(o);
    }
    
}
