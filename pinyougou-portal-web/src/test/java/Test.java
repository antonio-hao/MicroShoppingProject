import redis.clients.jedis.Jedis;

public class Test {

    public static void main(String[] args) {
        Jedis jedis = new Jedis("192.168.144.130",7001);
        jedis.set("age", "1");
     System.out.println(jedis.get("name"));
    }

}
