package com.seu.sis.sis;


import com.seu.sis.task.model.PointValue;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public interface SisClient {

    public void createConnection(String ip, int port, String path, String user, String password) throws Exception;

    public void releaseConnection() throws Exception;
    public boolean isConnectionNull();
    public HashMap<String, PointValue> getRealDataByNames(List<String> names)throws Exception;

    public HashMap<String, PointValue> getRealDataByNames(Set<String> names)throws Exception;
    public HashMap<String, List<PointValue>> getHistoryDataByNames(List<String> names, Date t1,
                                                                   Date t2, int interval)throws Exception;
    public HashMap<String, List<PointValue>> getHistoryDataByNames(Set<String> names, Date t1,
                                                                   Date t2, int interval)throws Exception;
}
