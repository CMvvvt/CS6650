package org.example.models;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.example.Consumer;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

public class SkierDao {
    private Skier skier;
    private JedisPool pool;


    public SkierDao(Skier skier, JedisPool pool) {
        this.skier = skier;
        this.pool = pool;
    }

    public void createSkier() {
        Gson gson = new Gson();
        JsonObject formData = new JsonObject();

        formData.addProperty("seasonID", skier.getSeasonID());
        formData.addProperty("liftID", skier.getLiftID());
        formData.addProperty("dayID", skier.getDayID());
        String resortDayKey = skier.getResortID() + "_" + skier.getDayID();
        System.out.println("key: " + resortDayKey);

        System.out.println("formData: " + formData.toString());
        try (Jedis jedis = pool.getResource()){
            jedis.set("clientName", "Jedis");
            jedis.sadd(skier.getSkierID(), gson.toJson(formData));

            // use set to exclude repeated members
            jedis.sadd(resortDayKey, skier.getSkierID());
            Consumer.getPool().returnBrokenResource(jedis);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public Skier getSkier() {
        return skier;
    }

    public void setSkier(Skier skier) {
        this.skier = skier;
    }

    public JedisPool getPool() {
        return pool;
    }

    public void setPool(JedisPool pool) {
        this.pool = pool;
    }
}