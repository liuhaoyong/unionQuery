package com.wac.query.service;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.wac.query.dao.AbstractMapper;
import com.wac.query.dao.KfParamMapper;
import com.wac.query.models.KfParam;
import com.wac.query.utils.PageVo;

/**
 * @author huangjinsheng 全局参数
 */
@Service
@Transactional(propagation= Propagation.REQUIRED,readOnly=false,rollbackFor=Exception.class)
public class KfParamService extends AbstractService<KfParam,KfParam>{

    @Resource
    private KfParamMapper kfParamMapper;

    @Override
    @Resource(name="kfParamMapper")
    public void setAbstractDAO(AbstractMapper<KfParam,KfParam> abstractMapper) {
        this.abstractMapper = abstractMapper;
    }

    @Override
    public String tableJson(KfParam param, String sEcho, List<KfParam> list) {
        int total = numOfTotal(param);
        List<List<String>> aaData = list.stream().map(b -> {
        	 List<String> slist = new LinkedList();
             slist.add("" + b.getId());
             slist.add(b.getParamName());
             slist.add(b.getFieldName());
             slist.add(StringUtils.defaultString(b.getMemo()));
             slist.add(b.getCreateTime()!=null?DateFormatUtils.ISO_DATETIME_FORMAT.format(b.getCreateTime()):"");
             slist.add(b.getUpdateTime()!=null?DateFormatUtils.ISO_DATETIME_FORMAT.format(b.getUpdateTime()):"");
             slist.add("<a href='#' onclick='edit("+b.getId()+")'>修改</a>");
             return slist;
        }).collect(Collectors.toList());
        
        PageVo vo = new PageVo(sEcho,total+"",total+"",aaData);
        return vo.toString();
    }

}
