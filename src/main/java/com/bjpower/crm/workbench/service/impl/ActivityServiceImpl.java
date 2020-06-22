package com.bjpower.crm.workbench.service.impl;

import com.bjpower.crm.settings.dao.UserDao;
import com.bjpower.crm.settings.domain.User;
import com.bjpower.crm.utils.SqlSessionUtils;
import com.bjpower.crm.vo.PaginationVO;
import com.bjpower.crm.workbench.dao.ActivityDao;
import com.bjpower.crm.workbench.dao.ActivityRemarkDao;
import com.bjpower.crm.workbench.domain.Activity;
import com.bjpower.crm.workbench.domain.ActivityRemark;
import com.bjpower.crm.workbench.service.ActivityService;
import org.apache.ibatis.session.SqlSession;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ActivityServiceImpl implements ActivityService {

    private SqlSession sqlSession = SqlSessionUtils.getSession();
    private ActivityDao activityDao = sqlSession.getMapper(ActivityDao.class);
    private ActivityRemarkDao activityRemarkDao = sqlSession.getMapper(ActivityRemarkDao.class);
    private UserDao userDao = sqlSession.getMapper(UserDao.class);

    @Override
    public boolean delete(String[] ids) {
        boolean flag = true;
        // 查询出需要删除的备注数量
        int count1 = activityRemarkDao.getCountByAids(ids);
        // 删除备注，返回收到影响的条数（实际删除的数量）
        int count2 = activityRemarkDao.deleteByAids(ids);

        if (count1 != count2) {
            flag = false;
        }
        // 删除市场活动
        int count3 = activityDao.delete(ids);
        if (count3 != ids.length) {
            flag = false;
        }
        sqlSession.commit();
        return flag;
    }

    @Override
    public Map<String, Object> getUserListAndActivity(String id) {
        // 取uList
        List<User> uList = userDao.getUserList();
        // 取a
        Activity a = activityDao.getById(id);
        // 将uList 和 a 打包到map中
        Map<String, Object> map = new HashMap<>();
        map.put("uList", uList);
        map.put("a", a);
        // 返回map
        return map;
    }

    @Override
    public boolean update(Activity a) {
        boolean flag = true;
        int count = activityDao.update(a);

        if (count != 1) {
            flag = false;
        }
        sqlSession.commit(); // 提交事务
        return flag;
    }

    @Override
    public Activity detail(String id) {

        Activity a = activityDao.detail(id);

        return a;
    }

    @Override
    public List<ActivityRemark> getRemarkListByAid(String activityId) {
        List<ActivityRemark> arList = activityRemarkDao.getRemarkListByAid(activityId);
        return arList;
    }

    @Override
    public boolean deleteRemark(String id) {
        boolean flag = true;
        int count = activityRemarkDao.deleteById(id);
        if (count != 1) {
            flag = false;
        }
        sqlSession.commit();
        return flag;
    }

    @Override
    public boolean saveRemark(ActivityRemark ar) {
        boolean flag = true;
        int count = activityRemarkDao.saveRemark(ar);
        if (count != 1) {
            flag = false;
        }
        sqlSession.commit();
        return flag;
    }

    @Override
    public boolean updateRemark(ActivityRemark ar) {
        boolean flag = true;
        int count = activityRemarkDao.updateRemark(ar);
        if (count != 1) {
            flag = false;
        }
        sqlSession.commit();
        return flag;
    }

    @Override
    public List<Activity> getActivityListByClueId(String clueId) {
        List<Activity> acList = activityDao.getActivityListByClueId(clueId);
        return acList;
    }

    @Override
    public List<Activity> getActivityListByNameNoByClueId(Map<String, String> map) {
        List<Activity> aList = activityDao.getActivityListByNameNoByClueId(map);
        return aList;
    }

    @Override
    public List<Activity> getActivityListByName(String aname) {
        List<Activity> aList = activityDao.getActivityListByName(aname);
        return aList;
    }

    @Override
    public boolean save(Activity a) {
        boolean flag = true;
        int count = activityDao.save(a);

        if (count != 1) {
            flag = false;
        }
        sqlSession.commit(); // 提交事务
        return flag;
    }

    @Override
    public PaginationVO<Activity> pageList(Map<String, Object> map) {
        // 取得total
        int total = activityDao.getTotalByCondition(map);
        // 取得dataList
        List<Activity> dataList = activityDao.getActivityListByCondition(map);
        // 创建一个vo对象，将total和dataList加入到对象中
        PaginationVO<Activity> vo = new PaginationVO<>();
        vo.setTotal(total);
        vo.setDataList(dataList);
        // 将vo 返回
        return vo;
    }

}
