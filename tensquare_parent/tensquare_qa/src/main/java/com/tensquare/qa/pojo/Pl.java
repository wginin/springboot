package com.tensquare.qa.pojo;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * 标签和问题的中间表
 * Composite-id class must implement Serializable: com.tensquare.qa.pojo.Pl
 */
@Entity
@Table(name = "tb_pl")
public class Pl implements Serializable {
    //联合主键 通过两个保证数据唯一
    @Id
    private String problemid;
    @Id
    private String labelid;

    public String getProblemid() {
        return problemid;
    }

    public void setProblemid(String problemid) {
        this.problemid = problemid;
    }

    public String getLabelid() {
        return labelid;
    }

    public void setLabelid(String labelid) {
        this.labelid = labelid;
    }
}
