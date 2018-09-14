package com.lhy.mvp_rxjava_retrofit.entity;

import java.util.List;

/**
 * Created by lhy on 2018/9/12.
 */
public class DataTestInfo {


    private List<ListprojectBean> listproject;

    public List<ListprojectBean> getListproject() {
        return listproject;
    }

    public void setListproject(List<ListprojectBean> listproject) {
        this.listproject = listproject;
    }

    public static class ListprojectBean {
        /**
         * projectname : 呵呵
         * projectmoney : 50
         * projectid : 1
         * projecttype : 1
         */

        private String projectname;
        private String projectmoney;
        private String projectid;
        private String projecttype;

        public String getProjectname() {
            return projectname;
        }

        public void setProjectname(String projectname) {
            this.projectname = projectname;
        }

        public String getProjectmoney() {
            return projectmoney;
        }

        public void setProjectmoney(String projectmoney) {
            this.projectmoney = projectmoney;
        }

        public String getProjectid() {
            return projectid;
        }

        public void setProjectid(String projectid) {
            this.projectid = projectid;
        }

        public String getProjecttype() {
            return projecttype;
        }

        public void setProjecttype(String projecttype) {
            this.projecttype = projecttype;
        }


        @Override
        public String toString() {
            return "{" +
                    "projectname='" + projectname + '\'' +
                    ", projectmoney='" + projectmoney + '\'' +
                    ", projectid='" + projectid + '\'' +
                    ", projecttype='" + projecttype + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "DataTestInfo{" +
                "listproject=" + listproject +
                '}';
    }
}
