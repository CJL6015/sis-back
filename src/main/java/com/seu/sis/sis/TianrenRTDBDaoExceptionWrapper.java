package com.seu.sis.sis;

import com.gdtianren.rtdb.RTDBDao;
import com.gdtianren.rtdb.TagData;

import java.util.Date;
import java.util.List;

/***
 * @className: TianrenRTDBDaoExceptionWrapper
 * @description: TODO 描述
 * @author: mengzesen
 * @email: 383238372@qq.com
 * @date: 2023/2/18 20:40
 * @Company: copyright© by mengzesen
 ***/
public class TianrenRTDBDaoExceptionWrapper implements RTDBDao {
    private RTDBDao rtdbDao=null;

    public RTDBDao getRtdbDao() {
        return this.rtdbDao;
    }

    public void setRtdbDao(RTDBDao rtdbDao) {
        this.rtdbDao = rtdbDao;
    }
    @Override
    public void reset() {
        try {
            this.rtdbDao.reset();
        } catch (Exception e) {
            throw e;
        }
    }
    @Override
    public void clearCache(){
        try {
            this.rtdbDao.clearCache();
        } catch (Exception e) {
            throw e;
        }
    }

    public String toString() {
        return getClass() + ":" + this.rtdbDao;
    }

    @Override
    public TagData getAggregationDataByTagName(String tagName, Date stTime, Date edTime, int type) {
        try {
            return this.rtdbDao.getAggregationDataByTagName(tagName, stTime, edTime, type);
        } catch (Exception e) {
            throw new RuntimeException(getClass().getName() + tagName + " Exception:" + e.getLocalizedMessage());
        }
    }

    @Override
    public List<TagData> getAggregationDataByTagNameInBatch(String tagName, Date stTime, Date edTime, int interval, int type) {
        try {
            return this.rtdbDao.getAggregationDataByTagNameInBatch(tagName, stTime, edTime, interval, type);
        } catch (Exception e) {
            throw new RuntimeException(getClass().getName() + tagName + " Exception:" + e.getLocalizedMessage());
        }
    }

    @Override
    public TagData getAvgDataByTagName(String tagName, Date stTime, Date edTime) {
        try {
            return this.rtdbDao.getAvgDataByTagName(tagName, stTime, edTime);
        } catch (Exception e) {
            throw new RuntimeException(getClass().getName() + tagName + " Exception:" + e.getLocalizedMessage());
        }
    }

    @Override
    public TagData getMaxDataByTagName(String tagName, Date stTime, Date edTime) {
        try {
            return this.rtdbDao.getMaxDataByTagName(tagName, stTime, edTime);
        } catch (Exception e) {
            throw new RuntimeException(getClass().getName() + tagName + " Exception:" + e.getLocalizedMessage());
        }
    }

    @Override
    public TagData getMinDataByTagName(String tagName, Date stTime, Date edTime) {
        try {
            return this.rtdbDao.getMinDataByTagName(tagName, stTime, edTime);
        } catch (Exception e) {
            throw new RuntimeException(getClass().getName() + tagName + " Exception:" + e.getLocalizedMessage());
        }
    }

    @Override
    public TagData getHistoryDataByTime(String tagName, Date time) {
        try {
            return this.rtdbDao.getHistoryDataByTime(tagName, time);
        } catch (Exception e) {
            throw new RuntimeException(getClass().getName() + tagName + " Exception:" + e.getLocalizedMessage());
        }
    }

    @Override
    public List<TagData> getRTDataByBatch(List<String> tagList) {
        try {
            return this.rtdbDao.getRTDataByBatch(tagList);
        } catch (Exception e) {
            throw new RuntimeException(getClass().getName() + tagList + " Exception:" + e.getLocalizedMessage());
        }
    }

    @Override
    public TagData getRTDataByTagName(String tagName) {
        try {
            return this.rtdbDao.getRTDataByTagName(tagName);
        } catch (Exception e) {
            throw new RuntimeException(getClass().getName() + tagName + " Exception:" + e.getLocalizedMessage());
        }
    }

    @Override
    public List<TagData> getRawDataByTagName(String tagName, Date stTime, Date edTime) {
        try {
            return this.rtdbDao.getRawDataByTagName(tagName, stTime, edTime);
        } catch (Exception e) {
            throw new RuntimeException(getClass().getName() + tagName + " Exception:" + e.getLocalizedMessage());
        }
    }
    @Override
    public List<TagData> getSnapshotDataByTagName(String tagName, Date stTime, Date edTime, int interval) {
        try {
            return this.rtdbDao.getSnapshotDataByTagName(tagName, stTime, edTime, interval);
        } catch (Exception e) {
            throw new RuntimeException(getClass().getName() + tagName + " Exception:" + e.getLocalizedMessage());
        }
    }

}
