package com.spider.domain;

/**
 * 关注的博彩公司
 *
 * @author wsy
 */
public enum GamingCompany {

    JinBaoBo("23", "金宝博"), //真正的金宝博是23，24是ibc，由于改变公司名称程序改动量较大，所以将此id改为24
    LiJi("31", "利记"),
    Sporttery("0", "官网");

    GamingCompany(String id, String name) {

        this.id = id;
        this.name = name;
    }

    private String id;

    private String name;

    public String getId() {

        return id;
    }

    public String getName() {

        return this.name;
    }

    public static String abbr(String company) {

        if (company != null && company.contains("金")) {
            return "jbb";
        } else {
            return "lj";
        }
    }
}
