package com.kingpivot.base.sequenceDefine.service.impl;

import com.kingpivot.base.sequenceDefine.dao.SequenceDefineDao;
import com.kingpivot.base.sequenceDefine.model.SequenceDefine;
import com.kingpivot.base.sequenceDefine.service.SequenceDefineService;
import com.kingpivot.base.sequenceHistory.service.SequenceHistoryService;
import com.kingpivot.common.dao.BaseDao;
import com.kingpivot.common.service.BaseServiceImpl;
import com.kingpivot.common.utils.DateFormatUtil;
import com.kingpivot.common.utils.MyMatcher;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service("sequenceDefineService")
public class SequenceDefineServiceImpl extends BaseServiceImpl<SequenceDefine, String> implements SequenceDefineService {

    @Resource(name = "sequenceDefineDao")
    private SequenceDefineDao sequenceDefineDao;
    @Autowired
    private SequenceHistoryService sequenceHistoryService;

    @Override
    public BaseDao<SequenceDefine, String> getDAO() {
        return this.sequenceDefineDao;
    }

    @Override
    public synchronized String genCode(String code, String objectId) {
        SequenceDefine sequenceDefine = this.sequenceDefineDao.getValueByCode(code);
        SimpleDateFormat sf = new SimpleDateFormat("MM");
        if (!sf.format(sequenceDefine.getModifiedTime()).equals(sf.format(new Timestamp(System.currentTimeMillis())))) {
            sequenceDefine.setNextValue(1);
            sequenceDefineDao.save(sequenceDefine);
        }
        String back = null;
        if (null != sequenceDefine && StringUtils.isNotEmpty(sequenceDefine.getFormatString())) {
            String formatContent = sequenceDefine.getFormatString();
            List<String> list = MyMatcher.matcher(formatContent);
            if (null != list && !list.isEmpty() && null != sequenceDefine.getNextValue()) {
                Date date = new Date();
                String numStr = null;
                StringBuffer sb = null;
                boolean isInit = false;
                Integer dateInt = null;
                Integer initTimeInt = -1;
                for (String str : list) {
                    if (str.contains("y")
                            || str.contains("M")
                            || str.contains("d")
                            || str.contains("h")
                            || str.contains("m")
                            || str.contains("s")
                            || str.contains("H")) {
                        formatContent = formatContent.replaceAll(str, DateFormatUtil.formatDate(date, str));

                    } else if (str.toUpperCase().contains("N")) {
                        if (null != sequenceDefine.getTimeScope()) {
                            //（1=全部，2=年，3=月，4=日，5=时）
                            switch (sequenceDefine.getTimeScope()) {
                                case 2:

                                    dateInt = Integer.valueOf(DateFormatUtil.formatDate(date, "MM"));
                                    initTimeInt = Integer.valueOf(DateFormatUtil.formatDate(date, "yyyy"));
                                    if (dateInt >= 1 && !sequenceDefine.getIsInit() && sequenceDefine.getInitTimeInt() != initTimeInt) {
                                        numStr = sequenceDefine.getInitValue().toString();
                                        isInit = true;
                                        sequenceDefine.setInitTimeInt(initTimeInt);
                                    } else {
                                        numStr = sequenceDefine.getNextValue().toString();
                                    }
                                    break;
                                case 3:
                                    dateInt = Integer.valueOf(DateFormatUtil.formatDate(date, "dd"));
                                    initTimeInt = Integer.valueOf(DateFormatUtil.formatDate(date, "MM"));
                                    if (dateInt >= 1 && !sequenceDefine.getIsInit() && sequenceDefine.getInitTimeInt() != initTimeInt) {
                                        numStr = sequenceDefine.getInitValue().toString();
                                        sequenceDefine.setInitTimeInt(initTimeInt);
                                        isInit = true;
                                    } else {
                                        numStr = sequenceDefine.getNextValue().toString();
                                    }
                                    break;
                                case 4:
                                    dateInt = Integer.valueOf(DateFormatUtil.formatDate(date, "HH"));
                                    initTimeInt = Integer.valueOf(DateFormatUtil.formatDate(date, "dd"));
                                    if (dateInt >= 0 && !sequenceDefine.getIsInit() && sequenceDefine.getInitTimeInt() != initTimeInt) {
                                        numStr = sequenceDefine.getInitValue().toString();
                                        sequenceDefine.setInitTimeInt(initTimeInt);
                                        isInit = true;
                                    } else {
                                        numStr = sequenceDefine.getNextValue().toString();
                                    }
                                    break;
                                case 5:
                                    dateInt = Integer.valueOf(DateFormatUtil.formatDate(date, "mm"));
                                    initTimeInt = Integer.valueOf(DateFormatUtil.formatDate(date, "HH"));
                                    if (dateInt >= 0 && !sequenceDefine.getIsInit() && sequenceDefine.getInitTimeInt() != initTimeInt) {
                                        numStr = sequenceDefine.getInitValue().toString();
                                        sequenceDefine.setInitTimeInt(initTimeInt);
                                        isInit = true;
                                    } else {
                                        numStr = sequenceDefine.getNextValue().toString();
                                    }
                                    break;
                                default:
                                    numStr = sequenceDefine.getNextValue().toString();
                                    break;
                            }
                        }
                        if (StringUtils.isEmpty(numStr))
                            continue;

                        if (str.length() > numStr.length()) {
                            sb = new StringBuffer();
                            for (int i = 0; i < str.length() - numStr.length(); i++) {
                                sb.append("0");
                            }
                            sb.append(numStr);
                            formatContent = formatContent.replaceAll(str, sb.toString());
                        } else {
                            formatContent = formatContent.replaceAll(str, numStr);
                        }
                    }
                    back = formatContent;
                }
                if (isInit) {
                    sequenceDefine.setNextValue(sequenceDefine.getInitValue() + 1);
                } else {
                    sequenceDefine.setNextValue(sequenceDefine.getNextValue() + 1);
                }
                sequenceDefine.setIsInit(isInit);
                sequenceDefine.setModifiedTime(new Timestamp(System.currentTimeMillis()));
                this.sequenceDefineDao.save(sequenceDefine);
                sequenceHistoryService.saveHistory(sequenceDefine.getId(), StringUtils.isEmpty(numStr) ? sequenceDefine.getInitValue() : Integer.valueOf(numStr), objectId);
            }

        }
        return back;
    }
}
