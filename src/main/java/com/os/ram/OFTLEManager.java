package com.os.ram;

import java.util.ArrayList;

/**
 *
 * @author FeiFan Liang
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
