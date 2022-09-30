/*
 * Ski Data API for NEU Seattle distributed systems course
 * An API for an emulation of skier managment system for RFID tagged lift tickets. Basis for CS6650 Assignments for 2019
 *
 * OpenAPI spec version: 2.0
 * 
 *
 * NOTE: This class is auto generated by the swagger code generator program.
 * https://github.com/swagger-api/swagger-codegen.git
 * Do not edit the class manually.
 */

package io.swagger.client.api;

import io.swagger.client.model.ResortIDSeasonsBody;
import io.swagger.client.model.ResortSkiers;
import io.swagger.client.model.ResortsList;
import io.swagger.client.model.ResponseMsg;
import io.swagger.client.model.SeasonsList;
import org.junit.Test;
import org.junit.Ignore;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * API tests for ResortsApi
 */
@Ignore
public class ResortsApiTest {

    private final ResortsApi api = new ResortsApi();

    /**
     * Add a new season for a resort
     *
     * 
     *
     * @throws Exception
     *          if the Api call fails
     */
    @Test
    public void addSeasonTest() throws Exception {
        ResortIDSeasonsBody body = null;
        Integer resortID = null;
        api.addSeason(body, resortID);

        // TODO: test validations
    }
    /**
     * get a list of seasons for the specified resort
     *
     * 
     *
     * @throws Exception
     *          if the Api call fails
     */
    @Test
    public void getResortSeasonsTest() throws Exception {
        Integer resortID = 1;
        SeasonsList response = api.getResortSeasons(resortID);

        System.out.println(response);
        // TODO: test validations
    }
    /**
     * get number of unique skiers at resort/season/day
     *
     * 
     *
     * @throws Exception
     *          if the Api call fails
     */
    @Test
    public void getResortSkiersDayTest() throws Exception {
        Integer resortID = null;
        Integer seasonID = null;
        Integer dayID = null;
        ResortSkiers response = api.getResortSkiersDay(resortID, seasonID, dayID);

        // TODO: test validations
    }
    /**
     * get a list of ski resorts in the database
     *
     * 
     *
     * @throws Exception
     *          if the Api call fails
     */
    @Test
    public void getResortsTest() throws Exception {
        ResortsList response = api.getResorts();

        // TODO: test validations
    }
}
