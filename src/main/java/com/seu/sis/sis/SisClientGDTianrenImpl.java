package com.seu.sis.sis;

import com.caucho.hessian.client.HessianProxyFactory;
import com.gdtianren.rtdb.RTDBDao;
import com.gdtianren.rtdb.TagData;
import com.seu.sis.task.model.PointValue;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.*;

public class SisClientGDTianrenImpl implements SisClient {
    /**
     * rtdbDao.getServerTime();
     * rtdbDao.getRTDataByBatch();
     * rtdbDao.getSnapshotDataByTagName();
     * 发生异常时，上述接口函数无法抛出异常，只能打印出异常信息
     **/

    private static final String dateformat = "yyyy-MM-dd HH:mm:ss";
    private static final String defaultUrlbase = "http://192.0.0.39:8080"; //丰一,天仁sis
    private static final String defaultDaoPath = "/rtdb/remoting/RTDBDao-hessian";//丰一,天仁sis
    private static SimpleDateFormat sdf = new SimpleDateFormat(dateformat);
    //    private RTDBDaoExceptionWrapper rtdbDaoWrapper2=null;
    private TianrenRTDBDaoExceptionWrapper rtdbDaoWrapper = null;

    @Override
    public void createConnection(String ip, int port, String path, String user, String password) throws Exception {
        try {
            if (rtdbDaoWrapper == null) {
                String urlBase = "http://" + ip;
                if (port > 0 && port != 80) {
                    urlBase = urlBase + ":" + port;
                }
                if (urlBase.length() < 14) {
                    urlBase = defaultUrlbase;
                }
                String daoPath = path;
                if (daoPath.length() < 5) {
                    daoPath = defaultDaoPath;
                }
                HessianProxyFactory factory = new HessianProxyFactory();
                String url = (urlBase) + (daoPath);
                RTDBDao rtdbDao = (RTDBDao) factory.create(RTDBDao.class, url);
                rtdbDaoWrapper = new TianrenRTDBDaoExceptionWrapper();
                rtdbDaoWrapper.setRtdbDao(rtdbDao);
                setCloseHook();
            }
        } catch (Exception e) {
            rtdbDaoWrapper = null;
            throw e;
        }
    }

    @Override
    public void releaseConnection() throws Exception {
        try {
            if (rtdbDaoWrapper != null) {
                rtdbDaoWrapper.clearCache();
//                rtdbDaoWrapper.reset();//reset非常慢
            }
        } finally {
            rtdbDaoWrapper = null;
        }
    }

    @Override
    public boolean isConnectionNull() {
        if (rtdbDaoWrapper == null) {
            return true;
        }
        return false;
    }

    @Override
    public HashMap<String, PointValue> getRealDataByNames(List<String> names) throws Exception {
        HashMap<String, PointValue> ans = null;
        try {
            if (names.size() > 0) {
                List<TagData> dataList = rtdbDaoWrapper.getRTDataByBatch(names);
                if (dataList == null) {
                    throw new Exception("real data is null");
                }
                int n = names.size();
                if (n < 1) {
                    throw new Exception("real data size is 0");
                }
                ans = new HashMap<>();
                for (int i = 0; i < n; i++) {
                    String tag = names.get(i);
                    TagData tagData = dataList.get(i);
                    Date dt = tagData.getTime();
                    long tmst = dt.getTime();
                    dt.setTime(tmst - tmst % 1000);//去掉毫秒
                    tagData.setTime(dt);
                    PointValue point = new PointValue(tagData);
                    ans.put(tag, point);
                }
            }
            return ans;
        } catch (Exception e) {
            ans = null;
            throw e;
        }
    }

    @Override
    public HashMap<String, PointValue> getRealDataByNames(Set<String> names) throws Exception {
        HashMap<String, PointValue> ans = null;
        try {
            if (names.size() > 0) {
                List<String> tags = new ArrayList<>(names);
                List<TagData> dataList = rtdbDaoWrapper.getRTDataByBatch(tags);
                if (dataList == null) {
                    throw new Exception("real data is null");
                }
                int n = tags.size();
                if (n < 1) {
                    throw new Exception("real data size is 0");
                }
                ans = new HashMap<>();
                for (int i = 0; i < n; i++) {
                    String tag = tags.get(i);
                    TagData tagData = dataList.get(i);
                    Date dt = tagData.getTime();
                    long tmst = dt.getTime();
                    dt.setTime(tmst - tmst % 1000);//去掉毫秒
                    tagData.setTime(dt);
                    PointValue point = new PointValue(tagData);
                    ans.put(tag, point);
                }
            }
            return ans;
        } catch (Exception e) {
            ans = null;
            throw e;
        }
    }

    @Override
    public HashMap<String, List<PointValue>> getHistoryDataByNames(List<String> names, Date t1, Date t2,
                                                                   int interval) throws Exception {
        HashMap<String, List<PointValue>> ans = null;
        try {
            if (names.size() > 0) {
                ans = new HashMap<>();
//                int n=-1;
                for (String tag : names) {
                    List<PointValue> list = getHistDataByName(tag, t1, t2, interval);
//                    if(n!=-1 && n!=list.size()){//各测点返回数据size不一样则异常
//                                                  //2023/2/21 修改，去掉每个点的数据list的size必须一样的否则异常的逻辑
//                        throw new Exception(tag +"'s data size is unequal to others");
//                    }
//                    n=list.size();
                    ans.put(tag, list);
                }
            }
            return ans;
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public HashMap<String, List<PointValue>> getHistoryDataByNames(Set<String> names, Date t1, Date t2,
                                                                   int interval) throws Exception {
        HashMap<String, List<PointValue>> ans = null;
        try {
            if (names.size() > 0) {
                ans = new HashMap<>();
//                int n=-1;
                for (String tag : names) {
                    List<PointValue> list = getHistDataByName(tag, t1, t2, interval);
//                    if(n!=-1 && n!=list.size()){//各测点返回数据size不一样则异常
//                        throw new Exception(tag +"'s data size is unequal to others");
//                    }
//                    n=list.size();
                    ans.put(tag, list);
                }
            }
            return ans;
        } catch (Exception e) {
            ans = null;
            throw e;
        }
    }

    private List<PointValue> getHistDataByName(String name, Date t1, Date t2, int interval) throws Exception {
        List<PointValue> ans = null;
        try {
            List<TagData> list = rtdbDaoWrapper.getSnapshotDataByTagName(name, t1, t2, interval);
            if (list == null) {
                throw new Exception(name + "'s history data is null");
            }
            if (list.size() == 0) {
                throw new Exception(name + "'s history data size is 0");
            }
            ans = new ArrayList<>();
            for (TagData tagData : list) {
                Date dt = tagData.getTime();
                long tmst = dt.getTime();
                dt.setTime(tmst - tmst % 1000);//去掉毫秒
                tagData.setTime(dt);
                PointValue point = new PointValue(tagData);
                ans.add(point);
            }
            return ans;
        } catch (Exception e) {
            ans = null;
            throw e;
        }
    }

    private void setCloseHook() {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            Date dt = new Date();
            dt.setTime(Instant.now().toEpochMilli());
            String dtstr = dt.toString();
            System.out.println(dtstr + " Close sisdao" + this.getClass().getName());
            try {
                if (rtdbDaoWrapper != null) {
                    rtdbDaoWrapper.clearCache();
                }
            } catch (Exception e) {
                ;
            }
        }));
    }
}
