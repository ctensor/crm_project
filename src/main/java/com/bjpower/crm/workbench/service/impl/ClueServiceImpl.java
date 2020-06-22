package com.bjpower.crm.workbench.service.impl;

import com.bjpower.crm.utils.DateTimeUtil;
import com.bjpower.crm.utils.SqlSessionUtils;
import com.bjpower.crm.utils.UUIDUtil;
import com.bjpower.crm.workbench.dao.*;
import com.bjpower.crm.workbench.domain.*;
import com.bjpower.crm.workbench.service.ClueService;
import org.apache.ibatis.session.SqlSession;

import java.util.List;

public class ClueServiceImpl implements ClueService {

    private SqlSession sqlSession = SqlSessionUtils.getSession();
    //线索相关表
    private ClueDao clueDao = sqlSession.getMapper(ClueDao.class);
    private ClueActivityRelationDao clueActivityRelationDao = sqlSession.getMapper(ClueActivityRelationDao.class);
    private ClueRemarkDao clueRemarkDao = sqlSession.getMapper(ClueRemarkDao.class);

    // 客户相关表
    private CustomerDao customerDao = sqlSession.getMapper(CustomerDao.class);
    private CustomerRemarkDao customerRemarkDao = sqlSession.getMapper(CustomerRemarkDao.class);

    // 联系人相关表
    private ContactsDao contactsDao = sqlSession.getMapper(ContactsDao.class);
    private ContactsRemarkDao contactsRemarkDao = sqlSession.getMapper(ContactsRemarkDao.class);
    private ContactsActivityRelationDao contactsActivityRelationDao =
                                        sqlSession.getMapper(ContactsActivityRelationDao.class);

    // 交易相关表
    private TranDao tranDao = sqlSession.getMapper(TranDao.class);
    private TranHistoryDao tranHistoryDao = sqlSession.getMapper(TranHistoryDao.class);

    @Override
    public boolean save(Clue c) {

        boolean flag = true;

        int count = clueDao.save(c);

        if (count != 1) {
            flag = false;
        }

        sqlSession.commit();

        return flag;
    }

    @Override
    public Clue detail(String id) {
        Clue c = clueDao.detail(id);
        return c;
    }

    @Override
    public boolean unbund(String id) {
        boolean flag = true;

        int count = clueActivityRelationDao.unbund(id);

        if (count != 1) {
            flag = false;
        }

        sqlSession.commit();

        return flag;
    }

    @Override
    public boolean bund(String cid, String[] aids) {
        boolean flag = true;
        for (String aid : aids) {

            ClueActivityRelation car = new ClueActivityRelation();
            car.setId(UUIDUtil.getUUID());
            car.setClueId(cid);
            car.setActivityId(aid);

            int count = clueActivityRelationDao.bund(car);
            if (count != 1) {
                flag = false;
            }
            sqlSession.commit();
        }

        return flag;
    }

