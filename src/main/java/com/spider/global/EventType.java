package com.spider.global;

/**
 * Created by wsy on 2015/12/23.
 */
public enum EventType {

    Goal(1, "Goal", "/images/bf_img/1.png"),
    PenaltyScored(2, "Penalty scored", "/images/bf_img/7.png"),
    OwnGoal(3, "Own goal", "/images/bf_img/8.png"),
    Assit(4, "Assit", "/images/bf_img/12.png"),
    YellowCard(5, "Yellow Card", "/images/bf_img/3.png"),
    RedCard(6, "Red Card", "/images/bf_img/2.png"),
    SecondYellowCard(7, "Second Yellow Card", "/images/bf_img/9.png"),
    Mark(8, "Mark", "/images/bf_img/55.png"),
    Sub(9, "Sub", "/images/bf_img/11.png"),
    SubIn(10, "Sub in", "/images/bf_img/4.png"),
    SubOut(11, "Sub out", "/images/bf_img/5.png"),
    PenaltyMissed(12, "Penalty missed", "/images/bf_img/13.png"),
    PenaltySaved(13, "Penalty saved", "/images/bf_img/14.png"),
    ShotOnPost(14, "Shot on post", "/images/bf_img/15.png"),
    ManOfTheMatch(15, "Man of the match", "/images/bf_img/16.png"),
    FoulLeadToPanalty(16, "Foul lead to panalty", "/images/bf_img/20.png"),
    ErrorLeadToGoal(17, "Error lead to goal", "/images/bf_img/17.png"),
    ClearenceOffTheLine(18, "Clearence off the Line", "/images/bf_img/19.png"),
    LastManTackle(19, "Last man tackle", "/images/bf_img/18.png");

    EventType(int id, String desc, String imageUrl) {

        this.id = id;
        this.desc = desc;
        this.imageUrl = imageUrl;
    }

    private final int id;

    private final String desc;

    private final String imageUrl;

    public static String getDescByImageUrl(String imageUrl) {

        EventType[] eventTypes = EventType.values();
        for (EventType eventType : eventTypes) {
            if (eventType.imageUrl.equals(imageUrl)) {
                return eventType.desc;
            }
        }
        return "";

    }

    public static Integer getIdByImageUrl(String imageUrl) {

        EventType[] eventTypes = EventType.values();
        for (EventType eventType : eventTypes) {
            if (eventType.imageUrl.equals(imageUrl)) {
                return eventType.id;
            }
        }
        return null;

    }

    public static String getDescById(Integer eventType) {

        EventType[] eventTypes = EventType.values();
        for (EventType e : eventTypes) {
            if (e.id == eventType) {
                return e.desc;
            }
        }
        return null;
    }

    public int getId() {

        return id;
    }

    public String getDesc() {

        return desc;
    }

    public String getImageUrl() {

        return imageUrl;
    }
}
