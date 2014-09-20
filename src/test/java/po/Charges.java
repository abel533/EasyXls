package po;

import java.math.BigDecimal;
import java.util.Date;

public class Charges {
    private Integer id;

    private Integer communityid;

    private Integer ownersid;

    //业主姓名
    private String ownersname;

    //房号
    private String roomno;

    private Integer year;

    private BigDecimal property;

    private BigDecimal perproperty;

    private BigDecimal cleaning;

    private BigDecimal warm;

    private BigDecimal lighting;

    private BigDecimal other;

    //费用合计
    private BigDecimal total;

    private String otherinfo;

    private Date chargestime;

    private String createuser;

    private Date createtime;

    private String remark;

    //错误信息
    private String error;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCommunityid() {
        return communityid;
    }

    public void setCommunityid(Integer communityid) {
        this.communityid = communityid;
    }

    public Integer getOwnersid() {
        return ownersid;
    }

    public void setOwnersid(Integer ownersid) {
        this.ownersid = ownersid;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public BigDecimal getProperty() {
        return property;
    }

    public void setProperty(BigDecimal property) {
        this.property = property;
    }

    public BigDecimal getPerproperty() {
        return perproperty;
    }

    public void setPerproperty(BigDecimal perproperty) {
        this.perproperty = perproperty;
    }

    public BigDecimal getCleaning() {
        return cleaning;
    }

    public void setCleaning(BigDecimal cleaning) {
        this.cleaning = cleaning;
    }

    public BigDecimal getWarm() {
        return warm;
    }

    public void setWarm(BigDecimal warm) {
        this.warm = warm;
    }

    public BigDecimal getLighting() {
        return lighting;
    }

    public void setLighting(BigDecimal lighting) {
        this.lighting = lighting;
    }

    public BigDecimal getOther() {
        return other;
    }

    public void setOther(BigDecimal other) {
        this.other = other;
    }

    public String getOtherinfo() {
        return otherinfo;
    }

    public void setOtherinfo(String otherinfo) {
        this.otherinfo = otherinfo;
    }

    public Date getChargestime() {
        return chargestime;
    }

    public void setChargestime(Date chargestime) {
        this.chargestime = chargestime;
    }

    public String getCreateuser() {
        return createuser;
    }

    public void setCreateuser(String createuser) {
        this.createuser = createuser;
    }

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getOwnersname() {
        return ownersname;
    }

    public void setOwnersname(String ownersname) {
        this.ownersname = ownersname;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public String getRoomno() {
        return roomno;
    }

    public void setRoomno(String roomno) {
        this.roomno = roomno;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}