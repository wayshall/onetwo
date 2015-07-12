package com.yooyo.zhiyetong.service.impl;

import java.util.Date;
import java.util.List;
import java.util.stream.Stream;

import org.apache.commons.lang3.ArrayUtils;
import org.onetwo.common.exception.ServiceException;
import org.onetwo.easyui.EasyPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.PageHelper;
import com.yooyo.zhiyetong.dao.EstateDao;
import com.yooyo.zhiyetong.entity.Estate;
import com.yooyo.zhiyetong.entity.EstateExtEntity;
import com.yooyo.zhiyetong.mapper.EstateMapper;

@Service
@Transactional
public class EstateServiceImpl {

    @Autowired
    private EstateMapper estateMapper;

    @Autowired
    private EstateDao estateDao;
    
    public int save(Estate estate){
        Date now = new Date();
        estate.setCreateAt(now);
        estate.setUpdateAt(now);
        return estateMapper.insert(estate);
    }
    
    public Estate findByPrimaryKey(Long id){
        return estateMapper.selectByPrimaryKey(id);
    }
    
    public int update(Estate estate){
        estate.setUpdateAt(new Date());
        return estateMapper.updateByPrimaryKey(estate);
    }
    
    public void deleteByPrimaryKeys(Long...ids){
        if(ArrayUtils.isEmpty(ids))
            throw new ServiceException("请先选择数据！");
        Stream.of(ids).forEach(id->deleteByPrimaryKey(id));
    }
    
    public int deleteByPrimaryKey(Long id){
        Estate dict = findByPrimaryKey(id);
        if(dict==null){
            throw new ServiceException("找不到数据:" + id);
        }
        return estateMapper.deleteByPrimaryKey(id);
    }
}
