package com.github.yedp.ez.common.codec;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.yedp.ez.common.codec.vo.Msg;
import com.github.yedp.ez.common.codec.vo.QyWxGroupMsg;
import com.github.yedp.ez.common.util.CollectionUtils;
import com.github.yedp.ez.common.util.DateUtils;
import com.github.yedp.ez.common.util.JsonUtil;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiPredicate;

/**
 * @author yedp
 * @date 2021/12/3011:41
 * @comment
 **/
public class CollectionUtilsTest {
    private final static Logger log = LoggerFactory.getLogger(CollectionUtilsTest.class);

    List<Msg> msgList = new ArrayList<>();
    @Before
    public void before(){
        msgList.add(new Msg(1,"test1","0 0 1 * * ?"));
        msgList.add(new Msg(2,"test2","0 0 2 * * ?"));
        msgList.add(new Msg(3,"test3","0 0 3 * * ?"));
    }

    @Test
    public void testConvertList() throws JsonProcessingException {
        List<QyWxGroupMsg> qyWxGroupMsgList = CollectionUtils.convertList(msgList,QyWxGroupMsg.class);
        log.info("qyWxGroupMsgList : {}", JsonUtil.toJsonString(qyWxGroupMsgList));
    }

    @Test
    public void testConvertListWithPredict() throws JsonProcessingException {
        List<QyWxGroupMsg> qyWxGroupMsgList = CollectionUtils.convertList(msgList,QyWxGroupMsg.class,new BiPredicate<Msg, QyWxGroupMsg>(){

            @Override
            public boolean test(Msg msg, QyWxGroupMsg qyWxGroupMsg) {
                qyWxGroupMsg.setSendTime(DateUtils.nextDate(msg.getCron()));
                return true;
            }
        });
        log.info("qyWxGroupMsgList : {}", JsonUtil.toJsonString(qyWxGroupMsgList));
    }
}
