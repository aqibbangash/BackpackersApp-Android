package com.kit.backpackers.project_kit.Models;

/**
 * Created by Aqib on 17/05/2017.
 */
public class ExpLocations {

    public ExpLocations(int activeId, int backpackerId, int expeditionId, String name, Long lat, Long lng) {
        ActiveId = activeId;
        BackpackerId = backpackerId;
        ExpeditionId = expeditionId;
        Name = name;
        Lat = lat;
        Lng = lng;
    }

    int ActiveId;
    int BackpackerId;
    int ExpeditionId;
    String Name;
    Long Lat;
    Long Lng;


}