    @Override
    public boolean convert(String clueId, Tran t, String createBy) {

        String createTime = DateTimeUtil.getSysTime();

        boolean flag = true;

        // (1) 通过线索id获取线索对象（线索对象当中封装了线索的信息）
        Clue c = clueDao.getById(clueId);

        // (2) 通过线索对象提取客户信息，当该客户不存在的时候，新建客户（根据公司名称精确匹配，判断该客户是否存在）
        String company = c.getCompany();
        Customer cus = customerDao.getCustomerByName(company);

        if (cus == null) {
            // 创建一个客户
            cus = new Customer();
            cus.setId(UUIDUtil.getUUID());
            cus.setName(company);
            cus.setAddress(c.getAddress());
            cus.setWebsite(c.getWebsite());
            cus.setPhone(c.getPhone());
            cus.setOwner(c.getOwner());
            cus.setNextContactTime(c.getNextContactTime());
            cus.setName(company);
            cus.setDescription(c.getDescription());
            cus.setCreateTime(createTime);
            cus.setCreateBy(createBy);
            cus.setContactSummary(c.getContactSummary());

            // 添加客户
            int count1 = customerDao.save(cus);
            if (count1 != 1) {
                flag = false;
            }
            sqlSession.commit();
        }
        // ---------------------------------------------------------------------------
        // 经过第二步处理后，客户表的信息我们已经拥有了，将来在处理其他表的时候，如果要使用到
        // 客户的id，直接使用cus.getId();
        // ---------------------------------------------------------------------------

        //(3) 通过线索对象保存联系人信息，保存联系人
        Contacts con = new Contacts();
        con.setId(UUIDUtil.getUUID());
        con.setAddress(c.getAddress());
        con.setAppellation(c.getAppellation());
        con.setContactSummary(c.getContactSummary());
        con.setCreateBy(createBy);
        con.setCreateTime(createTime);
        con.setCustomerId(cus.getId());
        con.setDescription(c.getDescription());
        con.setJob(c.getJob());
        con.setMphone(c.getMphone());
        con.setNextContactTime(c.getNextContactTime());
        con.setOwner(c.getOwner());
        con.setSource(c.getSource());
        con.setEmail(c.getEmail());
        con.setFullname(c.getFullname());
        // 保存联系人
        int count2 = contactsDao.save(con);
        if (count2 != 1) {
            flag = false;
        }
        sqlSession.commit();
        // ---------------------------------------------------------------------------
        // 经过第三步处理后，联系人的信息我们已经拥有了，将来在处理其他表的时候，如果要使用到
        // 联系人的id，直接使用con.getId();
        // ---------------------------------------------------------------------------

        // (4)将线索备注转换到客户备注以及联系人的备注
        // 查询出与该线索相关的备注信息列表
        List<ClueRemark> clueRemarkList = clueRemarkDao.getListByClued(clueId);
        for (ClueRemark clueRemark : clueRemarkList) {
            // 取出备注信息
            String noteContent = clueRemark.getNoteContent();

            // 创建客户备注对象
            CustomerRemark customerRemark = new CustomerRemark();
            customerRemark.setId(UUIDUtil.getUUID());
            customerRemark.setCreateBy(createBy);
            customerRemark.setCreateTime(createTime);
            customerRemark.setCustomerId(cus.getId());
            customerRemark.setNoteContent(noteContent);
            customerRemark.setEditFlag("0");

            // 保存客户备注信息
            int count3 = customerRemarkDao.save(customerRemark);
            if (count3 != 1) {
                flag = false;
            }
            sqlSession.commit();
            // 创建联系人备注对象
            ContactsRemark contactsRemark = new ContactsRemark();
            contactsRemark.setId(UUIDUtil.getUUID());
            contactsRemark.setContactsId(con.getId());
            contactsRemark.setCreateBy(createBy);
            contactsRemark.setCreateTime(createTime);
            contactsRemark.setEditFlag("0");
            contactsRemark.setNoteContent(noteContent);

            // 保存联系人备注信息
            int count4 = contactsRemarkDao.save(contactsRemark);
            if (count4 != 1) {
                flag = false;
            }
            sqlSession.commit();
        }

        // (5) "线索市场活动的关系"转换到“联系人市场活动的关系”
        // 查询出与该线索关联的关联关系列表
        List<ClueActivityRelation> clueActivityRelationList =  clueActivityRelationDao.getListByClueId(clueId);
        for (ClueActivityRelation clueActivityRelation : clueActivityRelationList) {
            // 从每一条遍历出来的记录中取出关联的市场活动id
            String activityId = clueActivityRelation.getActivityId();

            // 创建联系人与市场活动的关联关系对象，让第三步生成的联系人与市场活动做关联
            ContactsActivityRelation contactsActivityRelation = new ContactsActivityRelation();
            contactsActivityRelation.setId(UUIDUtil.getUUID());
            contactsActivityRelation.setActivityId(activityId);
            contactsActivityRelation.setContactsId(con.getId());

            //添加联系人与市场活动关联关系对象
            int count5 = contactsActivityRelationDao.save(contactsActivityRelation);
            if (count5 != 1) {
                flag = false;
            }
            sqlSession.commit();
        }

        // (6) 如果有创建交易的需求，我们需要创建一条交易
        if (t != null) {
            /*
                    t 已经在Controller 已经封装好的信息
                        id,money,name,expectedDate,stage,activityId,createBy,createTime
             */
            t.setSource(c.getSource());
            t.setOwner(c.getOwner());
            t.setNextContactTime(c.getNextContactTime());
            t.setDescription(c.getDescription());
            t.setCustomerId(cus.getId());
            t.setContactSummary(c.getContactSummary());
            t.setContactsId(con.getId());


            // 添加交易
            int count6 = tranDao.save(t);
            if(count6 != 1) {
                flag = false;
            }
            sqlSession.commit();

            // 如果创建了交易，创建一条该交易的历史
            TranHistory th = new TranHistory();
            th.setId(UUIDUtil.getUUID());
            th.setCreateBy(createBy);
            th.setCreateTime(createTime);
            th.setExpectedDate(t.getExpectedDate());
            th.setMoney(t.getMoney());
            th.setStage(t.getStage());
            th.setTranId(t.getId());

            // 添加交易历史
            int count7 = tranHistoryDao.save(th);
            if (count7 != 1) {
                flag = false;
            }
            sqlSession.commit();

        }
        // （7）删除线索备注
        for (ClueRemark clueRemark : clueRemarkList) {
            int count8 = clueRemarkDao.delete(clueRemark);
            if (count8 != -1){
                flag = false;
            }
            sqlSession.commit();
        }
        // （8）删除线索市场活动关联关系表
        for (ClueActivityRelation clueActivityRelation : clueActivityRelationList) {
            int count9 = clueActivityRelationDao.delete(clueActivityRelation);
            if (count9 != -1) {
                flag = false;
            }
            sqlSession.commit();
        }
        // （9）删除线索
        int count10 = clueDao.delete(clueId);
        if (count10 != -1) {
            flag = false;
        }
        sqlSession.commit();

        return flag;
    }

    @Override
    public List<Clue> getClueList() {
        List<Clue> cList = clueDao.getClueList();

        return cList;
    }
}
