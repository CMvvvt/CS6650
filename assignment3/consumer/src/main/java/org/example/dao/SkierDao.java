package org.example.dao;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.example.Consumer;
import org.example.models.Skier;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

public class SkierDao {
    private JedisPool pool;

    public SkierDao(JedisPool pool) {
        this.pool = pool;
    }

    public void createSkier(Skier skier) {
        Gson gson = new Gson();
        JsonObject formData = new JsonObject();
        formData.addProperty("time", skier.getTime());
        formData.addProperty("seasonID", skier.getSeasonID());
        formData.addProperty("liftID", skier.getLiftID());
        formData.addProperty("dayID", skier.getDayID());
        String resortDayKey = skier.getResortID() + "_" + skier.getDayID();
        System.out.println("key:id " + resortDayKey + ":" + skier.getSkierID());
        System.out.println("formData: " + formData.toString());
        try (Jedis jedis = pool.getResource()){
            // data model 1 - handle the first 3 questions.
            jedis.sadd(skier.getSkierID(), gson.toJson(formData));
            // data model 2 - handle the 4th question. use set to exclude repeated members
            jedis.sadd(resortDayKey, skier.getSkierID());
            Consumer.getPool().returnBrokenResource(jedis);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public JedisPool getPool() {
        return pool;
    }

    public void setPool(JedisPool pool) {
        this.pool = pool;
    }
}