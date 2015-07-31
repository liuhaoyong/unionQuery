package com.wac.query.models;

/**
 * @author huangjinsheng
 */
public class KfDatabaseSource extends BaseBean{

    private static final long serialVersionUID = 7508623487147522020L;

    private String dbsName;
    private String userName;
    private String pwd;
    private String jdbcUrl;
    private Integer driverType;

    public String getDbsName() {
        return dbsName;
    }

    public void setDbsName(String dbsName) {
        this.dbsName = dbsName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getJdbcUrl() {
        return jdbcUrl;
    }

    public void setJdbcUrl(String jdbcUrl) {
        this.jdbcUrl = jdbcUrl;
    }

    public Integer getDriverType() {
        return driverType;
    }

    public void setDriverType(Integer driverType) {
        this.driverType = driverType;
    }

    @Override
    public String toString() {
        return "KfDatabaseSource{" +
                "dbsName='" + dbsName + '\'' +
                ", userName='" + userName + '\'' +
                ", pwd='" + pwd + '\'' +
                ", jdbcUrl='" + jdbcUrl + '\'' +
                ", driverType=" + driverType +
                "} " + super.toString();
    }
}
